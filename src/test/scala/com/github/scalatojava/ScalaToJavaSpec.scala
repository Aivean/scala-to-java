package com.github.scalatojava

import org.scalatest._

class ScalaToJavaSpec extends FlatSpec with Matchers {

  it should "be able to convert scala to java" in {
    ScalaToJava("""println("hello world")""") should include("class _")
  }
}
