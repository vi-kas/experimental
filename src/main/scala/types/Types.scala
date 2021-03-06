package com.vi_kas

import shapeless.{::, HNil}

package object types {
  type Body = String
  type Cap  = String
  type Color = String
  type Bottle = Body :: Cap :: Color :: HNil      //makes no sense, right?

  sealed trait Colors
  final case class Red(name: String)   extends Colors
  final case class Green(name: String) extends Colors
  final case class Blue(name: String)  extends Colors

  sealed trait TreeLike[A]
  case class Branch[A](left: TreeLike[A], right: TreeLike[A]) extends TreeLike[A]
  case class Leaf[A](value: A) extends TreeLike[A]

  case class Country(name: String, capital: String)
}

package object cjsonTypes {  //cjson is nothing but custom json
  sealed trait CJSValue
  case class CJsonObject(fields: List[(String, CJSValue)]) extends CJSValue
  case class CJsonArray(items: List[CJSValue]) extends CJSValue
  case class CJsonString(value: String) extends CJSValue
  case class CJsonNumber(value: Double) extends CJSValue
  case class CJsonBoolean(value: Boolean) extends CJSValue
  case object CJsonNull extends CJSValue

}