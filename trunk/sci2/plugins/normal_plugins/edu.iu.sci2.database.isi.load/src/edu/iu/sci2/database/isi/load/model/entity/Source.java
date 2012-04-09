package edu.iu.sci2.database.isi.load.model.entity;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

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

public class Source extends Entity<Source> {
	public final static Schema<Source> SCHEMA = new Schema<Source>(
		true,
		ISI.BOOK_SERIES_TITLE, DerbyFieldType.TEXT,
		ISI.BOOK_SERIES_SUBTITLE, DerbyFieldType.TEXT,
		ISI.CONFERENCE_HOST, DerbyFieldType.TEXT,
		ISI.CONFERENCE_LOCATION, DerbyFieldType.TEXT,
		ISI.CONFERENCE_SPONSORS, DerbyFieldType.TEXT,
		ISI.CONFERENCE_TITLE, DerbyFieldType.TEXT,
		ISI.CONFERENCE_DATES, DerbyFieldType.TEXT,
		ISI.FULL_TITLE, DerbyFieldType.TEXT,
		ISI.ISO_TITLE_ABBREVIATION, DerbyFieldType.TEXT,
		ISI.ISSN, DerbyFieldType.TEXT,
		ISI.PUBLICATION_TYPE, DerbyFieldType.TEXT,
		ISI.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION, DerbyFieldType.TEXT);

	private String twentyNineCharacterSourceTitleAbbreviation;
	private Tuple originalRow;

	public Source(
			DatabaseTableKeyGenerator keyGenerator,
			String twentyNineCharacterSourceTitleAbbreviation,
			Tuple originalRow) {
		super(
			keyGenerator,
			createInitialAttributes(
				twentyNineCharacterSourceTitleAbbreviation));
		this.twentyNineCharacterSourceTitleAbbreviation =
			twentyNineCharacterSourceTitleAbbreviation;
		this.originalRow = originalRow;
	}

	public String getBookSeriesTitle() {
		if (this.originalRow == null){
			return null;
		}
		
		return StringUtilities.trimIfNotNull(
				this.originalRow.getString(ISITag.BOOK_SERIES_TITLE.getColumnName()));
	}

	public String getBookSeriesSubtitle() {
		if (this.originalRow == null) {
			return null;
		}
		
		return StringUtilities.trimIfNotNull(
				this.originalRow.getString(ISITag.BOOK_SERIES_SUBTITLE.getColumnName()));
	}

	public String getConferenceHost() {
		if (this.originalRow == null) {
			return null;
		}
		
		return StringUtilities.trimIfNotNull(
				this.originalRow.getString(ISITag.CONFERENCE_HOST.getColumnName()));
	}

	public String getConferenceLocation() {
		if (this.originalRow == null) {
			return null;
		}
		
		return StringUtilities.trimIfNotNull(
				this.originalRow.getString(ISITag.CONFERENCE_LOCATION.getColumnName()));
	}

	public String getConferenceSponsors() {
		if (this.originalRow == null) {
			return null;
		}
		return StringUtilities.trimIfNotNull(
				this.originalRow.getString(ISITag.CONFERENCE_SPONSORS.getColumnName()));
	}

	public String getConferenceTitle() {
		if (this.originalRow == null) {
			return null;
		}
		return StringUtilities.trimIfNotNull(
				this.originalRow.getString(ISITag.CONFERENCE_TITLE.getColumnName()));
	}

	public String getConferenceDates() {
		if (this.originalRow == null) {
			return null;
		}
		return StringUtilities.trimIfNotNull(
				this.originalRow.getString(ISITag.CONFERENCE_DATES.getColumnName()));
	}
	
	public String getFullTitle() {
		if (this.originalRow == null) {
			return null;
		}
		return StringUtilities.trimIfNotNull(
				this.originalRow.getString(ISITag.FULL_JOURNAL_TITLE.getColumnName()));
	}

	public String getISOTitleAbbreviation() {
		if (this.originalRow == null) {
			return null;
		}
		return StringUtilities.trimIfNotNull(
				this.originalRow.getString(ISITag.ISO_JOURNAL_TITLE_ABBREVIATION.getColumnName()));
	}

	public String getISSN() {
		if (this.originalRow == null) {
			return null;
		}
		return StringUtilities.trimIfNotNull(
				this.originalRow.getString(ISITag.ISSN.getColumnName()));
	}

	public String getPublicationType() {
		if (this.originalRow == null) {
			return null;
		}
		return StringUtilities.trimIfNotNull(
				this.originalRow.getString(ISITag.PUBLICATION_TYPE.getColumnName()));
	}

	public String get29CharacterSourceTitleAbbreviation() {
		return this.twentyNineCharacterSourceTitleAbbreviation;
	}

	@Override
	public Dictionary<String, Object> getAttributesForInsertion() {
		Dictionary<String, Object> attributes = DictionaryUtilities.copy(super.getAttributes());
		fillAttributes(
			attributes,
			getBookSeriesTitle(),
			getBookSeriesSubtitle(),
			getConferenceHost(),
			getConferenceLocation(),
			getConferenceSponsors(),
			getConferenceTitle(),
			getConferenceDates(),
			getFullTitle(),
			getISOTitleAbbreviation(),
			getISSN(),
			getPublicationType());

		return attributes;
	}

	@Override
	public Object createMergeKey() {
		List<Object> mergeKey = new ArrayList<Object>();
		Integer primaryKey = getPrimaryKey();
		addStringOrAlternativeToMergeKey(mergeKey, getBookSeriesTitle(), null);
		addStringOrAlternativeToMergeKey(mergeKey, getBookSeriesSubtitle(), null);
		addStringOrAlternativeToMergeKey(mergeKey, getConferenceHost(), null);
		addStringOrAlternativeToMergeKey(mergeKey, getConferenceLocation(), null);
		addStringOrAlternativeToMergeKey(mergeKey, getConferenceSponsors(), null);
		addStringOrAlternativeToMergeKey(mergeKey, getConferenceTitle(), null);
		addStringOrAlternativeToMergeKey(mergeKey, getFullTitle(), null);
		addStringOrAlternativeToMergeKey(mergeKey, getISOTitleAbbreviation(), null);
		addStringOrAlternativeToMergeKey(mergeKey, getISSN(), null);
		addStringOrAlternativeToMergeKey(mergeKey, getPublicationType(), null);
		addStringOrAlternativeToMergeKey(
			mergeKey, this.twentyNineCharacterSourceTitleAbbreviation, primaryKey);

		return mergeKey.hashCode();
	}

	/**
	 * Warning! Unimplemented!
	 */
	@Override
	public void merge(Source otherSource) {
	}

	private static Dictionary<String, Object> createInitialAttributes(
			String twentyNineCharacterSourceTitleAbbreviation) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Object>(
				ISI.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION,
				twentyNineCharacterSourceTitleAbbreviation));

		return attributes;
	}

	public static void fillAttributes(
			Dictionary<String, Object> attributes,
			String bookSeriesTitle,
			String bookSeriesSubtitle,
			String conferenceHost,
			String conferenceLocation,
			String conferenceSponsors,
			String conferenceTitle,
			String conferenceDates,
			String fullTitle,
			String isoTitleAbbreviation,
			String issn,
			String publicationType) {
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Object>(ISI.BOOK_SERIES_TITLE, bookSeriesTitle),
			new DictionaryEntry<String, Object>(
				ISI.BOOK_SERIES_SUBTITLE, bookSeriesSubtitle),
			new DictionaryEntry<String, Object>(ISI.CONFERENCE_HOST, conferenceHost),
			new DictionaryEntry<String, Object>(
				ISI.CONFERENCE_LOCATION, conferenceLocation),
			new DictionaryEntry<String, Object>(
				ISI.CONFERENCE_SPONSORS, conferenceSponsors),
			new DictionaryEntry<String, Object>(ISI.CONFERENCE_TITLE, conferenceTitle),
			new DictionaryEntry<String, Object>(ISI.CONFERENCE_DATES, conferenceDates),
			new DictionaryEntry<String, Object>(ISI.FULL_TITLE, fullTitle),
			new DictionaryEntry<String, Object>(
				ISI.ISO_TITLE_ABBREVIATION, isoTitleAbbreviation),
			new DictionaryEntry<String, Object>(ISI.ISSN, issn),
			new DictionaryEntry<String, Object>(ISI.PUBLICATION_TYPE, publicationType));
	}
}