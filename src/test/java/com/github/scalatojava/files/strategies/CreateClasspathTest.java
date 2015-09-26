package com.github.scalatojava.files.strategies;

import com.github.scalatojava.files.FileTreeTraversal;
import com.github.scalatojava.files.TraverseStrategy;
import org.junit.Test;

import java.io.File;

/**
 * @author eitanraviv@github
 * @since 21 Sept 2015.
 */
public class CreateClasspathTest
{
	@Test
	public void test()
	{
		final File jarRepo = new File("/path/to/repository/with/jars");
		FileTreeTraversal t = new FileTreeTraversal();

		TraverseStrategy s1 = new CreateClasspath();
		t.traverse(jarRepo, s1);
		System.out.println(s1.result());
	}
}