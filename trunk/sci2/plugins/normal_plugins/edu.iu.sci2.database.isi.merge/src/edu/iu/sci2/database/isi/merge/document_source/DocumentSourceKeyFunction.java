package edu.iu.sci2.database.isi.merge.document_source;

import java.util.Map;
import java.util.UUID;

import prefuse.data.Tuple;

import com.google.common.base.Function;
import edu.iu.sci2.database.scholarly.model.entity.Source;

public class DocumentSourceKeyFunction implements Function<Tuple, String> {	
	private Map<String, String> nameFormLookup;

	public DocumentSourceKeyFunction(Map<String, String> nameFormLookup) {
		this.nameFormLookup = nameFormLookup;
	}
	
	public String apply(Tuple tuple) {
		String j9 = retrieveNormalized(tuple, Source.Field.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION.name());
		String fullTitle = retrieveNormalized(tuple, Source.Field.FULL_TITLE.name());
		
		//prepend book series title/subtitle, conference title?
		if(this.nameFormLookup.containsKey(fullTitle)) {
			return this.nameFormLookup.get(fullTitle);
		} else if (this.nameFormLookup.containsKey(j9)) {
			return this.nameFormLookup.get(j9);
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
		}
		return value.toLowerCase().trim();
	}
}
