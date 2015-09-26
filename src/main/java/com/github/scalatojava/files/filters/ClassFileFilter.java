package com.github.scalatojava.files.filters;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author eitanraviv@github
 * @since 21 Sept 2015.
 */
public final class ClassFileFilter implements FilenameFilter {
	@Override
	public boolean accept(final File dir, final String name) {
		return name.endsWith(".class");
	}
}
