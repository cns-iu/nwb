package edu.iu.nwb.converter.prefusescopus.util;

public class StringUtil {

	public static String join(String[] tokens, String separator) {
		StringBuilder joinedTokens = new StringBuilder();
		for (int i = 0; i < tokens.length; i++) {
			joinedTokens.append(tokens[i]);
			//add separator to end of all but last token
			if (i < tokens.length - 1) {
				joinedTokens.append(separator);
			}
		}
		return joinedTokens.toString();
	}
}
