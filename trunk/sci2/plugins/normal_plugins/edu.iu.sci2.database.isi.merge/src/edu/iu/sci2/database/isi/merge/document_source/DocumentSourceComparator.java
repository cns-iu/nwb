package edu.iu.sci2.database.isi.merge.document_source;

import java.util.Comparator;

import prefuse.data.Tuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased.AlphabeticalColumn;
import edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased.HasColumnValue;
import edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased.LongerColumn;
import edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased.ShorterColumn;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class DocumentSourceComparator implements Comparator<Tuple> {
	/* First, prefer having a source
	 * Then, prefer the source that is shortest
	 * Then, prefer the source that is first alphabetically
	 * Then, all the random other columns
	 * Then, prefer the j9 that is longest
	 * Then, prefer the j9 that is first alphabetically
	 * Note: a complete ordering over content is important,
	 * so that if someone does this twice in the same way,
	 * they get the same results, no matter what.
	 */
	public static final Ordering<Tuple> ordering =
		Ordering.compound(
				Lists.newArrayList(
					new HasColumnValue(ISI.FULL_TITLE),
					new ShorterColumn(ISI.FULL_TITLE),
					new AlphabeticalColumn(ISI.FULL_TITLE),
					new HasColumnValue(ISI.ISO_TITLE_ABBREVIATION),
					new HasColumnValue(ISI.BOOK_SERIES_TITLE),
					new HasColumnValue(ISI.BOOK_SERIES_SUBTITLE),
					new HasColumnValue(ISI.CONFERENCE_TITLE),
					new HasColumnValue(ISI.CONFERENCE_HOST),
					new HasColumnValue(ISI.CONFERENCE_LOCATION),
					new HasColumnValue(ISI.ISSN),
					new LongerColumn(ISI.ISO_TITLE_ABBREVIATION),
					new LongerColumn(ISI.BOOK_SERIES_TITLE),
					new LongerColumn(ISI.BOOK_SERIES_SUBTITLE),
					new LongerColumn(ISI.CONFERENCE_TITLE),
					new LongerColumn(ISI.CONFERENCE_HOST),
					new LongerColumn(ISI.CONFERENCE_LOCATION),
					new LongerColumn(ISI.ISSN),
					new AlphabeticalColumn(ISI.ISO_TITLE_ABBREVIATION),
					new AlphabeticalColumn(ISI.BOOK_SERIES_TITLE),
					new AlphabeticalColumn(ISI.BOOK_SERIES_SUBTITLE),
					new AlphabeticalColumn(ISI.CONFERENCE_TITLE),
					new AlphabeticalColumn(ISI.CONFERENCE_HOST),
					new AlphabeticalColumn(ISI.CONFERENCE_LOCATION),
					new AlphabeticalColumn(ISI.ISSN),
					new HasColumnValue(ISI.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION),
					new LongerColumn(ISI.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION),
					new AlphabeticalColumn(ISI.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION)));
	

	public int compare(Tuple tuple1, Tuple tuple2) {
		return ordering.compare(tuple1, tuple2);
	}
}
