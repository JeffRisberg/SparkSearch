package com.incra.model

import scala.slick.driver.MySQLDriver.simple._

/**
 * Definition of the Facility entity
 *
 * @author Jeff Risberg
 * @since 08/12/2015
 */
case class Facility(id: Option[Long], name: String, lat: Double, lng: Double) extends Entity[Long]

class FacilityTable(tag: Tag) extends Table[Facility](tag, "FACILITY") {

  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

  def name = column[String]("NAME")

  def lat = column[Double]("LAT")

  def lng = column[Double]("LNG")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, name, lat, lng) <>(Facility.tupled, Facility.unapply _)
}
