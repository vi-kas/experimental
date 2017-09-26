package ops

trait Penultimate[P] {
  type Out

  def apply(p: P): Out
}

object Penultimate {
  type Aux[P, O]  = Penultimate[P] { type Out = O}

  def apply[P](implicit penultimate: Penultimate[P]): Aux[P, penultimate.Out]/* 1. */ = penultimate
  /* method-end notes
     ------------
     Take Aways:  1. To Ensure Type members are visible on instances.
     ------------
   */
}

package penultimate {
  import shapeless.HList
  import shapeless.ops.hlist.{Init, Last}

  object Implicits {
    implicit def hlistPenultimate[H <: HList, M <: HList, O](
                                                           implicit
                                                           init: Init.Aux[H, M],
                                                           last: Last.Aux[M, O]
                                                           ): Penultimate.Aux[H, O] = {
                                  new Penultimate[H] {
                                    type Out = O
                                    def apply(p: H): O = last.apply(init.apply(p))
                                  }
    }

    /*implicit class PenultimateOps[P](p: P) {
      def penultimate[P](implicit instance: Penultimate[P]) : instance.Out = instance.apply(p)
    }*/

  }

}





