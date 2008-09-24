package edu.iu.nwb.preprocessing.timeslice;

import prefuse.data.Table;
import prefuse.data.Tuple;

public interface TableGroup {

	public abstract void addTupleToAll(Tuple tuple);

	public abstract void addTable(Table table);
	
	public abstract Table[] getTables();
}