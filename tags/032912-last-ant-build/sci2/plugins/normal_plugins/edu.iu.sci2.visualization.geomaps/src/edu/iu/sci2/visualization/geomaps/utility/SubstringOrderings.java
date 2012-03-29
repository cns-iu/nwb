package edu.iu.sci2.visualization.geomaps.utility;

import java.util.List;

import org.cishell.utilities.ToCaseFunction;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

public class SubstringOrderings {
	private SubstringOrderings() {}

	/**
	 * Let the "rank" of a String offered to the returned {@link Ordering} mean the position of the
	 * rightmost element in the List given to this method that is a case-insensitive substring of
	 * it.  (All elements having no such substring are assigned one special rank lesser than all
	 * others.)  Then the returned ordering judges first by rank and defers on equal ranks to the
	 * natural String ordering.
	 */
	public static Ordering<String> explicit(
			final List<? extends String> candidateSubstringsInOrder) {
		Function<String, Integer> rankFunction =
				new Function<String, Integer>() {
					/* The index of the last candidate substring that really is a substring of
					 * "string", else -1.  This is the "rank".
					 */
					@Override
					public Integer apply(final String string) {
						return Lists2.lastIndexOf(
								candidateSubstringsInOrder,
								StringPredicates.substringOf(string, ToCaseFunction.LOWER));
					}			
				};
				
		Ordering<String> bySubstringRank = Ordering.natural().onResultOf(rankFunction);
		
		return bySubstringRank.compound(Ordering.<String>natural());
	}

	/**
	 * @see #explicit(List)
	 */
	public static Ordering<String> explicit(
			String leastCandidateSubstring,
			String... remainingCandidateSubstringsInOrder) {
		return explicit(Lists.asList(leastCandidateSubstring, remainingCandidateSubstringsInOrder));
	}	
}
