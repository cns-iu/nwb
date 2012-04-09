package edu.iu.sci2.database.isi.load.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.IntegerParserWithDefault;
import org.cishell.utilities.StringUtilities;
import org.cishell.utilities.dictionary.DictionaryEntry;
import org.cishell.utilities.dictionary.DictionaryUtilities;

import prefuse.data.Tuple;
import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.ISITag;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class Document extends Entity<Document> {
	public static final Schema<Document> SCHEMA = new Schema<Document>(
		true,
		ISI.ABSTRACT_TEXT, DerbyFieldType.TEXT,
		ISI.ARTICLE_NUMBER, DerbyFieldType.TEXT,
		ISI.BEGINNING_PAGE, DerbyFieldType.INTEGER,
		ISI.CITED_REFERENCE_COUNT, DerbyFieldType.INTEGER,
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
		ISI.TITLE, DerbyFieldType.TEXT, 
		ISI.CITE_AS, DerbyFieldType.TEXT).
		FOREIGN_KEYS(
			ISI.FIRST_AUTHOR, ISI.PERSON_TABLE_NAME,
			ISI.DOCUMENT_SOURCE, ISI.SOURCE_TABLE_NAME);

	
	private String articleNumber;
	private String digitalObjectIdentifier;
	private Person firstAuthorPerson;
	private Source source;
	private Tuple originalRow;
	private Dictionary<String, String> arbitraryAttributes = new Hashtable<String, String>();
	private String citeAs;
	
	public Document(
			DatabaseTableKeyGenerator keyGenerator,
			String articleNumber,
			String digitalObjectIdentifier,
			Person firstAuthorPerson,
			Source source,
			Tuple originalRow,
			String citeAs) {
		super(
			keyGenerator,
			createInitialAttributes(articleNumber, digitalObjectIdentifier, firstAuthorPerson, source));
		this.articleNumber = articleNumber;
		this.digitalObjectIdentifier = digitalObjectIdentifier;
		this.firstAuthorPerson = firstAuthorPerson;
		this.originalRow = originalRow;
		this.citeAs = citeAs;
	}

	public String getDigitalObjectIdentifier() {
		return this.digitalObjectIdentifier;
	}

	public String getAbstractText() {
		return StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.ABSTRACT.getColumnName()));
	}

	public String getArticleNumber() {
		return this.articleNumber;
	}

	public Integer getBeginningPage() {
		return IntegerParserWithDefault.parse(StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.BEGINNING_PAGE.getColumnName())));
	}

	public Integer getCitedReferenceCount() {
		return IntegerParserWithDefault.parse(StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.CITED_REFERENCE_COUNT.getColumnName())));
	}

	public String getDocumentType() {
		return StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.DOCUMENT_TYPE.getColumnName()));
	}

	public Integer getEndingPage() {
		return IntegerParserWithDefault.parse(StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.ENDING_PAGE.getColumnName())));
	}

	public Person getFirstAuthorPerson() {
		return this.firstAuthorPerson;
	}

	public String getFundingAgencyAndGrantNumber() {
		return StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.FUNDING_AGENCY_AND_GRANT_NUMBER.getColumnName()));
	}

	public String getFundingText() {
		return StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.FUNDING_TEXT.getColumnName()));
	}

	public String getISBN() {
		return StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.ISBN.getColumnName()));
	}

	public String getISIDocumentDeliveryNumber() {
		return StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.ISI_DOCUMENT_DELIVERY_NUMBER.getColumnName()));
	}

	public String getISIUniqueArticleIdentifier() {
		return StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.UNIQUE_ID.getColumnName()));
	}

	public String getIssue() {
		return StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.ISSUE.getColumnName()));
	}

	public String getLanguage() {
		return StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.LANGUAGE.getColumnName()));
	}

	public Integer getPageCount() {
		return IntegerParserWithDefault.parse(StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.NUMBER_OF_PAGES.getColumnName())));
	}

	public String getPartNumber() {
		return StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.PART_NUMBER.getColumnName()));
	}

	public String getPublicationDate() {
		return StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.PUBLICATION_DATE.getColumnName()));
	}

	public Integer getPublicationYear() {
		return IntegerParserWithDefault.parse(StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.PUBLICATION_YEAR.getColumnName())));
	}

	public Source getSource() {
		return this.source;
	}

	public String getSpecialIssue() {
		return StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.SPECIAL_ISSUE.getColumnName()));
	}

	public String getSubjectCategory() {
		return StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.SUBJECT_CATEGORY.getColumnName()));
	}

	public String getSupplement() {
		return StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.SUPPLEMENT.getColumnName()));
	}

	public Integer getTimesCited() {
		return IntegerParserWithDefault.parse(
			StringUtilities.trimIfNotNull(
				this.originalRow.getString(ISITag.TIMES_CITED.getColumnName())));
	}

	public String getTitle() {
		return StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.TITLE.getColumnName()));
	}

	public String getCiteAs() {
		return this.citeAs;
	}
	
	public Integer getVolume() {
		return IntegerParserWithDefault.parse(StringUtilities.trimIfNotNull(
			this.originalRow.getString(ISITag.VOLUME.getColumnName())));
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
			SCHEMA.addField(name, DerbyFieldType.TEXT);
		}

		if (value != null) {
			this.arbitraryAttributes.put(name, value);
			getAttributes().put(name, value);
		}
	}

	@Override
	public Dictionary<String, Object> getAttributesForInsertion() {
		Dictionary<String, Object> attributes = DictionaryUtilities.copy(super.getAttributes());
		fillAttributes(
			attributes,
			getAbstractText(),
			getBeginningPage(),
			getCitedReferenceCount(),
			getDocumentType(),
			getVolume(),
			getEndingPage(),
			getFundingAgencyAndGrantNumber(),
			getFundingText(),
			getISBN(),
			getISIDocumentDeliveryNumber(),
			getISIUniqueArticleIdentifier(),
			getIssue(),
			getLanguage(),
			getPageCount(),
			getPartNumber(),
			getPublicationDate(),
			getPublicationYear(),
			getSpecialIssue(),
			getSubjectCategory(),
			getSupplement(),
			getTimesCited(),
			getTitle(),
			getCiteAs());

		return attributes;
	}

	@Override
	public Object createMergeKey() {
		return getPrimaryKey();
	}

	/**
	 * Warning!  Unimplemented!
	 */
	@Override
	public void merge(Document otherDocument) {
	}

	private static Dictionary<String, Object> createInitialAttributes(
			String articleNumber, String digitalObjectIdentifier, Person firstAuthorPerson, Source source) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Object>(ISI.ARTICLE_NUMBER, articleNumber),
			new DictionaryEntry<String, Object>(
				ISI.DIGITAL_OBJECT_IDENTIFIER, digitalObjectIdentifier));
		if(firstAuthorPerson != null) {
			attributes.put(ISI.FIRST_AUTHOR, firstAuthorPerson.getPrimaryKey());
		}
		if (source != null) {
			attributes.put(ISI.DOCUMENT_SOURCE, source.getPrimaryKey());
		}

		return attributes;
	}

	private static void fillAttributes(
			Dictionary<String, Object> attributes,
			String abstractText,
			Integer beginningPage,
			Integer citedReferenceCount,
			String documentType,
			Integer documentVolume,
			Integer endingPage,
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
			String specialIssue,
			String subjectCategory,
			String supplement,
			Integer timesCited,
			String title,
			String citeAs) {
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Object>(ISI.ABSTRACT_TEXT, abstractText),
			new DictionaryEntry<String, Object>(ISI.BEGINNING_PAGE, beginningPage),
			new DictionaryEntry<String, Object>(ISI.CITED_REFERENCE_COUNT, citedReferenceCount),
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
			new DictionaryEntry<String, Object>(ISI.TITLE, title),
			new DictionaryEntry<String, Object>(ISI.CITE_AS, citeAs));
	}
}