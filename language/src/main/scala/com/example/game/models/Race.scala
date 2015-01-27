package com.example.game.models

abstract class Race (name:String) {
}

class Human(name:String) extends Race(name)
class Elf(name:String) extends Race(name)
class Orc(name:String) extends Race(name)


