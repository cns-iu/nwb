package edu.iu.sci2.visualization.geomaps.utility;

import java.util.List;

import org.cishell.utilities.ToCaseFunction;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

public class SubstringOrderings {
	private SubstringOrderings() {}

	public static Ordering<String> explicit(
			final List<? extends String> candidateSubstringsInOrder) {
		return Ordering.natural().onResultOf(new Function<String, Integer>() {
			/* The index of the last candidate substring that really is a substring of "string",
			 * else -1.
			 */
			@Override
			public Integer apply(final String string) {
				return Lists2.lastIndexOf(
						candidateSubstringsInOrder,
						StringPredicates.isContainedBy(string, ToCaseFunction.LOWER));
			}			
		});
	}

	public static Ordering<String> explicit(
			String leastCandidateSubstring,
			String... remainingCandidateSubstringsInOrder) {
		return explicit(Lists.asList(leastCandidateSubstring, remainingCandidateSubstringsInOrder));
	}	
}
