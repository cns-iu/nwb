package edu.iu.epic.visualization.linegraph.model.table;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.util.TableIterator;
import prefuse.util.collections.IntIterator;
import edu.iu.epic.visualization.linegraph.model.TimestepBounds;

public class TableSubsetIterator extends TableIterator {

	private int previousRow = -1;
	private Table table;
	private TimestepBounds timestepBounds;
	private String timeStepTableColumnName;
	
	public TableSubsetIterator(Table table, 
							   IntIterator rows, 
							   String timeStepTableColumnName, 
							   TimestepBounds timestepBounds) {
		super(table, rows);
		this.table = table;
		this.timestepBounds = timestepBounds;
		this.timeStepTableColumnName = timeStepTableColumnName;
	}
	
	@Override
	public int nextInt() {
		previousRow = super.nextInt();
		return previousRow;
	}		
	
	/* This iterator will make sure that only the tuples which fall within the bounds provided
	 * to it are streamed. Since hasNext() is being used universally to fetch tuples from a 
	 * stream, it is ideal to test for bound-limits here.
	 * 
	 * Point of note is that bound checking is inclusive i.e. if lowerbound = 10 & upperbound = 20
	 * then all tuples having timesteps 10 through 20 (inclusive) will be considered.
	 * @see prefuse.data.util.TableIterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		
		if (super.hasNext()) {
			
			/*
			 * We want to avoid any array out of index exceptions, this will make sure that we 
			 * are only considering rows with +ve indices, which are real.
			 * */
			if (previousRow > -1) {
				
				Tuple row = this.table.getTuple(this.previousRow);
				String timeStep = row.get(this.timeStepTableColumnName).toString();
				
				if (Integer.parseInt(timeStep) > this.timestepBounds.getUpperbound()) {
					return false;
				} 
				
				if (Integer.parseInt(timeStep) < this.timestepBounds.getLowerbound()) {
					return false;
				}
			}
			
			return true;
		} else {
			return false;
		}
	}

}
