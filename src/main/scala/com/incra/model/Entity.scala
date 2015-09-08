package com.incra.model

/**
 * The <i>Entity</i> trait adds an immutable id to a class.
 *
 * @author Jeff Risberg
 * @since 06/10/14
 */
trait Entity[I] {
  val id: Option[I]

  def ===(other: Any) = other match {
    case that: Entity[_] => this.id == that.id
    case _ => false
  }
}
