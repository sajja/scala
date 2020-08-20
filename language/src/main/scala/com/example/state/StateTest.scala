package com.example.state

case class State[S, +A](run: S => (A, S)) {
  def flatMap[B](f: A => State[S, B]): State[S, B] = State(s => {
    val (i, j) = run(s)
    f(i).run(j)
  })

  def map[B](f: A => B): State[S, B] = flatMap(a => State.unit(f(a)))
}

object State {
  def unit[S, A](a: A): State[S, A] = State(s => (a, s))
}

class World(val name: String, val races: List[Race])


object World {
  def apply(name: String, l: List[Race]) = new World(name, l)
}


case class Race(name: String, level: Int, towns: Int) {
  override def toString: String = s"Name:$name Level:$level Towns:$towns"
}

object StateTest extends App {

  def printState[T](state: (T, World)) = println(s"\n${state._1} \n${state._2.name}: ${state._2.races.mkString("\n")}")

  def createRace(name: String): State[World, String] = State(world => {
    val race = Race(name, 0, 0)
    (s"Following race is created: $race", World(world.name, race :: world.races))
  })

  def build(name: String): State[World, Unit] = State(world => {
    (Unit, World(world.name, world.races.map((race: Race) =>
      if (race.name.equals(name)) Race(race.name, race.level, race.towns + 1) else race)))
  })

  val middleEarth = World("Middle earth", List())

  var t1 = createRace("Humans").run(middleEarth)
  printState(t1)

  printState(
    (for {
      i <- createRace("Humans")
      j <- createRace("Orcs")
      k <- createRace("Elves")
    } yield "Humans, Orcs and Elves created").run(middleEarth)
  )

  printState(
    (for{
      _ <-createRace("Humans")
      _ <-build("Humans")
      _ <-build("Humans")
      _ <-build("Humans")
      _ <-build("Humans")
    } yield {}).run(middleEarth)
  )
}
