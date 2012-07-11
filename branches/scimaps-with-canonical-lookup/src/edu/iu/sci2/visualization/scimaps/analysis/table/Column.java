package edu.iu.sci2.visualization.scimaps.analysis.table;

import java.util.Set;

import prefuse.data.Schema;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import edu.iu.sci2.visualization.scimaps.analysis.AbstractTabularAnalysis;

/**
 * A typed attribute, as of an {@link AbstractTabularAnalysis} or a {@link Row}.
 * 
 * <p>Identity is decided using only the column's name; the {@link Class} is not considered.
 * 
 * @param <T>
 *            the type of values that may be mapped to by this column
 */
public class Column<T> implements Comparable<Column<?>> {
	private final Class<T> clazz;
	private final String name;
	
	private Column(Class<T> clazz, String name) {
		this.clazz = Preconditions.checkNotNull(clazz);
		this.name = Preconditions.checkNotNull(name);
	}
	
	/**
	 * A column of type {@code clazz} named {@code name}.
	 */
	public static <T> Column<T> create(Class<T> clazz, String name) {
		return new Column<T>(clazz, name);
	}

	/**
	 * The type of this column.
	 */
	public Class<T> getClazz() {
		return clazz;
	}
	
	/**
	 * The name of this column.
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("clazz", clazz).add("name", name).toString();
	}
	
	/**
	 * Identity is decided by the column's name. The class is not considered.
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}

	/**
	 * Identity is decided by the column's name. The class is not considered.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof Column)) {
			return false;
		}
		Column<?> that = (Column<?>) o;

		return Objects.equal(this.name, that.name);
	}

	/**
	 * Comparison is decided by the natural String ordering of the column names.
	 */
	@Override
	public int compareTo(Column<?> that) {
		return this.name.compareTo(that.name);
	}

	/**
	 * @throws NullPointerException
	 *             if {@code columns} is null
	 * @throws IllegalArgumentException
	 *             if any column's name or class is null, or if any column names are duplicates
	 */
	public static Schema buildSchemaFor(Set<? extends Column<?>> columns) {
		Preconditions.checkNotNull(columns);
		
		Schema s = new Schema(columns.size());
		
		for (Column<?> column : columns) {
			s.addColumn(column.getName(), column.getClazz());
		}
					
		return s.lockSchema();
	}
}