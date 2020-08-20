package com.example.statemachine


class MyFSM[I, S](val state: S, f: (S, I) => S) {
  def runState(i: I): MyFSM[I, S] = {
    new MyFSM[I, S](f(state, i), f)
  }
}


