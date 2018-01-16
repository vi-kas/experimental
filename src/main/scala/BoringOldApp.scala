package com.vi_kas

import encoders.{CsvEncoder, cjsonEncoders, csvEncoder, jsonObjectEncoders}
import com.vi_kas.encoders.csvEncoder.Implicits._
import ops.opers.{LastLike, Second}
import com.vi_kas.types._
import com.vi_kas.notes.ShortNotes.typeTagging
import shapeless.{::, HNil}


object BoringOldApp {
  import CsvEncoder._

  private def hlistExample() = {
    lazy val bottleCsvEncoder: CsvEncoder[Bottle] = implicitly // boom!
    val bottle: Bottle = "Steel" :: "Round" :: "Blue" :: HNil  // products!
    println(s"Here it is your bottle : ${bottleCsvEncoder.encode(bottle)}")
  }

  private def hlistCovariantExample() = {
    /*
     * listOfColors has more than one type: List[Colors] and Product with Serializable with com.vi_kas.BoringOldApp.Colors
     * If you try removing : List[Colors] type reference, things may seem different to compiler.
     */
    val listOfColors: List[Colors] = List(Red("Red"), Blue("Blue"), Green("Green"))
    println(encodeCsv(listOfColors))
  }

  private def implicitDivergence() = {
    val concreteTreeLike = List(Branch(Branch(Leaf("LeftLeaf"), Leaf("RightLeaf")), Leaf("AbsoluteRightLeaf")))
    println(encodeCsv(concreteTreeLike))
  }

  private def dependentTypes() = {
    val lastONe = LastLike[String :: Int :: Char :: HNil]
    val second = Second[String :: Int :: Char :: HNil]
    println(lastONe("One" :: 2 :: '4' :: HNil))
    println(second("One" :: 2 :: '4' :: HNil))
  }

  private def genericRepr() = {
    println(getRepr(Blue("Blue").asInstanceOf[Colors]))  //Inl(Blue(Blue))
    println(getRepr(Blue("blue")))                      //blue :: HNil
    println(getRepr(Leaf("abc")))                      //abc :: HNil
  }

  private def typeTaggingAndPhantomTypes() = {
    typeTagging()
  }

  private def jsonEncode() = {
    import com.vi_kas.encoders.CJsonEncoder.encodeJson
    import com.vi_kas.encoders.cjsonEncoders.Implicits._
    import com.vi_kas.encoders.jsonObjectEncoders.Implicits._

    val country = Country("India", "New Delhi")
    println(encodeJson(List(country)))
  }

  private def penultimate() = {

  }

  def main(args: Array[String]): Unit = {   // app
    hlistExample()
    hlistCovariantExample()
    implicitDivergence()
    genericRepr()
    dependentTypes()

    typeTaggingAndPhantomTypes()
    jsonEncode()

    penultimate()     // lemma pattern!!
  }

  //This is complementatry!
}