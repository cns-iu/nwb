package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;

public class Reference extends Entity<Reference> {
	private String referenceString;
	private Document paper;
	private Person author;
	private Source source;
	private int year;
	private int volume;
	private int pageNumber;
	private String annotation;
	private boolean starred;

	public Reference(
			DatabaseTableKeyGenerator keyGenerator,
			String referenceString,
			Document paper,
			Person author,
			Source source,
			int year,
			int volume,
			int pageNumber,
			String annotation,
			boolean starred) {
		super(
			keyGenerator,
			createAttributes(
				referenceString,
				paper,
				author,
				source,
				year,
				volume,
				pageNumber,
				annotation,
				starred));
		this.referenceString = referenceString;
		this.paper = paper;
		this.author = author;
		this.source = source;
		this.year = year;
		this.volume = volume;
		this.pageNumber = pageNumber;
		this.annotation = annotation;
		this.starred = starred;
	}

	public String getReferenceString() {
		return this.referenceString;
	}

	public Document getPaper() {
		return this.paper;
	}

	public Person getAuthor() {
		return this.author;
	}

	public Source getSource() {
		return this.source;
	}

	public int getYear() {
		return this.year;
	}

	public int getVolume() {
		return this.volume;
	}

	public int getPageNumber() {
		return this.pageNumber;
	}

	public String getAnnotation() {
		return this.annotation;
	}

	public boolean authorWasStarred() {
		return this.starred;
	}

	public void setPaper(Document paper) {
		this.paper = paper;
		getAttributes().put(ISIDatabase.PAPER, paper);
	}

	public void setAuthor(Person author) {
		this.author = author;
		getAttributes().put(ISIDatabase.REFERENCE_AUTHOR, author);
	}

	public void setSource(Source source) {
		this.source = source;
		getAttributes().put(ISIDatabase.SOURCE, source);
	}

	public Reference merge(Reference otherReference) {
		// TODO: Implement this.
		return this;
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			String referenceString,
			Document paper,
			Person author,
			Source source,
			int year,
			int volume,
			int pageNumber,
			String annotation,
			boolean starred) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.REFERENCE_STRING, referenceString);

		if (paper != null) {
			attributes.put(ISIDatabase.PAPER, paper);
		}

		if (author != null) {
			attributes.put(ISIDatabase.REFERENCE_AUTHOR, author);
		}

		if (source != null) {
			attributes.put(ISIDatabase.SOURCE, source);
		}

		attributes.put(ISIDatabase.YEAR, year);
		attributes.put(ISIDatabase.REFERENCE_VOLUME, volume);
		attributes.put(ISIDatabase.PAGE_NUMBER, pageNumber);
		attributes.put(ISIDatabase.ANNOTATION, annotation);
		attributes.put(ISIDatabase.AUTHOR_WAS_STARRED, starred);

		return attributes;
	}
}