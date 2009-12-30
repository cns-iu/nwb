package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import static org.junit.Assert.fail;

import org.junit.Test;

public class ReferenceDataParserTest_3Tokens extends ReferenceDataParserTest {
	@Test
	public void test3Tokens_InvalidFirstToken() {
		fail();
	}

	/*
	 * Tests for the pattern:
	 * year, source, volume, page, or some other number
	 */

	@Test
	public void test3Tokens_YearFirst_NoSourcePrefix() {
		fail();
	}

	@Test
	public void test3Tokens_YearFirst_SourcePrefix() {
		fail();
	}

	@Test
	public void test3Tokens_YearFirst_InvalidThirdToken() {
		fail();
	}

	@Test
	public void test3Tokens_YearFirst_HasVolume() {
		fail();
	}

	@Test
	public void test3Tokens_YearFirst_HasPage() {
		fail();
	}

	@Test
	public void test3Tokens_YearFirst_HasSomeOtherNumber() {
		fail();
	}

	/*
	 * Tests for the pattern:
	 * person, year, source
	 */

	@Test
	public void test3Tokens_PersonFirst_InvalidPerson() {
		fail();
	}

	@Test
	public void test3Tokens_PersonFirst_ValidPerson() {
		fail();
	}

	@Test
	public void test3Tokens_PersonFirst_NoSourcePrefix() {
		fail();
	}

	@Test
	public void test3Tokens_PersonFirst_SourcePrefix() {
		fail();
	}
}
