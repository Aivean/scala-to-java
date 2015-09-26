package com.github.scalatojava

import java.io.{File, StringWriter, _}
import java.nio.file.{Files, Paths}
import java.util.Arrays

import com.strobel.assembler.InputTypeLoader
import com.strobel.assembler.metadata._
import com.strobel.decompiler.languages.Languages
import com.strobel.decompiler.languages.java.JavaFormattingOptions
import com.strobel.decompiler.{DecompilationOptions, DecompilerSettings, PlainTextOutput}

import scala.tools.nsc._

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

  def compile(scalaFile: File, containerDir: File): Array[File] = {
    val settings = new Settings
    settings processArgumentString "-target:jvm-1.8 -Xscript _ -usejavacp -d " +
      containerDir.getPath
    val compiler = new Global(settings)
    val runner = new compiler.Run
    runner.compile(List(scalaFile.getPath))
    val compiledFiles: Array[File] = containerDir.listFiles(new FilenameFilter {
        override def accept(dir: File, name: String): Boolean = name.endsWith(".class")
      }
    )
    return compiledFiles
  }

  def apply(value: String): String = {
    val containerDir = Files.createTempDirectory("s2j").toFile
    val scalaFile = new File(containerDir, "_.scala")
    Files.write(scalaFile.toPath, Arrays.asList(value))
    val compiled: Array[File] = compile(scalaFile, containerDir)
	val map: Array[String] = compiled.map(decompile)
	map.mkString("\n\n")
  }

}

