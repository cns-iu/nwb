package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;


import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.loader.isi.db.ISIDatabase;

public class Source extends Entity {
	private String fullTitle;
	private String publicationType;
	private String isoTitleAbbreviation;
	private String bookSeriesTitle;
	private String bookSeriesSubtitle;
	private String issn;
	private String twentyNineCharacterSourceTitleAbbreviation;
	private String conferenceTitle;
	private String conferenceDate;
	private String conferenceDonation;

	public Source(
			DatabaseTableKeyGenerator keyGenerator,
			String fullTitle,
			String publicationType,
			String isoTitleAbbreviation,
			String bookSeriesTitle,
			String bookSeriesSubtitle,
			String issn,
			String twentyNineCharacterSourceTitleAbbreviation,
			String conferenceTitle,
			String conferenceDate,
			String conferenceDonation) {
		super(keyGenerator);
		this.fullTitle = fullTitle;
		this.publicationType = publicationType;
		this.isoTitleAbbreviation = isoTitleAbbreviation;
		this.bookSeriesTitle = bookSeriesTitle;
		this.bookSeriesSubtitle = bookSeriesSubtitle;
		this.issn = issn;
		this.twentyNineCharacterSourceTitleAbbreviation =
			twentyNineCharacterSourceTitleAbbreviation;
		this.conferenceTitle = conferenceTitle;
		this.conferenceDate = conferenceDate;
		this.conferenceDonation = conferenceDonation;
	}

	public String getFullTitle() {
		return this.fullTitle;
	}

	public String getPublicationType() {
		return this.publicationType;
	}

	public String getISOTitleAbbreviation() {
		return this.isoTitleAbbreviation;
	}

	public String getBookSeriesTitle() {
		return this.bookSeriesTitle;
	}

	public String getBookSeriesSubtitle() {
		return this.bookSeriesSubtitle;
	}

	public String getISSN() {
		return this.issn;
	}

	public String get29CharacterSourceTitleAbbreviation() {
		return this.twentyNineCharacterSourceTitleAbbreviation;
	}

	public String getConferenceTitle() {
		return this.conferenceTitle;
	}

	public String getConferenceDate() {
		return this.conferenceDate;
	}

	public String getConferenceDonation() {
		return this.conferenceDonation;
	}

	public Dictionary<String, Object> createAttributes() {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(ISIDatabase.FULL_TITLE, this.fullTitle);
		attributes.put(ISIDatabase.PUBLICATION_TYPE, this.publicationType);
		attributes.put(ISIDatabase.ISO_TITLE_ABBREVIATION, this.isoTitleAbbreviation);
		attributes.put(ISIDatabase.BOOK_SERIES_TITLE, this.bookSeriesTitle);
		attributes.put(ISIDatabase.BOOK_SERIES_SUBTITLE, this.bookSeriesSubtitle);
		attributes.put(ISIDatabase.ISSN, this.issn);
		attributes.put(
			ISIDatabase.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION,
			this.twentyNineCharacterSourceTitleAbbreviation);
		attributes.put(ISIDatabase.CONFERENCE_TITLE, this.conferenceTitle);
		attributes.put(ISIDatabase.CONFERENCE_DATE, this.conferenceDate);
		attributes.put(ISIDatabase.CONFERENCE_DONATION, this.conferenceDonation);

		return attributes;
	}
}