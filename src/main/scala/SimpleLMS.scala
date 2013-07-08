import scala.virtualization.lms.common._

// We will attempt to create a DSL that allows us to 
// perform the basic function of scaling a vector
trait LinearAlgebra extends Base {
    // The abstract type 'Vector' will represent the vector data type    
    type Vector
    def vector_scale(v: Rep[Vector], k: Rep[Double]) : Rep[Vector]

    // the implicit class allows us to say "<some vector> * <scale factor>"
    implicit class VectorOps(v: Rep[Vector]) {
        def * (k: Rep[Double]) = vector_scale(v, k)
    }
}

// Base provides us two implicit functions that allows
// Unit/Null expressions to be lifted to Rep[Unit] / Rep[Null]
// and both of them invoke 'unit' which we emulate which lifts
// Double to Rep[Double]
trait Prog extends LinearAlgebra {
    def f(v: Rep[Vector]) : Rep[Vector] = v * unit(12.34)
}

trait ShallowInterpreter extends Base {
    override type Rep[+A] = A
    override protected def unit[A : Manifest](a: A ) = a
}

trait LinearAlgebraInterpreter extends LinearAlgebra with ShallowInterpreter {
    override type Vector = Seq[Double]
    override def vector_scale(v: Seq[Double], k: Double) = v map ( _ * k )
}
// At this point in time, the code we have seen so far is regular Scala code 
// build-out for a DSL; the generated code cannot benefit from optimizations
// We need another way and see below

trait LinearAlgebraExp extends LinearAlgebra with BaseExp {

    case class VectorScale(v: Exp[Vector], k: Exp[Double]) extends Def[Vector]

    override def vector_scale(v: Exp[Vector], k : Exp[Double]) = toAtom(VectorScale(v, k))
    override type Vector = Seq[Double]
}

trait ScalaGenLinearAlgebra extends ScalaGenBase {
    val IR : LinearAlgebraExp
    import IR._

    override def emitNode(sym: Sym[Any], node: Def[Any]) : Unit = node match {
        case VectorScale(v, k) => {
            emitValDef(sym, quote(v) + ".map(x => x * " + quote(k) + ")")
        }
        case _ => super.emitNode(sym, node)
    }
}

