package com.ohmyspark.practice.scalawithcats.Ch01Introduction

object PrintablePackage {
  trait Printable[A] {
    def format(value: A): String
  }

  object PrintableInstances {
    implicit val stringPrintable: Printable[String] = new Printable[String] {
      def format(value: String): String = value
    }

    implicit val intPeintable: Printable[Int] = new Printable[Int] {
      def format(value: Int): String = value.toString()
    }
  }

  object Printable {
    def format[A](value: A)(implicit p: Printable[A]): String = p.format(value)
    def print[A](value: A)(implicit p: Printable[A]): Unit =
      println(format(value))
  }

  object PrintableSyntax {
    implicit class PrintableOps[A](value: A) {
      def format(implicit p: Printable[A]) = p.format(value)
      def print(implicit p: Printable[A]) = println(format(p))
    }
  }
}

object PrintableCat extends App {

  import PrintablePackage.Printable
  import PrintablePackage.PrintableInstances._
  import PrintablePackage.PrintableSyntax._

  case class Cat(name: String, age: Int, color: String)

  implicit val catPrintable: Printable[Cat] =
    new Printable[Cat] {
      def format(value: Cat): String = {
        val name = Printable.format(value.name)
        val age = Printable.format(value.age)
        val color = Printable.format(value.color)
        s"$name is a $age year-old $color cat."
      }
    }

  val cat = Cat("Jonathan", 12, "gray")

  Printable.print(cat)
  cat.print
}
