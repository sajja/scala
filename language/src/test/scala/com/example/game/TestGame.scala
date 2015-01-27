package com.example.game

/**
 * Created by sajith on 1/27/15.
 */

import com.example.game.models._
import org.scalatest.{Matchers, FlatSpec}

class TestGame extends FlatSpec with  Matchers {
  val human = new Human("Human")
  val elf = new Human("Elf")
  val orc = new Human("Orc")

  val arthur = new Warrior("King arthur", human)
  val archer = new Ranger("Some archer", human)
  val woodenBow = new Bow("Elvan bow")
  val excaliber = new Sword("Excaliber")

  "Character" should "not be able to fight without a weapon" in {
    arthur.fireWeapon() shouldBe false
    archer.fireWeapon() shouldBe false
  }

  "Archer" should "be able to equip a bow" in {
    archer.canEquip(woodenBow) shouldBe true
  }

  "Archer" should "be not able to equip a sword" in {
    archer.canEquip(excaliber) shouldBe false
  }

  "Arthur" should "be able to equip a sword" in {
    arthur.canEquip(excaliber) shouldBe true
  }

  "Arthur" should "be not able to equip a bow" in {
    arthur.canEquip(woodenBow) shouldBe false
  }

  "Character" should "be able to fight with a weapon" in {
    arthur.equipPrimary(excaliber) shouldBe true
    archer.equipPrimary(woodenBow) shouldBe true

    arthur.fireWeapon() shouldBe true
    archer.fireWeapon() shouldBe true
  }
}
