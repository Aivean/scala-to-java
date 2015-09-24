package com.github.scalatojava.files.strategies;

import com.github.scalatojava.files.TraverseStrategy;
import com.github.scalatojava.files.filters.ScalaFileFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @author eitanraviv@github
 * @since 21 Sept 2015.
 */
public class CopyScalaFiles implements TraverseStrategy
{
	protected final File destDir;
	private final boolean overwrite;

	public CopyScalaFiles(File destDir, boolean overwrite)
	{
		if (destDir == null || !destDir.exists())
		{
			throw new IllegalArgumentException("dest dir does not exist");
		}
		this.destDir = destDir;
		this.overwrite = overwrite;
	}

	@Override
	public Object handleFile(File file)
	{
		return null;
	}

	@Override
	public Object handleDir(File dir)
	{
		if (dir == null)
		{
			return null;
		}
		File[] scalaSources = dir.listFiles(new ScalaFileFilter());
		for (File file : scalaSources)
		{

			final Path sourcePath = file.toPath();
			final Path destPath = destDir.toPath().resolve(file.getName());
			try
			{
				if (overwrite)
				{
					Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
				}
				else if (!destPath.toFile().exists())
				{
					Files.copy(sourcePath, destPath);
				}
				else
				{
					System.out.println(String.format("file already exists, could not copy %s to %s", sourcePath, destPath));
				}
			}
			catch (IOException e)
			{
				throw new IllegalArgumentException(String.format("could not copy %s to %s", sourcePath, destPath), e);
			}
		}
		return null;
	}

	@Override
	public Object result()
	{
		return destDir;
	}

	@Override
	public Object handleDone() {
		return null;
	}
}
