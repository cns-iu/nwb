package edu.iu.epic.visualization.linegraph.model.tuple;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import prefuse.data.Table;
import prefuse.util.collections.IntIterator;
import stencil.tuple.PrototypedTuple;
import stencil.tuple.Tuple;
import edu.iu.epic.visualization.linegraph.model.StencilStreamMetadata;
import edu.iu.epic.visualization.linegraph.model.TimestepBounds;
import edu.iu.epic.visualization.linegraph.model.table.TableSubsetIterator;

public class TableSubsetTupleStream implements TupleStream {
//	private String title;
	private Table table;
	private String timeStepColumnName;
	private String lineColumnName;
	private String lineColumnDisplayName;
	private String stencilStreamName;
	private List<String> stencilNames;
	private TableSubsetIterator rows;
	private int lastRow = -1;
	
	private TimestepBounds timestepBounds;
	
	/* TODO: Just use constants for a lot of these arguments?  I don't know how
	 * general-use this should be.
	 */
	public TableSubsetTupleStream(
			Table table,
			String timeStepColumnName,
			String lineColumnName,
			String lineColumnDisplayName,
			StencilStreamMetadata stencilStreamMetadata,
			TimestepBounds timestepBounds) {
		this.table = table;
		
		this.timestepBounds = timestepBounds == null 
									? new TimestepBounds() 
									: timestepBounds;
		
		this.timeStepColumnName = timeStepColumnName;
		this.lineColumnName = lineColumnName;
		this.lineColumnDisplayName = lineColumnDisplayName;
		
		this.stencilStreamName = stencilStreamMetadata.stencilStreamName;
		
		this.stencilNames = Arrays.asList(
			Tuple.SOURCE_KEY, 
			stencilStreamMetadata.stencilTimeStepID, 
			stencilStreamMetadata.stencilLineID, 
			stencilStreamMetadata.stencilValueID);

		reset();
	}
	
	public boolean hasNext() {
		return this.rows.hasNext();
	}
	
	public Tuple next() throws NoSuchElementException {
		return nextTuple();
	}
	
	public void remove() {
		// TODO: Implement this method.
	}
	
	public Tuple nextTuple() throws NoSuchElementException {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		
		this.lastRow = this.rows.nextInt();
		prefuse.data.Tuple row = this.table.getTuple(this.lastRow);
		String timeStep = row.get(this.timeStepColumnName).toString();
		String value = row.get(this.lineColumnName).toString();

		return new PrototypedTuple(
			this.stencilNames, Arrays.asList(new String[] {
				this.stencilStreamName, 
				timeStep, 
				this.lineColumnDisplayName, 
				value
			}));
	}
	
	public void reset() {
		
		this.rows = new TableSubsetIterator(table, 
				 							this.table.rows(),
				 							this.timeStepColumnName,
				 							this.timestepBounds);
		this.lastRow = -1;
	}
	
	public long streamSize() {
		IntIterator fakeRows = new TableSubsetIterator(
										table,
										this.table.rows(),
										this.timeStepColumnName,
										this.timestepBounds);
		
		long size = 0;
		
		while (fakeRows.hasNext()) {
			size++;
			fakeRows.next();
		}
		
		return size;
	}
}