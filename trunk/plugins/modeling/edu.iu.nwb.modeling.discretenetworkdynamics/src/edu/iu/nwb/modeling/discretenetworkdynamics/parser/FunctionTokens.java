package edu.iu.nwb.modeling.discretenetworkdynamics.parser;

public interface FunctionTokens {
	public static final String variables = "x\\d+";
	public static final String literals = "\\d+";
	public static final String operators = "[-+~\\*/\\^]";
	public static final String parenthesis = "[\\(\\)]";
	public static final String tokens = variables+"|"+literals+"|"+operators+"|"+parenthesis;
}
