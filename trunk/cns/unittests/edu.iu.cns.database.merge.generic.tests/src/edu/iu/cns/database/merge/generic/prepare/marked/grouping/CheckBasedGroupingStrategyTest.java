package edu.iu.cns.database.merge.generic.prepare.marked.grouping;

import static edu.iu.cns.database.merge.generic.prepare.marked.grouping.GroupingStrategyTestUtilities.CAPITALIZED_INITIALS_KEY_STRATEGY;
import static edu.iu.cns.database.merge.generic.prepare.marked.grouping.GroupingStrategyTestUtilities.COLUMN_NAME;
import static edu.iu.cns.database.merge.generic.prepare.marked.grouping.GroupingStrategyTestUtilities.SIZE_OF_COLLECTION;
import static edu.iu.cns.database.merge.generic.prepare.marked.grouping.GroupingStrategyTestUtilities.createTupleNamed;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Ordering;

import edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased.StringMetric;
import edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased.StringSimilarityMergeCheck;

/**
 * Exercises {@link KeyBasedGroupingStrategy}, {@link CheckBasedGroupingStrategy},
 * {@link StringSimilarityMergeCheck}, and {@link CompoundGroupingStrategy}.
 */
public class CheckBasedGroupingStrategyTest extends TestCase {
	private static final GroupingStrategy CAP_INITIALS_AND_NAME_SIM_CHECK_STRATEGY =
			CompoundGroupingStrategy.compound(
					CAPITALIZED_INITIALS_KEY_STRATEGY,
					new CheckBasedGroupingStrategy(
							new StringSimilarityMergeCheck(
									COLUMN_NAME, StringMetric.JARO.metric(), 0.5f)));
	
	protected static Table table;
	private static Collection<Integer> expectedTupleGroupSizes;

	@Override
	protected void setUp() throws Exception {
		table = new Table();		
		table.addColumn(COLUMN_NAME, String.class);
		
		Collection<? extends Collection<Tuple>> expectedTupleGroups =
				ImmutableSet.of(
						// "AB", string similarity check = yes
						ImmutableSet.of(createTupleNamed(table, "Abigail Benson"),
										createTupleNamed(table, "Abygail Benson")),						
						// "AB", string similarity check = no
						ImmutableSet.of(createTupleNamed(table, "Am Burger")),
						
						// "CD"
						ImmutableSet.of(createTupleNamed(table, "Carol Diamond"),
										createTupleNamed(table, "Carrol Diamond")),
										
						// "EF"
						ImmutableSet.of(createTupleNamed(table, "Earl Finnegan")));
		
		expectedTupleGroupSizes = Collections2.transform(expectedTupleGroups, SIZE_OF_COLLECTION);
	}
	
	public static void testGroupSizes() {
		Collection<Collection<Tuple>> actualTupleGroups =
				CAP_INITIALS_AND_NAME_SIM_CHECK_STRATEGY.splitIntoGroups(new Iterable<Tuple>() {
					@SuppressWarnings("unchecked")
					public Iterator<Tuple> iterator() {
						return table.tuples();
					}			
				});
		
		Collection<Integer> actualTupleGroupSizes =
				Collections2.transform(actualTupleGroups, SIZE_OF_COLLECTION);
		
		List<Integer> expectedSignature = Ordering.natural().sortedCopy(expectedTupleGroupSizes);
		List<Integer> actualSignature = Ordering.natural().sortedCopy(actualTupleGroupSizes);
		
		assertEquals(
				"Did not get expected tuple groupings.",
				expectedSignature,
				actualSignature);
	}
}
