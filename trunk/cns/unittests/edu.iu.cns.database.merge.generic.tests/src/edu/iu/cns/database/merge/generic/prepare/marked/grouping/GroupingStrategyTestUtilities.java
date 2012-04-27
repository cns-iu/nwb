package edu.iu.cns.database.merge.generic.prepare.marked.grouping;

import java.util.Collection;

import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public final class GroupingStrategyTestUtilities {
	static final String COLUMN_NAME = "name";	
	
	static final Function<Collection<?>, Integer> SIZE_OF_COLLECTION =
			new Function<Collection<?>, Integer>() {
				public Integer apply(Collection<?> collection) {
					return collection.size();
				}					
			};
	
	static final Function<String, String> TAKE_FIRST_CHARACTER_AND_CAPITALIZE =
			new Function<String, String>() {
				public String apply(String namePart) {
					return namePart.substring(0, 1).toUpperCase();
				}
			};
	static final Function<String, String> CAPITALIZED_INITIALS_OF =
			new Function<String, String>() {
				public String apply(String name) {
					return Joiner.on("").join(
							Collections2.transform(
									Lists.newArrayList(Splitter.on(" ").omitEmptyStrings().split(name)),
									TAKE_FIRST_CHARACTER_AND_CAPITALIZE));
				}
			};
	static final Function<Tuple, String> CAPITALIZED_INITIALS_OF_TUPLE_NAME =
			new Function<Tuple, String>() {
				public String apply(Tuple input) {
					return CAPITALIZED_INITIALS_OF.apply(input.getString(COLUMN_NAME));
				}
			};

	static final KeyBasedGroupingStrategy<String> CAPITALIZED_INITIALS_KEY_STRATEGY =
			new KeyBasedGroupingStrategy<String>(CAPITALIZED_INITIALS_OF_TUPLE_NAME);

	private GroupingStrategyTestUtilities() {}
	
	static Tuple createTupleNamed(Table table, String name) {		
		int tableRow = table.addRow();
		Tuple tuple = table.getTuple(tableRow);		
		tuple.setString(COLUMN_NAME, name);
		table.addTuple(tuple);
		
		return tuple;
	}
}
