package edu.iu.sci2.visualization.horizontallinegraph.utilities;

import org.cishell.utilities.StringUtilities;


public class PostScriptFormationUtilities {

	/** 
	 * Return the postscript need to rotate about an angle
	 * @param angle
	 * @return postscript representation of rotation
	 */
	public static String rotate(double angle) {
		return line(angle + " rotate");
	}

	/**
	 * 
	 * @param xTranslate
	 * @param yTranslate
	 * @return
	 */
	public static String translate(double xTranslate, double yTranslate) {
		return line(xTranslate + " " + yTranslate + " translate");
	}
	
	/**
	 * 
	 * @param xscale
	 * @param yscale
	 * @return
	 */
	public static String scale(double xscale, double yscale){
		return line(xscale +" " + yscale + " scale");
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String line(String str) {
		return str + "\n";
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String tabbed(String str) {
		return "\t" + str;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String setrgbcolor(String str) {
		return str + " setrgbcolor";
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String string(String string){
		matchParentheses(string);
		return "(" + string + ")";
	}

	/**
	 * ﻿sets the current color space in the graphics state to DeviceRGB and the
	 * current col- or to the component values specified by red, green, and
	 * blue. Each component must be a number in the range 0.0 to 1.0. If any of
	 * the operands is outside this range, the nearest valid value is
	 * substituted without error indication.
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	public static String setrgbcolor(float red, float green, float blue) {
		return line(String.format("%f %f %f setrgbcolor", red, green, blue));
	}

	
	/**
	 * 
	 * @param fontName
	 * @param fontSize
	 * @return
	 */
	public static String font(String fontName, double fontSize){
		StringBuilder font = new StringBuilder();
		
		font.append(line(String.format("/%s %f selectfont", fontName, fontSize)));
		
		return font.toString();
	}
	
	/**
	 * 
	 * @param label
	 * @param value
	 * @return
	 */
	public static String definition(String label, String value){
		return line("/" + label + " { " + value + " } def");
	}
	
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


}