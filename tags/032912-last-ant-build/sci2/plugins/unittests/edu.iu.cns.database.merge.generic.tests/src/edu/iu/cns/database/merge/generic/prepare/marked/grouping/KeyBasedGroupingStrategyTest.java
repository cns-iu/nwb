package edu.iu.cns.database.merge.generic.prepare.marked.grouping;

import static edu.iu.cns.database.merge.generic.prepare.marked.grouping.GroupingStrategyTestUtilities.CAPITALIZED_INITIALS_KEY_STRATEGY;
import static edu.iu.cns.database.merge.generic.prepare.marked.grouping.GroupingStrategyTestUtilities.COLUMN_NAME;
import static edu.iu.cns.database.merge.generic.prepare.marked.grouping.GroupingStrategyTestUtilities.SIZE_OF_COLLECTION;
import static edu.iu.cns.database.merge.generic.prepare.marked.grouping.GroupingStrategyTestUtilities.createTupleNamed;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;
import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Ordering;


public class KeyBasedGroupingStrategyTest extends TestCase {
	private static Table table;
	private static Collection<Integer> expectedTupleGroupSizes;

	@Override
	protected void setUp() throws Exception {
		table = new Table();		
		table.addColumn(COLUMN_NAME, String.class);
		
		Collection<? extends Collection<Tuple>> expectedTupleGroups =
				ImmutableSet.of(
						// "AB"
						ImmutableSet.of(createTupleNamed(table, "Abigail Benson"),
										createTupleNamed(table, "Archie Bunker"),
										createTupleNamed(table, "Am Burger")),
						// "CD"
						ImmutableSet.of(createTupleNamed(table, "Carol Diamond"),
										createTupleNamed(table, "Count Dracula")),
						// "EF"
						ImmutableSet.of(createTupleNamed(table, "Earl Finnegan")));
		
		expectedTupleGroupSizes = Collections2.transform(expectedTupleGroups, SIZE_OF_COLLECTION);
	}
	
	public static void testGroupSizes() {
		Collection<Collection<Tuple>> actualTupleGroups =
				CAPITALIZED_INITIALS_KEY_STRATEGY.splitIntoGroups(new Iterable<Tuple>() {
					@SuppressWarnings("unchecked")
					public Iterator<Tuple> iterator() {
						return table.tuples();
					}			
				});
		
		Collection<Integer> actualTupleGroupSizes =
				Collections2.transform(actualTupleGroups, SIZE_OF_COLLECTION);
		
		assertEquals(
				Ordering.natural().sortedCopy(expectedTupleGroupSizes),
				Ordering.natural().sortedCopy(actualTupleGroupSizes));
	}	
}
