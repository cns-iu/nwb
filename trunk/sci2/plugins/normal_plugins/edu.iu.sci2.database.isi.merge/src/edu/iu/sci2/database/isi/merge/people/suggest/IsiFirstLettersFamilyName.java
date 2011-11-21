package edu.iu.sci2.database.isi.merge.people.suggest;

import prefuse.data.Tuple;

import com.google.common.base.Function;

import edu.iu.nwb.shared.isiutil.database.ISI;

public class IsiFirstLettersFamilyName implements Function<Tuple, String> {
	private final int prefixLength;

	public IsiFirstLettersFamilyName(int prefixLength) {
		this.prefixLength = prefixLength;
	}

	public String apply(Tuple tuple) {
		//TODO: should we fall back on the unsplit name? shouldn't be necessary.
		String name = tuple.getString(ISI.FAMILY_NAME);
		return name.substring(0, Math.min(prefixLength, name.length())).toLowerCase();
	}

}
