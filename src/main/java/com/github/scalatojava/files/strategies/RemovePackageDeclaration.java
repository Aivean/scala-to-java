package com.github.scalatojava.files.strategies;

import com.github.scalatojava.files.TraverseStrategy;
import com.strobel.core.Closeables;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author eitanraviv@github
 * @since 21 Sept 2015
 */
public class RemovePackageDeclaration implements TraverseStrategy
{
	protected final File destFile;
	protected List<String> lines;

	public RemovePackageDeclaration(File destFile)
	{
		if (destFile == null || !destFile.exists())
		{
			throw new IllegalArgumentException("dest file does not exist");
		}
		this.destFile = destFile;
	}

	@Override
	public Object handleFile(File file)
	{
		if (file == null)
		{
			return null;
		}
		try
		{
			lines = IOUtils.readLines(new FileReader(file));
		}
		catch (IOException e)
		{
			e.printStackTrace();
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
		return destFile;
	}

	@Override
	public Object handleDone()
	{
		removePackageDeclarations();
		writeLines();
		return null;
	}

	private void removePackageDeclarations()
	{
		lines.removeIf(new Predicate<String>()
		{
			@Override
			public boolean test(String s)
			{
				return !s.matches("^package[\\s]+object.*") && s.matches("^package[\\s]+.*");
			}
		});
	}

	private void writeLines()
	{
		FileWriter writer = null;
		try
		{
			writer = new FileWriter(destFile);
			IOUtils.writeLines(lines, null, writer);
		}
		catch (IOException e)
		{
			System.out.println("could not write to: " + destFile.getAbsolutePath());
		}
		finally
		{
			if (writer != null)
			{
				Closeables.close(writer);
			}
		}
	}
}
