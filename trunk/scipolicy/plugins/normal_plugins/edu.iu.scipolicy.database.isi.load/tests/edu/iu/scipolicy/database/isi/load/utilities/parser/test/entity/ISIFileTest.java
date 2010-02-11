package edu.iu.scipolicy.database.isi.load.utilities.parser.test.entity;


import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.scipolicy.database.isi.load.model.entity.Document;
import edu.iu.scipolicy.database.isi.load.model.entity.ISIFile;
import edu.iu.scipolicy.database.isi.load.model.entity.relationship.DocumentOccurrence;
import edu.iu.scipolicy.database.isi.load.utilities.parser.RowItemTest;
import edu.iu.scipolicy.database.isi.load.utilities.parser.test.relationship.DocumentOccurrencesTest;

public class ISIFileTest extends RowItemTest {
	// ISIFile test data.
	public static final String ONE_ISI_FILE_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "OneISIFile.isi";
	/*public static final String MULTIPLE_ISI_FILES_WITH_NO_MERGES_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleISIFilesWithNoMerges.isi";*/
	/*public static final String MULTIPLE_ISI_FILES_WITH_MERGES_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleISIFilesWithMerges.isi";*/

	public static final String FIRST_DOCUMENT_TITLE =
		"Planarians Maintain a Constant Ratio of Different Cell Types During " +
		"Changes in Body Size by Using the Stem Cell System";

	public static final String FIRST_ISI_FILE_FORMAT_VERSION_NUMBER = "1.0";
	public static final String FIRST_ISI_FILE_FILE_TYPE = "ISI Export Format";

	@Test
	public void testZeroISIFilesGetParsed() throws Exception {
		DatabaseModel model = parseTestData(EMPTY_TEST_DATA_PATH);
		RowItemContainer<ISIFile> isiFiles = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.ISI_FILE_TABLE_NAME);

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
		DatabaseModel model = parseTestData(ONE_ISI_FILE_TEST_DATA_PATH);
		RowItemContainer<Document> documents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.DOCUMENT_TABLE_NAME);
		RowItemContainer<ISIFile> isiFiles = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.ISI_FILE_TABLE_NAME);
		RowItemContainer<DocumentOccurrence> documentOccurrences =
			model.getRowItemListOfTypeByDatabaseTableName(
				ISI.DOCUMENT_OCCURRENCES_TABLE_NAME);

		checkItemContainerValidity(documents, "documents");
		checkItemCount(documents, 2);
		checkItemContainerValidity(isiFiles, "isiFiles");
		checkItemCount(isiFiles, 1);
		checkItemContainerValidity(documentOccurrences, "document occurrences");
		checkItemCount(documentOccurrences, 2);

		Document firstDocument = getFirstDocument(documents);
		ISIFile firstISIFile = getFirstISIFile(isiFiles);
		DocumentOccurrence firstDocumentOccurrence = DocumentOccurrencesTest.getDocumentOccurrence(
			documentOccurrences.getItems(), firstDocument, firstISIFile);

		DocumentOccurrencesTest.checkDocumentOccurrence(firstDocumentOccurrence);
	}

	// These two can't be tested right now.

	/*@Test
	public void testMultipleISIFilesGetParsed() throws Exception {
		DatabaseModel model = parseTestData(MULTIPLE_ISI_FILES_WITH_NO_MERGES_TEST_DATA_PATH);
		RowItemContainer<ISIFile> isiFiles = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.ISI_FILE_TABLE_NAME);

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
		DatabaseModel model = parseTestData(ZERO_ISI_FILES_TEST_DATA_PATH);
		RowItemContainer<ISIFile> isiFiles = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.ISI_FILE_TABLE_NAME);

		if (isiFiles == null) {
			fail("isiFiles == null.  It shouldn't.  Ever.");
		}

		int numItems = isiFiles.getItems().size();

		if (numItems != 0) {
			fail("Found " + numItems + " items; expected 0.");
		}
	}*/

	// TODO: Test ISIFile.

	public static ISIFile getISIFile(
			Collection<ISIFile> isiFiles,
			String fileFormatVersionNumber,
			String fileName,
			String fileType) {
		for (ISIFile isiFile : isiFiles) {
			try {
				checkISIFile(isiFile, fileFormatVersionNumber, fileName, fileType);

				return isiFile;
			} catch (Throwable e) {}
		}

		return null;
	}

	public static void checkISIFile(
			ISIFile isiFile, String fileFormatVersionNumber, String fileName, String fileType) {
		if (isiFile == null) {
			String failMessage =
				"An exception should have been thrown " +
				"if the result ISIFile would have ended up null.";
			fail(failMessage);
		}

		compareProperty(
			"File Format Version Number",
			isiFile.getFileFormatVersionNumber(),
			fileFormatVersionNumber);
		compareProperty("File Name", isiFile.getFileName(), fileName);
		compareProperty("File Type", isiFile.getFileType(), fileType);
	}

	private static Document getFirstDocument(RowItemContainer<Document> documents) {
		return DocumentTest.getDocument(documents.getItems(), FIRST_DOCUMENT_TITLE);
	}

	private static ISIFile getFirstISIFile(RowItemContainer<ISIFile> isiFiles) {
		return getISIFile(
			isiFiles.getItems(),
			FIRST_ISI_FILE_FORMAT_VERSION_NUMBER,
			ONE_ISI_FILE_TEST_DATA_PATH,
			FIRST_ISI_FILE_FILE_TYPE);
	}
}
