package edu.iu.sci2.converter.nsf.csv_to_db;

import static org.junit.Assert.fail;

import java.io.File;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.iu.sci2.converter.nsf.csv_to_db.CSVtoDBAlgorithm;
import edu.iu.sci2.testutilities.TestContext;
import edu.iu.sci2.testutilities.TestUtilities;
import edu.iu.sci2.utilities.nsf.NsfNames;

public class CSVtoDBAlgorithmTest {
	private TestContext validCIShellContext;
	private TestContext invalidCIShellContext;
	private File validInputCSVFile;
	private File invalidInputCSVFile;
	
	@Before
	public void setUp() throws Exception {
		// Construct both CIShell contexts.
		this.validCIShellContext = TestUtilities.createFakeCIShellContext();
		this.invalidCIShellContext = TestUtilities.createFakeCIShellContext();
		
		// Construct both files.
		try {
			this.validInputCSVFile = constructValidCSVFile();
			this.invalidInputCSVFile = constructInvalidCSVFile();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// Start the valid CIShell context, but not the invalid one.
		this.validCIShellContext.start();
	}

	@After
	public void tearDown() throws Exception {
		this.validCIShellContext.stop();
	}

	@Test
	public void testExecuteWithInvalidFileAndInvalidDatabaseService() {
		if (!testExecuteGeneric(this.invalidInputCSVFile,
							    this.invalidCIShellContext)) {
			fail();
		}
	}
	
	@Test
	public void testExecuteWithValidFileAndInvalidDatabaseService() {
		if (!testExecuteGeneric(this.validInputCSVFile, this.invalidCIShellContext))
		{
			fail();
		}
	}
	
	@Test
	public void testExecuteWithInvalidFileAndValidDatabaseService() {
		if (!testExecuteGeneric(this.invalidInputCSVFile, this.validCIShellContext))
		{
			fail();
		}
	}
	
	@Test
	public void testExecuteWithValidFileAndValidDatabaseService() {
		if (testExecuteGeneric(this.validInputCSVFile, this.validCIShellContext))
			fail();
	}
	
	private boolean testExecuteGeneric(File inFile, CIShellContext ciShellContext) {
		boolean threwException = false;
		
		CSVtoDBAlgorithm algorithm = constructCSVToDBAlgorithm
			(inFile, ciShellContext);
		
		try {
			algorithm.execute();
		}
		catch (Exception e) {
			e.printStackTrace();
			threwException = true;
		}
		
		return threwException;
	}
	
	private File constructValidCSVFile() throws Exception {
		return genericConstructCSVFile(constructValidCSVFileData());
	}
	
	private File constructInvalidCSVFile() throws Exception {
		return genericConstructCSVFile(constructInvalidCSVFileData());
	}
	
	private File genericConstructCSVFile(String csvFileData) throws Exception {
		return FileUtilities.writeTextIntoTemporaryDirectory(csvFileData, "nsf");
	}
	
	private String constructValidCSVFileData() {
		String validCSVFileData =
			NsfNames.CSV.AWARD_NUMBER + "," + NsfNames.CSV.AWARD_TITLE + "," +
				NsfNames.CSV.AWARD_START_DATE + "," +
				NsfNames.CSV.AWARD_EXPIRATION_DATE + "," +
				NsfNames.CSV.AWARDED_AMOUNT_TO_DATE + "\r\n" +
			"94942, \"Micah\",7/3/1985,7/4/2009,1000\r\n" +
			"114336, \"Elisha\",11/19/1985,10/20/1994,5000\r\n" +
			"133401, \"Patrick\",3/15/1984,3/15/1985,800\r\n" +
			"266802, \"Mark\",1/1/1940,12/31/2010,10";
		
		return validCSVFileData;
	}
	
	private String constructInvalidCSVFileData() {
		String validCSVFileData =
			NsfNames.CSV.AWARD_NUMBER + "," + NsfNames.CSV.AWARD_TITLE + "," +
				NsfNames.CSV.AWARD_START_DATE + "," +
				NsfNames.CSV.AWARD_EXPIRATION_DATE + "," +
				NsfNames.CSV.AWARDED_AMOUNT_TO_DATE + "\r\n" +
			"94942,\"Micah\",7/3/1985,7/4/2009,1000\r\n" +
			"114336,\"Elisha\",11/19/1985,10/20/1994,5000\r\n" +
			"133401,\"Patrick\",3/15/1984,3/15/1985,800\r\n" +
			"266802,\"Mark\",1/1/1940,12/31/2010";
		
		return validCSVFileData;
	}
	
	private CSVtoDBAlgorithm constructCSVToDBAlgorithm(File inputCSVFile,
													   CIShellContext ciShellContext) {
		// First, wrap the input CSV file in data.
		Data inputCSVFileDataObject = new BasicData(inputCSVFile, "file:text/nsf");
		Data[] algorithmInData = new Data[] { inputCSVFileDataObject };
		
		return new CSVtoDBAlgorithm(algorithmInData, null, ciShellContext);
	}
}
