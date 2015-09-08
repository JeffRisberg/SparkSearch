package com.incra.model

/**
 * Created by jeff on 9/8/15.
 */

case class GridCell(lat: Double,
                    lng: Double,
                    var probability: Double
                     ) {

  def containsPoint(lat: Double, lng: Double) =
    (lat >= this.lowerLeftLat && lat <= this.upperRightLat &&
      lng >= this.lowerLeftLng && lng <= this.upperRightLng)

  def lowerLeftLat() = lat

  def lowerLeftLng() = lng

  def upperRightLat() = lat + GridCell.cellSize

  def upperRightLng() = lng + GridCell.cellSize
}

object GridCell {
  val cellSize = 0.2
}