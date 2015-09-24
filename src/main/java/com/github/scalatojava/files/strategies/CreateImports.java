package com.github.scalatojava.files.strategies;

import com.github.scalatojava.files.TraverseStrategy;

import java.io.File;
import java.util.NavigableSet;
import java.util.TreeSet;

import static com.github.scalatojava.Constants.FS;
import static com.github.scalatojava.Constants.LS;

/**
 * @author eitanraviv@github
 * @since 9/8/15
 */
public class CreateImports implements TraverseStrategy
{
	protected final NavigableSet<String> importSet;
	protected StringBuilder importStr;

	public CreateImports()
	{
		importSet = new TreeSet<>();
		importStr = new StringBuilder();
	}

	@Override
	public Object handleFile(File file)
	{
		return null;
	}

	@Override
	public Object handleDir(File dir)
	{
		String packageName = parsePackage(dir);
		if (packageName != null)
		{
			importSet.add("import " + packageName + "._");
		}
		return null;
	}

	private String parsePackage(File dir)
	{
		String lang;
		if (dir == null)
		{
			return null;
		}
		else if (!hasFiles(dir))
		{
			return null;
		}
		else if (isSourceDir(dir, "java"))
		{
			lang = "java";
		}
		else if (isSourceDir(dir, "scala"))
		{
			lang = "scala";
		}
		else
		{
			return null;
		}
		String dirPath = dir.getAbsolutePath();
		String packagePath = dirPath.substring(dirPath.lastIndexOf(lang) + lang.length() + FS.length(), dirPath.length());
		return packagePath.replace(FS, ".");
	}

	private boolean isSourceDir(File dir, String lang)
	{
		final String p = dir.getAbsolutePath();
		return p.contains("src" + FS + "main" + FS + lang) && !p.endsWith(lang);
	}

	private boolean hasFiles(File dir)
	{
		return dir != null && dir.listFiles().length > 0;
	}

	@Override
	public Object result()
	{
		return importStr.toString();
	}

	@Override
	public Object handleDone()
	{
		for (String s : importSet)
		{
			importStr.append(s).append(LS);
		}
		return importSet;
	}
}