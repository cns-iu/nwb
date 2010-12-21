package edu.iu.nwb.analysis.burst;

import prefuse.data.Table;

public interface DocumentRetriever {

	Object retrieve(Table data, int row, String documentColumn);

}
