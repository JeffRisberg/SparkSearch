package com.incra.services

import com.escalatesoft.subcut.inject.{BindingModule, Injectable}
import com.incra.model.{Site, SiteTable}

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.meta.MTable

/**
 * @author Jeff Risberg
 * @since 09/04/2015
 */
class SiteService(implicit val bindingModule: BindingModule) extends Injectable {
  private def mainDatabase = inject[Database]

  println("InitSiteService")
  mainDatabase withSession {
    implicit session =>
      val sites = TableQuery[SiteTable]

      // Create the tables, including primary and foreign keys
      if (MTable.getTables("site").list().isEmpty) {
        (sites.ddl).create

        sites += Site(None, "Loading Dock at River", 6.383, -10.031)
        sites += Site(None, "Cave Bats with Histoplasmosis", 6.427425, -10.732664)
        sites += Site(None, "Forest Canopy with Droppings", 6.945, -9.95)
        sites += Site(None, "Downstream Facilities", 6.645, -9.48)
      }
  }
  println("EndInitSiteService")

  /**
   *
   */
  def getEntityList(): List[Site] = {
    mainDatabase withSession {
      implicit session =>

        TableQuery[SiteTable].list
    }
  }

  /**
   *
   */
  def findById(id: Long): Option[Site] = {
    mainDatabase withSession {
      implicit session =>

        TableQuery[SiteTable].where(_.id === id).firstOption
    }
  }
}
