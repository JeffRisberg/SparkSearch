package com.incra.app

import com.escalatesoft.subcut.inject.BindingModule
import com.incra.model.GridCell
import com.incra.services._

/**
 * @author Jeff Risberg
 * @since late August 2015
 */
class MainServlet(implicit val bindingModule: BindingModule) extends SparkSearchStack {

  private def siteService = inject[SiteService]

  private def activityService = inject[ActivityService]

  private def facilityService = inject[FacilityService]

  private def originService = inject[OriginService]

  val gridCells = for (lat <- 5.6 to 7.8 by 0.2;
                       lng <- -11.0 to -9.0 by 0.2)
    yield {
      GridCell(lat, lng, 0.0)
    }

  get("/") {
    contentType = "text/html"

    val data1 = List("title" -> "Spark Search Example")
    val data2 = data1 ++ List("city" -> "Palo Alto", "state" -> "California", "population" -> 66363)

    ssp("/index", data2.toSeq: _*)
  }

  get("/process") {

    // clear the values in the grid cells
    gridCells.foreach(_.probability = 0.0)

    //run the process
    val processService = new ProcessService()

    val numTimesteps = 250
    val numTrials = 10000
    val parallelism = 1000

    val counts = processService.run(gridCells, numTimesteps, numTrials, parallelism)

    // update the cells
    val maxCount = counts.map { case (gridCellOpt, count) => if (gridCellOpt.isDefined) count else 0 }.max

    if (maxCount > 0) {
      counts.foreach { case (gridCellOpt, count) =>
        if (gridCellOpt.isDefined) {
          val gridCell = gridCellOpt.get
          var realGridCellOpt = gridCells.find { realGridCell => (realGridCell.lat == gridCell.lat && realGridCell.lng == gridCell.lng) }

          if (realGridCellOpt.isDefined) {
            val realGridCell = realGridCellOpt.get
            realGridCell.probability = Math.min(1.0, count.toDouble / maxCount.toDouble)
          }
        }
      }
    }

    // run the map
    contentType = "text/html"

    val origins = originService.getEntityList()
    val sites = siteService.getEntityList()

    val data1 = List("title" -> "Spark Search Example")
    val data2 = data1 ++ List("originOpt" -> None, "gridCells" -> gridCells, "origins" -> origins, "sites" -> sites)

    ssp("/map/index", data2.toSeq: _*)
  }

  get("/site") {
    contentType = "text/html"

    val sites = siteService.getEntityList()

    val data1 = List("title" -> "Spark Search Sites")
    val data2 = data1 ++ List("name" -> "Primary Reportings", "sites" -> sites)

    ssp("/site/index", data2.toSeq: _*)
  }

  get("/site.json") {
    contentType = formats("json")

    trapData {
      val origins = siteService.getEntityList()

      origins
    }
  }

  get("/site/:id") {
    contentType = "text/html"

    val siteOpt = siteService.findById(params("id").toLong)
    if (siteOpt.isDefined) {
      val site = siteOpt.get

      val data1 = List("title" -> "Possible Transfer Site")
      val data2 = data1 ++ List("site" -> site)

      ssp("/site/show", data2.toSeq: _*)
    }
    else {
      redirect("/site")
    }
  }

  get("/activity") {
    contentType = "text/html"

    val activities = activityService.getEntityList()

    val data1 = List("title" -> "Spark Search Activities")
    val data2 = data1 ++ List("name" -> "Biohazard Treatment Types", "activities" -> activities)

    ssp("/activity/index", data2.toSeq: _*)
  }

  get("/activity.json") {
    contentType = formats("json")

    trapData {
      val activities = activityService.getEntityList()

      activities
    }
  }

  get("/activity/:id") {
    contentType = "text/html"

    val activityOpt = activityService.findById(params("id").toLong)

    if (activityOpt.isDefined) {
      val activity = activityOpt.get

      val data1 = List("title" -> "Spark Search Activity")
      val data2 = data1 ++ List("activity" -> activity)

      ssp("/activity/show", data2.toSeq: _*)
    }
    else {
      redirect("/activity")
    }
  }

  get("/facility") {
    contentType = "text/html"

    val facilities = facilityService.getEntityList()

    val data1 = List("title" -> "Spark Search Facilities")
    val data2 = data1 ++ List("name" -> "Liberia", "facilities" -> facilities)

    ssp("/facility/index", data2.toSeq: _*)
  }

  get("/facility.json") {
    contentType = formats("json")

    trapData {
      val facilities = facilityService.getEntityList()

      facilities
    }
  }

  get("/facility/:id") {
    contentType = "text/html"

    val facilityOpt = facilityService.findById(params("id").toLong)
    if (facilityOpt.isDefined) {
      val facility = facilityOpt.get

      val data1 = List("title" -> "Spark Search Facilities")
      val data2 = data1 ++ List("facility" -> facility)

      ssp("/facility/show", data2.toSeq: _*)
    }
    else {
      redirect("/facility")
    }
  }

  get("/origin") {
    contentType = "text/html"

    val origins = originService.getEntityList()

    val data1 = List("title" -> "Spark Search Patient Origins")
    val data2 = data1 ++ List("name" -> "Primary Reportings", "origins" -> origins)

    ssp("/origin/index", data2.toSeq: _*)
  }

  get("/origin.json") {
    contentType = formats("json")

    trapData {
      val origins = originService.getEntityList()

      origins
    }
  }

  get("/origin/:id") {
    contentType = "text/html"

    val originOpt = originService.findById(params("id").toLong)
    if (originOpt.isDefined) {
      val origin = originOpt.get

      val data1 = List("title" -> "Spark Search Patient Origins")
      val data2 = data1 ++ List("origin" -> origin)

      ssp("/origin/show", data2.toSeq: _*)
    }
    else {
      redirect("/origin")
    }
  }

  get("/map") {
    contentType = "text/html"

    val origins = originService.getEntityList()
    val sites = siteService.getEntityList()

    val originOpt = if (params.contains("id")) originService.findById(params("id").toLong) else None

    val data1 = List("title" -> "Spark Search Example")
    val data2 = data1 ++ List("originOpt" -> originOpt, "gridCells" -> gridCells, "origins" -> origins, "sites" -> sites)

    ssp("/map/index", data2.toSeq: _*)
  }
}
