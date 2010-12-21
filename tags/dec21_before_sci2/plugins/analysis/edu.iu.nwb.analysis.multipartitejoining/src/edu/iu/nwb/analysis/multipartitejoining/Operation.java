package edu.iu.nwb.analysis.multipartitejoining;

import prefuse.data.Tuple;

public interface Operation {
	public void perform(Tuple tuple, String field, Object newValue);
}
