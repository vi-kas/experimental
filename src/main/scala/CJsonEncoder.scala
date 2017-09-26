package com.vi_kas

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

trait CJSonObjectEncoder[A]{
  def encode(value: A) : CJsonObject
}

object CJSonObjectEncoder {
  def apply[A](implicit enc: CJSonObjectEncoder[A]) : CJSonObjectEncoder[A] = enc

  def instance[A](func: A => CJsonObject): CJSonObjectEncoder[A] = (value: A) => func(value)
}
