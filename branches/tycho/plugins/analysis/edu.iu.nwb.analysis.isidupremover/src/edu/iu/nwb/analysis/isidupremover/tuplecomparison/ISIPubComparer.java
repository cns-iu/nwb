package edu.iu.nwb.analysis.isidupremover.tuplecomparison;

import prefuse.data.Tuple;

public interface ISIPubComparer {
	
	public int compare(Tuple tu1, Tuple tu2, StringBuffer log);
}
