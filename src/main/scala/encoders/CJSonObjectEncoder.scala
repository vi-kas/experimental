package com.vi_kas.encoders

import com.vi_kas.cjsonTypes.CJsonObject

trait CJSonObjectEncoder[A]{
  def encode(value: A) : CJsonObject
}

object CJSonObjectEncoder {
  def apply[A](implicit enc: CJSonObjectEncoder[A]) : CJSonObjectEncoder[A] = enc

  def instance[A](func: A => CJsonObject): CJSonObjectEncoder[A] = (value: A) => func(value)
}

package jsonObjectEncoders {

  import com.vi_kas.encoders.CJsonEncoder._
  import shapeless.labelled.FieldType
  import shapeless.{::, HList, HNil, LabelledGeneric, Lazy, Witness}

  object  Implicits {
    implicit val hnilJSEncoder: CJSonObjectEncoder[HNil] = CJSonObjectEncoder.instance(ins => CJsonObject(Nil))

    implicit def hlistObjectJSEncoder[K <: Symbol, H, T <: HList](    //K is sub-typed with Symbol? cuz we want to access the name of tag[1], ~> Symbol.name
                                                                      implicit
                                                                      witness: Witness.Aux[K],            //Because we want to access KeyTag value in the next block
                                                                      headEncoder: Lazy[CJsonEncoder[H]],
                                                                      tailEncoder: CJSonObjectEncoder[T]
                                                                 ): CJSonObjectEncoder[FieldType[K, H] :: T] = {
      val fieldName = witness.value.name
      CJSonObjectEncoder.instance { hl =>
        val head = headEncoder.value.encode(hl.head)
        val tail = tailEncoder.encode(hl.tail)
        CJsonObject((fieldName, head) :: tail.fields)
      }
    }
    /* method-end notes
       ------------
       Take Aways:  1. LabelledGeneric uses, Symbol for tags, that's why type bounded.
                    2. HLists containing Tags :> records!
       ------------
     */

    implicit def genericObjectEncoder[A, R <: HList](
                                                      implicit
                                                      gen: LabelledGeneric.Aux[A, R],   // 1.
                                                      hEncoder: Lazy[CJSonObjectEncoder[R]]
                                                    ): CJsonEncoder[A] = instance(ins => hEncoder.value.encode(gen.to(ins)))
    /* method-end notes
       ------------
       Take Aways:  1. gen: LabelledGeneric.Aux[A, R] is shorthand for gen: Generic[A]{type Repr = R}
       ------------
     */

  }
}
