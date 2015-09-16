package com.incra.model

import java.sql.Date

import scala.slick.driver.MySQLDriver.simple._

/**
 * Definition of the Origin entity
 *
 * @author Jeff Risberg
 * @since 06/11/2015
 */
case class Origin(id: Option[Long], name: String,
                     date: Date, lat: Double, lng: Double) extends Entity[Long]

class OriginTable(tag: Tag) extends Table[Origin](tag, "ORIGIN") {

  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

  def name = column[String]("NAME")

  def date = column[Date]("DATE")

  def lat = column[Double]("LAT")

  def lng = column[Double]("LNG")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, name, date, lat, lng) <>(Origin.tupled, Origin.unapply _)
}
