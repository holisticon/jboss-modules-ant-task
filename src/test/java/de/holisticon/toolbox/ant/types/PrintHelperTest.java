package de.holisticon.toolbox.ant.types;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PrintHelperTest {

	private ByteArrayOutputStream outContent;
	private PrintStream stream;
	private PrintHelper testee;

	@Before
	public void init() {
		outContent = new ByteArrayOutputStream();
		stream = new PrintStream(outContent);
		testee = PrintHelper.module(stream, "test", "1.0");
	}

	@Test
	public void testIgnoreResources() throws UnsupportedEncodingException {
		final List<String> resources = new ArrayList<String>();
		resources.add("foo.bar.any.name");
		resources.add("dir" + File.separator + "forbiddenName");
		resources.add(File.separator + "forbiddenName");
		resources.add("forbiddenName" + File.separator);
		testee.printModuleResources(resources);
		String result = outContent.toString("UTF-8");

		assertTrue(result.contains("foo.bar.any.name"));
		assertFalse(result.contains("forbiddenName"));
	}
}
