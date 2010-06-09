package edu.iu.epic.visualization.linegraph.utilities;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import prefuse.data.Table;
import prefuse.util.collections.IntIterator;
import stencil.tuple.PrototypedTuple;
import stencil.tuple.Tuple;

public class TableTupleStream implements TupleStream {
	private Table table;
	private String timeStepColumnName;
	private String lineColumnName;
	private String stencilStreamName;
	private List<String> stencilNames;
	private IntIterator rows;
	private int lastRow = -1;
	
	/* TODO: Just use constants for a lot of these arguments?  I don't know how
	 * general-use this should be.
	 */
	public TableTupleStream(
			Table table,
			String timeStepColumnName,
			String lineColumnName,
			String stencilStreamName,
			String stencilTimeStepID,
			String stencilLineID,
			String stencilValueID) {
		this.table = table;
		this.timeStepColumnName = timeStepColumnName;
		this.lineColumnName = lineColumnName;
		this.stencilStreamName = stencilStreamName;
		this.stencilNames = Arrays.asList(
			Tuple.SOURCE_KEY, stencilTimeStepID, stencilLineID, stencilValueID);

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
			// TODO: Meaningful exception message.
			throw new NoSuchElementException();
		}
		
		this.lastRow = this.rows.nextInt();
		prefuse.data.Tuple row = this.table.getTuple(this.lastRow);
		String timeStep = row.get(this.timeStepColumnName).toString();
		String value = row.get(this.lineColumnName).toString();

		return new PrototypedTuple(
			this.stencilNames,
			Arrays.asList(
				new String[] { this.stencilStreamName, timeStep, this.lineColumnName, value }));
	}
	
	public void reset() {
		this.rows = this.table.rows();
		this.lastRow = -1;
	}
	
	public long streamSize() {
		IntIterator fakeRows = this.table.rows();
		long size = 0;
		
		while (fakeRows.hasNext()) {
			size++;
			fakeRows.next();
		}
		
		return size;
	}
}