package edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased;

import java.util.Collection;

import junit.framework.TestCase;

import org.cishell.utilities.ToCaseFunction;

import prefuse.data.Table;
import prefuse.data.Tuple;
import uk.ac.shef.wit.simmetrics.similaritymetrics.InterfaceStringMetric;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased.StringMetric;
import edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased.StringSimilarityMergeCheck;

public class StringSimilarityMergeCheckTest extends TestCase {
	public static final String COLUMN_NAME = "column";
	public static final ImmutableList<Function<String, String>> SIMPLE_FUNCTIONS =
			ImmutableList.of(
					Functions.<String>identity(),
					ToCaseFunction.UPPER,
					ToCaseFunction.LOWER);
		
	protected Tuple hankTuple;
	protected Tuple frankTuple;
	
	@Override
	protected void setUp() {
		// Base table
		Table table = new Table();
		table.addColumn(COLUMN_NAME, String.class);
		
		// Hank
		int hankRow = table.addRow();
		this.hankTuple = table.getTuple(hankRow);		
		this.hankTuple.set(COLUMN_NAME, "hank");
		table.addTuple(this.hankTuple);
		
		// Frank
		int frankRow = table.addRow();
		this.frankTuple = table.getTuple(frankRow);
		this.frankTuple.set(COLUMN_NAME, "frank");
		table.addTuple(this.frankTuple);		
	}

	public void testEasiestThresholdSucceeds() {
		for (StringSimilarityMergeCheck check : simpleMergeChecksForThreshold(0.0f)) {
			assertTrue(check.toString(), check.shouldMerge(this.hankTuple, this.frankTuple));
		}
	}
	
	public void testHardestThresholdFails() {
		for (StringSimilarityMergeCheck check : simpleMergeChecksForThreshold(1.0f)) {
			assertFalse(check.toString(), check.shouldMerge(this.hankTuple, this.frankTuple));
		}
	}
	
	private static Collection<StringSimilarityMergeCheck> simpleMergeChecksForThreshold(
			float threshold) {
		Collection<StringSimilarityMergeCheck> simpleMergeChecks = Sets.newHashSet();
		
		for (StringMetric stringMetric : StringMetric.values()) {
			InterfaceStringMetric metric = stringMetric.metric();
			
			for (Function<String, String> simpleFunction : SIMPLE_FUNCTIONS) {
				simpleMergeChecks.add(
						new StringSimilarityMergeCheck(
								COLUMN_NAME, simpleFunction, metric, threshold));
			}
		}
		
		return simpleMergeChecks;
	}
}
