package edu.iu.scipolicy.journallocater;

public class Utils {
	public static String postScriptEscape(String rawReference) {
		// TODO Auto-generated method stub
		return rawReference.replace("\\", "\\\\").replace(")","\\)").replace("(", "\\(");
	}
}
