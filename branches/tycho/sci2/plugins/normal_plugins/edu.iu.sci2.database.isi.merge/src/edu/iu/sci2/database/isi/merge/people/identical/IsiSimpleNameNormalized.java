package edu.iu.sci2.database.isi.merge.people.identical;

import java.util.regex.Pattern;

import prefuse.data.Tuple;

import com.google.common.base.Function;

import edu.iu.nwb.shared.isiutil.database.ISI;


public class IsiSimpleNameNormalized implements Function<Tuple, String> {	
	Pattern replacePunctuation = Pattern.compile("[^a-zA-Z \\w\\d]");
	
	public String apply(Tuple tuple) {
		return normalize(tuple.getString(ISI.UNSPLIT_ABBREVIATED_NAME));
	}

	private String normalize(String string) {
		return replacePunctuation.matcher(string.toLowerCase()).replaceAll("");
	}
}
