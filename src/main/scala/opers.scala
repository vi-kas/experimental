package com.vi_kas
package ops

import shapeless.ops.hlist.Last
import shapeless.ops.hlist.Last.Aux
import shapeless.{::, HList, HNil}

object opers {

   /*
    * warn: Already exist as Last[L <: HList] t-c in shapeless.ops.hlist
    * info: Read comments from Second t-c to understand better
    */
    trait LastLike[H <: HList] {
      type LastOut
      def apply(value: H): LastOut
    }

    object LastLike {
      type Aux[L <: HList, LO] = LastLike[L] { type LastOut = LO }

      def apply[L <: HList](implicit instance: LastLike[L]): Aux[L, instance.LastOut] = instance

      implicit def hlistLastLike[A, T <: HList](implicit lt : LastLike[T]): Aux[A :: T, lt.LastOut] =
        new LastLike[A :: T] {
          type LastOut = lt.LastOut
          def apply(last : A :: T): LastOut = lt(last.tail)
        }

      implicit def hasSingleLastLike[A]: Aux[A :: HNil, A] = new LastLike[A :: HNil] {
        type LastOut = A
        def apply(lastLike: A :: HNil): LastOut = lastLike.head
      }
    }

   /*
    * Info: Second t-c returns second element from any hlist<not covering error scenarios>
    *   Think of it as Second is a type class that only can become concrete using HList members.
    *   It expects you to give HList type parameter S, the abstract member SOut is gonna work for us ;)
    */
    trait Second[S <: HList] {
      type SOut   // We want this information for output, also type param S will help to infer this SOut.
      def apply(value: S): SOut
    }

   /*
    * Info: 1. used Aux[S <: HList, SO] cuz wanted to summon<make an effort to produce (some quality) from within oneself> instances
    *       2. return type of apply is: Aux[S, instance.SOut] cuz we wanted to preserve type info. no type-erasure.
    *          apply method does not erase type member information
    *       3. implicitly from scala.Predef has that undesired behaviour
    */
    object Second {
      type Aux[S <: HList, SO] = Second[S] { type SOut = SO }

      def apply[S <: HList](implicit instance: Second[S]): Aux[S, instance.SOut] = instance

      implicit def hlistSecond[A, B, Rest <: HList]: Aux[A :: B :: Rest, B] = new Second[A :: B :: Rest] {
        type SOut = B
        def apply(value: A :: B :: Rest): B = value.tail.head
      }
    }

}
