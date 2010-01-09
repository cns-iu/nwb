package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;

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
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.Person;
import edu.iu.scipolicy.loader.isi.db.model.entity.relationship.Author;
import edu.iu.scipolicy.loader.isi.db.model.entity.relationship.Editor;
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

	public static final String ZERO_PEOPLE_TEST_DATA_PATH = BASE_TEST_DATA_PATH + "ZeroPeople.isi";
	public static final String ONE_AUTHOR_TEST_DATA_PATH = BASE_TEST_DATA_PATH + "OneAuthor.isi";
	public static final String MULTIPLE_AUTHORS_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleAuthors.isi";

	public static final String ONE_EDITOR_TEST_DATA_PATH = BASE_TEST_DATA_PATH + "OneEditor.isi";
	public static final String MULTIPLE_EDITORS_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleEditors.isi";

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

	public static Person getPerson(
			List<Person> people,
			String additionalName,
			String familyName,
			String firstInitial,
			String fullName,
			String middleInitial,
			String personalName,
			String unsplitAbbreviatedName) {
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

				return person;
			} catch (Throwable e) {
			}
		}

		return null;
	}

	public static Document getDocument(List<Document> documents, String title) {
		for (Document document : documents) {
			if (document.getTitle().equals(title)) {
				return document;
			}
		}

		return null;
	}

	public static Author getAuthor(List<Author> authors, Document document, Person authorPerson) {
		for (Author author : authors) {
			if ((author.getDocument() == document) && (author.getPerson() == authorPerson)) {
				return author;
			}
		}

		return null;
	}

	public static Editor getEditor(List<Editor> editors, Document document, Person editorPerson) {
		for (Editor editor : editors) {
			if ((editor.getDocument() == document) && (editor.getPerson() == editorPerson)) {
				return editor;
			}
		}

		return null;
	}

	public static void verifyPersonExists(
			List<Person> people,
			String additionalName,
			String familyName,
			String firstInitial,
			String fullName,
			String middleInitial,
			String personalName,
			String unsplitAbbreviatedName) throws Exception {
		if (getPerson(
				people,
				additionalName,
				familyName,
				firstInitial,
				fullName,
				middleInitial,
				personalName,
				unsplitAbbreviatedName) != null) {
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

	public static void checkDocuments(
			Document relationshipDocument, Document providedDocument, String displayName) {
		if (relationshipDocument != providedDocument) {
			String failMessage =
				displayName + " document and provided document are not the same." +
				"\n\t" + displayName + " document primary key: " +
					relationshipDocument.getPrimaryKey() +
				"\n\tProvided document primary key: " + providedDocument.getPrimaryKey() +
				"\n\t" + displayName + " document title: \"" +
					relationshipDocument.getTitle() + "\"" +
				"\n\tProvided document title: \"" + providedDocument.getTitle() + "\"";
			fail(failMessage);
		}
	}

	public static void checkPeople(
			Person relationshipPerson, Person providedPerson, String displayName) {
		if (relationshipPerson != providedPerson) {
			String failMessage =
				displayName + " person and provided person are not the same." +
				"\n\t" + displayName + " person: \"" +
					relationshipPerson.getUnsplitAbbreviatedName() + "\"" +
				"\n\tProvided person: \"" +
					providedPerson.getUnsplitAbbreviatedName() + "\"";
			fail(failMessage);
		}
	}

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
}
