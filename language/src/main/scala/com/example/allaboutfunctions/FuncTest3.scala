package com.example.allaboutfunctions

/**
 * Created by sajith on 8/17/14.
 */
object FuncTest3 {

  def sum(i:Int, j:Int) = i+j
  def prod(i:Int, j:Int) = i*j


  def someFancFn1(i:Int) = i match {
    case 0 => 0
    case anyOther => sum(i,200)
  }


  def someFancFn2(i:Int) = i match {
    case 0 => 100
    case anyOther => prod(i,3)
  }

  def f1(i:Int, j:Int, k:Int, f:(Int,Int)=>Int) = i match {
    case 0 => j
    case anyOther => f(i,k)
  }

  def f2[A](i:A, j:A, k:A, f:(A,A)=>A) = i match {
    case 0 => j
    case anyOther => f(i,k)
  }

  def main(args: Array[String]): Unit = {
    println(someFancFn1(0))
    println(someFancFn1(10))
    println(someFancFn2(0))
    println(someFancFn2(10))
    //same as above line
    println(f1(10,100,3,(i:Int,j:Int)=>i*j))
    //same as above less verbose
    println(f1(10,100,3,_*_))
    //same but more generic.
    f2[Int](10,100,3,_*_)
  }
}
