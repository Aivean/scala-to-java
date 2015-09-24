package com.github.scalatojava.files.strategies;

import com.github.scalatojava.Constants;
import com.github.scalatojava.files.TraverseStrategy;

import java.io.File;

/**
 * @author eitanraviv@github
 * @since 21 Sept 2015.
 */
public class CreateClasspath implements TraverseStrategy
{
	protected final StringBuilder classpath;

	public CreateClasspath()
	{
		classpath = new StringBuilder();
	}

	@Override
	public Object handleFile(File file)
	{
		if (isCompiledJar(file))
		{
			classpath.append(file.getAbsolutePath());
			classpath.append(Constants.PS);
		}
		return null;
	}

	private boolean isCompiledJar(File file)
	{
		return file != null && file.getName().endsWith(".jar") &&
				!file.getName().contains("dependencies.jar") &&
				!file.getName().contains("tests.jar") &&
				!file.getName().contains("javadoc.jar") &&
				!file.getName().contains("sources.jar");
	}

	@Override
	public Object handleDir(File dir)
	{
		return null;
	}

	@Override
	public Object result()
	{
		return classpath.toString();
	}

	@Override
	public Object handleDone()
	{
		classpath.append('.');
		return classpath;
	}
}