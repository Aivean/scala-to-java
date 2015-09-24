package com.github.scalatojava.inputs;

import com.github.scalatojava.Constants;
import com.github.scalatojava.compilation.ScalaToJavaWithClasspath;
import com.github.scalatojava.files.FileExtension;
import com.github.scalatojava.files.FileTreeTraversal;
import com.github.scalatojava.files.TraverseStrategy;
import com.github.scalatojava.files.strategies.CreateClasspath;
import com.strobel.core.StringUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author eitanraviv@github
 * @since 21 Sept 2015.
 */
public class ScalaToJavaFromStandaloneFile
{
	public void run(String filename, String repoPath, boolean slim) throws IOException
	{
		String scala = readScala(filename);
		String classpath = buildClasspath(repoPath);
		String java = translate(scala, classpath, slim);
		writeJava(filename, java);
	}

	private String buildClasspath(String repoPath)
	{
		final File repo = new File(repoPath);
		final FileTreeTraversal t = new FileTreeTraversal();
		TraverseStrategy classpath = new CreateClasspath();
		t.traverse(repo, classpath);
		return (String) classpath.result();
	}

	private String readScala(String filename) throws IOException
	{
		List<String> lines = Files.readAllLines(new File(filename).toPath());
		return StringUtilities.join(Constants.LS, lines);
	}

	private String translate(String scala, String classpath, boolean slim) throws IOException
	{
		return new ScalaToJavaWithClasspath(classpath, slim).apply(scala);
	}

	private void writeJava(String filename, String java) throws IOException
	{
		Path outPath = new File(FileExtension.replace(filename, ".java")).toPath();
		Files.write(outPath, java.getBytes());
		System.out.println("file was written to: " + outPath);
	}
}
