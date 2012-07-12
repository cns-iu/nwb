package edu.iu.sci2.visualization.scimaps.analysis.table;

import java.util.Map;

import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * A type-safe heterogeneous container for mapping each of a set of typed {@link Column}s to a
 * value of the appropriate type. The mappings may be {@link #copyIntoTuple(Tuple) copied into} to
 * a given {@link Tuple} if the Tuple's {@link Schema} is mutually
 * {@link Schema#isAssignableFrom(Schema) compatible} with the Schema implied by this row's
 * mappings. Only one insertion is allowed per Column.
 */
public class Row {
	private static final MapJoiner MAP_JOINER =
			Joiner.on(";").withKeyValueSeparator(": ").useForNull(String.valueOf((Object) null));
	
	private final Map<Column<?>, Object> columnValues;
	
	/**
	 * A new, empty Row.
	 */
	public Row() {
		this.columnValues = Maps.newHashMap();
	}

	/**
	 * Sets the value in {@link Column} {@code column} to {@code value}.
	 * 
	 * @throws NullPointerException
	 *             if column is null, or value is null and the column is of primitive type
	 * @throws IllegalStateException
	 *             if the row already has a value set for this column
	 */
	public <T> Row put(Column<T> column, T value) {
		Preconditions.checkNotNull(column, "column null");
		if (column.getClazz().isPrimitive() && value == null) {
			throw new NullPointerException(
					String.format("Cannot set null value on primitive column %s.", column));
		}
		
		Preconditions.checkState(!columnValues.containsKey(column),
				"Duplicate setting %s for column %s; value %s is already set.",
				value, column, columnValues.get(column));
		
		columnValues.put(column, value);
		return this;
	}
	
	/**
	 * Writes the columnValues of this row into {@code tuple}.
	 * 
	 * @throws IllegalStateException
	 *             if for any {@link Column} the tuple indicates that it cannot accept columnValues of
	 *             that type in that column, or if the {@link Schema} implied by the columnValues set for
	 *             this row is not mutually compatible (by names and types) with the Schema of the
	 *             given tuple according to a two-way check of
	 *             {@link Schema#isAssignableFrom(Schema)}
	 */
	public void copyIntoTuple(Tuple tuple) {
		Schema impliedSchema = Column.buildSchemaFor(columnValues.keySet());
		Schema targetSchema = tuple.getSchema();
		
		// Check that the entry schema is >= the tuple schema (that we're not missing any columnValues)  
		Preconditions.checkState(
				impliedSchema.isAssignableFrom(targetSchema),
				"The schema (%s) implied by this entry's columnValues (%s) is not assignable from the target schema (%s).",
				impliedSchema, MAP_JOINER.join(columnValues), targetSchema);
		
		// Check that the tuple schema is >= the entry schema (that we don't have any extra columnValues)
		Preconditions.checkState(
				targetSchema.isAssignableFrom(impliedSchema),
				"The target schema (%s) is not assignable from the schema (%s) implied by this entry's columnValues (%s).",
				targetSchema, impliedSchema, MAP_JOINER.join(columnValues));
		
		for (Map.Entry<Column<?>, Object> entry : columnValues.entrySet()) {
			Column<?> column = entry.getKey();
			Object value = entry.getValue();
			String name = column.getName();
			
			Preconditions.checkState(tuple.canSet(name, column.getClazz()),
					"Cannot set value %s in column %s of tuple %s.", value, column.getName(), tuple);
			
			tuple.set(column.getName(), value);
		}
	}

	/**
	 * @see Row#copyIntoTuple(Tuple)
	 */
	public void addAsNewRowToTable(Table table) {
		copyIntoTuple(table.getTuple(table.addRow()));
	}
}