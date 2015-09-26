package com.github.scalatojava;

import com.github.scalatojava.inputs.ScalaToJavaFromSourceTree;
import com.github.scalatojava.inputs.ScalaToJavaFromStandaloneFile;
import com.strobel.core.ArrayUtilities;
import com.strobel.core.StringUtilities;

import java.io.File;

/**
 * @author eitanraviv@github
 * @since 21 Sept 2015.
 */
public class MainExtended
{
	public static void main(String[] args) throws Exception
	{
		String pathToTranslate = System.getProperty("path");
		String repo = System.getProperty("repo");
		boolean slim = ArrayUtilities.contains(args, "--slim");


		if (StringUtilities.isNullOrEmpty(pathToTranslate))
		{
			new ScalaToJavaFromStandardInput().run(args);
			return;
		}

		File file = new File(pathToTranslate);
		if (file.isDirectory())
		{
			new ScalaToJavaFromSourceTree().run(pathToTranslate, repo, slim);
		}
		else if (file.isFile())
		{
			new ScalaToJavaFromStandaloneFile().run(pathToTranslate, repo, slim);
		}

		System.out.println("done");
	}
}
