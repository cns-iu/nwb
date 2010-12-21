package edu.iu.scipolicy.database.isi.load.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;
import org.cishell.utilities.dictionary.DictionaryEntry;
import org.cishell.utilities.dictionary.DictionaryUtilities;

import prefuse.data.Tuple;
import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.ISITag;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.scipolicy.database.isi.load.utilities.parser.ReferenceDataParser;
import edu.iu.scipolicy.database.isi.load.utilities.parser.exception.ReferenceParsingException;

public class Reference extends Entity<Reference> {
	public final static Schema<Reference> SCHEMA = new Schema<Reference>(
		true,
		ISI.ANNOTATION, DerbyFieldType.TEXT,
		ISI.REFERENCE_ARTICLE_NUMBER, DerbyFieldType.TEXT,
		ISI.REFERENCE_AUTHOR, DerbyFieldType.FOREIGN_KEY,
		ISI.DIGITAL_OBJECT_IDENTIFIER, DerbyFieldType.TEXT,
		ISI.REFERENCE_OTHER_INFORMATION, DerbyFieldType.TEXT,
		ISI.PAGE_NUMBER, DerbyFieldType.INTEGER,
		ISI.PAPER, DerbyFieldType.FOREIGN_KEY,
		ISI.REFERENCE_STRING, DerbyFieldType.TEXT,
		ISI.REFERENCE_VOLUME, DerbyFieldType.INTEGER,
		ISI.SOURCE, DerbyFieldType.FOREIGN_KEY,
		ISI.YEAR, DerbyFieldType.INTEGER,
		ISI.REFERENCE_WAS_STARRED, DerbyFieldType.TEXT).
		FOREIGN_KEYS(
			ISI.PAPER, ISI.DOCUMENT_TABLE_NAME,
			ISI.REFERENCE_AUTHOR, ISI.PERSON_TABLE_NAME,
			ISI.SOURCE, ISI.SOURCE_TABLE_NAME);

	private RowItemContainer<Person> people;
	private DatabaseTableKeyGenerator sourceKeyGenerator;
	private Document paper;
	private Source source;
	private Tuple originalRow;
	private int referenceStringIndex;

	public Reference(
			DatabaseTableKeyGenerator keyGenerator,
			RowItemContainer<Person> people,
			DatabaseTableKeyGenerator sourceKeyGenerator,
			Source source,
			Tuple originalRow,
			int referenceStringIndex) {
		super(
			keyGenerator,
			createInitialAttributes(source));
		this.people = people;
		this.sourceKeyGenerator = sourceKeyGenerator;
		this.source = source;
		this.originalRow = originalRow;
		this.referenceStringIndex = referenceStringIndex;
	}

	public String getArticleNumber() {
		try {
			ReferenceDataParser referenceData = new ReferenceDataParser(
				this.people.getKeyGenerator(), this.sourceKeyGenerator, getRawReferenceString());

			return referenceData.getArticleNumber();
		} catch (ReferenceParsingException e) {
			return null;
		}
	}

	public String getDigitalObjectIdentifier() {
		try {
			ReferenceDataParser referenceData = new ReferenceDataParser(
				this.people.getKeyGenerator(), this.sourceKeyGenerator, getRawReferenceString());

			return referenceData.getDigitalObjectIdentifier();
		} catch (ReferenceParsingException e) {
			return null;
		}
	}

	public Document getPaper() {
		return this.paper;
	}

	public String getRawReferenceString() {
		String rawReferencesString = StringUtilities.simpleClean(
			this.originalRow.getString(ISITag.CITED_REFERENCES.getColumnName()));

		return StringUtilities.getNthToken(
			rawReferencesString, "\\|", this.referenceStringIndex, true, false);
	}

	public Source getSource() {
		return this.source;
	}

	public boolean wasStarred() {
		try {
			ReferenceDataParser referenceData = new ReferenceDataParser(
				this.people.getKeyGenerator(), this.sourceKeyGenerator, getRawReferenceString());

			return referenceData.wasStarred();
		} catch (ReferenceParsingException e) {
			return false;
		}
	}

	public void setPaper(Document paper) {
		this.paper = paper;
		getAttributes().put(ISI.PAPER, paper.getPrimaryKey());
	}

	public void setSource(Source source) {
		this.source = source;
		getAttributes().put(ISI.SOURCE, source.getPrimaryKey());
	}

	@Override
	public Dictionary<String, Object> getAttributesForInsertion() {
		Dictionary<String, Object> attributes = DictionaryUtilities.copy(super.getAttributes());

		try {
			ReferenceDataParser referenceData = new ReferenceDataParser(
				this.people.getKeyGenerator(), this.sourceKeyGenerator, getRawReferenceString());
			Person parsedAuthorPerson = referenceData.getAuthorPerson();
			Person mergedAuthorPerson = null;

			if (parsedAuthorPerson != null) {
				mergedAuthorPerson = this.people.add(referenceData.getAuthorPerson());
			}

			fillAttributes(
				attributes,
				referenceData.getAnnotation(),
				referenceData.getArticleNumber(),
				mergedAuthorPerson,
				getDigitalObjectIdentifier(),
				referenceData.getOtherInformation(),
				referenceData.getPageNumber(),
				this.paper,
				getRawReferenceString(),
				referenceData.getVolume(),
				this.source,
				referenceData.getYear(),
				referenceData.wasStarred());
		} catch (ReferenceParsingException e) {}

		return attributes;
	}

	@Override
	public Object createMergeKey() {
		return StringUtilities.alternativeIfNotNull_Empty_OrWhitespace(
			getRawReferenceString(), getPrimaryKey()).hashCode();
	}

	@Override
	public void merge(Reference otherReference) {
	}

	private static Dictionary<String, Object> createInitialAttributes(
//			String articleNumber,
//			String digitalObjectIdentifier,
//			String rawReferenceString,
			Source source) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		/*DictionaryUtilities.addIfNotNull(
			attributes,
//			new DictionaryEntry<String, Object>(ISI.ARTICLE_NUMBER, articleNumber),
//			new DictionaryEntry<String, Object>(
//				ISI.DIGITAL_OBJECT_IDENTIFIER, digitalObjectIdentifier),
			new DictionaryEntry<String, Object>(ISI.REFERENCE_STRING, rawReferenceString));*/

			if (source != null) {
				attributes.put(ISI.SOURCE, source.getPrimaryKey());
			}

		return attributes;
	}

	private static void fillAttributes(
			Dictionary<String, Object> attributes,
			String annotation,
			String articleNumber,
			Person authorPerson,
			String digitalObjectIdentifier,
			String otherInformation,
			Integer pageNumber,
			Document paper,
			String rawReferenceString,
			Integer referenceVolume,
			Source source,
			Integer year,
			boolean wasStarred) {
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Object>(ISI.ANNOTATION, annotation),
			new DictionaryEntry<String, Object>(ISI.REFERENCE_ARTICLE_NUMBER, articleNumber),
			new DictionaryEntry<String, Object>(
				ISI.DIGITAL_OBJECT_IDENTIFIER, digitalObjectIdentifier),
			new DictionaryEntry<String, Object>(ISI.REFERENCE_OTHER_INFORMATION, otherInformation),
			new DictionaryEntry<String, Object>(ISI.PAGE_NUMBER, pageNumber),
			new DictionaryEntry<String, Object>(ISI.REFERENCE_STRING, rawReferenceString),
			new DictionaryEntry<String, Object>(ISI.REFERENCE_VOLUME, referenceVolume),
			new DictionaryEntry<String, Object>(ISI.YEAR, year),
			new DictionaryEntry<String, Object>(ISI.REFERENCE_WAS_STARRED, wasStarred));

		if (authorPerson != null) {
			attributes.put(ISI.REFERENCE_AUTHOR, authorPerson.getPrimaryKey());
		}

		if (paper != null) {
			attributes.put(ISI.PAPER, paper.getPrimaryKey());
		}

		if (source != null) {
			attributes.put(ISI.SOURCE, source.getPrimaryKey());
		}
	}
}