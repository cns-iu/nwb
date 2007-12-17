package edu.iu.nwb.analysis.isidupremover.tuplecomparison;

import prefuse.data.Tuple;

public class TotalCitationComparer implements ISIPubComparer {

	private static final String TOTAL_CITATION_TAG = "TC";
	
	public int compare(Tuple tu1, Tuple tu2,  StringBuilder log) {
		int tu1TotalCitations = tu1.getInt(TOTAL_CITATION_TAG);
		int tu2TotalCitations = tu2.getInt(TOTAL_CITATION_TAG);
		
		return tu1TotalCitations - tu2TotalCitations;
	}
}
