package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;

public class Reference extends Entity<Reference> implements Comparable<Reference> {
	public final static Schema<Reference> SCHEMA = new Schema<Reference>(
		true,
		ISIDatabase.ANNOTATION, Schema.TEXT_CLASS,
		ISIDatabase.REFERENCE_AUTHOR, Schema.FOREIGN_KEY_CLASS,
		ISIDatabase.AUTHOR_WAS_STARRED, Schema.TEXT_CLASS,
		ISIDatabase.PAGE_NUMBER, Schema.INTEGER_CLASS,
		ISIDatabase.PAPER, Schema.FOREIGN_KEY_CLASS,
		ISIDatabase.REFERENCE_STRING, Schema.TEXT_CLASS,
		ISIDatabase.REFERENCE_VOLUME, Schema.INTEGER_CLASS,
		ISIDatabase.SOURCE, Schema.FOREIGN_KEY_CLASS,
		ISIDatabase.YEAR, Schema.INTEGER_CLASS).
		FOREIGN_KEYS(
			ISIDatabase.PAPER, ISIDatabase.DOCUMENT_TABLE_NAME,
			ISIDatabase.REFERENCE_AUTHOR, ISIDatabase.PERSON_TABLE_NAME,
			ISIDatabase.SOURCE, ISIDatabase.SOURCE_TABLE_NAME);

	private String annotation;
	private Person author;
	private boolean authorWasStarred;
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
				pageNumber,
				paper,
				rawReferenceString,
				referenceVolume,
				source,
				year));
		this.annotation = annotation;
		this.author = author;
		this.authorWasStarred = authorWasStarred;
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

	public Person getAuthor() {
		return this.author;
	}

	public boolean authorWasStarred() {
		return this.authorWasStarred;
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
		getAttributes().put(ISIDatabase.REFERENCE_AUTHOR, author);
	}

	public void setPaper(Document paper) {
		this.paper = paper;
		getAttributes().put(ISIDatabase.PAPER, paper);
	}

	public void setSource(Source source) {
		this.source = source;
		getAttributes().put(ISIDatabase.SOURCE, source);
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
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			String annotation,
			Person author,
			boolean authorWasStarred,
			int pageNumber,
			Document paper,
			String rawReferenceString,
			int referenceVolume,
			Source source,
			int year) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.ANNOTATION, annotation);

		if (author != null) {
			attributes.put(ISIDatabase.REFERENCE_AUTHOR, author);
		}

		attributes.put(ISIDatabase.AUTHOR_WAS_STARRED, authorWasStarred);
		attributes.put(ISIDatabase.PAGE_NUMBER, pageNumber);

		if (paper != null) {
			attributes.put(ISIDatabase.PAPER, paper);
		}

		attributes.put(ISIDatabase.REFERENCE_STRING, rawReferenceString);
		attributes.put(ISIDatabase.REFERENCE_VOLUME, referenceVolume);

		if (source != null) {
			attributes.put(ISIDatabase.SOURCE, source);
		}

		attributes.put(ISIDatabase.YEAR, year);

		return attributes;
	}
}