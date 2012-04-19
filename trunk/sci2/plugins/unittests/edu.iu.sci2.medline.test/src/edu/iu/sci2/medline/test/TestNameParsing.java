package edu.iu.sci2.medline.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import edu.iu.sci2.medline.common.MedlineField;
import edu.iu.sci2.medline.common.MedlineUtilities;
import edu.iu.sci2.medline.common.MedlineUtilities.NameParsingException;
import edu.iu.sci2.medline.common.MedlineUtilities.ParsedName;

public class TestNameParsing {

	@SuppressWarnings("static-method")
	@Test
	public void testGoodAuthorNames() {
		try {
		String lastName = "McCrary";
		ParsedName parsed = MedlineUtilities.parseName(MedlineField.AUTHOR, "McCrary SV");
		assertTrue("'" + parsed.getLastName() + "' =? '" + lastName + "'",  parsed.getLastName().equals("McCrary"));
		parsed = MedlineUtilities.parseName(MedlineField.AUTHOR, "Mc Crary SV");

		parsed = MedlineUtilities.parseName(MedlineField.AUTHOR, "Mc Crary SV 2nd");
		parsed = MedlineUtilities.parseName(MedlineField.AUTHOR, "Smith AB 3rd");
		parsed = MedlineUtilities.parseName(MedlineField.AUTHOR, "Foa EB");
		parsed = MedlineUtilities.parseName(MedlineField.FULL_AUTHOR, "Foa, Edna B");
		parsed = MedlineUtilities.parseName(MedlineField.FULL_AUTHOR, "McCrary, Sam");
		parsed = MedlineUtilities.parseName(MedlineField.FULL_AUTHOR, "Mc Crary, Sam");
		parsed = MedlineUtilities.parseName(MedlineField.FULL_AUTHOR,
				"Mc Crary, Sam 3rd");
		parsed = MedlineUtilities.parseName(MedlineField.FULL_AUTHOR,
				"Mc Crary, Sam B 3rd");
		} catch (NameParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("No NameParsingException s should have occurred.");
		}
	}

}
