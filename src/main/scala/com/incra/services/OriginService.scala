package com.incra.services

import java.sql.Date

import com.escalatesoft.subcut.inject.{BindingModule, Injectable}
import com.incra.model.{OriginTable, Origin}

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.meta.MTable

/**
 * @author Jeff Risberg
 * @since 09/04/2015
 */
class OriginService(implicit val bindingModule: BindingModule) extends Injectable {
  private def mainDatabase = inject[Database]

  println("InitOriginService")
  mainDatabase withSession {
    implicit session =>
      val origins = TableQuery[OriginTable]

      // Create the tables, including primary and foreign keys
      if (MTable.getTables("origin").list().isEmpty) {
        (origins.ddl).create

        origins += Origin(None, "Initial Case", new Date(115, 5, 5), 6.383, -10.031)
        origins += Origin(None, "Followup #1", new Date(115, 5, 8), 6.427425, -10.732664)
        origins += Origin(None, "Followup #2", new Date(115, 5, 9), 6.945, -9.95)
        origins += Origin(None, "Followup #3", new Date(115, 5, 10), 6.645, -9.48)
        origins += Origin(None, "Followup #4", new Date(115, 5, 10), 6.745, -9.12)
        origins += Origin(None, "Followup #5", new Date(115, 5, 10), 6.645, -9.67)
      }
  }
  println("EndInitOriginService")

  /**
   *
   */
  def getEntityList(): List[Origin] = {
    mainDatabase withSession {
      implicit session =>

        TableQuery[OriginTable].list
    }
  }

  /**
   *
   */
  def findById(id: Long): Option[Origin] = {
    mainDatabase withSession {
      implicit session =>

        TableQuery[OriginTable].where(_.id === id).firstOption
    }
  }
}
