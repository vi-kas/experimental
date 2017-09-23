package com.vi_kas

import com.vi_kas.ops.opers.{LastLike, Second}
import com.vi_kas.types._
import encoderImplicits._
import shapeless.{::, HNil}
import shapeless.ops.hlist.Last

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
    val listOfColors: List[Colors] = List(Red("Red"), Blue("Blue"), Green("Green"))

    println(encodeCsv(listOfColors))

    val concreteTreeLike = List(Branch(Branch(Leaf("LeftLeaf"), Leaf("RightLeaf")), Leaf("AbsoluteRightLeaf")))
    println(encodeCsv(concreteTreeLike))

    println(getRepr(Blue("Blue").asInstanceOf[Colors]))  //Inl(Blue(Blue))
    println(getRepr(Blue("blue")))                      //blue :: HNil
    println(getRepr(Leaf("abc")))                      //abc :: HNil

    val lastONe = LastLike[String :: Int :: Char :: HNil]
    val second = Second[String :: Int :: Char :: HNil]
    println(lastONe("One" :: 2 :: '4' :: HNil))
    println(second("One" :: 2 :: '4' :: HNil))
  }
}