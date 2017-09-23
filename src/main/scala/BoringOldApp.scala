package com.vi_kas

import EncoderImplicits._
import shapeless.{::, HNil}

object BoringOldApp {
  import CsvEncoder._

  def main(args: Array[String]): Unit = {   // app

    lazy val bottleCsvEncoder: CsvEncoder[Bottle] = implicitly // boom!

    val bottle: Bottle = "Steel" :: "Round" :: "Blue" :: HNil  // products!

    println(s"Here it is your bottle : ${bottleCsvEncoder.encode(bottle)}")

    /*
     * listOfColors has more than one type: List[Colors] and Product with Serializable with com.vi_kas.BoringOldApp.Colors
     * If you try removing : List[Colors] type reference, things may seem different to compiler.
     */
    val listOfColors: List[Colors] = List(
      Red("Red"),
      Blue("Blue"),
      Green("Green")
    )

    println(encodeCsv(listOfColors))
  }

  sealed trait Colors

  final case class Red(name: String)   extends Colors
  final case class Green(name: String) extends Colors
  final case class Blue(name: String)  extends Colors

  type Body = String
  type Cap  = String
  type Color = String
  type Bottle = Body :: Cap :: Color :: HNil      //makes no sense, right?
}