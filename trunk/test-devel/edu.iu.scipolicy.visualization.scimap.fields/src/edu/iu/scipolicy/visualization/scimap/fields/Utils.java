package edu.iu.scipolicy.visualization.scimap.fields;

public class Utils {
	public static String postScriptEscape(String rawReference) {
		return rawReference.replace("\\", "\\\\").replace(")","\\)").replace("(", "\\(");
	}
}
