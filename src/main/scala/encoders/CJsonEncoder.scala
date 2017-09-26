package com.vi_kas.encoders

import com.vi_kas.cjsonTypes._

trait CJsonEncoder[A] {
  def encode(value: A): CJSValue
}

object CJsonEncoder {
  def apply[A](implicit enc: CJsonEncoder[A]): CJsonEncoder[A] = enc

  def instance[A](func: A => CJSValue): CJsonEncoder[A] = (value: A) => func(value) // Shorthand for instantiating CJsonEncoder and overriding encode method

  def encodeJson[A](list: List[A])(implicit cJsonEncoder: CJsonEncoder[A]) =             //utility
    list.map(mem => cJsonEncoder.encode(mem)).mkString("\n")

}

package cjsonEncoders {
  import com.vi_kas.encoders.CJsonEncoder._

  object Implicits {

    /* LabelledGeneric Encoders */
    implicit val stringJSEncoder: CJsonEncoder[String] = instance(str => CJsonString(str))

    implicit def listEncoder[A](implicit enc: CJsonEncoder[A]): CJsonEncoder[List[A]] =
      instance(list => CJsonArray(list.map(enc.encode)))

    implicit def optionEncoder[A](implicit enc: CJsonEncoder[A]): CJsonEncoder[Option[A]] =
      instance(option => option.map(enc.encode).getOrElse(CJsonNull))
  }
}