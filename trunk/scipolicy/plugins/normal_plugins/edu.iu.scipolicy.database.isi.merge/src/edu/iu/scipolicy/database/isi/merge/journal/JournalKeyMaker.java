package edu.iu.scipolicy.database.isi.merge.journal;

import java.util.Map;

import prefuse.data.Tuple;
import edu.iu.cns.database.merge.generic.maker.KeyMaker;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class JournalKeyMaker implements KeyMaker {	
	private Map<String, String> j9ToPrimary;
	
	public JournalKeyMaker(Map<String, String> j9ToPrimary) {
		this.j9ToPrimary = j9ToPrimary;
	}

	
	/* If the J9 field of the given source corresponds to a known primary J9, give that.
	 * Otherwise give the J9 field.
	 */
	public Object makeKey(Tuple tuple) {
		String j9 = tuple.getString(ISI.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION);		
		j9 = j9.toLowerCase();
		
		if (j9ToPrimary.containsKey(j9)) {
			return j9ToPrimary.get(j9).toLowerCase();
		} else {
			return j9;
		}
	}
}
