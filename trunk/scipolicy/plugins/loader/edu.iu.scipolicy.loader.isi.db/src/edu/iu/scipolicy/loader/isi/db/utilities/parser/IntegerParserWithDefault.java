package edu.iu.scipolicy.loader.isi.db.utilities.parser;

public class IntegerParserWithDefault {
	public static final int DEFAULT = -1;

	public static int parse(String target) {
		try {
			return Integer.parseInt(target);
		} catch (Exception e) {
			return DEFAULT;
		}
	}
}