import CsvEncoder._
import shapeless.{HNil, ::, HList}

package object EncoderImplicits {

  implicit val stringEncoder: CsvEncoder[String] = instance(str => List(str))

  //instances of HLists
  implicit val hnilEncoder: CsvEncoder[HNil] = instance(hnil => Nil)

  implicit def hlistEncoder[H, T <: HList](
                           implicit
                           headEncoder: CsvEncoder[H],
                           tailEncoder: CsvEncoder[T]
                           ): CsvEncoder[H :: T] = instance {
                                case h :: t => headEncoder.encode(h) ++ tailEncoder.encode(t)
                            }

}
