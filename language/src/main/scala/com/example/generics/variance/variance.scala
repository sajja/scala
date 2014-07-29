package com.example.generics.variance

class Employee
class Manager extends Employee
class Award[T](val recipient:T)

class CovariantAward[+T](val recipient:T)


object TestVariance {
  def main(args: Array[String]) {
    val theMan = new Manager
    val ordinaryGuy = new Employee
    val managerAward = new Award[Manager](theMan)
    val empAward = new Award[Employee](ordinaryGuy)
//    val employeAward:Award[Employee] = ( //--does not compile, invariance
    val awards = List[Award[Employee]](empAward)
//    val genericAwards = List[Award[Employee]](managerAward) //--does not compile invariance
    val covarManAward:CovariantAward[Manager] = new CovariantAward[Manager](theMan)
    val covarEmpAward:CovariantAward[Employee] = covarManAward //compiles because +T
    val covarAwards:CovariantAward[Employee] = new CovariantAward[Manager](theMan)//compiles because +T

  }

}