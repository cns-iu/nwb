package edu.iu.scipolicy.database.isi.load.utilities.parser;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.Collection;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;
import org.cishell.utilities.Pair;
import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.nwb.shared.isiutil.ISITableReaderHelper;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.nwb.shared.isiutil.exception.ReadISIFileException;
import edu.iu.scipolicy.database.isi.load.utilities.ISITablePreprocessor;
import edu.iu.scipolicy.testutilities.TestUtilities;

// TODO: Just make this a utility class?
public class RowItemTest {
	public static final boolean SHOULD_NORMALIZE_AUTHOR_NAMES = true;
	public static final boolean SHOULD_CLEAN_AUTHOR_NAME_CAPITALIZATIONS = true;
	public static final boolean SHOULD_FILL_FILE_METADATA = true;
	public static final boolean SHOULD_CLEAN_CITED_REFERENCES = false;

	public static final String ISI_MIME_TYPE = "file:text/isi";

	public static final String BASE_TEST_DATA_PATH =
		"/edu/iu/scipolicy/database/isi/load/testdata/";

	public static final String EMPTY_TEST_DATA_PATH = BASE_TEST_DATA_PATH + "Empty.isi";

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
		Dictionary<String, Object> attributes = item.getAttributes();
		Integer orderListed = (Integer)attributes.get(ISI.ORDER_LISTED);

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
		if (StringUtilities.isNull_Empty_OrWhitespace(itemProperty)) {
			if (!StringUtilities.isNull_Empty_OrWhitespace(compareTo)) {
				String failMessage =
					propertyName +
					" do not match: Result is empty (\"" + itemProperty + "\")" +
					" and Comparison is not (\"" + compareTo + "\").";
				fail(failMessage);
			} else {
			}
		} else {
			if (StringUtilities.isNull_Empty_OrWhitespace(compareTo)) {
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

		return new ISITableModelParser(ProgressMonitor.NULL_MONITOR).parseModel(
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
