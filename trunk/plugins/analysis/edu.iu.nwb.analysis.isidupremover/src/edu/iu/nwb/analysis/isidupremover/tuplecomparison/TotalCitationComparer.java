package edu.iu.nwb.analysis.isidupremover.tuplecomparison;

import prefuse.data.Tuple;
import edu.iu.nwb.shared.isiutil.ISITag;

public class TotalCitationComparer implements ISIPubComparer {
	public int compare(Tuple tu1, Tuple tu2,  StringBuffer log) {
		int tu1TotalCitations = tu1.getInt(ISITag.CITED_REFERENCE_COUNT.columnName);
		int tu2TotalCitations = tu2.getInt(ISITag.CITED_REFERENCE_COUNT.columnName);
		
		return tu1TotalCitations - tu2TotalCitations;
	}
}
