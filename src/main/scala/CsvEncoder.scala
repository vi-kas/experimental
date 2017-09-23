package com.vi_kas

trait CsvEncoder[A] {     //good old t-c
  def encode(a: A): List[String]      // it does what it says
}

object CsvEncoder {
  def apply[A](implicit enc: CsvEncoder[A]): CsvEncoder[A] = enc                        //summoner

  def instance[A](func: A => List[String]): CsvEncoder[A] = (value: A) => func(value) // constructor

  def encodeCsv[A](list: List[A])(implicit csvEncoder: CsvEncoder[A]) =             //utility
    list.map(mem => csvEncoder.encode(mem).mkString(", ")).mkString("\n")
}
