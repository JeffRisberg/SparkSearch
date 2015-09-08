package com.incra.model

import scala.slick.lifted.MappedTo

/**
 * TeamworkType for a Challenge.
 *
 * @author Jeff Risberg
 * @since 10/04/14
 */
object TeamworkType extends scala.Enumeration {

  val Individual = TeamworkType(1L, "Individual")

  val Team = TeamworkType(2L, "Team")

  case class TeamworkType(value: Long, name: String) extends Val(nextId, name)

  final def withKey(k: Long): TeamworkType = {
    values.iterator.map(_.asInstanceOf[TeamworkType]).find(_.value == k).get
  }

  final def list: List[TeamworkType] = values.toList.map(_.asInstanceOf[TeamworkType])
}


