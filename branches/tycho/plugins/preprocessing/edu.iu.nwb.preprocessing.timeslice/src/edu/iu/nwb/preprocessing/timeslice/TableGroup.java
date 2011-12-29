package edu.iu.nwb.preprocessing.timeslice;

import prefuse.data.Table;
import prefuse.data.Tuple;

public interface TableGroup {
	void addTupleToAll(Tuple tuple);

	void addTable(Table table);
	
	Table[] getTables();
}