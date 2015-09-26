package com.github.scalatojava.files.strategies;

import com.github.scalatojava.files.TraverseStrategy;
import com.strobel.core.Closeables;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

/**
 * @author eitanraviv@github
 * @since 21 Sept 2015.
 */
public class PrependText implements TraverseStrategy
{
	protected static final String LS = System.getProperty("line.separator");
	protected String prepend;
	protected StringBuilder newText;

	public PrependText(String prepend)
	{
		this.prepend = prepend;
	}

	@Override
	public Object handleFile(File file)
	{
		if (file == null)
		{
			return null;
		}
		FileReader rd = null;
		FileWriter fw = null;
		try
		{
			rd = new FileReader(file);
			List<String> lines = IOUtils.readLines(rd);
			StringBuilder text = concatLines(lines);
			newText = prepend(text);
			fw = new FileWriter(file);
			fw.write(newText.toString());
			return newText;
		}
		catch (Exception e)
		{
			System.out.println("could not read file: " + file.getAbsolutePath());
			return null;
		}
		finally
		{
			Closeables.close(rd, fw);
		}
	}

	private StringBuilder prepend(StringBuilder text)
	{
		if (prepend == null)
		{
			return text;
		}
		else
		{
			return new StringBuilder(prepend.length() + text.length() + LS.length()).
					append(prepend).
					append(LS).
					append(text);
		}
	}

	private StringBuilder concatLines(List<String> lines)
	{
		StringBuilder sb = new StringBuilder();
		for (String line: lines)
		{
			sb.append(line).append(LS);
		}
		return sb;
	}

	@Override
	public Object handleDir(File dir)
	{
		return null;
	}

	@Override
	public Object result() {
		return newText;
	}

	@Override
	public Object handleDone()
	{
		return null;
	}
}
