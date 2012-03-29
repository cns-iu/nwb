package edu.iu.sci2.database.nsf.merge;

import java.util.regex.Pattern;

import com.google.common.base.Function;

import prefuse.data.Tuple;
import edu.iu.sci2.utilities.nsf.NsfDatabaseFieldNames;


public class NsfSimpleNameNormalized implements Function<Tuple, String> {	
	Pattern replacePunctuation = Pattern.compile("[^a-zA-Z \\w\\d]");
	
	public String apply(Tuple tuple) {
		return normalize(tuple.getString(NsfDatabaseFieldNames.FORMATTED_FULL_NAME));
	}

	private String normalize(String string) {
		return replacePunctuation.matcher(string.toLowerCase()).replaceAll("");
	}
}
