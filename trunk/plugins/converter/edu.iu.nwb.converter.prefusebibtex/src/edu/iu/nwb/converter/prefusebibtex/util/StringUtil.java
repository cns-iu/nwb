package edu.iu.nwb.converter.prefusebibtex.util;

public class StringUtil {

	public static String join(String[] tokens, String separator) {
		StringBuffer joinedTokens = new StringBuffer();
		for (int ii = 0; ii < tokens.length; ii++) {
			joinedTokens.append(tokens[ii]);
			//add separator to end of all but last token
			if (ii < tokens.length - 1) {
				joinedTokens.append(separator);
			}
		}
		
		return joinedTokens.toString();
	}
}
