package com.example.inheritance

class DeliveryConfigProxy(delConf: DeliveryConfig, repo: DeliveryConfigRepository) extends DeliveryConfig(delConf.id, delConf.setting, delConf.parentId) {
  override def getSetting(): String = {
    def getS(dc: DeliveryConfig): String = {
      if (dc.setting != null) dc.setting
      else if (dc.parentId != -1) getS(repo.get(dc.parentId).get)
      else null
    }

    val settingVal = getS(this)

    settingVal
  }
}

class DeliveryConfigRepository(val dcs: Map[Int, DeliveryConfig] = Map()) {
  def put(deliveryConfig: DeliveryConfig): DeliveryConfigRepository = DeliveryConfigRepository(this.dcs.+(deliveryConfig.id -> deliveryConfig))

  def get(id: Int): Option[DeliveryConfig] = dcs.get(id)

  def twoTimes(i: Int): Int = {
    i * 2
  }

  def multiply(i: Int): Int = {
    val times = readInt()
    i * times
  }

}

object DeliveryConfigRepository {
  def apply: DeliveryConfigRepository = new DeliveryConfigRepository()

  def apply(dcs: Map[Int, DeliveryConfig] = Map()): DeliveryConfigRepository = new DeliveryConfigRepository(dcs)
}

object TestDeliveryConfig {
  def main(args: Array[String]): Unit = {
    val dcParent = DeliveryConfig(1, "Base value", -1)
    val dcChild1 = DeliveryConfig(2, "Child 1", 1)
    val dcChild2 = DeliveryConfig(3, null, 2)
    val repo = DeliveryConfigRepository().put(dcParent).put(dcChild1).put(dcChild2)

    val dcp = new DeliveryConfigProxy(dcChild2, repo)
    println(dcp.getSetting())
  }
}
