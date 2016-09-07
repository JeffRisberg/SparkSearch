package com.incra.model

import java.sql.Date

import scala.slick.driver.MySQLDriver.simple._

/**
 * Definition of the Site entity
 *
 * @author Jeff Risberg
 * @since 06/11/2015
 */
case class Site(id: Option[Long], name: String,
                  lat: Double, lng: Double) extends Entity[Long]

class SiteTable(tag: Tag) extends Table[Site](tag, "SITE") {

  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

  def name = column[String]("NAME")

  def lat = column[Double]("LAT")

  def lng = column[Double]("LNG")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, name, lat, lng) <>(Site.tupled, Site.unapply _)
}
