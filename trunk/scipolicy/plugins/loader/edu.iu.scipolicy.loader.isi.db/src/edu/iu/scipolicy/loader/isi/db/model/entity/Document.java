package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;

public class Document extends Entity<Document> implements Comparable<Document> {
	public static final Schema<Document> SCHEMA = new Schema<Document>(
		true,
		ISIDatabase.ABSTRACT_TEXT, Schema.TEXT_CLASS,
		ISIDatabase.ARTICLE_NUMBER, Schema.TEXT_CLASS,
		ISIDatabase.BEGINNING_PAGE, Schema.INTEGER_CLASS,
		ISIDatabase.CITED_REFERENCE_COUNT, Schema.INTEGER_CLASS,
		ISIDatabase.CITED_YEAR, Schema.INTEGER_CLASS,
		ISIDatabase.DIGITAL_OBJECT_IDENTIFIER, Schema.TEXT_CLASS,
		ISIDatabase.DOCUMENT_TYPE, Schema.TEXT_CLASS,
		ISIDatabase.DOCUMENT_VOLUME, Schema.TEXT_CLASS,
		ISIDatabase.ENDING_PAGE, Schema.INTEGER_CLASS,
		ISIDatabase.FIRST_AUTHOR, Schema.FOREIGN_KEY_CLASS,
		ISIDatabase.FUNDING_AGENCY_AND_GRANT_NUMBER, Schema.TEXT_CLASS,
		ISIDatabase.FUNDING_TEXT, Schema.TEXT_CLASS,
		ISIDatabase.ISBN, Schema.TEXT_CLASS,
		ISIDatabase.ISI_DOCUMENT_DELIVERY_NUMBER, Schema.TEXT_CLASS,
		ISIDatabase.ISI_UNIQUE_ARTICLE_IDENTIFIER, Schema.TEXT_CLASS,
		ISIDatabase.ISSUE, Schema.TEXT_CLASS,
		ISIDatabase.LANGUAGE, Schema.TEXT_CLASS,
		ISIDatabase.PAGE_COUNT, Schema.INTEGER_CLASS,
		ISIDatabase.PART_NUMBER, Schema.TEXT_CLASS,
		ISIDatabase.PUBLICATION_DATE, Schema.TEXT_CLASS,
		ISIDatabase.PUBLICATION_YEAR, Schema.INTEGER_CLASS,
		ISIDatabase.SPECIAL_ISSUE, Schema.TEXT_CLASS,
		ISIDatabase.SUBJECT_CATEGORY, Schema.TEXT_CLASS,
		ISIDatabase.SUPPLEMENT, Schema.TEXT_CLASS,
		ISIDatabase.TIMES_CITED, Schema.INTEGER_CLASS,
		ISIDatabase.TITLE, Schema.TEXT_CLASS).
		FOREIGN_KEYS(ISIDatabase.FIRST_AUTHOR, ISIDatabase.PERSON_TABLE_NAME);

	private String abstractText;
	private String articleNumber;
	private int beginningPage;
	private int citedReferenceCount;
	private int citedYear;
	private String digitalObjectIdentifier;
	private String documentType;
	private String documentVolume;
	private int endingPage;
	private Person firstAuthorPerson;
	private String fundingAgencyAndGrantNumber;
	private String fundingText;
	private String isbn;
	private String isiDocumentDeliveryNumber;
	private String isiUniqueArticleIdentifier;
	private String issue;
	private String language;
	private int pageCount;
	private String partNumber;
	private String publicationDate;
	private int publicationYear;
	private String specialIssue;
	private String subjectCategory;
	private String supplement;
	private int timesCited;
	private String title;

	public Document(
			DatabaseTableKeyGenerator keyGenerator,
			String abstractText,
			String articleNumber,
			int beginningPage,
			int citedReferenceCount,
			int citedYear,
			String digitalObjectIdentifier,
			String documentType,
			String documentVolume,
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
		super(
			keyGenerator,
			createAttributes(
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
				title));
		this.abstractText = abstractText;
		this.articleNumber = articleNumber;
		this.beginningPage = beginningPage;
		this.citedReferenceCount = citedReferenceCount;
		this.citedYear = citedYear;
		this.digitalObjectIdentifier = digitalObjectIdentifier;
		this.documentType = documentType;
		this.documentVolume = documentVolume;
		this.endingPage = endingPage;
		this.firstAuthorPerson = firstAuthorPerson;
		this.fundingAgencyAndGrantNumber = fundingAgencyAndGrantNumber;
		this.fundingText = fundingText;
		this.isbn = isbn;
		this.isiDocumentDeliveryNumber = isiDocumentDeliveryNumber;
		this.isiUniqueArticleIdentifier = isiUniqueArticleIdentifier;
		this.issue = issue;
		this.language = language;
		this.pageCount = pageCount;
		this.partNumber = partNumber;
		this.publicationDate = publicationDate;
		this.publicationYear = publicationYear;
		this.specialIssue = specialIssue;
		this.subjectCategory = subjectCategory;
		this.supplement = supplement;
		this.timesCited = timesCited;
		this.title = title;
	}

	public String getDigitalObjectIdentifier() {
		return this.digitalObjectIdentifier;
	}

	public String getAbstractText() {
		return this.abstractText;
	}

	public String getArticleNumber() {
		return this.articleNumber;
	}

	public int getBeginningPage() {
		return this.beginningPage;
	}

	public int getCitedReferenceCount() {
		return this.citedReferenceCount;
	}

	public int getCitedYear() {
		return this.citedYear;
	}

	public String getDocumentType() {
		return this.documentType;
	}

	public int getEndingPage() {
		return this.endingPage;
	}

	public Person getFirstAuthorPerson() {
		return this.firstAuthorPerson;
	}

	public String getFundingAgencyAndGrantNumber() {
		return this.fundingAgencyAndGrantNumber;
	}

	public String getFundingText() {
		return this.fundingText;
	}

	public String getISBN() {
		return this.isbn;
	}

	public String getISIDocumentDeliveryNumber() {
		return this.isiDocumentDeliveryNumber;
	}

	public String getISIUniqueArticleIdentifier() {
		return this.isiUniqueArticleIdentifier;
	}

	public String getIssue() {
		return this.issue;
	}

	public String getLanguage() {
		return this.language;
	}

	public int getPageCount() {
		return this.pageCount;
	}

	public String getPartNumber() {
		return this.partNumber;
	}

	public String getPublicationDate() {
		return this.publicationDate;
	}

	public int getPublicationYear() {
		return this.publicationYear;
	}

	public String getSpecialIssue() {
		return this.specialIssue;
	}

	public String getSubjectCategory() {
		return this.subjectCategory;
	}

	public String getSupplement() {
		return this.supplement;
	}

	public int getTimesCited() {
		return this.timesCited;
	}

	public String getTitle() {
		return this.title;
	}

	public String getVolume() {
		return this.documentVolume;
	}

	public void setFirstAuthorPerson(Person firstAuthorPerson) {
		this.firstAuthorPerson = firstAuthorPerson;

		getAttributes().put(ISIDatabase.FIRST_AUTHOR, firstAuthorPerson);
	}

	public int compareTo(Document otherDocument) {
		// TODO:
		return -1;
	}

	public boolean shouldMerge(Document otherDocument) {
		// TODO:
		return false;
	}

	public void merge(Document otherDocument) {
		// TODO:
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			String abstractText,
			String articleNumber,
			int beginningPage,
			int citedReferenceCount,
			int citedYear,
			String digitalObjectIdentifier,
			String documentType,
			String documentVolume,
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
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.ABSTRACT_TEXT, abstractText);
		attributes.put(ISIDatabase.ARTICLE_NUMBER, articleNumber);
		attributes.put(ISIDatabase.BEGINNING_PAGE, beginningPage);
		attributes.put(ISIDatabase.CITED_REFERENCE_COUNT, citedReferenceCount);
		attributes.put(ISIDatabase.CITED_YEAR, citedYear);
		attributes.put(ISIDatabase.DIGITAL_OBJECT_IDENTIFIER, digitalObjectIdentifier);
		attributes.put(ISIDatabase.DOCUMENT_TYPE, documentType);
		attributes.put(ISIDatabase.DOCUMENT_VOLUME, documentVolume);
		attributes.put(ISIDatabase.ENDING_PAGE, endingPage);

		// Apparently, anonymous papers are allowed...?
		if (firstAuthorPerson != null) {
			attributes.put(ISIDatabase.FIRST_AUTHOR, firstAuthorPerson);
		}

		attributes.put(ISIDatabase.ISBN, isbn);
		attributes.put(ISIDatabase.ISI_DOCUMENT_DELIVERY_NUMBER, isiDocumentDeliveryNumber);
		attributes.put(ISIDatabase.ISI_UNIQUE_ARTICLE_IDENTIFIER, isiUniqueArticleIdentifier);
		attributes.put(ISIDatabase.ISSUE, issue);
		attributes.put(ISIDatabase.LANGUAGE, language);
		attributes.put(ISIDatabase.PAGE_COUNT, pageCount);
		attributes.put(ISIDatabase.PUBLICATION_DATE, publicationDate);
		attributes.put(ISIDatabase.PUBLICATION_YEAR, publicationYear);
		attributes.put(ISIDatabase.SPECIAL_ISSUE, specialIssue);
		attributes.put(ISIDatabase.SUPPLEMENT, supplement);
		attributes.put(ISIDatabase.TIMES_CITED, timesCited);
		attributes.put(ISIDatabase.TITLE, title);

		return attributes;
	}
}