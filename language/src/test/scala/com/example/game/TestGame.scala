package com.example.game

/**
 * Created by sajith on 1/27/15.
 */

import com.example.game.models._
import org.scalatest.{Matchers, FlatSpec}

class TestGame extends FlatSpec with Matchers {
  val human = new Human("Human")
  val elf = new Elf("Elf")
  val orc = new Orc("Orc")

  val arthur = new Warrior("King arthur", human)
  val hellscreem = new Warrior("Hellscreem", orc)
  val archer = new Ranger("Some archer", human)
  val woodenBow = new Bow("Elvan bow", 10)
  val excaliber = new Sword("Excaliber", 20)
  val battleAxe = new BattleAxe("SoulCarver", 30)

  "Character" should "not be able to fight without a weapon" in {
    arthur.fireWeapon() shouldBe -1
    archer.fireWeapon() shouldBe -1
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

  "Hellscreem" should "be able do more damage than arthur" in {
    arthur.equipPrimary(excaliber) shouldBe true
    hellscreem.equipPrimary(excaliber) shouldBe true
    hellscreem.fireWeapon() should be > arthur.fireWeapon()
  }
}
