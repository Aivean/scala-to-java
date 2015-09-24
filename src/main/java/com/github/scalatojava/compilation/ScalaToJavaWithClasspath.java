package com.github.scalatojava.compilation;

import com.github.scalatojava.Constants;
import com.github.scalatojava.NoRetryMetadataSystem;
import com.github.scalatojava.files.FileExtension;
import com.github.scalatojava.files.filters.ClassFileFilter;
import com.strobel.assembler.InputTypeLoader;
import com.strobel.assembler.metadata.Buffer;
import com.strobel.assembler.metadata.TypeDefinition;
import com.strobel.decompiler.DecompilationOptions;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.PlainTextOutput;
import com.strobel.decompiler.languages.Languages;
import com.strobel.decompiler.languages.java.JavaFormattingOptions;
import com.strobel.decompiler.languages.java.JavaLanguage;
import scala.collection.JavaConversions;
import scala.tools.nsc.Global;
import scala.tools.nsc.Settings;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.Arrays;

public class ScalaToJavaWithClasspath
{
	protected String classpath;
	protected boolean slim;

	public ScalaToJavaWithClasspath(String classpath, boolean slim)
	{
		this.classpath = classpath;
		this.slim = slim;
	}

	public String decompile(final File file) throws IOException
	{
		final InputTypeLoader typeLoader = new InputTypeLoader();
		typeLoader.tryLoadType(file.getPath(), new Buffer(Files.readAllBytes(file.toPath())));
		final JavaLanguage javaLanguage = Languages.java();
		final NoRetryMetadataSystem metadataSystem = new NoRetryMetadataSystem(typeLoader);
		final TypeDefinition resolvedType = metadataSystem.resolveType(FileExtension.remove(file.getName()), false);

		final DecompilerSettings settings = new DecompilerSettings();
		settings.setLanguage(javaLanguage);
		settings.setTypeLoader(typeLoader);
		settings.setFormattingOptions(JavaFormattingOptions.createDefault());
		final DecompilationOptions options = new DecompilationOptions();
		options.setSettings(settings);
		options.setFullDecompilation(true);
		final StringWriter writer = new StringWriter();
		final PlainTextOutput output = new PlainTextOutput(writer);
		javaLanguage.decompileType(resolvedType, output, options);
		writer.flush();
		String java = writer.toString();
		if (slim)
			java = java.replaceFirst("(?s)public final class _\\$\\s+.*", "");
		return java;
	}

	public File[] compile(final File scalaSourceFile, final File containerDirForCompiledFiles)
	{
		final Global.Run runner = getCompilerRun(containerDirForCompiledFiles);
		runner.compile(JavaConversions.asScalaBuffer(Arrays.asList(scalaSourceFile.getPath())).toList());
		return containerDirForCompiledFiles.listFiles(new ClassFileFilter());
	}

	private Global.Run getCompilerRun(File dir)
	{
		final Settings settings = new Settings();
		settings.processArgumentString(prepareCompileArgs(dir));
		final Global compiler = new Global(settings);
		return compiler.new Run();
	}

	private String prepareCompileArgs(File dir)
	{
		StringBuilder args = new StringBuilder();
		args.append(" -target:jvm-1.8")
			.append(" -explaintypes")
			.append(" -Xscript _")
			.append(" -usejavacp")
			.append(" -d ").append(dir.getPath());
		if (classpath != null && classpath.trim().length() > 0)
		{
			args.append(" -classpath \"").append(classpath).append("\"");
		}
		return args.toString();
	}

	public String apply(final String value) throws IOException
	{
		final File tempDir = Files.createTempDirectory("s2j").toFile();
		final File scalaFile = new File(tempDir, "_.scala");
		Files.write(scalaFile.toPath(), value.getBytes());
		File[] compileds = compile(scalaFile, tempDir);
		StringBuilder javaSource = new StringBuilder();
		for (File compiled : compileds)
		{
			String s = decompile(compiled);
			javaSource.append(s).append(Constants.LS);
		}
		return javaSource.toString();
	}
}
