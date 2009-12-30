package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;

public class Document extends Entity<Document> implements Comparable<Document> {
	public static final Schema<Document> SCHEMA = new Schema<Document>(
		ISIDatabase.DIGITAL_OBJECT_IDENTIFIER, Schema.TEXT_CLASS,
		ISIDatabase.TITLE, Schema.TEXT_CLASS,
		ISIDatabase.ARTICLE_NUMBER, Schema.TEXT_CLASS,
		ISIDatabase.FIRST_AUTHOR, Schema.FOREIGN_KEY_CLASS,
		ISIDatabase.LANGUAGE, Schema.TEXT_CLASS,
		ISIDatabase.DOCUMENT_TYPE, Schema.TEXT_CLASS,
		ISIDatabase.CITED_REFERENCE_COUNT, Schema.INTEGER_CLASS,
		ISIDatabase.ABSTRACT_TEXT, Schema.TEXT_CLASS,
		ISIDatabase.TIMES_CITED, Schema.INTEGER_CLASS,
		ISIDatabase.BEGINNING_PAGE, Schema.INTEGER_CLASS,
		ISIDatabase.ENDING_PAGE, Schema.INTEGER_CLASS,
		ISIDatabase.PAGE_COUNT, Schema.INTEGER_CLASS,
		ISIDatabase.ISI_UNIQUE_ARTICLE_IDENTIFIER, Schema.TEXT_CLASS,
		ISIDatabase.PUBLICATION_YEAR, Schema.INTEGER_CLASS,
		ISIDatabase.PUBLICATION_DATE, Schema.TEXT_CLASS,
		ISIDatabase.DOCUMENT_VOLUME, Schema.TEXT_CLASS,
		ISIDatabase.ISSUE, Schema.TEXT_CLASS,
		ISIDatabase.PART_NUMBER, Schema.TEXT_CLASS,
		ISIDatabase.SUPPLEMENT, Schema.TEXT_CLASS,
		ISIDatabase.SPECIAL_ISSUE, Schema.TEXT_CLASS,
		ISIDatabase.ISI_DOCUMENT_DELIVERY_NUMBER, Schema.TEXT_CLASS,
		ISIDatabase.ISBN, Schema.TEXT_CLASS,
		ISIDatabase.EMAIL_ADDRESS, Schema.TEXT_CLASS).
		FOREIGN_KEYS(ISIDatabase.FIRST_AUTHOR, ISIDatabase.PERSON_TABLE_NAME);

	private String digitalObjectIdentifier;
	private String title;
	private String articleNumber;
	private Person firstAuthor;
	private String language;
	private String documentType;
	private int citedReferenceCount;
	private String abstractText;
	private int timesCited;
	private int beginningPage;
	private int endingPage;
	private int pageCount;
	private String isiUniqueArticleIdentifier;
	private int publicationYear;
	private String publicationDate;
	private String volume;
	private String issue;
	private String partNumber;
	private String supplement;
	private String specialIssue;
	private String isiDocumentDeliveryNumber;
	private String isbn;
	private String emailAddress;

	public Document(
			DatabaseTableKeyGenerator keyGenerator,
			String digitalObjectIdentifier,
			String title,
			String articleNumber,
			Person firstAuthor,
			String language,
			String documentType,
			int citedReferenceCount,
			String abstractText,
			int timesCited,
			int beginningPage,
			int endingPage,
			int pageCount,
			String isiUniqueArticleIdentifier,
			int publicationYear,
			String publicationDate,
			String volume,
			String issue,
			String partNumber,
			String supplement,
			String specialIssue,
			String isiDocumentDeliveryNumber,
			String isbn,
			String emailAddress) {
		super(
			keyGenerator,
			createAttributes(
				digitalObjectIdentifier,
				title,
				articleNumber,
				firstAuthor,
				language,
				documentType,
				citedReferenceCount,
				abstractText,
				timesCited,
				beginningPage,
				endingPage,
				pageCount,
				isiUniqueArticleIdentifier,
				publicationYear,
				publicationDate,
				volume,
				partNumber,
				issue,
				supplement,
				specialIssue,
				isiDocumentDeliveryNumber,
				isbn,
				emailAddress));
		this.digitalObjectIdentifier = digitalObjectIdentifier;
		this.title = title;
		this.articleNumber = articleNumber;
		this.firstAuthor = firstAuthor;
		this.language = language;
		this.documentType = documentType;
		this.citedReferenceCount = citedReferenceCount;
		this.abstractText = abstractText;
		this.timesCited = timesCited;
		this.beginningPage = beginningPage;
		this.endingPage = endingPage;
		this.pageCount = pageCount;
		this.isiUniqueArticleIdentifier = isiUniqueArticleIdentifier;
		this.publicationYear = publicationYear;
		this.publicationDate = publicationDate;
		this.volume = volume;
		this.partNumber = partNumber;
		this.issue = issue;
		this.supplement = supplement;
		this.specialIssue = specialIssue;
		this.isiDocumentDeliveryNumber = isiDocumentDeliveryNumber;
		this.isbn = isbn;
		this.emailAddress = emailAddress;
	}

	public String getDigitalObjectIdentifier() {
		return this.digitalObjectIdentifier;
	}

	public String getTitle() {
		return this.title;
	}

	public String getArticleNumber() {
		return this.articleNumber;
	}

	public Person getFirstAuthor() {
		return this.firstAuthor;
	}

	public String getLanguage() {
		return this.language;
	}

	public String getDocumentType() {
		return this.documentType;
	}

	public int getCitedReferenceCount() {
		return this.citedReferenceCount;
	}

	public String getAbstractText() {
		return this.abstractText;
	}

	public int getTimesCited() {
		return this.timesCited;
	}

	public int getBeginningPage() {
		return this.beginningPage;
	}

	public int getEndingPage() {
		return this.endingPage;
	}

	public int getPageCount() {
		return this.pageCount;
	}

	public String getISIUniqueArticleIdentifier() {
		return this.isiUniqueArticleIdentifier;
	}

	public int getPublicationYear() {
		return this.publicationYear;
	}

	public String getPublicationDate() {
		return this.publicationDate;
	}

	public String getVolume() {
		return this.volume;
	}

	public String getIssue() {
		return this.issue;
	}

	public String getPartNumber() {
		return this.partNumber;
	}

	public String getSupplement() {
		return this.supplement;
	}

	public String getSpecialIssue() {
		return this.specialIssue;
	}

	public String getISIDocumentDeliveryNumber() {
		return this.isiDocumentDeliveryNumber;
	}

	public String getISBN() {
		return this.isbn;
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public int compareTo(Document otherDocument) {
		return -1;
	}

	public boolean shouldMerge(Document otherDocument) {
		return false;
	}

	public void merge(Document otherDocument) {
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			String digitalObjectIdentifier,
			String title,
			String articleNumber,
			Person firstAuthor,
			String language,
			String documentType,
			int citedReferenceCount,
			String abstractText,
			int timesCited,
			int beginningPage,
			int endingPage,
			int pageCount,
			String isiUniqueArticleIdentifier,
			int publicationYear,
			String publicationDate,
			String volume,
			String issue,
			String partNumber,
			String supplement,
			String specialIssue,
			String isiDocumentDeliveryNumber,
			String isbn,
			String emailAddress) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.DIGITAL_OBJECT_IDENTIFIER, digitalObjectIdentifier);
		attributes.put(ISIDatabase.TITLE, title);
		attributes.put(ISIDatabase.ARTICLE_NUMBER, articleNumber);
		attributes.put(ISIDatabase.FIRST_AUTHOR, firstAuthor);
		attributes.put(ISIDatabase.LANGUAGE, language);
		attributes.put(ISIDatabase.DOCUMENT_TYPE, documentType);
		attributes.put(ISIDatabase.CITED_REFERENCE_COUNT, citedReferenceCount);
		attributes.put(ISIDatabase.ABSTRACT_TEXT, abstractText);
		attributes.put(ISIDatabase.TIMES_CITED, timesCited);
		attributes.put(ISIDatabase.BEGINNING_PAGE, beginningPage);
		attributes.put(ISIDatabase.ENDING_PAGE, endingPage);
		attributes.put(ISIDatabase.PAGE_COUNT, pageCount);
		attributes.put(ISIDatabase.ISI_UNIQUE_ARTICLE_IDENTIFIER, isiUniqueArticleIdentifier);
		attributes.put(ISIDatabase.PUBLICATION_YEAR, publicationYear);
		attributes.put(ISIDatabase.PUBLICATION_DATE, publicationDate);
		attributes.put(ISIDatabase.DOCUMENT_VOLUME, volume);
		attributes.put(ISIDatabase.ISSUE, issue);
		attributes.put(ISIDatabase.SUPPLEMENT, supplement);
		attributes.put(ISIDatabase.SPECIAL_ISSUE, specialIssue);
		attributes.put(ISIDatabase.ISI_DOCUMENT_DELIVERY_NUMBER, isiDocumentDeliveryNumber);
		attributes.put(ISIDatabase.ISBN, isbn);
		attributes.put(ISIDatabase.EMAIL_ADDRESS, emailAddress);

		return attributes;
	}
}