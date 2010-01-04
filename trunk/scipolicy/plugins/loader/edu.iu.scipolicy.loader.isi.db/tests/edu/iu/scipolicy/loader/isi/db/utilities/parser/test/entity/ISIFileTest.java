package edu.iu.scipolicy.loader.isi.db.utilities.parser.test.entity;


import static org.junit.Assert.fail;

import org.junit.Test;

import edu.iu.cns.database.loader.framework.RowItemContainer;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.ISIModel;
import edu.iu.scipolicy.loader.isi.db.model.entity.ISIFile;
import edu.iu.scipolicy.loader.isi.db.utilities.parser.BaseRowItemParsingTest;

public class ISIFileTest extends BaseRowItemParsingTest {
	public static final String ZERO_ISI_FILES_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "ZeroISIFiles.isi";
	public static final String ONE_ISI_FILE_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "OneISIFile.isi";
	/*public static final String MULTIPLE_ISI_FILES_WITH_NO_MERGES_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleISIFilesWithNoMerges.isi";*/
	/*public static final String MULTIPLE_ISI_FILES_WITH_MERGES_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleISIFilesWithMerges.isi";*/

	@Test
	public void testZeroISIFilesGetParsed() throws Exception {
		ISIModel model = parseTestData(ZERO_ISI_FILES_TEST_DATA_PATH);
		RowItemContainer<ISIFile> isiFiles = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.ISI_FILE_TABLE_NAME);

		if (isiFiles == null) {
			fail("isiFiles == null.  It shouldn't.  Ever.");
		}

		int numItems = isiFiles.getItems().size();

		if (numItems != 0) {
			fail("Found " + numItems + " item(s); expected 0.");
		}
	}

	@Test
	public void testOneISIFileGetsParsed() throws Exception {
		ISIModel model = parseTestData(ONE_ISI_FILE_TEST_DATA_PATH);
		RowItemContainer<ISIFile> isiFiles = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.ISI_FILE_TABLE_NAME);

		if (isiFiles == null) {
			fail("isiFiles == null.  It shouldn't.  Ever.");
		}

		int numItems = isiFiles.getItems().size();

		if (numItems != 1) {
			fail("Found " + numItems + " item(s); expected 1.");
		}

		ISIFile isiFile = (ISIFile)isiFiles.getItems().get(0);
		String isiFileName = isiFile.getFileName();

		if (!isiFileName.equals(ONE_ISI_FILE_TEST_DATA_PATH)) {
			String failMessage =
				"ISIFile file name is: \"" + isiFileName + "\"" +
				"\nIt should be: \"" + ONE_ISI_FILE_TEST_DATA_PATH + "\"";
			fail(failMessage);
		}
	}

	// These two can't be tested right now.

	/*@Test
	public void testMultipleISIFilesGetParsed() throws Exception {
		ISIModel model = parseTestData(MULTIPLE_ISI_FILES_WITH_NO_MERGES_TEST_DATA_PATH);
		RowItemContainer<ISIFile> isiFiles = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.ISI_FILE_TABLE_NAME);

		if (isiFiles == null) {
			fail("isiFiles == null.  It shouldn't.  Ever.");
		}

		int numItems = isiFiles.getItems().size();

		if (numItems != 2) {
			fail("Found " + numItems + " item(s); expected 2.");
		}
	}*/

	/*@Test
	public void testMultipleISIFilesGetMerged() throws Exception {
		ISIModel model = parseTestData(ZERO_ISI_FILES_TEST_DATA_PATH);
		RowItemContainer<ISIFile> isiFiles = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.ISI_FILE_TABLE_NAME);

		if (isiFiles == null) {
			fail("isiFiles == null.  It shouldn't.  Ever.");
		}

		int numItems = isiFiles.getItems().size();

		if (numItems != 0) {
			fail("Found " + numItems + " items; expected 0.");
		}
	}*/

	// TODO: Test ISIFile.
}
