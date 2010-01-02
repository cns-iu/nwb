package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import java.io.File;
import java.util.Collection;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.nwb.shared.isiutil.ISITableReaderHelper;
import edu.iu.nwb.shared.isiutil.exception.ReadISIFileException;
import edu.iu.scipolicy.testutilities.TestUtilities;

public class BaseRowItemParsingTest {
	public static final boolean SHOULD_NORMALIZE_AUTHOR_NAMES = true;
	public static final boolean SHOULD_CLEAN_AUTHOR_NAME_CAPITALIZATIONS = true;
	public static final boolean SHOULD_FILL_FILE_METADATA = true;
	public static final boolean SHOULD_CLEAN_CITED_REFERENCES = false;

	public static final String ISI_MIME_TYPE = "file:text/isi";

	public static final String BASE_TEST_DATA_PATH = "/edu/iu/scipolicy/loader/isi/db/testdata/";
	public static final String TEST_DATA_PATH = BASE_TEST_DATA_PATH + "TestISIData1.isi";

	protected Table inputTable;
	protected Collection<Integer> rows;

	@Before
	public void setUp() throws Exception {
		CIShellContext context = TestUtilities.createFakeCIShellContext();
		LogService logger = (LogService)context.getService(LogService.class.getName());
		Data inData = createInputData(TEST_DATA_PATH);
		this.inputTable = convertISIToTable(inData, logger);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test public void test(){}

	private Data createInputData(String testDataPath) throws Exception {
		File file = FileUtilities.safeLoadFileFromClasspath(BaseRowItemParsingTest.class, testDataPath);

		return new BasicData(file, ISI_MIME_TYPE);
	}

	private Table convertISIToTable(Data isiData, LogService logger) throws Exception {
    	/*
    	 * TODO: If you want to do template style commenting, describe what's going on throughout
    	 *  the method.
    	 */ 
   		// TODO: (What happens after you read the input ISI data?)
    	// Read the input ISI data.

    	File inISIFile = (File)isiData.getData();

    	try {
    		return ISITableReaderHelper.readISIFile(
    			inISIFile,
    			logger,
    			SHOULD_NORMALIZE_AUTHOR_NAMES,
    			SHOULD_CLEAN_AUTHOR_NAME_CAPITALIZATIONS,
    			SHOULD_FILL_FILE_METADATA,
    			SHOULD_CLEAN_CITED_REFERENCES);
    	} catch (ReadISIFileException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}
    }
}
