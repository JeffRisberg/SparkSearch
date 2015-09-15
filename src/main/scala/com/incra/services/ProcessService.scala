package com.incra.services

import com.incra.model.{GridCell, Particle}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

import org.apache.commons.math3.distribution.NormalDistribution
import org.apache.commons.math3.random.MersenneTwister

/**
 * Created by jeff on 9/8/15.
 */
class ProcessService extends Serializable {

  def run(gridCells: Seq[GridCell], numTimesteps: Int, numTrials: Int, parallelism: Int): Array[(Option[GridCell],Int)] = {

    val conf = new SparkConf()
      .setAppName("SearchSim")
      .set("spark.executor.memory", "6g")
      .setMaster("local[4]")

    val sc = new SparkContext(conf)

    val baseSeed = 1001L

    val gcBroadcast = sc.broadcast(gridCells)

    println("start the trials!")
    val endpoints = computeTrialReturns(sc, numTimesteps, baseSeed, numTrials, parallelism)
    endpoints.cache()

    println("print the results")
    endpoints.foreach { endpoint => println(endpoint) }

    // Update the grid cells
    // This is done in parallel
    val endCells = endpoints.map(endpoint => {
      val gridCellOpt = gcBroadcast.value.find {
        cell => cell.containsPoint(endpoint.x, endpoint.y)
      }

      (gridCellOpt, 1)
    })

    // This is done in parallel
    val counts = endCells.reduceByKey { case (x, y) => x + y }

    // We call collect to convert RDD to cell counts in master process
    println("computation begins here")

    counts.collect
  }

  def computeTrialReturns(sc: SparkContext,
                          numTimesteps: Int,
                          baseSeed: Long,
                          numTrials: Int,
                          parallelism: Int): RDD[Particle] = {

    // Generate different seeds so that our simulations don't all end up with the same results
    val seeds = (baseSeed until baseSeed + parallelism)
    val seedRdd = sc.parallelize(seeds, parallelism)

    // Main computation: run simulations and compute aggregate return for each
    seedRdd.flatMap(
      trialResults(_, numTimesteps, numTrials / parallelism))
  }

  def trialResults(seed: Long, numTimesteps: Int, numTrials: Int): Seq[Particle] = {

    val rand = new MersenneTwister(seed)
    val trialReturns = new Array[Particle](numTrials)

    val latitudeDistribution = new NormalDistribution(rand, 7.02, 0.05, 0.0)
    val longitudeDistribution = new NormalDistribution(rand, -9.44, 0.05, 0.0)

    val dLatitudeDistribution = new NormalDistribution(rand, -0.0008, 0.0008, 0.0)
    val dLongitudeDistribution = new NormalDistribution(rand, -0.0016, 0.0015, 0.0)

    // limit the speed to be > stallspeed and < speed of sound

    for (i <- 0 until numTrials) {
      val x = latitudeDistribution.sample()
      val y = longitudeDistribution.sample()
      val dX = dLatitudeDistribution.sample()
      val dY = dLongitudeDistribution.sample()

      val target = Particle(x, y, dX, dY)
      for (t <- 1 until numTimesteps) {
        target.step(1.0)
      }

      trialReturns(i) = target
    }
    trialReturns
  }
}