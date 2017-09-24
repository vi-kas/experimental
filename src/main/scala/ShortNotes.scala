package com.vi_kas.notes

import shapeless.{Generic, Witness}
import shapeless.labelled.FieldType
import shapeless.syntax.SingletonOps
import shapeless.syntax.singleton._

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

  def typeTagging() = {
    val num = 113
    val labelledNum = "labelledNum" ->> num  //labelledNum: Int with shapeless.labelled.KeyTag[String("labelledNum"), Int] = 113
    println(labelledNum)

    println(s"getFieldName: ${getFieldName(labelledNum)}")
    println(s"getFieldValue: ${getFieldValue(labelledNum)}")
  }
  /* method-end notes
   * 1. ->> Returns the provided value[String("labelledNum")] tagged with the singleton type[Int]
            of this value as its key in a record-like structure.
   * 2.  KeyTag["labelledNum", Int] is a phantom type encoding name and type of field.
         Tags exist at compile time, have no run-time representation. ~> Witness alert!
   */


  def getFieldName[K, V](value: FieldType[K, V])(implicit w: Witness.Aux[K]) : K = w.value
  /* method-end notes
   * 1. The idea is to get the field name.
        We'll use Witness[converts compile time entities such as Tags to values!] and

   * 2. FieldTypes[simplifies extracting the tag and base types from a tagged type ~> ex: labelledNum]
        type FieldType[K, V] = V with KeyTag[K, V]
   */

  def getFieldValue[K, V](value: FieldType[K, V]) = value
  /* method-end notes
   * 1. No witness present ~> got the value!
   */

  /* shapeless records: HList of Tagged elements
   * are used by LabelledGeneric!
   */


}
