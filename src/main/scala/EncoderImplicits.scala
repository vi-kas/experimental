package com.vi_kas


import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, Lazy}

package object encoderImplicits {
  import CsvEncoder._

  def getRepr[A](value: A)(implicit gen: Generic[A]) = gen.to(value)

  implicit val stringEncoder: CsvEncoder[String] = instance(str => List(str))

  //instances of Products
  implicit val hnilEncoder: CsvEncoder[HNil] = instance(hnil => Nil)

  implicit def hlistEncoder[H, T <: HList](
                           implicit
                           headEncoder: Lazy[CsvEncoder[H]],      //Lazy ~> head of hlist
                           tailEncoder: CsvEncoder[T]
                           ): CsvEncoder[H :: T] = instance {
                                case h :: t => headEncoder.value.encode(h) ++ tailEncoder.encode(t)
                      }

  //instances of CoProducts
  implicit val cNilEncoder: CsvEncoder[CNil] = instance(str => throw new Exception("Really?"))    // Cus it won't be needed

  implicit def coProductCsvEncoder[H, T <: Coproduct](
                                                     implicit
                                                     headEncoder: Lazy[CsvEncoder[H]],  //Lazy ~> coProducts
                                                     tailEncoder: CsvEncoder[T]
                                                     ): CsvEncoder[H :+: T] = instance{   // :+: is disjunction of types
                           case Inl(h) => headEncoder.value.encode(h)
                           case Inr(t) => tailEncoder.encode(t)
                      }

  //generic encoder
  implicit def genericEncoder[A, R](
                                implicit
                                gen: Generic.Aux[A, R],                 //converts concrete type => generic representation
                                enc: Lazy[CsvEncoder[R]]               //desired = CsvEncoder[gen.Repr]
                                ): CsvEncoder[A] = {
                            instance(a => enc.value.encode(gen.to(a)))      //Convert concrete<instance> to generic value reprn.
                       }
  /* method-end notes
    ------------
    Aux Pattern:
    ------------
      cause: we can’t refer to a type member of one parameter from another parameter in the same block.
      gen: Generic.Aux[A, R] is shorthand for gen: Generic[A]{type Repr = R}

    --------------------
    Implicit Divergence:
    --------------------
      cause: compiler sees same type cons twice, it assumes, branch of search is “diverging”.
      This is a problem for shapeless because types like ::[H, T] and :+:[H, T] can appear several
      times as the compiler expands different generic representa ons.
      we used Lazy[
          1. suppress implicit divergence at compile time.
          2. defers evaluation of implicit params at runtime, permitting the derivation of self-referential implicits.
      ] to resolve!
    --------------------
   */
}

package object cjsonEncoderImplicits {
  import com.vi_kas.CJsonEncoder._
  import com.vi_kas.cjsonTypes.{CJsonBoolean, CJsonNumber, CJsonString, CJsonArray, CJsonNull}

  /* LabelledGeneric Encoders */
  implicit val stringJSEncoder: CJsonEncoder[String] = instance(str => CJsonString(str))
  implicit val intJSEncoder: CJsonEncoder[Int] = instance(num => CJsonNumber(num))
  implicit val doubleJSEncoder: CJsonEncoder[Double] = instance(doub => CJsonNumber(doub))
  implicit val boolJSEncoder: CJsonEncoder[Boolean] = instance(bool => CJsonBoolean(bool))

  implicit def listEncoder[A](implicit enc: CJsonEncoder[A]): CJsonEncoder[List[A]] =
    instance(list => CJsonArray(list.map(enc.encode)))

  implicit def optionEncoder[A](implicit enc: CJsonEncoder[A]): CJsonEncoder[Option[A]] =
    instance(option => option.map(enc.encode).getOrElse(CJsonNull))

 }