package com.example.game

import akka.actor.ActorSystem
import com.example.game.models._
import org.scalatest.{BeforeAndAfterAll, Matchers, FlatSpec}

class TestGame extends FlatSpec with Matchers with BeforeAndAfterAll {
  implicit val system = ActorSystem("Game")

  val human = new Human("Human")
  val elf = new Elf("Elf")
  val orc = new Orc("Orc")

  val normalDamage = Normal("Normal", 10)

  val arthur = new Warrior("King arthur", 50, human)
  val hellscreem = new Warrior("Hellscreem", 80, orc)
  val archer = new Ranger("Some archer",30, human)

  val woodenBow = new Bow("Elvan bow", normalDamage)
  val excaliber = new Sword("Excaliber", normalDamage)
  val battleAxe = new BattleAxe("SoulCarver", normalDamage)

  "Character" should "not be able to fight without a weapon" in {
    arthur.useWeapon(hellscreem) shouldBe -1
    archer.useWeapon(hellscreem) shouldBe -1
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
    hellscreem.useWeapon(arthur) should be > arthur.useWeapon(hellscreem)
  }

  "Weapons" should "be customized" in {
    val fireEnhanced = new FireEnhance("Fire damage", 20, Some[Normal](normalDamage))
    val dragonBlade = new Sword("Dragon blade", fireEnhanced)

    val faryFire = new PoisonEnhance("Poison damage", 5, Some[FireEnhance](fireEnhanced))
    val soulClaver = new Sword("SoulCalver", faryFire)

    dragonBlade.use() shouldBe 30
    soulClaver.use() shouldBe 35
  }

  "BattleAxe" should "not be wield by humans" in {
    arthur.canEquip(battleAxe) shouldBe false
  }

  "BattleAxe" should "only be wield by orc" in {
    hellscreem.canEquip(battleAxe) shouldBe true
  }

  "When attacked target" should "take damage" in {
    val grunt = new Warrior("Grunt",50,orc)
    val longSword = new Sword("Exotic sword", new FireEnhance("Fire", 20, Some[Normal](normalDamage)))
    arthur.equipPrimary(longSword)
    arthur.useWeapon(grunt)
    grunt.hitPoints shouldBe 20
  }

  override def beforeAll(): Unit = {


  }
}