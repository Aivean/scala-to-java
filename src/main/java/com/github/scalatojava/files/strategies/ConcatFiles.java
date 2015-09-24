package com.github.scalatojava.files.strategies;

import com.github.scalatojava.Constants;
import com.github.scalatojava.files.TraverseStrategy;
import com.strobel.core.StringUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * @author eitanraviv@github
 * @since 21 Sept 2015.
 */
public class ConcatFiles implements TraverseStrategy
{
	protected final File destFile;
	protected final StringBuilder concatenated;

	public ConcatFiles(File destFile)
	{
		if (destFile == null)
		{
			throw new IllegalArgumentException("dest file cannot be null");
		}
		else if (!destFile.exists())
		{
			try
			{
				destFile.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		this.destFile = destFile;
		concatenated = new StringBuilder();
	}

	@Override
	public Object handleFile(File file)
	{
		try
		{
			if (file == null || file.getName().contains("package.scala")) {
				return null;
			}
			final List<String> lines = Files.readAllLines(file.toPath());
			String text = StringUtilities.join(Constants.LS, lines);
			concatenated.append(text).append(Constants.LS);
		}
		catch (Exception e)
		{
			System.out.println("could not read file: " + file.toPath());
		}
		return null;
	}

	@Override
	public Object handleDir(File dir)
	{
		return null;
	}

	@Override
	public Object result() {
		return concatenated.toString();
	}

	@Override
	public Object handleDone()
	{
		try
		{
			Files.write(destFile.toPath(), concatenated.toString().getBytes(), StandardOpenOption.WRITE);
		}
		catch (IOException e)
		{
			System.out.println("could not write concatenated content to: " + destFile.toPath());
		}
		return null;
	}
}
