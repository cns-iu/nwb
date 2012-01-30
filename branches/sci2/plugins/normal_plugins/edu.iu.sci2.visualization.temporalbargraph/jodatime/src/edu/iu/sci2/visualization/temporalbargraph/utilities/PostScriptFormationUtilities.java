package edu.iu.sci2.visualization.temporalbargraph.utilities;

import java.awt.Color;

import org.cishell.utilities.StringUtilities;


public class PostScriptFormationUtilities {
	public static final double POINTS_PER_INCH = 72;
	
	public static String matchParentheses(String originalLabel) {
		int openingParenthesisCount =
			StringUtilities.countOccurrencesOfChar(originalLabel, '(');
		int closingParenthesisCount =
			StringUtilities.countOccurrencesOfChar(originalLabel, ')');
		
		if (openingParenthesisCount > closingParenthesisCount) {
			int closingParenthesisToAddCount =
				(openingParenthesisCount - closingParenthesisCount);
			
			return originalLabel +
				StringUtilities.multiply(")", closingParenthesisToAddCount);
		} else if (openingParenthesisCount < closingParenthesisCount) {
			int openingParenthesisToAddCount =
				(closingParenthesisCount - openingParenthesisCount);
			
			return StringUtilities.multiply("(", openingParenthesisToAddCount) + originalLabel;
				
		} else {
			return originalLabel;
		}
	}
	
	public static String getRGB(Color color) {
		return color.getRed() / 255.0 + " " + color.getGreen() / 255.0 + " "
				+ color.getBlue() / 255.0;
	}


}