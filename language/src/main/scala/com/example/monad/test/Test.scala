package com.example.monad.test

/**
 * Created by sajith on 9/10/15.
 */

trait IO {
  self=>
  def run:Unit

  def ++ (io:IO) = new IO {
    def run (io:IO) = {
      self.run
      io.run
    }

    override def run: Unit = println("")
  }
}
class Test {

}
