package edu.iu.scipolicy.database.isi.load.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.dictionary.DictionaryEntry;
import org.cishell.utilities.dictionary.DictionaryUtilities;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class Document extends Entity<Document> {
	public static final Schema<Document> SCHEMA = new Schema<Document>(
		true,
		ISI.ABSTRACT_TEXT, DerbyFieldType.TEXT,
		ISI.ARTICLE_NUMBER, DerbyFieldType.TEXT,
		ISI.BEGINNING_PAGE, DerbyFieldType.INTEGER,
		ISI.CITED_REFERENCE_COUNT, DerbyFieldType.INTEGER,
		ISI.CITED_YEAR, DerbyFieldType.INTEGER,
		ISI.DIGITAL_OBJECT_IDENTIFIER, DerbyFieldType.TEXT,
		ISI.DOCUMENT_TYPE, DerbyFieldType.TEXT,
		ISI.DOCUMENT_VOLUME, DerbyFieldType.INTEGER,
		ISI.ENDING_PAGE, DerbyFieldType.INTEGER,
		ISI.FIRST_AUTHOR, DerbyFieldType.FOREIGN_KEY,
		ISI.FUNDING_AGENCY_AND_GRANT_NUMBER, DerbyFieldType.TEXT,
		ISI.FUNDING_TEXT, DerbyFieldType.TEXT,
		ISI.ISBN, DerbyFieldType.TEXT,
		ISI.ISI_DOCUMENT_DELIVERY_NUMBER, DerbyFieldType.TEXT,
		ISI.ISI_UNIQUE_ARTICLE_IDENTIFIER, DerbyFieldType.TEXT,
		ISI.ISSUE, DerbyFieldType.TEXT,
		ISI.LANGUAGE, DerbyFieldType.TEXT,
		ISI.PAGE_COUNT, DerbyFieldType.INTEGER,
		ISI.PART_NUMBER, DerbyFieldType.TEXT,
		ISI.PUBLICATION_DATE, DerbyFieldType.TEXT,
		ISI.PUBLICATION_YEAR, DerbyFieldType.INTEGER,
		ISI.DOCUMENT_SOURCE, DerbyFieldType.FOREIGN_KEY,
		ISI.SPECIAL_ISSUE, DerbyFieldType.TEXT,
		ISI.SUBJECT_CATEGORY, DerbyFieldType.TEXT,
		ISI.SUPPLEMENT, DerbyFieldType.TEXT,
		ISI.TIMES_CITED, DerbyFieldType.INTEGER,
		ISI.TITLE, DerbyFieldType.TEXT).
		FOREIGN_KEYS(
			ISI.FIRST_AUTHOR, ISI.PERSON_TABLE_NAME,
			ISI.DOCUMENT_SOURCE, ISI.SOURCE_TABLE_NAME);

	private String abstractText;
	private String articleNumber;
	private Integer beginningPage;
	private Integer citedReferenceCount;
	private Integer citedYear;
	private String digitalObjectIdentifier;
	private String documentType;
	private Integer documentVolume;
	private Integer endingPage;
	private Person firstAuthorPerson;
	private String fundingAgencyAndGrantNumber;
	private String fundingText;
	private String isbn;
	private String isiDocumentDeliveryNumber;
	private String isiUniqueArticleIdentifier;
	private String issue;
	private String language;
	private Integer pageCount;
	private String partNumber;
	private String publicationDate;
	private Integer publicationYear;
	private Source source;
	private String specialIssue;
	private String subjectCategory;
	private String supplement;
	private Integer timesCited;
	private String title;
	private Dictionary<String, String> arbitraryAttributes = new Hashtable<String, String>();

	public Document(
			DatabaseTableKeyGenerator keyGenerator,
			String abstractText,
			String articleNumber,
			Integer beginningPage,
			Integer citedReferenceCount,
			Integer citedYear,
			String digitalObjectIdentifier,
			String documentType,
			Integer documentVolume,
			Integer endingPage,
			Person firstAuthorPerson,
			String fundingAgencyAndGrantNumber,
			String fundingText,
			String isbn,
			String isiDocumentDeliveryNumber,
			String isiUniqueArticleIdentifier,
			String issue,
			String language,
			Integer pageCount,
			String partNumber,
			String publicationDate,
			Integer publicationYear,
			Source source,
			String specialIssue,
			String subjectCategory,
			String supplement,
			Integer timesCited,
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
				source,
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
		this.source = source;
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

	public Integer getBeginningPage() {
		return this.beginningPage;
	}

	public Integer getCitedReferenceCount() {
		return this.citedReferenceCount;
	}

	public Integer getCitedYear() {
		return this.citedYear;
	}

	public String getDocumentType() {
		return this.documentType;
	}

	public Integer getEndingPage() {
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

	public Integer getPageCount() {
		return this.pageCount;
	}

	public String getPartNumber() {
		return this.partNumber;
	}

	public String getPublicationDate() {
		return this.publicationDate;
	}

	public Integer getPublicationYear() {
		return this.publicationYear;
	}

	public Source getSource() {
		return this.source;
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

	public Integer getTimesCited() {
		return this.timesCited;
	}

	public String getTitle() {
		return this.title;
	}

	public Integer getVolume() {
		return this.documentVolume;
	}

	public Dictionary<String, String> getArbitraryAttributes() {
		return this.arbitraryAttributes;
	}

	public void setFirstAuthorPerson(Person firstAuthorPerson) {
		this.firstAuthorPerson = firstAuthorPerson;

		getAttributes().put(ISI.FIRST_AUTHOR, firstAuthorPerson.getPrimaryKey());
	}

	public void setSource(Source source) {
		this.source = source;

		getAttributes().put(ISI.DOCUMENT_SOURCE, source.getPrimaryKey());
	}

	/// This side-effects Document.SCHEMA.
	public void addArbitraryAttribute(String name, String value) {
		if (SCHEMA.findField(name) == null) {
			System.err.println("found field is null");
			SCHEMA.addField(name, DerbyFieldType.TEXT);
		}

		if (value != null) {
			this.arbitraryAttributes.put(name, value);
			getAttributes().put(name, value);
		}
	}

	public boolean shouldMerge(Document otherDocument) {
		/*
		 * TODO: At this point, it doesn't make sense for Documents to ever get merged.
		 * The only scenario in which that would happen is if two Documents are actually duplicates
		 *  within the same dataset.
		 * However, duplicates in the same dataset get preprocessed out.
		 * If we ever support merging between multiple datasets, it's quite possible Documents
		 *  will need to be merged, but until then...
		 */
		return false;
	}

	public void merge(Document otherDocument) {
	}

	public static Dictionary<String, Object> createAttributes(
			String abstractText,
			String articleNumber,
			Integer beginningPage,
			Integer citedReferenceCount,
			Integer citedYear,
			String digitalObjectIdentifier,
			String documentType,
			Integer documentVolume,
			Integer endingPage,
			Person firstAuthorPerson,
			String fundingAgencyAndGrantNumber,
			String fundingText,
			String isbn,
			String isiDocumentDeliveryNumber,
			String isiUniqueArticleIdentifier,
			String issue,
			String language,
			Integer pageCount,
			String partNumber,
			String publicationDate,
			Integer publicationYear,
			Source source,
			String specialIssue,
			String subjectCategory,
			String supplement,
			Integer timesCited,
			String title) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Object>(ISI.ABSTRACT_TEXT, abstractText),
			new DictionaryEntry<String, Object>(ISI.ARTICLE_NUMBER, articleNumber),
			new DictionaryEntry<String, Object>(ISI.BEGINNING_PAGE, beginningPage),
			new DictionaryEntry<String, Object>(
				ISI.CITED_REFERENCE_COUNT, citedReferenceCount),
			new DictionaryEntry<String, Object>(ISI.CITED_YEAR, citedYear),
			new DictionaryEntry<String, Object>(
				ISI.DIGITAL_OBJECT_IDENTIFIER, digitalObjectIdentifier),
			new DictionaryEntry<String, Object>(ISI.DOCUMENT_TYPE, documentType),
			new DictionaryEntry<String, Object>(ISI.DOCUMENT_VOLUME, documentVolume),
			new DictionaryEntry<String, Object>(ISI.ENDING_PAGE, endingPage),
			new DictionaryEntry<String, Object>(ISI.FUNDING_TEXT, fundingText),
			new DictionaryEntry<String, Object>(
				ISI.FUNDING_AGENCY_AND_GRANT_NUMBER, fundingAgencyAndGrantNumber),
			new DictionaryEntry<String, Object>(ISI.ISBN, isbn),
			new DictionaryEntry<String, Object>(
				ISI.ISI_DOCUMENT_DELIVERY_NUMBER, isiDocumentDeliveryNumber),
			new DictionaryEntry<String, Object>(
				ISI.ISI_UNIQUE_ARTICLE_IDENTIFIER, isiUniqueArticleIdentifier),
			new DictionaryEntry<String, Object>(ISI.ISSUE, issue),
			new DictionaryEntry<String, Object>(ISI.LANGUAGE, language),
			new DictionaryEntry<String, Object>(ISI.PAGE_COUNT, pageCount),
			new DictionaryEntry<String, Object>(ISI.PART_NUMBER, partNumber),
			new DictionaryEntry<String, Object>(ISI.PUBLICATION_DATE, publicationDate),
			new DictionaryEntry<String, Object>(ISI.PUBLICATION_YEAR, publicationYear),
			new DictionaryEntry<String, Object>(ISI.SPECIAL_ISSUE, specialIssue),
			new DictionaryEntry<String, Object>(ISI.SUBJECT_CATEGORY, subjectCategory),
			new DictionaryEntry<String, Object>(ISI.SUPPLEMENT, supplement),
			new DictionaryEntry<String, Object>(ISI.TIMES_CITED, timesCited),
			new DictionaryEntry<String, Object>(ISI.TITLE, title));

		if (firstAuthorPerson != null) {
			attributes.put(ISI.FIRST_AUTHOR, firstAuthorPerson.getPrimaryKey());
		}

		if (source != null) {
			attributes.put(ISI.DOCUMENT_SOURCE, source.getPrimaryKey());
		}

		return attributes;
	}
}