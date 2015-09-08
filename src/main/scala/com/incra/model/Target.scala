package com.incra.model

case class Target(var x: Double,
                  var y: Double,
                  var dX: Double,
                  var dY: Double) {
  def step(dt: Double): Unit = {
    x = x + dX
    y = y + dY
  }
}