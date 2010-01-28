package edu.iu.scipolicy.database.isi.load.utilities.parser;

import static org.junit.Assert.fail;

import org.junit.Test;

public class ReferenceDataParserTest_NumberOfTokens extends ReferenceDataParserTest {
	/*
	 * Tests for less than 2 reference tokens.
	 */

	@Test
	public void test0Tokens() {
		try {
			runTest(ZERO_REFERENCE_TOKENS_STRING, false, false);
			fail("An exception should have been thrown when passing an empty string.");
		} catch (Exception e) {}
	}

	@Test
	public void test1Token() {
		try {
			runTest(ONE_REFERENCE_TOKEN_STRING, false, false);
			fail("An exception should have been thrown when passing an empty string.");
		} catch (Exception e) {}
	}

	/*
	 * Test for greater than 6 (7, to be exact) reference tokens.
	 */

	@Test
	public void test7Tokens() {
		try {
			runTest(SEVEN_REFERENCE_TOKENS_STRING, false, false);
			fail("An exception should have been thrown when passing an empty string.");
		} catch (Exception e) {}
	}

	/*
	 * Just for fun, let's test all of the valid numbers of tokens.
	 */

	@Test
	public void testValidNumbersOfTokens() throws Exception {
		runTest(TWO_REFERENCE_TOKEN_STRING, false, false);
		runTest(THREE_REFERENCE_TOKEN_STRING, false, false);
		runTest(FOUR_REFERENCE_TOKEN_STRING, false, false);
		runTest(FIVE_REFERENCE_TOKEN_STRING, false, false);
		runTest(SIX_REFERENCE_TOKEN_STRING, false, false);
	}
}
