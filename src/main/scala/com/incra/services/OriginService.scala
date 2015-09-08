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

        origins += Origin(None, "Initial Case", new Date(215,5,5), 6.38, -10.5)
        origins += Origin(None, "Followup #1", new Date(215,5,8), 6.427425, -10.732664)
        origins += Origin(None, "Followup #2", new Date(215,5,9), 6.245, -10.45)
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
