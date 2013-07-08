lms_tutorial
============

Used for illustrating what i learnt and did while learning LMS for Scala

How to run the example
======================
> show scala-version
[info] 2.10.1
> console
[info] Starting scala interpreter...
[info]
Welcome to Scala version 2.10.1 (Java HotSpot(TM) 64-Bit Server VM, Java 1.7.0_17).
Type in expressions to have them evaluated.
Type :help for more information.

scala> import scala.virtualization.lms.common._
import scala.virtualization.lms.common._

scala> val p = new Prog with LinearAlgebraExp with EffectExp
p: Prog with LinearAlgebraExp with scala.virtualization.lms.common.EffectExp = $anon$1@1d6a90fd

scala> val codegen = new ScalaGenEffect with ScalaGenLinearAlgebra { val IR: p.type = p }
codegen: scala.virtualization.lms.common.ScalaGenEffect with ScalaGenLinearAlgebra{val IR: <refinement>.type} = scala

scala> codegen.emitSource(p.f, "F", new java.io.PrintWriter(System.out))
/*****************************************
  Emitting Generated Code
*******************************************/
class F extends ((scala.collection.Seq[Double])=>(scala.collection.Seq[Double])) {
def apply(x0:scala.collection.Seq[Double]): scala.collection.Seq[Double] = {
val x1 = x0.map(x => x * 12.34)
x1
}
}
/*****************************************
  End of Generated Code
*******************************************/
res0: List[(codegen.IR.Sym[Any], Any)] = List()

scala>
