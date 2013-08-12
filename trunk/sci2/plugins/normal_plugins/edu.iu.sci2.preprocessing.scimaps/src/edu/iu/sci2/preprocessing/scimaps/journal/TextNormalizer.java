package edu.iu.sci2.preprocessing.scimaps.journal;

import java.text.Normalizer;

import com.google.common.base.CharMatcher;

public final class TextNormalizer {
	private TextNormalizer() {
		
	}
	
	public static String normalize(String string) {
		// Convert UTF-8 characters (ex: s·Ìn) to ASCII (e.g sa?i?n)
		String result = Normalizer.normalize(string, Normalizer.Form.NFD).toLowerCase();
		
		// Remove leading white space, leading and trailing "the" character
		result = CharMatcher.anyOf("the ").trimLeadingFrom(result);
		result = CharMatcher.anyOf(" the").trimTrailingFrom(result);
		
		// Replace "&" with "and"
		result = result.replace("&", "and");
		
		// Extract Latin characters
		result = CharMatcher.inRange('a', 'z').retainFrom(result);

		return result;
	}
}
