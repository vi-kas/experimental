package com.vi_kas

import CsvEncoder._
import shapeless.{::, Generic, HList, HNil}

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

  //generic encoder
  implicit def genericEncoder[A, R](
                                implicit
                                gen: Generic.Aux[A, R],                  //converts concrete type => generic representation
                                enc: CsvEncoder[R]                      //desired = CsvEncoder[gen.Repr]
                                ): CsvEncoder[A] = {
                            instance(a => enc.encode(gen.to(a)))      //Convert concrete<instance> to generic value reprn.
                       }
  /* method-end notes
    ------------
    Aux Pattern:
    ------------
      cause: we can’t refer to a type member of one parameter from another parameter in the same block.
      gen: Generic.Aux[A, R] is shorthand for gen: Generic[A]{type Repr = R}

    ------------
   */

}