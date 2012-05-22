package edu.iu.nwb.analysis.extractnetfromtable.components;

import java.util.HashMap;
import java.util.Map;

import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AbstractAggregateFunction;

/**
 * Contains the {@link AbstractAggregateFunction}s for each column of a
 * particular row.
 * 
 * @author dmcoe
 * 
 */
public class ValueAttributes {
	private int rowNumber;
	private Map<Integer, AbstractAggregateFunction> columnNumberToFunction;

	/**
	 * 
	 * @param rowNumber
	 *            The row this {@link ValueAttributes} represents.
	 */
	public ValueAttributes(int rowNumber) {
		this.rowNumber = rowNumber;
		this.columnNumberToFunction = new HashMap<Integer, AbstractAggregateFunction>();
	}

	/**
	 * Add a function for a column number. Previous values will be replaced.
	 * 
	 * @param columnNumber
	 *            The column that the given {@link AbstractAggregateFunction} is
	 *            associated with.
	 * @param aggregateFunction
	 *            The {@link AbstractAggregateFunction} that the column is
	 *            associated with.
	 */
	public void putFunction(int columnNumber,
			AbstractAggregateFunction aggregateFunction) {
		this.columnNumberToFunction.put(columnNumber, aggregateFunction);
	}

	/**
	 * Get the associated {@link AbstractAggregateFunction} for a given column.
	 * 
	 * @param columnNumber
	 *            The column number.
	 * @return The associated {@link AbstractAggregateFunction}.
	 */
	public AbstractAggregateFunction getFunction(int columnNumber) {
		return this.columnNumberToFunction.get(columnNumber);
	}

	/**
	 * The row number that this {@link ValueAttributes} represents.
	 * 
	 * @return The row number.
	 */
	public int getRowNumber() {
		return this.rowNumber;
	}
}
