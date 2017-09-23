import shapeless.{::, HNil}
import EncoderImplicits._

trait CsvEncoder[A] {     //good old t-c
  def encode(a: A): List[String]      // it does what it says
}

object CsvEncoder {
  def apply[A](implicit enc: CsvEncoder[A]): CsvEncoder[A] = enc  //summoner

  def instance[A](func: A => List[String]): CsvEncoder[A] = (value: A) => func(value) // constructor
}

object BoringOldApp {
  def main(args: Array[String]): Unit = {   // app

    lazy val bottleCsvEncoder: CsvEncoder[Bottle] = implicitly // boom!

    val bottle: Bottle = "Steel" :: "Round" :: "Blue" :: HNil  // products!

    println(s"Here it is your bottle : ${bottleCsvEncoder.encode(bottle)}")
  }

  type Body = String
  type Cap  = String
  type Color = String
  type Bottle = Body :: Cap :: Color :: HNil      //makes no sense, right?
}