package com.github.scalatojava.files.strategies;

import com.github.scalatojava.Constants;
import com.github.scalatojava.files.FileTreeTraversal;
import com.github.scalatojava.files.TraverseStrategy;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author eitanraviv@github
 * @since 21 Sept 2015.
 */
public class CopyScalaFilesTest
{
	@Test
	public void test() throws IOException
	{
		final File destDir = Files.createTempDirectory(Constants.S2J + CopyScalaFilesTest.class.getSimpleName()).toFile();
		System.out.println(String.format("all files written to %s", destDir.toPath()));
		destDir.delete();
		destDir.mkdir();

		TraverseStrategy s = new CopyScalaFiles(destDir, false);
		FileTreeTraversal t = new FileTreeTraversal();
		t.traverse(new File("path/to/module/to/translate"), s);
	}

	@Test
	public void testOverwrite() throws IOException
	{
		final File destDir = Files.createTempDirectory(Constants.S2J + CopyScalaFilesTest.class.getSimpleName()).toFile();
		System.out.println(String.format("all files written to %s", destDir.toPath()));
		destDir.delete();
		destDir.mkdir();

		TraverseStrategy s = new CopyScalaFiles(destDir, true);
		FileTreeTraversal t = new FileTreeTraversal();
		t.traverse(new File("path/to/module/to/translate"), s);
	}
}