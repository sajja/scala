package com.example.slick.audit.domain


class MyT(id: Int, name: String)

case class MyTable(id: Int, name: String) extends MyT(id, name)

case class MyAuditTable(id: Int, name: String, version: Int) extends MyT(id, name)


