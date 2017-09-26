package com.vi_kas.encoders

trait CsvEncoder[A] {     //good old t-c
  def encode(a: A): List[String]      // it does what it says
}

object CsvEncoder {
  def apply[A](implicit enc: CsvEncoder[A]): CsvEncoder[A] = enc                        //summoner

  def instance[A](func: A => List[String]): CsvEncoder[A] = (value: A) => func(value) // constructor

  def encodeCsv[A](list: List[A])(implicit csvEncoder: CsvEncoder[A]) =             //utility
    list.map(mem => csvEncoder.encode(mem).mkString(", ")).mkString("\n")
}

package csvEncoder {

  import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, Lazy}

  object Implicits {
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
}