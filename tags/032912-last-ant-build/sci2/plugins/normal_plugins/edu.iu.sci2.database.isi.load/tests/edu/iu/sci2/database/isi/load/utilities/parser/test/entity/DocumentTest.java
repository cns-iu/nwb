package edu.iu.sci2.database.isi.load.utilities.parser.test.entity;


import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.iu.sci2.database.isi.load.model.entity.Document;
import edu.iu.sci2.database.isi.load.model.entity.Person;
import edu.iu.sci2.database.isi.load.utilities.parser.RowItemTest;

public class DocumentTest extends RowItemTest {
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		/*
		 * This is so all tests pass (because I'd like them to) and my Eclipse can't seem to
		 *  auto-import fail for some reason.
		 */
		if (false) {
			fail();
		}
	}

	public static void checkDocument(
			Document document,
			String abstractText,
			String articleNumber,
			int beginningPage,
			int citedReferenceCount,
			int citedYear,
			String digitalObjectIdentifier,
			String documentType,
			Integer documentVolume,
			int endingPage,
			Person firstAuthorPerson,
			String fundingAgencyAndGrantNumber,
			String fundingText,
			String isbn,
			String isiDocumentDeliveryNumber,
			String isiUniqueArticleIdentifier,
			String issue,
			String language,
			int pageCount,
			String partNumber,
			String publicationDate,
			int publicationYear,
			String specialIssue,
			String subjectCategory,
			String supplement,
			int timesCited,
			String title) {
		if (document == null) {
			String failMessage =
				"An exception should have been thrown " +
				"if the result Document would have ended up null.";
			fail(failMessage);
		}

		compareProperty("Abstract Text", document.getAbstractText(), abstractText);
		compareProperty("Article Number", document.getArticleNumber(), articleNumber);
		compareProperty("Beginning Page", document.getBeginningPage(), beginningPage);
		compareProperty(
			"Cited Reference Count", document.getCitedReferenceCount(), citedReferenceCount);
		compareProperty("Cited Year", document.getCitedYear(), citedYear);
		compareProperty(
			"Digital Object Identifier",
			document.getDigitalObjectIdentifier(),
			digitalObjectIdentifier);
		compareProperty("Document Type", document.getDocumentType(), documentType);
		compareProperty("Document Volume", document.getVolume(), documentVolume);
		compareProperty("Ending Page", document.getEndingPage(), endingPage);
		compareProperty(
			"first Author Person",
			document.getFirstAuthorPerson().getPrimaryKey(),
			firstAuthorPerson.getPrimaryKey());
		compareProperty(
			"Funding Agency and Grant Number",
			document.getFundingAgencyAndGrantNumber(),
			fundingAgencyAndGrantNumber);
		compareProperty("Funding Text", document.getFundingText(), fundingText);
		compareProperty("ISBN", document.getISBN(), isbn);
		compareProperty(
			"ISI Document Delivery Number",
			document.getISIDocumentDeliveryNumber(), isiDocumentDeliveryNumber);
		compareProperty(
			"ISI Unique Article Identifier",
			document.getISIUniqueArticleIdentifier(),
			isiUniqueArticleIdentifier);
		compareProperty("Issue", document.getIssue(), issue);
		compareProperty("Language", document.getLanguage(), language);
		compareProperty("Page Count", document.getPageCount(), pageCount);
		compareProperty("Part Number", document.getPartNumber(), partNumber);
		compareProperty("Publication Date", document.getPublicationDate(), publicationDate);
		compareProperty("Publication Year", document.getPublicationYear(), publicationYear);
		compareProperty("Special Issue", document.getSpecialIssue(), specialIssue);
		compareProperty("Subject Category", document.getSubjectCategory(), subjectCategory);
		compareProperty("Supplement", document.getSupplement(), supplement);
		compareProperty("Times Cited", document.getTimesCited(), timesCited);
		compareProperty("Title", document.getTitle(), title);
	}

	public static Document getDocument(
			List<Document> documents,
			String abstractText,
			String articleNumber,
			int beginningPage,
			int citedReferenceCount,
			int citedYear,
			String digitalObjectIdentifier,
			String documentType,
			Integer documentVolume,
			int endingPage,
			Person firstAuthorPerson,
			String fundingAgencyAndGrantNumber,
			String fundingText,
			String isbn,
			String isiDocumentDeliveryNumber,
			String isiUniqueArticleIdentifier,
			String issue,
			String language,
			int pageCount,
			String partNumber,
			String publicationDate,
			int publicationYear,
			String specialIssue,
			String subjectCategory,
			String supplement,
			int timesCited,
			String title) {
		for (Document document : documents) {
			try {
				checkDocument(
					document,
					abstractText,
					articleNumber,
					beginningPage,
					citedReferenceCount,
					citedYear,
					digitalObjectIdentifier,
					documentType,
					documentVolume,
					endingPage,
					firstAuthorPerson,
					fundingAgencyAndGrantNumber,
					fundingText,
					isbn,
					isiDocumentDeliveryNumber,
					isiUniqueArticleIdentifier,
					issue,
					language,
					pageCount,
					partNumber,
					publicationDate,
					publicationYear,
					specialIssue,
					subjectCategory,
					supplement,
					timesCited,
					title);

				return document;
			} catch (Throwable e) {}
		}

		return null;
	}

	public static Document getDocument(Collection<Document> documents, String title) {
		for (Document document : documents) {
			if (document.getTitle().equals(title)) {
				return document;
			}
		}

		return null;
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
}
