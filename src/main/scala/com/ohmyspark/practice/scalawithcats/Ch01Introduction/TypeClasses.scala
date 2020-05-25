package com.ohmyspark.practice.scalawithcats.Ch01Introduction

object TypeClasses {

  sealed trait Json
  final case class JsObject(get: Map[String, Json]) extends Json
  final case class JsString(get: String) extends Json
  final case class JsNumber(get: Double) extends Json
  final case object JsNull extends Json

  case class Person(firstName: String, lastName: String)

  // This is type class
  trait JsonWriter[A] {
    def write(value: A): Json
  }

  // These are type class instances
  object JsonWriterInstances {
    implicit val stringWriter: JsonWriter[String] = new JsonWriter[String] {
      def write(value: String): Json = JsString(value)
    }

    implicit val personWriter: JsonWriter[Person] = new JsonWriter[Person] {
      def write(value: Person): Json =
        JsObject(
          Map(
            "first_name" -> JsString(value.firstName),
            "last_name" -> JsString(value.lastName)
          )
        )
    }

    implicit def optionWriter[A](implicit writer: JsonWriter[A]): JsonWriter[Option[A]] =
      new JsonWriter[Option[A]] {
        def write(value: Option[A]): Json =
          value match {
              case Some(x) => writer.write(x)
              case None => JsNull
          }
      }
  }

  // This is type class interface
  object JsonWriter {
    def toJson[A](value: A)(implicit writer: JsonWriter[A]) =
      writer.write(value)
  }

  object JsonWriterSyntax {
    implicit class JsonWriterOps[A](value: A) {
      def toJson(implicit writer: JsonWriter[A]) = writer.write(value)
    }
  }

}

object TypeClassInterface extends App {
  import TypeClasses.{JsonWriter, Person}
  import TypeClasses.JsonWriterInstances._

  println(JsonWriter.toJson(Person("Pavel", "Filatov")))
  println(JsonWriter.toJson("Hello world"))
}

object TypeClassSyntax extends App {
  import TypeClasses.Person
  import TypeClasses.JsonWriterInstances._
  import TypeClasses.JsonWriterSyntax._

  println(Person("Pavel", "Filatov").toJson)
  println("Hello world".toJson)
  
  println(Option(Person("Pavel", "Filatov")).toJson)
  println(Option("Hello world!").toJson)

  println((None: Option[String]).toJson)
  println((None: Option[Person]).toJson)
}
