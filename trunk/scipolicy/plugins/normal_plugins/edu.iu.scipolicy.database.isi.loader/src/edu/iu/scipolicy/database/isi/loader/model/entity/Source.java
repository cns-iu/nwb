package edu.iu.scipolicy.database.isi.loader.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class Source extends Entity<Source> implements Comparable<Source> {
	public final static Schema<Source> SCHEMA = new Schema<Source>(
		true,
		ISI.BOOK_SERIES_TITLE, DerbyFieldType.TEXT,
		ISI.BOOK_SERIES_SUBTITLE, DerbyFieldType.TEXT,
		ISI.CONFERENCE_HOST, DerbyFieldType.TEXT,
		ISI.CONFERENCE_LOCATION, DerbyFieldType.TEXT,
		ISI.CONFERENCE_SPONSORS, DerbyFieldType.TEXT,
		ISI.CONFERENCE_TITLE, DerbyFieldType.TEXT,
		ISI.FULL_TITLE, DerbyFieldType.TEXT,
		ISI.ISO_TITLE_ABBREVIATION, DerbyFieldType.TEXT,
		ISI.ISSN, DerbyFieldType.TEXT,
		ISI.PUBLICATION_TYPE, DerbyFieldType.TEXT,
		ISI.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION, DerbyFieldType.TEXT);

	private String bookSeriesTitle;
	private String bookSeriesSubtitle;
	private String conferenceHost;
	private String conferenceLocation;
	private String conferenceSponsors;
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
			String conferenceHost,
			String conferenceLocation,
			String conferenceSponsors,
			String conferenceTitle,
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
				conferenceHost,
				conferenceLocation,
				conferenceSponsors,
				conferenceTitle,
				fullTitle,
				isoTitleAbbreviation,
				issn,
				publicationType,
				twentyNineCharacterSourceTitleAbbreviation));
		this.bookSeriesTitle = bookSeriesTitle;
		this.bookSeriesSubtitle = bookSeriesSubtitle;
		this.conferenceHost = conferenceHost;
		this.conferenceLocation = conferenceLocation;
		this.conferenceSponsors = conferenceSponsors;
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

	public String getConferenceHost() {
		return this.conferenceHost;
	}

	public String getConferenceLocation() {
		return this.conferenceLocation;
	}

	public String getConferenceSponsors() {
		return this.conferenceSponsors;
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
		this.conferenceHost = StringUtilities.simpleMerge(
			this.conferenceHost, otherSource.getConferenceHost());
		this.conferenceLocation = StringUtilities.simpleMerge(
			this.conferenceLocation, otherSource.getConferenceLocation());
		this.conferenceSponsors = StringUtilities.simpleMerge(
			this.conferenceSponsors, otherSource.getConferenceSponsors());
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
			String conferenceHost,
			String conferenceLocation,
			String conferenceSponsors,
			String conferenceTitle,
			String fullTitle,
			String isoTitleAbbreviation,
			String issn,
			String publicationType,
			String twentyNineCharacterSourceTitleAbbreviation) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISI.BOOK_SERIES_TITLE, bookSeriesTitle);
		attributes.put(ISI.BOOK_SERIES_SUBTITLE, bookSeriesSubtitle);
		attributes.put(ISI.CONFERENCE_HOST, conferenceHost);
		attributes.put(ISI.CONFERENCE_LOCATION, conferenceLocation);
		attributes.put(ISI.CONFERENCE_SPONSORS, conferenceSponsors);
		attributes.put(ISI.CONFERENCE_TITLE, conferenceTitle);
		attributes.put(ISI.FULL_TITLE, fullTitle);
		attributes.put(ISI.ISO_TITLE_ABBREVIATION, isoTitleAbbreviation);
		attributes.put(ISI.ISSN, issn);
		attributes.put(ISI.PUBLICATION_TYPE, publicationType);
		attributes.put(
			ISI.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION,
			twentyNineCharacterSourceTitleAbbreviation);

		return attributes;
	}
}