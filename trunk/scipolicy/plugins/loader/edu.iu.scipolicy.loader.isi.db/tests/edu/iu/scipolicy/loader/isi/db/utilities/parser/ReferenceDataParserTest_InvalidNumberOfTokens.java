package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import static org.junit.Assert.fail;

import org.junit.Test;

public class ReferenceDataParserTest_InvalidNumberOfTokens extends ReferenceDataParserTest {
	/*
	 * Tests for less than 2 reference tokens.
	 */

	@Test
	public void test0Tokens() {
		try {
			runTest(ZERO_REFERENCE_TOKENS_STRING, false);
			fail("An exception should have been thrown when passing an empty string.");
		} catch (Exception e) {}
	}

	@Test
	public void test1Token() {
		try {
			runTest(ONE_REFERENCE_TOKEN_STRING, false);
			fail("An exception should have been thrown when passing an empty string.");
		} catch (Exception e) {}
	}

	/*
	 * Test for greater than 5 (6, to be exact) reference tokens.
	 */

	@Test
	public void test6Tokens() {
		try {
			runTest(SIX_REFERENCE_TOKENS_STRING, false);
			fail("An exception should have been thrown when passing an empty string.");
		} catch (Exception e) {}
	}
}
