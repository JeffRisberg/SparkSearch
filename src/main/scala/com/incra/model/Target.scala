package com.incra.model

case class Particle(var x: Double,
                  var y: Double,
                  var dX: Double,
                  var dY: Double) {
  def step(dT: Double): Unit = {
    x = x + dX * dT
    y = y + dY * dT
  }
}