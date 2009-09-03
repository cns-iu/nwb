package edu.iu.nwb.preprocessing.timeslice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import prefuse.data.Table;
import prefuse.data.Tuple;

public class MultiTableGroup implements TableGroup {
	
	private List tables = new ArrayList();
	
	/* Put a tuple into every table in the group
	 * 
	 * (non-Javadoc)
	 * @see edu.iu.nwb.preprocessing.timeslice.TableSet#addTupleToAll(prefuse.data.Tuple)
	 */
	public void addTupleToAll(Tuple tuple) {
		Iterator iter = tables.iterator();
		while (iter.hasNext()) {
			Table table = (Table) iter.next();
			table.addTuple(tuple);
		}
	}
	
	/* Add a table to the group of tables
	 * 
	 * (non-Javadoc)
	 * @see edu.iu.nwb.preprocessing.timeslice.TableGroup#addTable(prefuse.data.Table)
	 */
	public void addTable(Table table) {
		tables.add(table);
		
	}

	/* Return all the tables in the group as an array
	 * 
	 * (non-Javadoc)
	 * @see edu.iu.nwb.preprocessing.timeslice.TableGroup#getTables()
	 */
	public Table[] getTables() {
		return (Table[]) tables.toArray(new Table[]{});
	}
}
