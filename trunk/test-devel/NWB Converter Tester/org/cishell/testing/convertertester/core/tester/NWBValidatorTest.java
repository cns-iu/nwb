/**
 * 
 */
package org.cishell.testing.convertertester.core.tester;


import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;

/**
 * @author kelleyt
 *
 */
public class NWBValidatorTest {
	private File goodFile1;
	private File goodFile2;
	private File badFile1;
	private File badFile2;
	private static final String pathName = "/home/kelleyt/workspace/NWB Converter Tester/test_files/NWB Files/";
	private ValidateNWBFile validator;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		validator = new ValidateNWBFile();
		goodFile1 = openFile("TestFile1.nwb");
		goodFile2 = openFile("TestFile2.nwb");
		badFile1 = openFile("TestFile6.nwb");
		badFile2 = openFile("TestFile4.nwb");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		goodFile1 = null;
		validator = null;
	}
	
	/*@Test
	public void testFileOpenandClose(){
		assertTrue(goodFile1.exists());		
	}*/
	
	@Test
	public void testGoodValidator() throws Exception{
		validator.validateNWBFormat(goodFile1);
		assertTrue(validator.getValidationResult());		
	}
	
	@Test
	public void testGoodValidator2() throws Exception{
		validator.validateNWBFormat(goodFile2);
		assertTrue(validator.getValidationResult());
	}
	
	/*@Test(expected = NullPointerException.class)
	public void testNullFileValidator() {
		//TODO: uncomment this and handle the exception.
		//validator.validateNWBFormat(null);
		
	}*/
	
	@Test
	public void testBadFileValidator() throws Exception {
		validator.validateNWBFormat(badFile1);
		assertFalse(validator.getValidationResult());
	}
	
	@Test
	public void testBadFileValidator2() throws Exception {
		validator.validateNWBFormat(badFile2);
		assertFalse(validator.getValidationResult());
	}
	
	
	
	public static File openFile(String fileName) throws Exception {
		File f = new File(NWBValidatorTest.pathName+fileName);
		return f;
	}
	
	

}
