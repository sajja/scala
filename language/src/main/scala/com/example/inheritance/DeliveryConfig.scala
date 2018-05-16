package com.example.inheritance

case class DeliveryConfig(id: Int, setting: String, parentId: Int = -1) {
  def getSetting(): String = setting
}


