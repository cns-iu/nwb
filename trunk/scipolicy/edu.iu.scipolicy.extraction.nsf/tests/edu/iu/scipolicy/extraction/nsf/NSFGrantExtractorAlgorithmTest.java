package edu.iu.scipolicy.extraction.nsf;

import static org.junit.Assert.fail;

import java.io.File;

import org.cishell.templates.database.SQLFormationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class NSFGrantExtractorAlgorithmTest extends BaseNSFExtractorAlgorithmTest {
	private File temporaryNSFFile;
	private NSFGrantExtractorAlgorithm nsfGrantExtractorAlgorithm;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		temporaryNSFFile = createTemporaryNSFFile("GrantExtractor");
		nsfGrantExtractorAlgorithm = createTestNSFGrantExtractor(temporaryNSFFile);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFormSQL() {
		try {
			nsfGrantExtractorAlgorithm.formSQL();
		}
		catch (SQLFormationException e) {
			fail();
		}
	}

	@Test
	public void testImplodeStringArray() {
		String implodedString = nsfGrantExtractorAlgorithm.implodeStringArray(new String[] { }, "");
		
		if (!implodedString.equals(""))
			fail();
	}

}
