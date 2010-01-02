package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;

public class Source extends Entity<Source> implements Comparable<Source> {
	public final static Schema<Source> SCHEMA = new Schema<Source>(
		ISIDatabase.BOOK_SERIES_TITLE, Schema.TEXT_CLASS,
		ISIDatabase.BOOK_SERIES_SUBTITLE, Schema.TEXT_CLASS,
		ISIDatabase.CONFERENCE_DATE, Schema.TEXT_CLASS,
		ISIDatabase.CONFERENCE_DONATION, Schema.TEXT_CLASS,
		ISIDatabase.CONFERENCE_TITLE, Schema.TEXT_CLASS,
		ISIDatabase.FULL_TITLE, Schema.TEXT_CLASS,
		ISIDatabase.ISO_TITLE_ABBREVIATION, Schema.TEXT_CLASS,
		ISIDatabase.ISSN, Schema.TEXT_CLASS,
		ISIDatabase.PUBLICATION_TYPE, Schema.TEXT_CLASS,
		ISIDatabase.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION, Schema.TEXT_CLASS);

	private String bookSeriesTitle;
	private String bookSeriesSubtitle;
	private String conferenceDate;
	private String conferenceDonation;
	private String conferenceTitle;
	private String fullTitle;
	private String isoTitleAbbreviation;
	private String issn;
	private String publicationType;
	private String twentyNineCharacterSourceTitleAbbreviation;

	public Source(
			DatabaseTableKeyGenerator keyGenerator,
			String bookSeriesTitle,
			String bookSeriesSubtitle,
			String conferenceTitle,
			String conferenceDate,
			String conferenceDonation,
			String fullTitle,
			String isoTitleAbbreviation,
			String issn,
			String publicationType,
			String twentyNineCharacterSourceTitleAbbreviation) {
		super(
			keyGenerator,
			createAttributes(
				bookSeriesTitle,
				bookSeriesSubtitle,
				conferenceDate,
				conferenceDonation,
				conferenceTitle,
				fullTitle,
				isoTitleAbbreviation,
				issn,
				publicationType,
				twentyNineCharacterSourceTitleAbbreviation));
		this.bookSeriesTitle = bookSeriesTitle;
		this.bookSeriesSubtitle = bookSeriesSubtitle;
		this.conferenceDate = conferenceDate;
		this.conferenceDonation = conferenceDonation;
		this.conferenceTitle = conferenceTitle;
		this.fullTitle = fullTitle;
		this.isoTitleAbbreviation = isoTitleAbbreviation;
		this.issn = issn;
		this.publicationType = publicationType;
		this.twentyNineCharacterSourceTitleAbbreviation =
			twentyNineCharacterSourceTitleAbbreviation;
	}

	public String getBookSeriesTitle() {
		return this.bookSeriesTitle;
	}

	public String getBookSeriesSubtitle() {
		return this.bookSeriesSubtitle;
	}

	public String getConferenceDate() {
		return this.conferenceDate;
	}

	public String getConferenceDonation() {
		return this.conferenceDonation;
	}

	public String getConferenceTitle() {
		return this.conferenceTitle;
	}

	public String getFullTitle() {
		return this.fullTitle;
	}

	public String getISOTitleAbbreviation() {
		return this.isoTitleAbbreviation;
	}

	public String getISSN() {
		return this.issn;
	}

	public String getPublicationType() {
		return this.publicationType;
	}

	public String get29CharacterSourceTitleAbbreviation() {
		return this.twentyNineCharacterSourceTitleAbbreviation;
	}

	public int compareTo(Source otherSource) {
		return get29CharacterSourceTitleAbbreviation().compareTo(
			otherSource.get29CharacterSourceTitleAbbreviation());
	}

	public boolean shouldMerge(Source otherSource) {
		return (
			StringUtilities.validAndEquivalent(this.issn, otherSource.getISSN()) ||
			StringUtilities.validAndEquivalent(
				this.twentyNineCharacterSourceTitleAbbreviation,
				otherSource.get29CharacterSourceTitleAbbreviation()));
	}

	public void merge(Source otherSource) {
		this.bookSeriesTitle =
			StringUtilities.simpleMerge(this.bookSeriesTitle, otherSource.getBookSeriesTitle());
		this.bookSeriesSubtitle = StringUtilities.simpleMerge(
			this.bookSeriesSubtitle, otherSource.getBookSeriesSubtitle());
		this.conferenceDate = StringUtilities.simpleMerge(
			this.conferenceDate, otherSource.getConferenceDate());
		this.conferenceDonation = StringUtilities.simpleMerge(
			this.conferenceDonation, otherSource.getConferenceDonation());
		this.conferenceTitle = StringUtilities.simpleMerge(
			this.conferenceTitle, otherSource.getConferenceTitle());
		this.fullTitle = StringUtilities.simpleMerge(this.fullTitle, otherSource.getFullTitle());
		this.isoTitleAbbreviation = StringUtilities.simpleMerge(
			this.isoTitleAbbreviation, otherSource.getISOTitleAbbreviation());	
		this.issn = StringUtilities.simpleMerge(this.issn, otherSource.getISSN());
		this.publicationType =
			StringUtilities.simpleMerge(this.publicationType, otherSource.getPublicationType());
		this.twentyNineCharacterSourceTitleAbbreviation = StringUtilities.simpleMerge(
			this.twentyNineCharacterSourceTitleAbbreviation,
			otherSource.get29CharacterSourceTitleAbbreviation());
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			String bookSeriesTitle,
			String bookSeriesSubtitle,
			String conferenceDate,
			String conferenceDonation,
			String conferenceTitle,
			String fullTitle,
			String isoTitleAbbreviation,
			String issn,
			String publicationType,
			String twentyNineCharacterSourceTitleAbbreviation) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.BOOK_SERIES_TITLE, bookSeriesTitle);
		attributes.put(ISIDatabase.BOOK_SERIES_SUBTITLE, bookSeriesSubtitle);
		attributes.put(ISIDatabase.CONFERENCE_DATE, conferenceDate);
		attributes.put(ISIDatabase.CONFERENCE_DONATION, conferenceDonation);
		attributes.put(ISIDatabase.CONFERENCE_TITLE, conferenceTitle);
		attributes.put(ISIDatabase.FULL_TITLE, fullTitle);
		attributes.put(ISIDatabase.ISO_TITLE_ABBREVIATION, isoTitleAbbreviation);
		attributes.put(ISIDatabase.ISSN, issn);
		attributes.put(ISIDatabase.PUBLICATION_TYPE, publicationType);
		attributes.put(
			ISIDatabase.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION,
			twentyNineCharacterSourceTitleAbbreviation);

		return attributes;
	}
}