package edu.iu.nwb.preprocessing.timeslice;

import prefuse.data.Table;
import prefuse.data.Tuple;

public class SingleTableGroup extends MultiTableGroup {
	
	Table table;

	/* Adds a tuple to the current table.
	 * 
	 * (non-Javadoc)
	 * @see edu.iu.nwb.preprocessing.timeslice.MultiTableGroup#addTupleToAll(prefuse.data.Tuple)
	 */
	public void addTupleToAll(Tuple tuple) {
		table.addTuple(tuple);
	}

	/* Adds a table to the stored group and saves it as the current table
	 * (non-Javadoc)
	 * @see edu.iu.nwb.preprocessing.timeslice.MultiTableGroup#addTable(prefuse.data.Table)
	 */
	public void addTable(Table table) {
		super.addTable(table);
		this.table = table;
	}

	/* Returns all the stored group's tables.
	 * 
	 * (non-Javadoc)
	 * @see edu.iu.nwb.preprocessing.timeslice.MultiTableGroup#getTables()
	 */
	public Table[] getTables() {
		return super.getTables();
	}

}
