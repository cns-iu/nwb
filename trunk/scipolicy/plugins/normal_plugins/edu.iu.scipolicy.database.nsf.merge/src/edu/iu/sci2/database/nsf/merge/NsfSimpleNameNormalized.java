package edu.iu.sci2.database.nsf.merge;

import java.util.regex.Pattern;

import prefuse.data.Tuple;
import edu.iu.cns.database.merge.generic.maker.KeyMaker;
import edu.iu.scipolicy.utilities.nsf.NSF_Database_FieldNames;


public class NsfSimpleNameNormalized implements KeyMaker {
	
	Pattern replacePunctuation = Pattern.compile("[^a-zA-Z \\w\\d]");
	
	public Object makeKey(Tuple tuple) {
		return normalize(tuple.getString(NSF_Database_FieldNames.FORMATTED_FULL_NAME));
	}

	private Object normalize(String string) {
		// TODO Auto-generated method stub
		return replacePunctuation.matcher(string.toLowerCase()).replaceAll("");
	}

}
