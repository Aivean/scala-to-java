package com.github.scalatojava

/**
 * @author <a href="mailto:ivan.zaytsev@webamg.com">Ivan Zaytsev</a>
 *         2015-07-12
 */
object Main extends App {
  val source = Iterator.continually(scala.io.StdIn.readLine()).takeWhile(
    _ != null).mkString("\n")

  println()
  private val java = ScalaToJava(source)

  println(java.replaceFirst("(?s)public final class _\\$\\s+.*", ""))
}