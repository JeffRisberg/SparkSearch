package com.incra.services

import com.escalatesoft.subcut.inject.{BindingModule, Injectable}
import com.incra.model.{Activity, ActivityTable}

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.meta.MTable

/**
 * @author Jeff Risberg
 * @since 10/08/2014
 */
class ActivityService(implicit val bindingModule: BindingModule) extends Injectable {
  private def mainDatabase = inject[Database]

  println("InitActivityService")
  mainDatabase withSession {
    implicit session =>
      val activities = TableQuery[ActivityTable]

      // Create the tables, including primary and foreign keys
      if (MTable.getTables("activity").list().isEmpty) {
        (activities.ddl).create

        activities += Activity(None, "Diagnostic Scan", "A-678-2", "low")
        activities += Activity(None, "Isolation", "TI-723", "low")
        activities += Activity(None, "Treatment", "TR-167", "medium")
        activities += Activity(None, "Experimental Vaccine", "V-EX-0945", "high")
      }
  }
  println("EndInitActivityService")

  /**
   *
   */
  def getEntityList(): List[Activity] = {
    mainDatabase withSession {
      implicit session =>

        TableQuery[ActivityTable].list
    }
  }

  /**
   *
   */
  def findById(id: Long): Option[Activity] = {
    mainDatabase withSession {
      implicit session =>

        TableQuery[ActivityTable].where(_.id === id).firstOption
    }
  }
}
