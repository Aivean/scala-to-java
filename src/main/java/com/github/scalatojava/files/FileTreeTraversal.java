package com.github.scalatojava.files;

import java.io.File;

/**
 * @author eitanraviv@github
 * @since 21 Sept 2015.
 */
public class FileTreeTraversal
{
	protected TraverseStrategy cs;

	public void traverse(File root, TraverseStrategy cs)
	{
		this.cs = cs;
		if (root.isFile())
		{
			cs.handleFile(root);
		}
		else
		{
			traverseRecursive(root);
		}
		cs.handleDone();
	}

	protected void traverseRecursive(File file)
	{
		if (file == null)
		{
			return;
		}
		else if (file.isFile())
		{
			cs.handleFile(file);
		}
		else
		{
			cs.handleDir(file);
			for (File f : file.listFiles())
			{
				traverseRecursive(f);
			}
		}
	}
}
