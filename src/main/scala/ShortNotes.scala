package com.vi_kas.notes

import shapeless.{Generic}

object ShortNotes {

 /*
  * You know Generic type class instances depend upon two types
  * 1. type parameter [T]
  * 2. abstract type Repr
  */
 def getRepr[A](value: A)(implicit gen: Generic[A]) = gen.to(value)

 trait Generic2[A, Repr]

 def getRepr2[A, R](value: A)(implicit gen: Generic[A]): R = AnyRef.asInstanceOf[R]   //X gen.to(value) ~> CTE | actual def is irrelevant
  /* method-end notes
     ------------
     Take Aways:
     ------------
       1. method getRepr relates to dependent types
          cause: result of getRepr depends on its value parameters via type members.
       2. think of type parameters help as 'input and type members as 'output
    */

}
