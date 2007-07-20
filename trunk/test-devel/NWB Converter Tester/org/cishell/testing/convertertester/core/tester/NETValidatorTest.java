package org.cishell.testing.convertertester.core.tester;


import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.iu.nwb.converter.pajeknet.common.ValidateNETFile;

public class NETValidatorTest {
	private File goodFile1;

	private static final String pathName = "/home/kelleyt/workspace/NWB Converter Tester/test_files/NET Files/";
	private ValidateNETFile validator;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		validator = new ValidateNETFile();
		goodFile1 = openFile("TestFile8.net");
		//goodFile2 = openFile("TestFile2.nwb");
		//badFile1 = openFile("TestFile6.nwb");
		//badFile2 = openFile("TestFile4.nwb");
	}

	@After
	public void tearDown() throws Exception {
		validator = null;
		goodFile1 = null;
	}
	
	@Test
	public void testGoodValidator() throws Exception{
		validator.validateNETFormat(goodFile1);
		//System.out.println(validator.getValidationResult());
		System.out.println(validator.getErrorMessages());
		assertTrue(validator.getValidationResult());		
	}
	
	public static File openFile(String fileName) throws Exception {
		File f = new File(NETValidatorTest.pathName+fileName);
		return f;
	}
	

}
