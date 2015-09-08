package com.incra.model

import scala.slick.driver.MySQLDriver.simple._

/**
 * Definition of the Activity entity
 *
 * @author Jeffrey Risberg
 * @since 06/10/2015
 */
case class Activity(id: Option[Long], name: String, description: String, uom: String) extends Entity[Long]

class ActivityTable(tag: Tag) extends Table[Activity](tag, "ACTIVITY") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

  def name = column[String]("NAME")

  def description = column[String]("DESCRIPTION")

  def uom = column[String]("UOM")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, name, description, uom) <>(Activity.tupled, Activity.unapply _)
}
