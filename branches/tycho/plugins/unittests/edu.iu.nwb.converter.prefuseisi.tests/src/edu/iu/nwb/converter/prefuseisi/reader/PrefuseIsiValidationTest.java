package edu.iu.nwb.converter.prefuseisi.reader;

import junit.framework.TestCase;

public class PrefuseIsiValidationTest extends TestCase {

	public void testSigSearcher() {
		assertTrue(PrefuseIsiValidation.lineMatchesSignature("FN ISI Export Format"));
	}

	public void testExclusion() {
		// To be sorta restrictive, we want the file to have a line that
		// *starts* with "FN "
		assertFalse(PrefuseIsiValidation.lineMatchesSignature("This is not a real ISI file"));
		assertFalse(PrefuseIsiValidation.lineMatchesSignature(" Not ISI even though it says FN ISI"));
	}

	public void testNewFormats() {
		assertTrue(PrefuseIsiValidation.lineMatchesSignature("FN Thomson Reuters Web Of Knowledge"));

		assertTrue(PrefuseIsiValidation.lineMatchesSignature("FN Weird Random Format"));
	}
}
