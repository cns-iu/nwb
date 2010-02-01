package edu.iu.scipolicy.database.isi.merge;

import prefuse.data.Tuple;
import edu.iu.cns.database.merge.generic.maker.KeyMaker;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class IsiFirstLetterLastName implements KeyMaker {

	public Object makeKey(Tuple tuple) {
		String name = tuple.getString(ISI.UNSPLIT_ABBREVIATED_NAME);
		if(name.length() > 0) {
			return name.substring(0, 1).toLowerCase();
		} else {
			//really, this shouldn't happen. But just in case.
			return "";
		}
	}

}
