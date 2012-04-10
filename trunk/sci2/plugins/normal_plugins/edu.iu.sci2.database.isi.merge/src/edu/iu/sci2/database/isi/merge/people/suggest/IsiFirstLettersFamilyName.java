package edu.iu.sci2.database.isi.merge.people.suggest;

import prefuse.data.Tuple;

import com.google.common.base.Function;
import edu.iu.sci2.database.scholarly.model.entity.Person;

public class IsiFirstLettersFamilyName implements Function<Tuple, String> {
	private final int prefixLength;

	public IsiFirstLettersFamilyName(int prefixLength) {
		this.prefixLength = prefixLength;
	}

	public String apply(Tuple tuple) {
		//TODO: should we fall back on the unsplit name? shouldn't be necessary.
		String name = tuple.getString(Person.Field.FAMILY_NAME.name());
		return name.substring(0, Math.min(this.prefixLength, name.length())).toLowerCase();
	}

}
