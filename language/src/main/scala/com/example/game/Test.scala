package com.example.game

import com.example.game.models._

/**
 * Created by sajith on 1/26/15.
 */
object Test {

  def testPrimaryAttack(player:Character): Unit = {
    player.fireWeapon()
  }

  def weaponEquipTest(player:Character, weapon: Weapon) = {
    println("Trying to equip " + weapon.toString + "for player " + player.toString)
  }

  def main(args: Array[String]): Unit = {
  }
}
