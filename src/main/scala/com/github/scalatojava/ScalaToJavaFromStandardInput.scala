package com.github.scalatojava

class ScalaToJavaFromStandardInput
{
	def run(args: Array[String])
	{
		val source = Iterator.continually(scala.io.StdIn.readLine()).takeWhile(
			_ != null).mkString("\n")

		println()
		val java = ScalaToJava(source)

		println(if (args.contains("--slim")) java.replaceFirst("(?s)public final class _\\$\\s+.*", "") else java)
	}
}
