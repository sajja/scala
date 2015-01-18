package com.example.mixin

trait Submergable {
  def submerge() = println("diving.....")
  def surface() = println("surfacing...")
}

trait Flyable {
  def fly() = println("firing thrusters ......")
}


class Vehicle
class Boat
class DolphinBoat extends Boat with Submergable
class Experimental extends Vehicle with Submergable with Flyable

object Test {
  def main(args: Array[String]) {
    val dolphinBoat = new DolphinBoat
    dolphinBoat.submerge()
    dolphinBoat.surface()

    val experiment = new Experimental
    experiment.fly()
    experiment.submerge()
    experiment.surface()
  }
}
