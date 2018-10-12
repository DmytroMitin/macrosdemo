//import shapeless.labelled.FieldType
//import shapeless.{HList, HNil, LabelledGeneric, Witness, ::}
import scala.language.experimental.macros
import scala.reflect.macros.whitebox

object ArrayLikeFields {
  def extract[T](implicit extr: Extract[T]): Set[String] = extr()

  trait Extract[T] {
    def apply(): Set[String]
  }

  object Extract {
    implicit def materializeExtract[T]: Extract[T] = macro materializeExtractImpl[T]

    def materializeExtractImpl[T: c.WeakTypeTag](c: whitebox.Context): c.Expr[Extract[T]] = {
      import c.universe._

      val tree = weakTypeOf[T].decls
        .collectFirst {
          case m: MethodSymbol if m.isPrimaryConstructor => m
        }
        .map(y => y.paramLists.headOption.getOrElse(Seq.empty))
        .getOrElse(Seq.empty)
        .map(s => q"${s.name.decodedName.toString}")

      c.Expr[Extract[T]] {
        q"""new ArrayLikeFields.Extract[${weakTypeOf[T]}] {
          override def apply(): _root_.scala.collection.immutable.Set[_root_.java.lang.String] =
            _root_.scala.collection.immutable.Set(..$tree)
        }"""
      }
    }
  }
}

//object ArrayLikeFields {
//  def extract[T: Extract]: Set[String] = implicitly[Extract[T]].apply()
//
//  trait Extract[T] {
//    def apply(): Set[String]
//  }
//
//  object Extract {
//    def instance[T](strs: Set[String]): Extract[T] = () => strs
//
//    implicit def genericExtract[T, Repr <: HList](implicit
//                                                  labelledGeneric: LabelledGeneric.Aux[T, Repr],
//                                                  extract: Extract[Repr]
//                                                 ): Extract[T] = instance(extract())
//
//    implicit def hconsExtract[K <: Symbol, V, T <: HList](implicit
//                                                          extract: Extract[T],
//                                                          witness: Witness.Aux[K]
//                                                         ): Extract[FieldType[K, V] :: T] =
//      instance(extract() + witness.value.name)
//
//    implicit val hnilExtract: Extract[HNil] = instance(Set())
//  }
//}