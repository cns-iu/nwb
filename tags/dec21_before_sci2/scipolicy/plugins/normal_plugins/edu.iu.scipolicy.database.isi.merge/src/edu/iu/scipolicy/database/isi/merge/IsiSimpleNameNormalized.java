package edu.iu.scipolicy.database.isi.merge;

import java.util.regex.Pattern;

import prefuse.data.Tuple;
import edu.iu.cns.database.merge.generic.maker.KeyMaker;
import edu.iu.nwb.shared.isiutil.database.ISI;


public class IsiSimpleNameNormalized implements KeyMaker {
	
	Pattern replacePunctuation = Pattern.compile("[^a-zA-Z \\w\\d]");
	
	public Object makeKey(Tuple tuple) {
		return normalize(tuple.getString(ISI.UNSPLIT_ABBREVIATED_NAME));
	}

	private Object normalize(String string) {
		// TODO Auto-generated method stub
		return replacePunctuation.matcher(string.toLowerCase()).replaceAll("");
	}

}
