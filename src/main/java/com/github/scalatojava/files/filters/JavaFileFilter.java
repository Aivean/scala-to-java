package com.github.scalatojava.files.filters;

import java.io.File;
import java.io.FileFilter;

/**
 * @author eitanraviv@github
 * @since 21 Sept 2015.
 */
public class JavaFileFilter implements FileFilter
{
	@Override
	public boolean accept(File pathname)
	{
		return false;
	}
}
