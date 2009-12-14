package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.awt.print.Paper;
import java.util.Dictionary;
import java.util.Hashtable;


import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.loader.isi.db.ISIDatabase;

public class Reference extends Entity {
	private String referenceString;
	private Paper paper;
	private Person person;
	private Source source;
	private int year;
	private String volume;
	private int pageNumber;

	public Reference(
			DatabaseTableKeyGenerator keyGenerator,
			String referenceString,
			Paper paper,
			Person person,
			Source source,
			int year,
			String volume,
			int pageNumber) {
		super(
			keyGenerator,
			createAttributes(referenceString, paper, person, source, year, volume, pageNumber));
		this.referenceString = referenceString;
		this.paper = paper;
		this.person = person;
		this.source = source;
		this.year = year;
		this.volume = volume;
		this.pageNumber = pageNumber;
	}

	public String getReferenceString() {
		return this.referenceString;
	}

	public Paper getPaper() {
		return this.paper;
	}

	public Person getPerson() {
		return this.person;
	}

	public Source getSource() {
		return this.source;
	}

	public int getYear() {
		return this.year;
	}

	public String getVolume() {
		return this.volume;
	}

	public int getPageNumber() {
		return this.pageNumber;
	}

	public static Dictionary<String, Object> createAttributes(
			String referenceString,
			Paper paper,
			Person person,
			Source source,
			int year,
			String volume,
			int pageNumber) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(ISIDatabase.REFERENCE_STRING, referenceString);
		attributes.put(ISIDatabase.PAPER, paper);
		attributes.put(ISIDatabase.PERSON, person);
		attributes.put(ISIDatabase.SOURCE, source);
		attributes.put(ISIDatabase.YEAR, year);
		attributes.put(ISIDatabase.REFERENCE_VOLUME, volume);
		attributes.put(ISIDatabase.PAGE_NUMBER, pageNumber);

		return attributes;
	}
}