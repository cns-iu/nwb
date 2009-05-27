package edu.iu.nwb.converter.prefusescopus.util;

public class StringUtil {

	public static String join(String[] tokens, String separator) {
		StringBuffer joinedTokens = new StringBuffer();
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
