package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.Collection;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;
import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.RowItemContainer;
import edu.iu.cns.database.loader.framework.utilities.DatabaseModel;
import edu.iu.cns.shared.utilities.Pair;
import edu.iu.nwb.shared.isiutil.ISITableReaderHelper;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
import edu.iu.nwb.shared.isiutil.exception.ReadISIFileException;
import edu.iu.scipolicy.loader.isi.db.utilities.ISITablePreprocessor;
import edu.iu.scipolicy.testutilities.TestUtilities;

// TODO: Just make this a utility class?
public class RowItemTest {
	public static final boolean SHOULD_NORMALIZE_AUTHOR_NAMES = true;
	public static final boolean SHOULD_CLEAN_AUTHOR_NAME_CAPITALIZATIONS = true;
	public static final boolean SHOULD_FILL_FILE_METADATA = true;
	public static final boolean SHOULD_CLEAN_CITED_REFERENCES = false;

	public static final String ISI_MIME_TYPE = "file:text/isi";

	public static final String BASE_TEST_DATA_PATH = "/edu/iu/scipolicy/loader/isi/db/testdata/";

	// ISIFile test data.
	public static final String ONE_ISI_FILE_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "OneISIFile.isi";
	/*public static final String MULTIPLE_ISI_FILES_WITH_NO_MERGES_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleISIFilesWithNoMerges.isi";*/
	/*public static final String MULTIPLE_ISI_FILES_WITH_MERGES_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleISIFilesWithMerges.isi";*/

	// Author test data.
	public static final String EMPTY_TEST_DATA_PATH = BASE_TEST_DATA_PATH + "Empty.isi";
	public static final String ONE_AUTHOR_TEST_DATA_PATH = BASE_TEST_DATA_PATH + "OneAuthor.isi";
	public static final String MULTIPLE_AUTHORS_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleAuthors.isi";

	// Editor test data.
	public static final String ONE_EDITOR_TEST_DATA_PATH = BASE_TEST_DATA_PATH + "OneEditor.isi";
	public static final String MULTIPLE_EDITORS_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleEditors.isi";

	// TODO: Moving these into a testing utilities class or something?  meh.

	public static void checkItemContainerValidity(RowItemContainer<?> items, String displayName) {
		if (items == null) {
			fail(displayName + " == null.  It shouldn't.  Ever.");
		}
	}

	public static void checkItemCount(RowItemContainer<?> items, int itemCount) {
		int numItems = items.getItems().size();

		if (numItems != itemCount) {
			fail("Found " + numItems + " item(s); expected " + itemCount + ".");
		}
	}

	public static void checkItemOrderListed(RowItem<?> item, int providedOrderListed) {
		Dictionary<String, Comparable<?>> attributes = item.getAttributes();
		Integer orderListed = (Integer)attributes.get(ISIDatabase.ORDER_LISTED);

		if (orderListed != null) {
			int orderListedValue = orderListed.intValue();

			if (orderListedValue != providedOrderListed) {
				String failMessage =
					"Order listed values do not match." +
					"\n\tItem order listed: " + orderListedValue +
					"\n\tProvided order listed: " + providedOrderListed;
				fail(failMessage);
			}
		}
	}

	public static void compareProperty(
			String propertyName, String itemProperty, String compareTo) {
		if (StringUtilities.isEmptyOrWhiteSpace(itemProperty)) {
			if (!StringUtilities.isEmptyOrWhiteSpace(compareTo)) {
				String failMessage =
					propertyName +
					" do not match: Result is empty (\"" + itemProperty + "\")" +
					" and Comparison is not (\"" + compareTo + "\").";
				fail(failMessage);
			} else {
			}
		} else {
			if (StringUtilities.isEmptyOrWhiteSpace(compareTo)) {
				String failMessage =
					propertyName + " do not match: Result is not empty and Comparison is.";
				fail(failMessage);
			} else if (itemProperty.compareToIgnoreCase(compareTo) != 0) {
				String failMessage =
					propertyName + " do not match:" +
					"\n\tResult: \"" + itemProperty + "\"" +
					"\n\tComparison: \"" + compareTo + "\"";
				fail(failMessage);
			}
		}
	}

	public static void compareProperty(String propertyName, int itemProperty, int compareTo) {
		compareProperty(propertyName, "" + itemProperty, "" + compareTo);
	}

	protected DatabaseModel parseTestData(String testDataPath) throws Exception {
		Pair<Table, Collection<Integer>> testData = prepareTestData(testDataPath);

		return new ISITableModelParser().parseModel(
			testData.getFirstObject(), testData.getSecondObject());
	}

	protected Pair<Table, Collection<Integer>> prepareTestData(String testDataPath)
			throws Exception {
		CIShellContext context = TestUtilities.createFakeCIShellContext();
		LogService logger = (LogService)context.getService(LogService.class.getName());
		Data inData = createInputData(testDataPath);
		Table isiTable = convertISIToTable(testDataPath, inData, logger);

		return new Pair<Table, Collection<Integer>>(
			isiTable,
			ISITablePreprocessor.removeRowsWithDuplicateDocuments(isiTable));
	}

	private Data createInputData(String testDataPath) throws Exception {
		File file =
			FileUtilities.safeLoadFileFromClasspath(RowItemTest.class, testDataPath);

		return new BasicData(file, ISI_MIME_TYPE);
	}

	private Table convertISIToTable(
			String originalFileName, Data isiData, LogService logger) throws Exception {
    	File inISIFile = (File)isiData.getData();

    	try {
    		return ISITableReaderHelper.readISIFile(
    			originalFileName,
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
