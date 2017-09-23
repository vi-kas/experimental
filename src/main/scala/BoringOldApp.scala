package com.vi_kas

import com.vi_kas.Types._
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

    val concreteTreeLike = List(Branch(Branch(Leaf("LeftLeaf"), Leaf("RightLeaf")), Leaf("AbsoluteRightLeaf")))
    println(encodeCsv(concreteTreeLike))
  }
}