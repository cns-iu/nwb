package edu.iu.sci2.database.star.load.entity;

import java.sql.Types;
import java.util.Collection;
import java.util.Dictionary;

import org.cishell.utilities.dictionary.DictionaryEntry;
import org.cishell.utilities.dictionary.DictionaryIterator;

import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.sci2.database.star.load.parameter.ColumnDescriptor;

public abstract class GenericEntity extends Entity<GenericEntity> {
	private Collection<ColumnDescriptor> columnDescriptors;
	private boolean shouldMergeIdenticalValues;

	public GenericEntity(
			DatabaseTableKeyGenerator keyGenerator,
			Dictionary<String, Object> attributes,
			Collection<ColumnDescriptor> columnDescriptors,
			boolean shouldMergeIdenticalValues) {
		super(keyGenerator, attributes);
		this.columnDescriptors = columnDescriptors;
		this.shouldMergeIdenticalValues = shouldMergeIdenticalValues;
	}

	public Collection<ColumnDescriptor> getColumnDescriptors() {
		return this.columnDescriptors;
	}

	@Override
	public Object createMergeKey() {
		if (this.shouldMergeIdenticalValues) {
			return createJustAttributesForInsertion();
		} else {
			return getPrimaryKey();
		}
	}

	@Override
	public void merge(GenericEntity otherItem) {
		Dictionary<String, Object> attributes = createJustAttributesForInsertion();
		Dictionary<String, Object> otherItemAttributes =
			otherItem.createJustAttributesForInsertion();

		for (DictionaryEntry<String, Object> entry :
				new DictionaryIterator<String, Object>(otherItemAttributes)) {
			attributes.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public abstract Dictionary<String, Object> getAttributesForInsertion();
	public abstract Dictionary<String, Object> createJustAttributesForInsertion();

	public static Object interpretValue(String value, ColumnDescriptor column) {
		// This is terrible.
		switch (column.getType().getSQLType()) {
		case Types.INTEGER:
			return Integer.parseInt(value);
		case Types.DOUBLE:
			return Double.parseDouble(value);
		default:
			return value;
		}
	}
}