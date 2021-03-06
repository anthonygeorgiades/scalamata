package org.everpeace.scalamata

object PDAExample extends App{

  import org.everpeace.scalamata._
  import org.everpeace.scalamata.DFAExample.Alpha
  import org.everpeace.scalamata.DFAExample.A
  import org.everpeace.scalamata.DFAExample.B

  sealed abstract class S
  case object S0 extends S
  case object S1 extends S
  case object S2 extends S

  // Pushdown Automata for a^nb^n:
  // initial state = S0, accepted state = S2
  //
  // +-(A,ε->A)-+            +-(B,A->ε)-+
  // +--->S0----+--(B,A->ε)--+--->S1----+-->(ε,φ->φ)-->S2
  val σ:(S, Either[Alpha,ε],Either[Either[Alpha, φ],ε]) =>Set[(S, Either[Either[Alpha, φ],ε])] = {
    case (S0, Left(A), Right(ε)) => Set((S0, Left(Left(A))))
    case (S0, Left(B), Left(Left(A))) => Set((S1, Right(ε)))
    case (S1, Left(B), Left(Left(A))) => Set((S1, Right(ε)))
    case (S1, Right(ε),Left(Right(φ))) => Set((S2, Left(Right(φ))))
    case _ => Set.empty
  }

  val a_nb_n = PDA(σ,S0,Set[S](S2))

  println("check for Pushdown Automata for A^nB^n")
  check(Seq(A,B),a_nb_n)
  check(Seq(A,A,B),a_nb_n)
  check(Seq(A,A,B,B),a_nb_n)

  def check(input: Seq[Alpha], a: Automata[Set[S], Alpha]) = println("%-6s".format(input) + "=> " + a.accept(input))
}
