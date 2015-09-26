package com.github.scalatojava.inputs;

import com.github.scalatojava.Constants;
import com.github.scalatojava.compilation.ScalaToJavaWithClasspath;
import com.github.scalatojava.files.FileExtension;
import com.github.scalatojava.files.FileTreeTraversal;
import com.github.scalatojava.files.TraverseStrategy;
import com.github.scalatojava.files.filters.ClassFileFilter;
import com.github.scalatojava.files.strategies.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author eitanraviv@github
 * @since 21 Sept 2015.
 */
public class ScalaToJavaFromSourceTree
{
	public void run(String rootPath, String repoPath, boolean slim) throws IOException
	{
		/**
		 * Sources root dir is the root dir of the compilation unit.
		 * compilation unit - a collection of files which can produce a jar;
		 * for example, a project in eclipse or a module in intellij-idea
		 */
		final File sourcesRootDir = new File(rootPath);

		//The repository where all the dependencies of the above compilation unit reside
		final File repo = new File(repoPath);

		//Temporary directory where the output of this program will be written to
		final File containerDir = Files.createTempDirectory(Constants.S2J).toFile();
		System.out.println(String.format("all files written to %s", containerDir.toPath()));

		//output directories
		final File collectDir = containerDir.toPath().resolve("collect").toFile();
		final File concatDir = containerDir.toPath().resolve("concat").toFile();
		final File compileDir = containerDir.toPath().resolve("compiled").toFile();

		boolean success = collectDir.mkdir();
		success &= concatDir.mkdir();
		success &= compileDir.mkdir();

		if (!success) {
			throw new IllegalStateException("could not create output dirs");
		}

		final File concatenatedScala = concatDir.toPath().resolve("concat.scala").toFile();
		final File adjustedScala = concatDir.toPath().resolve("adjusted.scala").toFile();
		final FileTreeTraversal t = new FileTreeTraversal();

		//copy all scala file in the source tree to a single directory for further handling
		TraverseStrategy copy = new CopyScalaFiles(collectDir, true);
		t.traverse(sourcesRootDir, copy);

		//the compiler requires a single file containing all the scala to be translated
		TraverseStrategy concat = new ConcatFiles(concatenatedScala);
		t.traverse(collectDir, concat);

		adjustedScala.createNewFile();
		//the compiler does not accept package declarations
		TraverseStrategy remove = new RemovePackageDeclaration(adjustedScala);
		t.traverse(concatenatedScala, remove);

		//if there are java files in the source tree, the corresponding imports need to
		//be added to the scala file
		TraverseStrategy imports = new CreateImports();
		t.traverse(sourcesRootDir, imports);

		TraverseStrategy prependImports = new PrependText((String) imports.result());
		t.traverse(adjustedScala, prependImports);

		//build the classpath from the repo
		TraverseStrategy classpath = new CreateClasspath();
		t.traverse(repo, classpath);

		translate(compileDir, adjustedScala, (String) classpath.result(), slim);
		cleanup(compileDir);
		System.out.println(String.format("all files written to %s", containerDir.toPath()));
	}

	/**
	 * compile the scala file to class files, and then decompile them one by one
	 * @param compileDir the directory that will contain the compiled .class files and the decompiled .java files
	 * @param adjustedScala the scala file to translate from
	 * @param classpath the classpath of the scala sources in the scala file
	 * @param slim directive to the decompiler about the output
	 * @throws IOException
	 */
	private void translate(File compileDir, File adjustedScala, String classpath, boolean slim) throws IOException
	{
		ScalaToJavaWithClasspath s2j = new ScalaToJavaWithClasspath(classpath, slim);
		s2j.compile(adjustedScala, compileDir);

		for (File f: compileDir.listFiles(new ClassFileFilter()))
		{
			System.out.println("decompiling: " + f.getName());
			String java = s2j.decompile(f);
			File jf = new File(FileExtension.replace(f.getAbsolutePath(), ".java"));
			Files.write(jf.toPath(), java.getBytes());
		}
	}

	/**
	 * delete the .class files from the output directory
	 * @param compileDir
	 */
	private void cleanup(File compileDir)
	{
		TraverseStrategy delete = new DeleteClassFiles();
		new FileTreeTraversal().traverse(compileDir, delete);
		if (! (boolean)delete.result()) {
			System.out.println("could not delete all class files in " + compileDir.getName());
		}
	}
}
