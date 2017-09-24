package com.vi_kas

import com.vi_kas.cjsonTypes._

trait CJsonEncoder[A] {
  def encode(value: A): CJSValue
}

object CJsonEncoder {
  def apply[A](implicit enc: CJsonEncoder[A]): CJsonEncoder[A] = enc

  def instance[A](func: A => CJSValue): CJsonEncoder[A] = (value: A) => func(value) // Shorthand for instantiating CJsonEncoder and overriding encode method

}
