package com.ohmyspark.practice.scalawithcats.Ch01Introduction

import cats._
import cats.implicits._

import java.util.Date
import java.time.LocalDate

object MeetCats extends App {

  println(Show[Int].show(123))
  println(Show[String].show("123"))

  println(123.show)
  println("123".show)

  // 1st option to create a new instance
  implicit val dateShow: Show[Date] = new Show[Date] {
    def show(t: Date): String = s"${t.getTime()} ms since the epoch."
  }

  // Try it out
  println(new Date().show)

  // 2nd option to create a new instance
  implicit val localDateShow: Show[LocalDate] =
    Show.show[LocalDate](d => s"The date is: ${d.toString()}")

  // Try it out
  println(LocalDate.of(2020, 5, 21).show)

  // Exercise: recreate Cat
  case class Cat(name: String, age: Int, color: String)

  implicit val catShow: Show[Cat] = Show.show { cat =>
    val name = cat.name.show
    val age = cat.age.show
    val color = cat.color.show
    s"$name is a $age year-old $color cat."
  }

  println(Cat("Garfield", 38, "ginger and black").show)
}
