package edu.iu.nwb.converter.edgelist.common;

public class InvalidEdgeListFormatException extends Exception {
	private static final long serialVersionUID = 1L;
	public static final String REVIEW_SPEC_MESSAGE =
		"The file selected as .edge is not a valid EdgeList file." + "\n"
		+ "Please review the EdgeList file format specification at "
		+ "https://nwb.cns.iu.edu/community/?n=LoadData.Edgelist" + "\n";

	public InvalidEdgeListFormatException(String message) {
		super(message + "  " + REVIEW_SPEC_MESSAGE);
	}
}
