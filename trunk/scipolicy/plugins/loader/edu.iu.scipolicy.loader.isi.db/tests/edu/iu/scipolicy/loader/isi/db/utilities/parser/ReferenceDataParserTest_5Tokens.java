package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import static org.junit.Assert.fail;

import org.junit.Test;

public class ReferenceDataParserTest_5Tokens extends ReferenceDataParserTest {
	/*
	 * Tests for the pattern:
	 * person, year, journal, volume, page number
	 * (This is most likely the only acceptable pattern.)
	 * Person parsing is tested in PersonParsingTest.  No need to test it here.
	 */

	@Test
	public void test5Tokens_PersonFirst_NoSourcePrefix() {
		fail();
	}

	@Test
	public void test5Tokens_PersonFirst_SourcePrefix() {
		fail();
	}

	@Test
	public void test5Tokens_PersonFirst_HasNoVolume() {
		fail();
	}

	@Test
	public void test5Tokens_PersonFirst_HasVolume() {
		fail();
	}

	@Test
	public void test5Tokens_PersonFirst_HasNoPageNumber() {
		fail();
	}

	@Test
	public void test5Tokens_PersonFirst_HasPageNumber() {
		fail();
	}
}
