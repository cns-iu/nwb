package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;
import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.cns.shared.utilities.Pair;
import edu.iu.nwb.shared.isiutil.ISITableReaderHelper;
import edu.iu.nwb.shared.isiutil.exception.ReadISIFileException;
import edu.iu.scipolicy.loader.isi.db.model.ISIModel;
import edu.iu.scipolicy.loader.isi.db.model.entity.Person;
import edu.iu.scipolicy.loader.isi.db.utilities.ISITablePreprocessor;
import edu.iu.scipolicy.testutilities.TestUtilities;

// TODO: Just make this a utility class?
public class BaseRowItemParsingTest {
	public static final boolean SHOULD_NORMALIZE_AUTHOR_NAMES = true;
	public static final boolean SHOULD_CLEAN_AUTHOR_NAME_CAPITALIZATIONS = true;
	public static final boolean SHOULD_FILL_FILE_METADATA = true;
	public static final boolean SHOULD_CLEAN_CITED_REFERENCES = false;

	public static final String ISI_MIME_TYPE = "file:text/isi";

	public static final String BASE_TEST_DATA_PATH = "/edu/iu/scipolicy/loader/isi/db/testdata/";

	protected ISIModel parseTestData(String testDataPath) throws Exception {
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
			FileUtilities.safeLoadFileFromClasspath(BaseRowItemParsingTest.class, testDataPath);

		return new BasicData(file, ISI_MIME_TYPE);
	}

	private Table convertISIToTable(
			String originalFileName, Data isiData, LogService logger) throws Exception {
    	/*
    	 * TODO: If you want to do template style commenting, describe what's going on throughout
    	 *  the method.
    	 */ 
   		// TODO: (What happens after you read the input ISI data?)
    	// Read the input ISI data.

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

	// TODO: Moving these into a testing utilities class or something?  meh.

	public static void verifyPersonExists(
			List<Person> people,
			String additionalName,
			String familyName,
			String firstInitial,
			String fullName,
			String middleInitial,
			String personalName,
			String unsplitAbbreviatedName) throws Exception {
		for (Person person : people) {
			try {
				checkPerson(
					person,
					additionalName,
					familyName,
					firstInitial,
					fullName,
					middleInitial,
					personalName,
					unsplitAbbreviatedName);

				return;
			} catch (Throwable e) {
			}
		}

		String exceptionMessage =
			"No person with the following specifications were found:" +
			"\n\tAdditional name: " + additionalName +
			"\n\tFamily name: " + familyName +
			"\n\tFirst initial: " + firstInitial +
			"\n\tFull name: " + fullName +
			"\n\tMiddle initial: " + middleInitial +
			"\n\tPersonal name: " + personalName +
			"\n\tUnsplit abbreviated name: " + unsplitAbbreviatedName;
		throw new Exception(exceptionMessage);
	}

	public static void checkPerson(
			Person person,
			String additionalName,
			String familyName,
			String firstInitial,
			String fullName,
			String middleInitial,
			String personalName,
			String unsplitAbbreviatedName) {
		if (person == null) {
			String failMessage =
				"An exception should have been thrown " +
				"if the result Person would have ended up null.";
			fail(failMessage);
		}

		comparePersonProperty("Additional Names", person.getAdditionalName(), additionalName);
		comparePersonProperty("Family Names", person.getFamilyName(), familyName);
		comparePersonProperty("First Initials", person.getFirstInitial(), firstInitial);
		comparePersonProperty("Full Names", person.getFullName(), fullName);
		comparePersonProperty("Middle Initials", person.getMiddleInitial(), middleInitial);
		comparePersonProperty("Personal Names", person.getPersonalName(), personalName);
		comparePersonProperty(
			"Unsplit Abbreviated Names",
			person.getUnsplitAbbreviatedName(),
			unsplitAbbreviatedName);
	}

	public static void comparePersonProperty(
			String propertyName, String personProperty, String compareTo) {
		if (StringUtilities.isEmptyOrWhiteSpace(personProperty)) {
			if (!StringUtilities.isEmptyOrWhiteSpace(compareTo)) {
				String failMessage =
					propertyName +
					" do not match: Result is empty (\"" + personProperty + "\")" +
					" and Comparison is not (\"" + compareTo + "\").";
				fail(failMessage);
			} else {
			}
		} else {
			// personProperty is NOT empty or white space.

			if (StringUtilities.isEmptyOrWhiteSpace(compareTo)) {
				String failMessage =
					propertyName + " do not match: Result is not empty and Comparison is.";
				fail(failMessage);
			} else if (personProperty.compareToIgnoreCase(compareTo) != 0) {
				String failMessage =
					propertyName + " do not match:" +
					"\n\tResult: \"" + personProperty + "\"" +
					"\n\tComparison: \"" + compareTo + "\"";
				fail(failMessage);
			}
		}
	}
}
