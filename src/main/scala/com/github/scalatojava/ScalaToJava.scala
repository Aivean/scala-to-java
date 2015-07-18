package com.github.scalatojava

import java.io.{File, StringWriter}
import java.nio.file.{Paths, Files}

import com.strobel.assembler.InputTypeLoader
import com.strobel.assembler.metadata._
import com.strobel.decompiler.languages.Languages
import com.strobel.decompiler.languages.java.JavaFormattingOptions
import com.strobel.decompiler.{PlainTextOutput, DecompilerSettings, DecompilationOptions}
import scala.collection.mutable
import scala.tools.nsc._
import java.io._
import java.util.Arrays

/**
 *  Scala-to-Java translator
 *  @author <a href="mailto:stanley.shyiko@gmail.com">Stanley Shyiko</a>
 */
object ScalaToJava {

  def decompile(file: File): String = {
    val settings = new DecompilerSettings
    settings.setLanguage(Languages.java)
    settings.setTypeLoader(new InputTypeLoader)
    settings.setFormattingOptions(JavaFormattingOptions.createDefault)

    val options = new DecompilationOptions
    options.setSettings(settings)
    options.setFullDecompilation(true)

    settings.getTypeLoader.tryLoadType(file.getPath,
      new Buffer(Files.readAllBytes(Paths.get(file.getPath))))

    val metadataSystem = new NoRetryMetadataSystem(settings.getTypeLoader)
    val resolvedType = metadataSystem.resolveType(file.getName.substring(0,
      file.getName.length - ".class".length), mightBePrimitive = false)
    val writer = new StringWriter
    val output = new PlainTextOutput(writer)
    settings.getLanguage.decompileType(resolvedType, output, options)
    writer.flush()
    writer.toString
  }

  def compile(input: File, output: File): Array[File] = {
    val settings = new Settings
    settings processArgumentString "-target:jvm-1.8 -Xscript _ -usejavacp -d " +
      output.getPath
    val compiler = new Global(settings)
    val runner = new compiler.Run
    runner.compile(List(input.getPath))
    output.listFiles(new FilenameFilter {
      override def accept(dir: File, name: String): Boolean = name.endsWith(".class")
    })
  }

  def apply(value: String): String = {
    val output = Files.createTempDirectory("s2j").toFile
    val input = new File(output, "_.scala")
    Files.write(input.toPath, Arrays.asList(value))
    compile(input, output).map(decompile).mkString("\n\n")
  }

  class NoRetryMetadataSystem(typeLoader: ITypeLoader) extends MetadataSystem(typeLoader) {

    val failedTypes = mutable.Set[String]()

    override def resolveType(descriptor: String, mightBePrimitive: Boolean): TypeDefinition = {
      if (failedTypes.contains(descriptor)) {
        return null
      }
      val r = super.resolveType(descriptor, mightBePrimitive)
      if (r == null) {
        failedTypes.add(descriptor)
      }
      r
    }
  }
}

