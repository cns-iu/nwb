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
 * An analysis of a sequence of bins in terms of their values over a set of typed columns.
 * Subclasses must provide a sequence of bins and a method for realizing each as a row over the
 * provided columns. Additional rows not corresponding to a particular bin (such as totals or
 * averages) may be added at the end by overriding the optional method createAdditionalRows().
 * 
 * @param <B>
 *            The bin type. One row will be created for each bin.
 */
public abstract class AbstractTabularAnalysis<B> {
	private final ImmutableSet<? extends Column<?>> columns;
	
	/**
	 * A new tabular analysis over the provided columns.
	 */
	protected AbstractTabularAnalysis(Set<? extends Column<?>> columns) {
		Preconditions.checkNotNull(columns);
		
		this.columns = ImmutableSet.copyOf(columns);
	}
	
	/**
	 * A sequence of bins.  One {@link Row} will be created for each bin.
	 */
	protected abstract Iterable<B> getBins();
	
	/**
	 * An instance of {@link Row} characterizing {@code bin} in terms of the columns of this
	 * analysis.
	 */
	protected abstract Row createRowFor(B bin);
	
	/**
	 * A sequence of {@link Row}s to add at the end of {@link #copyAsTable()}. These might be
	 * summaries, special bins, or something else entirely. Subclasses may choose to not override
	 * this method; the default implementation will provide no additional rows.
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
		
		// Add a Row for each bin
		for (B bin : getBins()) {
			createRowFor(bin).addAsNewRowToTable(table);
		}
		
		// Add any special extra rows at the end (summaries, missing values, ...)
		for (Row row : createAdditionalRows()) {
			row.addAsNewRowToTable(table);
		}
		
		return table;
	}
}
