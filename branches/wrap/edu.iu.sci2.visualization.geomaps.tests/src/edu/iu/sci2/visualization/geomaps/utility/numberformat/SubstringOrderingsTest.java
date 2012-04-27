package edu.iu.sci2.visualization.geomaps.utility.numberformat;

import junit.framework.TestCase;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

import edu.iu.sci2.visualization.geomaps.utility.SubstringOrderings;

public class SubstringOrderingsTest extends TestCase {
	private static Ordering<String> goodnessSubstringOrdering;
	
	@Override
	protected void setUp() throws Exception {
		SubstringOrderingsTest.goodnessSubstringOrdering =
				SubstringOrderings.explicit("good", "better", "best");
	}
	
	public static void testSubstringOrdering() {
		assertEquals(
				ImmutableList.of(
						"ignored :( :( :(",
						"overlooked :( :( :(",
						"passed over :( :( :(",
						"pretty good..",
						"even better!",
						"the absolute best no question"
					),
				goodnessSubstringOrdering.sortedCopy(ImmutableList.of(
						"ignored :( :( :(",
						"pretty good..",
						"the absolute best no question",
						"passed over :( :( :(",
						"even better!",
						"overlooked :( :( :(")));
	}
}
