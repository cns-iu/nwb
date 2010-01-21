package edu.iu.scipolicy.database.isi.load.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class Reference extends Entity<Reference> implements Comparable<Reference> {
	public final static Schema<Reference> SCHEMA = new Schema<Reference>(
		true,
		ISI.ANNOTATION, DerbyFieldType.TEXT,
		ISI.REFERENCE_AUTHOR, DerbyFieldType.FOREIGN_KEY,
		ISI.AUTHOR_WAS_STARRED, DerbyFieldType.TEXT,
		ISI.DIGITAL_OBJECT_IDENTIFIER, DerbyFieldType.TEXT,
		ISI.PAGE_NUMBER, DerbyFieldType.INTEGER,
		ISI.PAPER, DerbyFieldType.FOREIGN_KEY,
		ISI.REFERENCE_STRING, DerbyFieldType.TEXT,
		ISI.REFERENCE_VOLUME, DerbyFieldType.INTEGER,
		ISI.SOURCE, DerbyFieldType.FOREIGN_KEY,
		ISI.YEAR, DerbyFieldType.INTEGER).
		FOREIGN_KEYS(
			ISI.PAPER, ISI.DOCUMENT_TABLE_NAME,
			ISI.REFERENCE_AUTHOR, ISI.PERSON_TABLE_NAME,
			ISI.SOURCE, ISI.SOURCE_TABLE_NAME);

	private String annotation;
	private Person author;
	private boolean authorWasStarred;
	private String digitalObjectIdentifier;
	private int pageNumber;
	private Document paper;
	private String rawReferenceString;
	private int referenceVolume;
	private Source source;
	private int year;

	public Reference(
			DatabaseTableKeyGenerator keyGenerator,
			String annotation,
			Person author,
			boolean authorWasStarred,
			String digitalObjectIdentifier,
			int pageNumber,
			Document paper,
			String rawReferenceString,
			int referenceVolume,
			Source source,
			int year) {
		super(
			keyGenerator,
			createAttributes(
				annotation,
				author,
				authorWasStarred,
				digitalObjectIdentifier,
				pageNumber,
				paper,
				rawReferenceString,
				referenceVolume,
				source,
				year));
		this.annotation = annotation;
		this.author = author;
		this.authorWasStarred = authorWasStarred;
		this.digitalObjectIdentifier = digitalObjectIdentifier;
		this.pageNumber = pageNumber;
		this.paper = paper;
		this.rawReferenceString = rawReferenceString;
		this.referenceVolume = referenceVolume;
		this.source = source;
		this.year = year;
	}

	public String getAnnotation() {
		return this.annotation;
	}

	public Person getAuthorPerson() {
		return this.author;
	}

	public boolean authorWasStarred() {
		return this.authorWasStarred;
	}

	public String getDigitalObjectIdentifier() {
		return this.digitalObjectIdentifier;
	}

	public int getPageNumber() {
		return this.pageNumber;
	}

	public Document getPaper() {
		return this.paper;
	}

	public String getRawReferenceString() {
		return this.rawReferenceString;
	}

	public int getReferenceVolume() {
		return this.referenceVolume;
	}

	public Source getSource() {
		return this.source;
	}

	public int getYear() {
		return this.year;
	}

	public void setAuthor(Person author) {
		this.author = author;
		getAttributes().put(ISI.REFERENCE_AUTHOR, author.getPrimaryKey());
	}

	public void setPaper(Document paper) {
		this.paper = paper;
		getAttributes().put(ISI.PAPER, paper.getPrimaryKey());
	}

	public void setSource(Source source) {
		this.source = source;
		getAttributes().put(ISI.SOURCE, source.getPrimaryKey());
	}

	public int compareTo(Reference otherReference) {
		// TODO:
		return -1;
	}

	public boolean shouldMerge(Reference otherReference) {
		return StringUtilities.validAndEquivalent(
			this.rawReferenceString, otherReference.getRawReferenceString());
	}

	public void merge(Reference otherReference) {
		// TODO:
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			String annotation,
			Person author,
			boolean authorWasStarred,
			String digitalObjectIdentifier,
			int pageNumber,
			Document paper,
			String rawReferenceString,
			int referenceVolume,
			Source source,
			int year) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISI.ANNOTATION, annotation);

		if (author != null) {
			attributes.put(ISI.REFERENCE_AUTHOR, author.getPrimaryKey());
		}

		attributes.put(ISI.AUTHOR_WAS_STARRED, authorWasStarred);
		attributes.put(ISI.DIGITAL_OBJECT_IDENTIFIER, digitalObjectIdentifier);
		attributes.put(ISI.PAGE_NUMBER, pageNumber);

		if (paper != null) {
			attributes.put(ISI.PAPER, paper.getPrimaryKey());
		}

		attributes.put(ISI.REFERENCE_STRING, rawReferenceString);
		attributes.put(ISI.REFERENCE_VOLUME, referenceVolume);

		if (source != null) {
			attributes.put(ISI.SOURCE, source.getPrimaryKey());
		}

		attributes.put(ISI.YEAR, year);

		return attributes;
	}
}