package edu.iu.scipolicy.references.map;

public class Utils {
	public static String postscriptEscape(String rawReference) {
		// TODO Auto-generated method stub
		return rawReference.replace("\\", "\\\\").replace(")","\\)").replace("(", "\\(");
	}
}
