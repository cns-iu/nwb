package edu.iu.scipolicy.database.isi.merge.journal;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import prefuse.data.Tuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import edu.iu.cns.database.merge.generic.maker.LongerColumn;
import edu.iu.cns.database.merge.generic.maker.PreferrableFormComparator;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class JournalComparator implements PreferrableFormComparator {
	public static final List<? extends Comparator<Tuple>> columns =
		Lists.newArrayList(
				new LongerColumn(ISI.FULL_TITLE),
				new LongerColumn(ISI.ISO_TITLE_ABBREVIATION),
				new LongerColumn(ISI.ISSN),
				new LongerColumn(ISI.CONFERENCE_TITLE),
				new LongerColumn(ISI.BOOK_SERIES_TITLE),
				new LongerColumn(ISI.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION));
	
	private Map<String, String> j9ToPrimary;
	
	public JournalComparator(Map<String, String> j9ToPrimary) {
		this.j9ToPrimary = j9ToPrimary;
	}
	

	/* First consider whether either is primary.
	 * If neither is, consider the lengths of the specified columns.
	 */
	public int compare(Tuple tuple1, Tuple tuple2) {
		String j91 =
			tuple1.getString(ISI.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION).toLowerCase();
		String j92 =
			tuple2.getString(ISI.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION).toLowerCase();
		
		if (isPrimary(j91)) {
			return 1;
		}
		
		if (isPrimary(j92)) {
			return -1;
		}
		
		return Ordering.compound(columns).compare(tuple1, tuple2);
	}
	
	private boolean isPrimary(String j9) {
		return j9.equalsIgnoreCase(j9ToPrimary.get(j9));
	}
}
