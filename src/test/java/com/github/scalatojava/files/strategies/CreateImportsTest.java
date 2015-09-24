package com.github.scalatojava.files.strategies;

import com.github.scalatojava.files.FileTreeTraversal;
import com.github.scalatojava.files.TraverseStrategy;
import org.junit.Test;

import java.io.File;

/**
 * @author eitanraviv@github
 * @since 21 Sept 2015.
 */
public class CreateImportsTest
{
	@Test
	public void test()
	{
		FileTreeTraversal t = new FileTreeTraversal();
		final File sourceDir = new File("path/to/module/to/translate");
		TraverseStrategy s = new CreateImports();
		t.traverse(sourceDir, s);
		System.out.println(s.result());
	}
}