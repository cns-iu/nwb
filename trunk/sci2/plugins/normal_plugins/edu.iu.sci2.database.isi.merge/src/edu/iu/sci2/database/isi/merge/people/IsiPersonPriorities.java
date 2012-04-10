package edu.iu.sci2.database.isi.merge.people;

import java.util.Comparator;

import prefuse.data.Tuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased.LongerColumn;
import edu.iu.sci2.database.scholarly.model.entity.Person;

public class IsiPersonPriorities implements Comparator<Tuple> {	
	private Ordering<Tuple> ordering =
			Ordering.compound(Lists.newArrayList(
					new LongerColumn(Person.Field.FULL_NAME.name()),
					new LongerColumn(Person.Field.FIRST_NAME.name()),
					new LongerColumn(Person.Field.RAW_NAME.name()),
					new LongerColumn(Person.Field.MIDDLE_INITIAL.name())));
	
	
	public int compare(Tuple o1, Tuple o2) {
		return this.ordering.compare(o1, o2);
	}
}
