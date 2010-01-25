package edu.iu.scipolicy.database.isi.load.utilities.parser;

import static org.junit.Assert.fail;

import org.cishell.utilities.IntegerParserWithDefault;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IntegerParserWithDefaultTest {
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParse_ValidTarget() {
		try {
			for (int ii = 0; ii < 100; ii++) {
				int parsedInt = IntegerParserWithDefault.parse("" + ii);

				if (parsedInt != ii) {
					fail("parse() failed on " + ii);
				}
			}

			for (int ii = -1; ii > -100; ii--) {
				int parsedInt = IntegerParserWithDefault.parse("" + ii);

				if (parsedInt != ii) {
					fail("parse() failed on " + ii);
				}
			}
		} catch (Exception e) {
			fail("An exception was thrown somehow: \"" + e.getMessage() + "\"");
		}
	}

	@Test
	public void testParse_InvalidTarget() {
		try {
			Integer parsedInt = IntegerParserWithDefault.parse("sdafsdf1");

			if (parsedInt != IntegerParserWithDefault.DEFAULT) {
				fail(
					"parse() returned " +
					parsedInt +
					" instead of " +
					IntegerParserWithDefault.DEFAULT);
			}
		} catch (Exception e) {
			fail("An exception should never be thrown: \"" + e.getMessage() + "\"");
		}
	}
}
