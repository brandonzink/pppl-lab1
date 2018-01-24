package jsy.student

import jsy.lab1._

object Lab1 extends jsy.util.JsyApplication with jsy.lab1.Lab1Like {
  import jsy.lab1.Parser
  import jsy.lab1.ast._

  /*
   * CSCI 3155: Lab 1
   * Brandon Zink
   *
   * Partner: Cameron Connor
   * Collaborators: Google for help with the math
   */

  /*
   * Fill in the appropriate portions above by replacing things delimited
   * by '<'... '>'.
   *
   * Replace the '???' expression with your code in each function. The
   * '???' expression is a Scala expression that throws a NotImplementedError
   * exception.
   *
   * Do not make other modifications to this template, such as
   * - adding "extends App" or "extends Application" to your Lab object,
   * - adding a "main" method, and
   * - leaving any failing asserts.
   *
   * Your lab will not be graded if it does not compile.
   *
   * This template compiles without error. Before you submit comment out any
   * code that does not compile or causes a failing assert.  Simply put in a
   * '???' as needed to get something that compiles without error.
   */

  /*
   * Example: Test-driven development of plus
   *
   * A convenient, quick-and-dirty way to experiment, especially with small code
   * fragments, is to use the interactive Scala interpreter. The simplest way
   * to use the interactive Scala interpreter in IntelliJ is through a worksheet,
   * such as Lab1Worksheet.sc. A Scala Worksheet (e.g., Lab1Worksheet.sc) is code
   * evaluated in the context of the project with results for each expression
   * shown inline.
   *
   * Step 0: Sketch an implementation in Lab1.scala using ??? for unimmplemented things.
   * Step 1: Do some experimentation in Lab1Worksheet.sc.
   * Step 2: Write a test in Lab1Spec.scala, which should initially fail because of the ???.
   * Step 3: Fill in the ??? to finish the implementation to make your test pass.
   */

  //def plus(x: Int, y: Int): Int = ???
  def plus(x: Int, y: Int): Int = x + y

  /* Exercises */

  def abs(n: Double): Double = if (n < 0) -n else n

  //if a is true, then the answer is not b, else a is false and the answer is just b
  def xor(a: Boolean, b: Boolean): Boolean = if(a) !b else b

  def repeat(s: String, n: Int): String = {
    n compare 0 match{
      //If illegal entry, throw error
      case -1 => throw new IllegalArgumentException

      //If 0, return empty
      case 0 => ""

      //Else do the specified repeat using recursion
      case 1 => s + repeat(s,n-1)
    }
  }

  //Using the function from the pdf and the math library "pow" function
  def sqrtStep(c: Double, xn: Double): Double = {
    xn-((Math.pow(xn, 2)-c)/(2*xn))
  }

  def sqrtN(c: Double, x0: Double, n: Int): Double = {
    //Make sure that x is greater than 0
    require(n >= 0)

    //If 0 return 0, else use recursion to compute (I googled the math on that one)
    n match{
      case 0 => 0
      case _ => sqrtN(c, sqrtStep(c, x0), n-1)
    }
  }

  def sqrtErr(c: Double, x0: Double, epsilon: Double): Double = {
    //Require positive values, or else the math won't work
    require(x0 >= 0 && epsilon > 0)

    //If they don't match, keep running the recursion, if they do, return
    epsilon compare abs(x0 * x0 - c) match {
      case -1 => sqrtErr(c, sqrtStep(c, x0), epsilon)
      case _ => x0
    }
  }

  def sqrt(c: Double): Double = {
    require(c >= 0)
    if (c == 0) 0 else sqrtErr(c, 1.0, 0.0001)
  }

  /* Search Tree */

  // Defined in Lab1Like.scala:
  //
  // sealed abstract class SearchTree
  // case object Empty extends SearchTree
  // case class Node(l: SearchTree, d: Int, r: SearchTree) extends SearchTree

  def repOk(t: SearchTree): Boolean = {
    def check(t: SearchTree, min: Int, max: Int): Boolean = t match {
      case Empty => true

      //Check if both children are within range of children nodes, else recurse over children
      case Node(l, d, r) => if (d < min || d > max) false else check(l, min, d) && check(r, d, max)
    }
    check(t, Int.MinValue, Int.MaxValue)
  }

  def insert(t: SearchTree, n: Int): SearchTree = t match{
    //Once you hit the end of the line where it should be inserted
    case Empty => Node(Empty, n, Empty)
    //Recurse depending on greater than less than until it finds the correct spot
    case Node(l, d, r) => if (d > n) Node(insert(l, n), d, r) else Node(l, d, insert(r, n))
  }

  def deleteMin(t: SearchTree): (SearchTree, Int) = {
    require(t != Empty)
    (t: @unchecked) match {
      case Node(Empty, d, r) => (r, d)
      case Node(l, d, r) =>
        val (l1, m) = deleteMin(l)

        //Return the seach tree and the deleted min value
        (Node(l1, d, r), m)
    }
  }

  //EXPLAIN
  def delete(t: SearchTree, n: Int): SearchTree = t match {
    case Empty => Empty
    case Node(Empty, d, Empty) => n compare d match {
      case 0 => Empty
      case _ => Node(Empty, d, Empty)
    }
    case Node(l, d, r) => n compare d match {
      case -1 => Node(delete(l, n), d, r) // n < d
      case 1 => Node(l, d, delete(r, n)) // n > d
      case 0 => { // n == d
        val (t1, x) = deleteMin(r)
        Node(l, x, t1)
      }
    }
  }

  /* JavaScripty */

  def eval(e: Expr): Double = e match {
    case N(n) => n
    case Unary(Neg, e1) => -eval(e1)
    case Binary(Plus, e1, e2) => eval(e1) + eval(e2)
    case Binary(Minus, e1, e2) => eval(e1) - eval(e2)
    case Binary(Times, e1, e2) => eval(e1) * eval(e2)
    case Binary(Div, e1, e2) => eval(e1) / eval(e2)
  }

 // Interface to run your interpreter from a string.  This is convenient
 // for unit testing.
 def eval(s: String): Double = eval(Parser.parse(s))



 /* Interface to run your interpreter from the command-line.  You can ignore the code below. */

 def processFile(file: java.io.File) {
    if (debug) { println("Parsing ...") }

    val expr = Parser.parseFile(file)

    if (debug) {
      println("\nExpression AST:\n  " + expr)
      println("------------------------------------------------------------")
    }

    if (debug) { println("Evaluating ...") }

    val v = eval(expr)

    println(prettyNumber(v))
  }

}
