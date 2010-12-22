package edu.iu.sci2.database.isi.load.utilities.parser.test.entity;


import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.database.isi.load.model.entity.Document;
import edu.iu.sci2.database.isi.load.model.entity.Keyword;
import edu.iu.sci2.database.isi.load.model.entity.relationship.DocumentKeyword;
import edu.iu.sci2.database.isi.load.utilities.parser.ISITableModelParser;
import edu.iu.sci2.database.isi.load.utilities.parser.RowItemTest;
import edu.iu.sci2.database.isi.load.utilities.parser.test.relationship.DocumentKeywordsTest;

public class KeywordTest extends RowItemTest {
	public static final String ONE_KEYWORD_OF_EACH_TYPE_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "OneKeywordOfEachType.isi";
	public static final String MULTIPLE_KEYWORDS_OF_EACH_TYPE_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleKeywordsOfEachType.isi";
	public static final String KEYWORDS_GET_MERGED_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "KeywordsGetMerged.isi";
	public static final String NO_MERGING_BETWEEN_KEYWORD_TYPES_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "NoMergingBetweenKeywordTypes.isi";

	public static final String FIRST_AUTHOR_KEYWORD = "planarian";
	public static final String SECOND_AUTHOR_KEYWORD = "regeneration";
	public static final String FIRST_KEYWORD_PLUS = "CENTRAL-NERVOUS-SYSTEM";
	public static final String SECOND_KEYWORD_PLUS = "TUMOR-SUPPRESSOR GENE";

	public static final String FIRST_DOCUMENT_TITLE =
		"Planarians Maintain a Constant Ratio of Different Cell Types During " +
		"Changes in Body Size by Using the Stem Cell System";
	public static final String SECOND_DOCUMENT_TITLE =
		"Minimum spanning trees of weighted scale-free networks";

	@Test
	public void testZeroKeywordsGetParsed() throws Exception {
		DatabaseModel model = parseTestData(EMPTY_TEST_DATA_PATH);
		RowItemContainer<Keyword> keywords = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.KEYWORD_TABLE_NAME);

		checkItemContainerValidity(keywords, "keywords");
		checkItemCount(keywords, 0);
	}

	@Test
	public void testOneKeywordOfEachType() throws Exception {
		DatabaseModel model = parseTestData(ONE_KEYWORD_OF_EACH_TYPE_TEST_DATA_PATH);
		RowItemContainer<Document> documents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.DOCUMENT_TABLE_NAME);
		RowItemContainer<Keyword> keywords = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.KEYWORD_TABLE_NAME);
		RowItemContainer<DocumentKeyword> documentKeywords =
			model.getRowItemListOfTypeByDatabaseTableName(
				ISI.DOCUMENT_KEYWORDS_TABLE_NAME);

		checkItemContainerValidity(documents, "documents");
		checkItemCount(documents, 1);
		checkItemContainerValidity(keywords, "keywords");
		checkItemCount(keywords, 2);
		checkItemContainerValidity(documentKeywords, "document keywords");
		checkItemCount(documentKeywords, 2);

		Document firstDocument = getFirstDocument(documents);
		Keyword firstAuthorKeyword = getFirstAuthorKeyword(keywords);
		Keyword firstKeywordPlus = getFirstKeywordPlus(keywords);
		DocumentKeyword firstAuthorDocumentKeyword = DocumentKeywordsTest.getDocumentKeyword(
			documentKeywords.getItems(), firstDocument, firstAuthorKeyword);
		DocumentKeyword firstKeywordPlusDocumentKeyword = DocumentKeywordsTest.getDocumentKeyword(
			documentKeywords.getItems(), firstDocument, firstKeywordPlus);

		checkKeyword(
			firstAuthorKeyword, FIRST_AUTHOR_KEYWORD, ISITableModelParser.AUTHOR_KEYWORDS);
		checkKeyword(firstKeywordPlus, FIRST_KEYWORD_PLUS, ISITableModelParser.KEYWORDS_PLUS);
		DocumentKeywordsTest.checkDocumentKeyword(firstAuthorDocumentKeyword, 0);
		DocumentKeywordsTest.checkDocumentKeyword(firstKeywordPlusDocumentKeyword, 0);
	}

	@Test
	public void testMultipleKeywordsOfEachType() throws Exception {
		DatabaseModel model = parseTestData(MULTIPLE_KEYWORDS_OF_EACH_TYPE_TEST_DATA_PATH);
		RowItemContainer<Document> documents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.DOCUMENT_TABLE_NAME);
		RowItemContainer<Keyword> keywords = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.KEYWORD_TABLE_NAME);
		RowItemContainer<DocumentKeyword> documentKeywords =
			model.getRowItemListOfTypeByDatabaseTableName(
				ISI.DOCUMENT_KEYWORDS_TABLE_NAME);

		checkItemContainerValidity(documents, "documents");
		checkItemCount(documents, 1);
		checkItemContainerValidity(keywords, "keywords");
		checkItemCount(keywords, 4);
		checkItemContainerValidity(documentKeywords, "document keywords");
		checkItemCount(documentKeywords, 4);

		Document firstDocument = getFirstDocument(documents);
		Keyword firstAuthorKeyword = getFirstAuthorKeyword(keywords);
		Keyword secondAuthorKeyword = getSecondAuthorKeyword(keywords);
		Keyword firstKeywordPlus = getFirstKeywordPlus(keywords);
		Keyword secondKeywordPlus = getSecondKeywordPlus(keywords);
		DocumentKeyword firstAuthorDocumentKeyword = DocumentKeywordsTest.getDocumentKeyword(
			documentKeywords.getItems(), firstDocument, firstAuthorKeyword);
		DocumentKeyword secondAuthorDocumentKeyword = DocumentKeywordsTest.getDocumentKeyword(
			documentKeywords.getItems(), firstDocument, secondAuthorKeyword);
		DocumentKeyword firstKeywordPlusDocumentKeyword = DocumentKeywordsTest.getDocumentKeyword(
			documentKeywords.getItems(), firstDocument, firstKeywordPlus);
		DocumentKeyword secondKeywordPlusDocumentKeyword = DocumentKeywordsTest.getDocumentKeyword(
			documentKeywords.getItems(), firstDocument, secondKeywordPlus);

		checkKeyword(
			firstAuthorKeyword, FIRST_AUTHOR_KEYWORD, ISITableModelParser.AUTHOR_KEYWORDS);
		checkKeyword(
			secondAuthorKeyword, SECOND_AUTHOR_KEYWORD, ISITableModelParser.AUTHOR_KEYWORDS);
		checkKeyword(firstKeywordPlus, FIRST_KEYWORD_PLUS, ISITableModelParser.KEYWORDS_PLUS);
		checkKeyword(secondKeywordPlus, SECOND_KEYWORD_PLUS, ISITableModelParser.KEYWORDS_PLUS);
		DocumentKeywordsTest.checkDocumentKeyword(firstAuthorDocumentKeyword, 0);
		DocumentKeywordsTest.checkDocumentKeyword(secondAuthorDocumentKeyword, 1);
		DocumentKeywordsTest.checkDocumentKeyword(firstKeywordPlusDocumentKeyword, 0);
		DocumentKeywordsTest.checkDocumentKeyword(secondKeywordPlusDocumentKeyword, 1);
	}

	@Test
	public void testAuthorKeywordsGetMerged() throws Exception {
		DatabaseModel model = parseTestData(KEYWORDS_GET_MERGED_TEST_DATA_PATH);
		RowItemContainer<Document> documents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.DOCUMENT_TABLE_NAME);
		RowItemContainer<Keyword> keywords = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.KEYWORD_TABLE_NAME);
		RowItemContainer<DocumentKeyword> documentKeywords =
			model.getRowItemListOfTypeByDatabaseTableName(
				ISI.DOCUMENT_KEYWORDS_TABLE_NAME);

		checkItemContainerValidity(documents, "documents");
		checkItemCount(documents, 2);
		checkItemContainerValidity(keywords, "keywords");
		checkItemCount(keywords, 4);
		checkItemContainerValidity(documentKeywords, "document keywords");
		checkItemCount(documentKeywords, 6);

		Document firstDocument = getFirstDocument(documents);
		Document secondDocument = getSecondDocument(documents);
		Keyword firstAuthorKeyword = getFirstAuthorKeyword(keywords);
		Keyword secondAuthorKeyword = getSecondAuthorKeyword(keywords);
		Keyword firstKeywordPlus = getFirstKeywordPlus(keywords);
		Keyword secondKeywordPlus = getSecondKeywordPlus(keywords);
		DocumentKeyword firstAuthorDocumentKeyword = DocumentKeywordsTest.getDocumentKeyword(
			documentKeywords.getItems(), firstDocument, firstAuthorKeyword);
		DocumentKeyword secondAuthorDocumentKeyword = DocumentKeywordsTest.getDocumentKeyword(
			documentKeywords.getItems(), firstDocument, secondAuthorKeyword);
		DocumentKeyword thirdAuthorDocumentKeyword = DocumentKeywordsTest.getDocumentKeyword(
			documentKeywords.getItems(), secondDocument, secondAuthorKeyword);
		DocumentKeyword firstKeywordPlusDocumentKeyword = DocumentKeywordsTest.getDocumentKeyword(
			documentKeywords.getItems(), firstDocument, firstKeywordPlus);
		DocumentKeyword secondKeywordPlusDocumentKeyword = DocumentKeywordsTest.getDocumentKeyword(
			documentKeywords.getItems(), firstDocument, secondKeywordPlus);
		DocumentKeyword thirdKeywordPlusDocumentKeyword = DocumentKeywordsTest.getDocumentKeyword(
			documentKeywords.getItems(), secondDocument, secondKeywordPlus);

		checkKeyword(
			firstAuthorKeyword, FIRST_AUTHOR_KEYWORD, ISITableModelParser.AUTHOR_KEYWORDS);
		checkKeyword(
			secondAuthorKeyword, SECOND_AUTHOR_KEYWORD, ISITableModelParser.AUTHOR_KEYWORDS);
		checkKeyword(firstKeywordPlus, FIRST_KEYWORD_PLUS, ISITableModelParser.KEYWORDS_PLUS);
		checkKeyword(secondKeywordPlus, SECOND_KEYWORD_PLUS, ISITableModelParser.KEYWORDS_PLUS);
		DocumentKeywordsTest.checkDocumentKeyword(firstAuthorDocumentKeyword, 0);
		DocumentKeywordsTest.checkDocumentKeyword(secondAuthorDocumentKeyword, 1);
		DocumentKeywordsTest.checkDocumentKeyword(thirdAuthorDocumentKeyword, 0);
		DocumentKeywordsTest.checkDocumentKeyword(firstKeywordPlusDocumentKeyword, 0);
		DocumentKeywordsTest.checkDocumentKeyword(secondKeywordPlusDocumentKeyword, 1);
		DocumentKeywordsTest.checkDocumentKeyword(thirdKeywordPlusDocumentKeyword, 0);
	}

	@Test
	public void testNoMergingBetweenKeywordTypes() throws Exception {
		DatabaseModel model = parseTestData(NO_MERGING_BETWEEN_KEYWORD_TYPES_TEST_DATA_PATH);
		RowItemContainer<Document> documents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.DOCUMENT_TABLE_NAME);
		RowItemContainer<Keyword> keywords = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.KEYWORD_TABLE_NAME);
		RowItemContainer<DocumentKeyword> documentKeywords =
			model.getRowItemListOfTypeByDatabaseTableName(
				ISI.DOCUMENT_KEYWORDS_TABLE_NAME);

		checkItemContainerValidity(documents, "documents");
		checkItemCount(documents, 1);
		checkItemContainerValidity(keywords, "keywords");
		checkItemCount(keywords, 2);
		checkItemContainerValidity(documentKeywords, "document keywords");
		checkItemCount(documentKeywords, 2);

		Document firstDocument = getFirstDocument(documents);
		Keyword firstAuthorKeyword = getFirstAuthorKeyword(keywords);
		Keyword firstKeywordPlus = getAlternateFirstKeywordPlus(keywords);
		DocumentKeyword firstAuthorDocumentKeyword = DocumentKeywordsTest.getDocumentKeyword(
			documentKeywords.getItems(), firstDocument, firstAuthorKeyword);
		DocumentKeyword firstKeywordPlusDocumentKeyword = DocumentKeywordsTest.getDocumentKeyword(
			documentKeywords.getItems(), firstDocument, firstKeywordPlus);

		checkKeyword(
			firstAuthorKeyword, FIRST_AUTHOR_KEYWORD, ISITableModelParser.AUTHOR_KEYWORDS);
		checkKeyword(firstKeywordPlus, FIRST_AUTHOR_KEYWORD, ISITableModelParser.KEYWORDS_PLUS);
		DocumentKeywordsTest.checkDocumentKeyword(firstAuthorDocumentKeyword, 0);
		DocumentKeywordsTest.checkDocumentKeyword(firstKeywordPlusDocumentKeyword, 0);
	}

	// TODO: Test Keyword.

	public static Keyword getKeyword(
			Collection<Keyword> keywords, String keywordText, String type) {
		for (Keyword keyword : keywords) {
			try {
				checkKeyword(keyword, keywordText, type);

				return keyword;
			} catch (Throwable e) {}
		}

		return null;
	}

	public static void checkKeyword(Keyword keyword, String keywordText, String type) {
		if (keyword == null) {
			String failMessage =
				"An exception should have been thrown " +
				"if the result Keyword would have ended up null.";
			fail(failMessage);
		}

		compareProperty("Keyword", keyword.getKeyword(), keywordText);
		compareProperty("Type", keyword.getType(), type);
	}

	private static Document getFirstDocument(RowItemContainer<Document> documents) {
		return DocumentTest.getDocument(documents.getItems(), FIRST_DOCUMENT_TITLE);
	}

	private static Document getSecondDocument(RowItemContainer<Document> documents) {
		return DocumentTest.getDocument(documents.getItems(), SECOND_DOCUMENT_TITLE);
	}

	private static Keyword getFirstAuthorKeyword(RowItemContainer<Keyword> keywords) {
		return (getKeyword(
			keywords.getItems(),
			FIRST_AUTHOR_KEYWORD,
			ISITableModelParser.AUTHOR_KEYWORDS));
	}

	private static Keyword getSecondAuthorKeyword(RowItemContainer<Keyword> keywords) {
		return (getKeyword(
			keywords.getItems(),
			SECOND_AUTHOR_KEYWORD,
			ISITableModelParser.AUTHOR_KEYWORDS));
	}

	private static Keyword getFirstKeywordPlus(RowItemContainer<Keyword> keywords) {
		return (getKeyword(
			keywords.getItems(),
			FIRST_KEYWORD_PLUS,
			ISITableModelParser.KEYWORDS_PLUS));
	}

	private static Keyword getAlternateFirstKeywordPlus(RowItemContainer<Keyword> keywords) {
		return (getKeyword(
			keywords.getItems(),
			FIRST_AUTHOR_KEYWORD,
			ISITableModelParser.KEYWORDS_PLUS));
	}

	private static Keyword getSecondKeywordPlus(RowItemContainer<Keyword> keywords) {
		return (getKeyword(
			keywords.getItems(),
			SECOND_KEYWORD_PLUS,
			ISITableModelParser.KEYWORDS_PLUS));
	}
}
