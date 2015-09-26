package com.github.scalatojava.files.strategies;

import com.github.scalatojava.files.FileExtension;
import com.github.scalatojava.files.TraverseStrategy;

import java.io.File;

/**
 * @author eitanraviv@github
 * @since 21 Sept 2015.
 */
public class DeleteClassFiles implements TraverseStrategy
{
	protected boolean allDeleted = true;

	@Override
	public Object handleFile(File file)
	{
		if (file != null && "class".equals(FileExtension.get(file.getName())))
		{
			allDeleted &= file.delete();
		}
		return null;
	}

	@Override
	public Object handleDir(File dir)
	{
		return null;
	}

	@Override
	public Object result()
	{
		return allDeleted;
	}

	@Override
	public Object handleDone() {
		return allDeleted;
	}
}
