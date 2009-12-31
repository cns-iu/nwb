package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import static org.junit.Assert.fail;

import org.junit.Test;

public class ReferenceDataParserTest_4Tokens extends ReferenceDataParserTest {
	@Test
	public void test4Tokens_InvalidFirstToken() {
		fail();
	}

	/*
	 * Tests for the pattern:
	 * year, source, volume, page
	 */

	@Test
	public void test4Tokens_YearFirst_NoSourcePrefix() {
		fail();
	}

	@Test
	public void test4Tokens_YearFirst_SourcePrefix() {
		fail();
	}

	@Test
	public void test4Tokens_YearFirst_InvalidVolume() {
		fail();
	}

	@Test
	public void test4Tokens_YearFirst_ValidVolume() {
		fail();
	}

	@Test
	public void test4Tokens_YearFirst_InvalidPageNumber() {
		fail();
	}

	@Test
	public void test4Tokens_YearFirst_ValidPageNumber() {
		fail();
	}

	/*
	 * Tests for the pattern:
	 * person; year; source; volume, page, chapter, or some other number
	 * Person parsing is tested in PersonParsingTest.  No need to test it here.
	 */

	@Test
	public void test4Tokens_PersonFirst_NoSourcePrefix() {
		fail();
	}

	@Test
	public void test4Tokens_PersonFirst_SourcePrefix() {
		fail();
	}

	@Test
	public void test4Tokens_PersonFirst_HasVolume() {
		fail();
	}

	@Test
	public void test4Tokens_PersonFirst_HasPageNumber() {
		fail();
	}

	@Test
	public void test4Tokens_PersonFirst_HasSomeOtherNumber() {
		fail();
	}
}
