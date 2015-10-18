package com.example.cake.modules.robotics

trait RobotModule

trait Weaponize {
  this: WeaponModule =>
}

trait WeaponModule {
  var currentWeapon: Option[Weapon] = None

  val availableWeapons: List[Weapon]

  def equip(weapon: Weapon) = currentWeapon = Some(weapon)

  def fire() = for {w <- currentWeapon} yield w.fire()

  abstract class Weapon(val name: String) {
    def fire() = println(s"firering weapon $name")
  }
}

trait NonLethalWeapons extends WeaponModule {
  override val availableWeapons: List[Weapon] = List(new PepperSpray, new Teaser)

  class PepperSpray extends Weapon("Pepper spray")

  class Teaser extends Weapon("Teaser")
}

trait WoldDominationWeapons extends WeaponModule {
  override val availableWeapons: List[Weapon] = List(new RailGun, new EmpCannon, new PlasmaRiffel)

  class RailGun extends Weapon("Rail gun")

  class EmpCannon extends Weapon("Emp cannon")

  class PlasmaRiffel extends Weapon("Plasma riffel")
}

trait SpeechModule {
  val speech: Speech

  trait Speech {
    def speak(data: String)
  }

  object Speech {
    def apply(t: String) =
      t match {
        case "binary" => new BinarySpeech
        case _ => new BasicSpeech
      }
  }

  class BasicSpeech extends Speech {
    override def speak(data: String) = println(s"I have basic speech module installed: $data")
  }

  class BinarySpeech extends Speech {
    override def speak(data: String) = println(s"I have binary speech module installed: $data")
  }

}

trait MovementModule {
  val movement = new Movement

  class Movement {
    def forward = println("Move foward")

    def backward = println("move backward")

    def right = println("move right")

    def left = println("move left")
  }

}

object RobotTester {
  def main(args: Array[String]) {
    val basicBot = new RobotModule with MovementModule
    val basicSpeekBot = new RobotModule with SpeechModule {
      override val speech: Speech = Speech("")
    }

    val binaryBot = new RobotModule with SpeechModule {
      override val speech = Speech("binary")
    }

    basicBot.movement.forward
    basicBot.movement.backward
    basicSpeekBot.speech.speak("Yo ....")
    binaryBot.speech.speak("Yo..")

    val guardBot = new RobotModule with MovementModule with Weaponize with NonLethalWeapons
    guardBot.availableWeapons.foreach(a => println(a.name))

    val terminator = new RobotModule with MovementModule with Weaponize with WoldDominationWeapons

    terminator.availableWeapons.foreach(a => println(a.name))
    terminator.fire()
    terminator.equip(terminator.availableWeapons.head)
    terminator.fire()

  }
}