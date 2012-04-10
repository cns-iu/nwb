package edu.iu.sci2.database.isi.merge.document_source;

import java.util.Comparator;

import prefuse.data.Tuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased.AlphabeticalColumn;
import edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased.HasColumnValue;
import edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased.LongerColumn;
import edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased.ShorterColumn;
import edu.iu.sci2.database.scholarly.model.entity.Source;

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
					new HasColumnValue(Source.Field.FULL_TITLE.name()),
					new ShorterColumn(Source.Field.FULL_TITLE.name()),
					new AlphabeticalColumn(Source.Field.FULL_TITLE.name()),
					new HasColumnValue(Source.Field.ISO_TITLE_ABBREVIATION.name()),
					new HasColumnValue(Source.Field.BOOK_SERIES_TITLE.name()),
					new HasColumnValue(Source.Field.BOOK_SERIES_SUBTITLE.name()),
					new HasColumnValue(Source.Field.CONFERENCE_TITLE.name()),
					new HasColumnValue(Source.Field.CONFERENCE_HOST.name()),
					new HasColumnValue(Source.Field.CONFERENCE_LOCATION.name()),
					new HasColumnValue(Source.Field.ISSN.name()),
					new LongerColumn(Source.Field.ISO_TITLE_ABBREVIATION.name()),
					new LongerColumn(Source.Field.BOOK_SERIES_TITLE.name()),
					new LongerColumn(Source.Field.BOOK_SERIES_SUBTITLE.name()),
					new LongerColumn(Source.Field.CONFERENCE_TITLE.name()),
					new LongerColumn(Source.Field.CONFERENCE_HOST.name()),
					new LongerColumn(Source.Field.CONFERENCE_LOCATION.name()),
					new LongerColumn(Source.Field.ISSN.name()),
					new AlphabeticalColumn(Source.Field.ISO_TITLE_ABBREVIATION.name()),
					new AlphabeticalColumn(Source.Field.BOOK_SERIES_TITLE.name()),
					new AlphabeticalColumn(Source.Field.BOOK_SERIES_SUBTITLE.name()),
					new AlphabeticalColumn(Source.Field.CONFERENCE_TITLE.name()),
					new AlphabeticalColumn(Source.Field.CONFERENCE_HOST.name()),
					new AlphabeticalColumn(Source.Field.CONFERENCE_LOCATION.name()),
					new AlphabeticalColumn(Source.Field.ISSN.name()),
					new HasColumnValue(Source.Field.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION.name()),
					new LongerColumn(Source.Field.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION.name()),
					new AlphabeticalColumn(Source.Field.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION.name())));
	

	public int compare(Tuple tuple1, Tuple tuple2) {
		return ordering.compare(tuple1, tuple2);
	}
}
