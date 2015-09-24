package com.github.scalatojava.files;

import com.strobel.core.StringUtilities;

/**
 * @author eitanraviv@github
 * @since 21 Sept 2015.
 */
public class FileExtension
{
	public static String replace(String filename, String extensionReplacement)
	{
		guardUndefined(filename);
		if (filename.lastIndexOf(".") == -1) {
			filename += ".";
		}
		if (StringUtilities.isNullOrEmpty(extensionReplacement)) {
			return filename;
		}
		if (extensionReplacement.startsWith(".")) {
			extensionReplacement = extensionReplacement.substring(1);
		}

		filename = filename.substring(0, filename.lastIndexOf(".") + 1) + extensionReplacement;
		guardUndefined(filename);
		return filename;
	}

	public static String get(String filename)
	{
		guardUndefined(filename);
		if (filename.lastIndexOf(".") == -1) {
			return "";
		}
		return filename.substring(filename.lastIndexOf(".") + 1, filename.length());
	}

	public static String remove(String filename)
	{
		guardUndefined(filename);
		if (filename.lastIndexOf(".") == -1) {
			return filename;
		}
		return filename.substring(0, filename.lastIndexOf("."));
	}

	private static void guardUndefined(String filename)
	{
		if (filename == null || filename.equals(".") || filename.equals(".."))
		{
			throw new IllegalArgumentException("the operation is undefined on this filename");
		}
	}
}
