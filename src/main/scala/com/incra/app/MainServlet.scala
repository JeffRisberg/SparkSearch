package com.incra.app

import com.escalatesoft.subcut.inject.BindingModule
import com.incra.services.{FacilityService, OriginService, ActivityService}

/**
 * @author Jeff Risberg
 * @since late August 2014
 */
class MainServlet(implicit val bindingModule: BindingModule) extends SparkSearchStack {

  private def activityService = inject[ActivityService]

  private def facilityService = inject[FacilityService]

  private def originService = inject[OriginService]

  get("/") {
    contentType = "text/html"

    val data1 = List("title" -> "Spark Search Example")
    val data2 = data1 ++ List("city" -> "Palo Alto", "state" -> "California", "population" -> 66363)

    ssp("/index", data2.toSeq: _*)
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

    val data1 = List("title" -> "Spark Search Challenges")
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

      val data1 = List("title" -> "Spark Search Challenge")
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

    val data1 = List("title" -> "Spark Search Leaderboards")
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

      val data1 = List("title" -> "Spark Search Leaderboard")
      val data2 = data1 ++ List("origin" -> origin)

      ssp("/origin/show", data2.toSeq: _*)
    }
    else {
      redirect("/origin")
    }
  }
}
