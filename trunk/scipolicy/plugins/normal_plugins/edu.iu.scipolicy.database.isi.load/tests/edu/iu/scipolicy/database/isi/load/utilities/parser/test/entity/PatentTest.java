package edu.iu.scipolicy.database.isi.load.utilities.parser.test.entity;


import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.scipolicy.database.isi.load.model.entity.Document;
import edu.iu.scipolicy.database.isi.load.model.entity.Patent;
import edu.iu.scipolicy.database.isi.load.model.entity.relationship.CitedPatent;
import edu.iu.scipolicy.database.isi.load.utilities.parser.RowItemTest;
import edu.iu.scipolicy.database.isi.load.utilities.parser.test.relationship.CitedPatentsTest;

public class PatentTest extends RowItemTest {
	public static final String ONE_PATENT_TEST_DATA_PATH = BASE_TEST_DATA_PATH + "OnePatent.isi";
	public static final String MULTIPLE_DOCUMENTS_WITHOUT_PATENTS_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleDocumentsWithoutPatents.isi";
	public static final String MULTIPLE_DISTINCT_PATENTS_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleDistinctPatents.isi";
	public static final String MULTIPLE_PATENTS_THAT_GET_MERGED_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultiplePatentsThatGetMerged.isi";

	public static final String FIRST_DOCUMENT_TITLE =
		"Planarians Maintain a Constant Ratio of Different Cell Types During " +
		"Changes in Body Size by Using the Stem Cell System";
	public static final String SECOND_DOCUMENT_TITLE =
		"Minimum spanning trees of weighted scale-free networks";

	public static final String FIRST_PATENT_NUMBER = "CP-4055";
	public static final String SECOND_PATENT_NUMBER = "CP-4056";

	@Test
	public void testZeroPatentsGetParsed() throws Exception {
		DatabaseModel model = parseTestData(EMPTY_TEST_DATA_PATH);
		RowItemContainer<Patent> patents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PATENT_TABLE_NAME);

		checkItemContainerValidity(patents, "patents");
		checkItemCount(patents, 0);
	}

	@Test
	public void testOnePatentGetsParsed() throws Exception {
		DatabaseModel model = parseTestData(ONE_PATENT_TEST_DATA_PATH);
		RowItemContainer<Patent> patents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PATENT_TABLE_NAME);
		RowItemContainer<CitedPatent> citedPatents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.CITED_PATENTS_TABLE_NAME);

		checkItemContainerValidity(patents, "patents");
		checkItemCount(patents, 1);
		checkItemContainerValidity(citedPatents, "cited patents");
		checkItemCount(citedPatents, 1);
	}

	@Test
	public void testDocumentsWithoutPatents() throws Exception {
		DatabaseModel model = parseTestData(MULTIPLE_DOCUMENTS_WITHOUT_PATENTS_TEST_DATA_PATH);
		RowItemContainer<Patent> patents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PATENT_TABLE_NAME);
		RowItemContainer<CitedPatent> citedPatents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.CITED_PATENTS_TABLE_NAME);

		checkItemContainerValidity(patents, "patents");
		checkItemCount(patents, 0);
		checkItemContainerValidity(citedPatents, "cited patents");
		checkItemCount(citedPatents, 0);
	}

	@Test
	public void testMultipleDistinctPatentsGetParsed() throws Exception {
		DatabaseModel model = parseTestData(MULTIPLE_DISTINCT_PATENTS_TEST_DATA_PATH);
		RowItemContainer<Document> documents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.DOCUMENT_TABLE_NAME);
		RowItemContainer<Patent> patents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PATENT_TABLE_NAME);
		RowItemContainer<CitedPatent> citedPatents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.CITED_PATENTS_TABLE_NAME);

		checkItemContainerValidity(documents, "documents");
		checkItemCount(documents, 2);
		checkItemContainerValidity(patents, "patents");
		checkItemCount(patents, 2);
		checkItemContainerValidity(citedPatents, "cited patents");
		checkItemCount(citedPatents, 2);

		Document firstDocument = getFirstDocument(documents);
		Document secondDocument = getSecondDocument(documents);
		Patent firstPatent = getFirstPatent(patents);
		Patent secondPatent = getSecondPatent(patents);
		CitedPatent firstCitedPatent = CitedPatentsTest.getCitedPatent(
			citedPatents.getItems(), firstDocument, firstPatent);
		CitedPatent secondCitedPatent = CitedPatentsTest.getCitedPatent(
			citedPatents.getItems(), secondDocument, secondPatent);

		checkPatent(firstPatent, FIRST_PATENT_NUMBER);
		checkPatent(secondPatent, SECOND_PATENT_NUMBER);
		CitedPatentsTest.checkCitedPatent(firstCitedPatent);
		CitedPatentsTest.checkCitedPatent(secondCitedPatent);
	}

	@Test
	public void testMultiplePatentsGetMerged() throws Exception {
		DatabaseModel model = parseTestData(MULTIPLE_PATENTS_THAT_GET_MERGED_TEST_DATA_PATH);
		RowItemContainer<Document> documents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.DOCUMENT_TABLE_NAME);
		RowItemContainer<Patent> patents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PATENT_TABLE_NAME);
		RowItemContainer<CitedPatent> citedPatents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.CITED_PATENTS_TABLE_NAME);

		checkItemContainerValidity(documents, "documents");
		checkItemCount(documents, 2);
		checkItemContainerValidity(patents, "patents");
		checkItemCount(patents, 1);
		checkItemContainerValidity(citedPatents, "cited patents");
		checkItemCount(citedPatents, 2);

		Document firstDocument = getFirstDocument(documents);
		Document secondDocument = getSecondDocument(documents);
		Patent firstPatent = getFirstPatent(patents);
		CitedPatent firstCitedPatent = CitedPatentsTest.getCitedPatent(
			citedPatents.getItems(), firstDocument, firstPatent);
		CitedPatent secondCitedPatent = CitedPatentsTest.getCitedPatent(
			citedPatents.getItems(), secondDocument, firstPatent);

		checkPatent(firstPatent, FIRST_PATENT_NUMBER);
		CitedPatentsTest.checkCitedPatent(firstCitedPatent);
		CitedPatentsTest.checkCitedPatent(secondCitedPatent);
	}

	public static Patent getPatent(Collection<Patent> patents, String patentNumber) {
		for (Patent patent : patents) {
			try {
				checkPatent(patent, patentNumber);

				return patent;
			} catch (Throwable e) {
			}
		}

		return null;
	}

	public static void checkPatent(Patent patent, String patentNumber) {
		if (patent == null) {
			String failMessage =
				"An exception should have been thrown " +
				"if the result Patent would have ended up null.";
			fail(failMessage);
		}

		compareProperty("Patent Number", patent.getPatentNumber(), patentNumber);
	}

	private static Document getFirstDocument(RowItemContainer<Document> documents) {
		return DocumentTest.getDocument(documents.getItems(), FIRST_DOCUMENT_TITLE);
	}

	private static Document getSecondDocument(RowItemContainer<Document> documents) {
		return DocumentTest.getDocument(documents.getItems(), SECOND_DOCUMENT_TITLE);
	}

	private static Patent getFirstPatent(RowItemContainer<Patent> patents) {
		return getPatent(patents.getItems(), FIRST_PATENT_NUMBER);
	}

	private static Patent getSecondPatent(RowItemContainer<Patent> patents) {
		return getPatent(patents.getItems(), SECOND_PATENT_NUMBER);
	}
}
