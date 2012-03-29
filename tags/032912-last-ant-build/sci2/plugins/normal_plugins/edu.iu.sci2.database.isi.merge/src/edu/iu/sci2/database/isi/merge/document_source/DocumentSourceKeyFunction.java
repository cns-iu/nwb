package edu.iu.sci2.database.isi.merge.document_source;

import java.util.Map;
import java.util.UUID;

import prefuse.data.Tuple;

import com.google.common.base.Function;

import edu.iu.nwb.shared.isiutil.database.ISI;

public class DocumentSourceKeyFunction implements Function<Tuple, String> {	
	private Map<String, String> nameFormLookup;

	public DocumentSourceKeyFunction(Map<String, String> nameFormLookup) {
		this.nameFormLookup = nameFormLookup;
	}
	
	public String apply(Tuple tuple) {
		String j9 = retrieveNormalized(tuple, ISI.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION);
		String fullTitle = retrieveNormalized(tuple, ISI.FULL_TITLE);
		
		//prepend book series title/subtitle, conference title?
		if(nameFormLookup.containsKey(fullTitle)) {
			return nameFormLookup.get(fullTitle);
		} else if (nameFormLookup.containsKey(j9)) {
			return nameFormLookup.get(j9);
		} else if (j9.length() == 0) {
			return UUID.randomUUID().toString();
		} else {
			return j9;
		}
	}

	private static String retrieveNormalized(Tuple tuple, String field) {
		String value = tuple.getString(field);
		if(value == null) {
			return "";
		} else {
			return value.toLowerCase().trim();
		}
	}
}
