package edu.iu.sci2.database.star.load.entity;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.dictionary.DictionaryEntry;
import org.cishell.utilities.dictionary.DictionaryIterator;

import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.sci2.database.star.load.parameter.ColumnDescriptor;

public class CoreEntity extends GenericEntity {
	private String[] row;

	public CoreEntity(
			DatabaseTableKeyGenerator keyGenerator,
			String[] row,
			Collection<ColumnDescriptor> columnDescriptors,
			boolean shouldMergeIdenticalValues) {
		super(keyGenerator, createAttributes(), columnDescriptors, shouldMergeIdenticalValues);
		this.row = row;
	}

	@Override
	public Dictionary<String, Object> getAttributesForInsertion() {
		Dictionary<String, Object> attributes = createJustAttributesForInsertion();

		for (DictionaryEntry<String, Object> entry :
				new DictionaryIterator<String, Object>(getAttributes())) {
			attributes.put(entry.getKey(), entry.getValue());
		}

		return attributes;
	}

	@Override
	public Dictionary<String, Object> createJustAttributesForInsertion() {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();

		for (ColumnDescriptor column : getColumnDescriptors()) {
			attributes.put(
				column.getNameForDatabase(),
				interpretValue(this.row[column.getColumnIndex()], column));
		}

		return attributes;
	}

	private static Dictionary<String, Object> createAttributes() {
		return new Hashtable<String, Object>();
	}
}