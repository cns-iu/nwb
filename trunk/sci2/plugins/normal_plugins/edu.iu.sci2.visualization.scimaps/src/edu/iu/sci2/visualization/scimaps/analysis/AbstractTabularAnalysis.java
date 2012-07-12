package edu.iu.sci2.visualization.scimaps.analysis;

import java.util.Set;

import prefuse.data.Schema;
import prefuse.data.Table;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import edu.iu.sci2.visualization.scimaps.analysis.table.Column;
import edu.iu.sci2.visualization.scimaps.analysis.table.Row;

/**
 * An analysis of a sequence of elements, in terms of their values over a set of typed columns. The
 * createRowFor() method is called once per element. Subclasses must provide a sequence of elements
 * and a method for realizing each as a row over the provided columns. Additional rows not
 * corresponding to a particular element (perhaps totals or averages) may be added at the end by
 * overriding the optional method createAdditionalRows().
 * 
 * @param <E>
 *            The element type. One row will be created for each element.
 */
public abstract class AbstractTabularAnalysis<E> {
	private final ImmutableSet<? extends Column<?>> columns;
	
	/**
	 * A new tabular analysis over the provided columns.
	 */
	protected AbstractTabularAnalysis(Set<? extends Column<?>> columns) {
		Preconditions.checkNotNull(columns);
		
		this.columns = ImmutableSet.copyOf(columns);
	}
	
	/**
	 * A sequence of analysis elements.  One {@link Row} will be created for each element.
	 */
	protected abstract Iterable<E> getElements();
	
	/**
	 * An instance of {@link Row} characterizing {@code element} in terms of the columns of this
	 * analysis.
	 */
	protected abstract Row createRowFor(E element);
	
	/**
	 * A sequence of {@link Row}s to add at the end of {@link #copyAsTable()}. These might be
	 * summaries, distinguished elements, or something else entirely. Subclasses may choose to not
	 * override this method; the default implementation will provide no additional rows.
	 */
	protected Iterable<Row> createAdditionalRows() {
		return ImmutableList.of();
	}

	/**
	 * A copy of this tabular analysis as a {@link Table}. The table is created by instantiating
	 * the {@link Schema} is generated using the columns provided to the analysis constructor.
	 */
	public Table copyAsTable() {
		Table table = Column.buildSchemaFor(columns).instantiate();
		
		// Add one row for each element
		for (E element : getElements()) {
			createRowFor(element).addAsNewRowToTable(table);
		}
		
		// Add any requested special extra rows at the end (summaries, missing values, ...)
		for (Row row : createAdditionalRows()) {
			row.addAsNewRowToTable(table);
		}
		
		return table;
	}
}
