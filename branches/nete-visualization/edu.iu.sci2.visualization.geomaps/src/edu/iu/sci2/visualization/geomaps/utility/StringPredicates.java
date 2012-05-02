package edu.iu.sci2.visualization.geomaps.utility;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;

public class StringPredicates {
	private StringPredicates() {}

	/**
	 * @return	A Predicate for whether {@code string}.contains(a candidate substring).
	 * @see StringPredicates#substringOf(String, Function)
	 * @see Functions#identity()
	 */
	public static Predicate<String> substringOf(String string) {
		return substringOf(string, Functions.<String>identity());
	}
	
	/**
	 * @param normalizer	A String->String Function applied to the base string and candidate substring before testing.
	 * @return	A Predicate for whether {@code string}.contains(a candidate substring).
	 * @see String#contains(CharSequence)
	 */
	public static Predicate<String> substringOf(
			final String string,
			final Function<? super String, ? extends String> normalizer) {
		final String normalizedBaseString = normalizer.apply(string);
		
		return new Predicate<String>() {
			@Override
			public boolean apply(String candidateSubstring) {
				return normalizedBaseString.contains(normalizer.apply(candidateSubstring));
			}				
		};
	}
}
