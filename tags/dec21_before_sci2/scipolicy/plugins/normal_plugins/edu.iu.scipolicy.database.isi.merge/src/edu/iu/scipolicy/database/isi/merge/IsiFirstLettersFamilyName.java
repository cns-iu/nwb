package edu.iu.scipolicy.database.isi.merge;

import prefuse.data.Tuple;
import edu.iu.cns.database.merge.generic.maker.KeyMaker;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class IsiFirstLettersFamilyName implements KeyMaker {

	private final int prefixLength;

	public IsiFirstLettersFamilyName(int prefixLength) {
		this.prefixLength = prefixLength;
	}

	public Object makeKey(Tuple tuple) {
		//TODO: should we fall back on the unsplit name? shouldn't be necessary.
		String name = tuple.getString(ISI.FAMILY_NAME);
		return name.substring(0, Math.min(prefixLength, name.length())).toLowerCase();
	}

}
