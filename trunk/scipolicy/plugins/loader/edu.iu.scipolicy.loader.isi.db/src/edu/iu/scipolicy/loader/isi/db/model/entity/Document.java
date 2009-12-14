package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;


import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.loader.isi.db.ISIDatabase;

public class Document extends Entity {
	private String digitalObjectIdentifier;
	private String title;
	private String articleNumber;
	private Person firstAuthor;
	private String language;
	private String documentType;
	private int citedReferenceCount;
	private String abstractText;
	private int timesCited;
	private String beginningPage;
	private String endingPage;
	private int pageCount;
	private String isiUniqueArticleIdentifier;
	private int publicationYear;
	private Date publicationDate;
	private String volume;
	private String issue;
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
			String beginningPage,
			String endingPage,
			int pageCount,
			String isiUniqueArticleIdentifier,
			int publicationYear,
			Date publicationDate,
			String volume,
			String issue,
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

	public String getBeginningPage() {
		return this.beginningPage;
	}

	public String getEndingPage() {
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

	public Date getPublicationDate() {
		return this.publicationDate;
	}

	public String getVolume() {
		return this.volume;
	}

	public String getIssue() {
		return this.issue;
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

	public static Dictionary<String, Object> createAttributes(
			String digitalObjectIdentifier,
			String title,
			String articleNumber,
			Person firstAuthor,
			String language,
			String documentType,
			int citedReferenceCount,
			String abstractText,
			int timesCited,
			String beginningPage,
			String endingPage,
			int pageCount,
			String isiUniqueArticleIdentifier,
			int publicationYear,
			Date publicationDate,
			String volume,
			String issue,
			String supplement,
			String specialIssue,
			String isiDocumentDeliveryNumber,
			String isbn,
			String emailAddress) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
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