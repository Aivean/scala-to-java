package com.github.scalatojava.files;

import java.io.File;

/**
 * @author eitanraviv@github
 * @since 21 Sept 2015.
 */
public interface TraverseStrategy
{
	Object handleFile(File file);
	Object handleDir(File dir);
	Object result();
	Object handleDone();
}
