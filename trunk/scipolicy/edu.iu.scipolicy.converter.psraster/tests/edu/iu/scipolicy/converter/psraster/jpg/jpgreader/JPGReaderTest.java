package edu.iu.scipolicy.converter.psraster.jpg.jpgreader;

import java.io.IOException;
import java.util.Enumeration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JPGReaderTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecute() {
		System.err.println("JPGReader");
		try {
			Enumeration resources =
				this.getClass().getClassLoader().getResources("SphinxKit.jpg");
			
			if (resources.hasMoreElements())
				System.err.println("Resource: " + resources.nextElement());
			else
				System.err.println("No resources found.");
		}
		catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
		}
	}

}
