package com.github.scalatojava.files;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * @author eitanraviv@github
 * @since 21 Sep 2015
 */
public class FileExtensionTest
{
	@Rule
	public ExpectedException exception = ExpectedException.none();

	/********** remove ************/

	@Test
	public void testRemove0()
	{
		//arrange + act
		final String filename = FileExtension.remove("");
		//assert
		assertEquals("", filename);
	}

	@Test
	public void testRemove1()
	{
		//arrange + act
		final String filename = FileExtension.remove("name");
		//assert
		assertEquals("name", filename);
	}

	@Test
	public void testRemove2()
	{
		//arrange + act
		final String filename = FileExtension.remove("name.");
		//assert
		assertEquals("name", filename);
	}

	@Test
	public void testRemove3()
	{
		//arrange + act
		final String filename = FileExtension.remove("name.e");
		//assert
		assertEquals("name", filename);
	}

	@Test
	public void testRemove4()
	{
		//arrange + act
		final String filename = FileExtension.remove("name.ext");
		//assert
		assertEquals("name", filename);
	}

	@Test
	public void testRemove5()
	{
		//arrange + act
		final String filename = FileExtension.remove("first.last.ext");
		//assert
		assertEquals("first.last", filename);
	}

	@Test
	public void testRemove6()
	{
		//arrange
		exception.expect(IllegalArgumentException.class);
		//act
		FileExtension.remove(".");
	}

	@Test
	public void testRemove7()
	{
		//arrange
		exception.expect(IllegalArgumentException.class);
		//act
		FileExtension.remove("..");
	}

	@Test
	public void testRemove8()
	{
		//arrange
		exception.expect(IllegalArgumentException.class);
		//act
		FileExtension.remove(null);
	}

	/********** get ************/

	@Test
	public void testGet0()
	{
		//arrange + act
		final String ext = FileExtension.get("");
		//assert
		assertEquals("", ext);
	}

	@Test
	public void testGet1()
	{
		//arrange + act
		final String ext = FileExtension.get("name");
		//assert
		assertEquals("", ext);
	}

	@Test
	public void testGet2()
	{
		//arrange + act
		final String ext = FileExtension.get("name.");
		//assert
		assertEquals("", ext);
	}

	@Test
	public void testGet3()
	{
		//arrange + act
		final String ext = FileExtension.get("name.e");
		//assert
		assertEquals("e", ext);
	}

	@Test
	public void testGet4()
	{
		//arrange + act
		final String ext = FileExtension.get("name.ext");
		//assert
		assertEquals("ext", ext);
	}

	@Test
	public void testGet5()
	{
		//arrange + act
		final String ext = FileExtension.get("first.last.ext");
		//assert
		assertEquals("ext", ext);
	}

	@Test
	public void testGet6()
	{
		//arrange
		exception.expect(IllegalArgumentException.class);
		//act
		FileExtension.get(".");
	}

	@Test
	public void testGet7()
	{
		//arrange
		exception.expect(IllegalArgumentException.class);
		//act
		FileExtension.get("..");
	}

	@Test
	public void testGet8()
	{
		//arrange
		exception.expect(IllegalArgumentException.class);
		//act
		FileExtension.get(null);
	}

	/********** replace ************/

	@Test
	public void testReplace0()
	{
		//arrange + act
		final String filename = FileExtension.replace("", "");
		//assert
		assertEquals(".", filename);
	}

	@Test
	public void testReplace1()
	{
		//arrange + act
		final String filename = FileExtension.replace("name", "txt");
		//assert
		assertEquals("name.txt", filename);
	}

	@Test
	public void testReplace2()
	{
		//arrange + act
		final String filename = FileExtension.replace("name.", "txt");
		//assert
		assertEquals("name.txt", filename);
	}

	@Test
	public void testReplace3()
	{
		//arrange + act
		final String filename = FileExtension.replace("name.e", "txt");
		//assert
		assertEquals("name.txt", filename);
	}

	@Test
	public void testReplace4()
	{
		//arrange + act
		final String filename = FileExtension.replace("name.ext", "txt");
		//assert
		assertEquals("name.txt", filename);
	}

	@Test
	public void testReplace5()
	{
		//arrange + act
		final String filename = FileExtension.replace("first.last.ext", "txt");
		//assert
		assertEquals("first.last.txt", filename);
	}

	@Test
	public void testReplace6()
	{
		//arrange
		exception.expect(IllegalArgumentException.class);
		//act
		FileExtension.replace(".", "");
	}

	@Test
	public void testReplace7()
	{
		//arrange
		exception.expect(IllegalArgumentException.class);
		//act
		FileExtension.replace("..", "");
	}

	@Test
	public void testReplace8()
	{
		//arrange
		exception.expect(IllegalArgumentException.class);
		//act
		FileExtension.replace(null, "");
	}

	@Test
	public void testReplace9()
	{
		//act
		final String filename = FileExtension.replace("", null);
		assertEquals(".", filename);
	}

	@Test
	public void testReplace10()
	{
		//arrange + act
		final String filename = FileExtension.replace("name", ".txt");
		//assert
		assertEquals("name.txt", filename);
	}

	@Test
	public void testReplace11()
	{
		//arrange + act
		final String filename = FileExtension.replace("name.", ".txt");
		//assert
		assertEquals("name.txt", filename);
	}

	@Test
	public void testReplace12()
	{
		//arrange + act
		final String filename = FileExtension.replace("name.e", ".txt");
		//assert
		assertEquals("name.txt", filename);
	}

	@Test
	public void testReplace13()
	{
		//arrange + act
		final String filename = FileExtension.replace("", ".txt");
		//assert
		assertEquals(".txt", filename);
	}

	@Test
	public void testReplace14()
	{
		//arrange + act
		final String filename = FileExtension.replace("", "txt");
		//assert
		assertEquals(".txt", filename);
	}

	@Test
	public void testReplace15()
	{
		//arrange
		exception.expect(IllegalArgumentException.class);
		//act
		FileExtension.replace("", ".");
	}

	@Test
	public void testReplace16()
	{
		//arrange
		exception.expect(IllegalArgumentException.class);
		//act
		FileExtension.replace(".", "");
	}
}
