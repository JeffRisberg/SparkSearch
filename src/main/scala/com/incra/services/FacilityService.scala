package com.incra.services

import java.sql.Date

import com.escalatesoft.subcut.inject.{BindingModule, Injectable}
import com.incra.model.{Facility, FacilityTable, TeamworkType}

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.meta.MTable

/**
 * @author Jeff Risberg
 * @since 10/08/2014
 */
class FacilityService(implicit val bindingModule: BindingModule) extends Injectable {
  private def mainDatabase = inject[Database]

  println("InitFacilityService")
  mainDatabase withSession {
    implicit session =>
      val facilities = TableQuery[FacilityTable]

      // Create the tables, including primary and foreign keys
      if (MTable.getTables("facility").list().isEmpty) {
        (facilities.ddl).create

        facilities += Facility(None, "Fall Hiking", 5.6, 135.6)
        facilities += Facility(Some(101), "Walk to the Moon", 5.6, -135.6)
        facilities += Facility(None, "Holiday Ship-Shape", 3.45, -120.6)
      }
  }
  println("EndInitFacilityService")

  /**
   *
   */
  def getEntityList(): List[Facility] = {
    mainDatabase withSession {
      implicit session =>

        TableQuery[FacilityTable].list
    }
  }

  /**
   *
   */
  def findById(id: Long): Option[Facility] = {
    mainDatabase withSession {
      implicit session =>

        TableQuery[FacilityTable].where(_.id === id).firstOption
    }
  }
}
