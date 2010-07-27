package edu.iu.sci2.database.star.common.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.ListUtilities;
import org.cishell.utilities.dictionary.DictionaryEntry;
import org.cishell.utilities.dictionary.DictionaryIterator;
import org.cishell.utilities.dictionary.DictionaryUtilities;

import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

public class LeafEntity extends GenericEntity {
	String value;

	public LeafEntity(
			DatabaseTableKeyGenerator keyGenerator,
			String value,
			ColumnDescriptor column,
			boolean shouldMergeIdenticalValues) {
		super(
			keyGenerator,
			createAttributes(),
			ListUtilities.createAndFillList(column),
			shouldMergeIdenticalValues);
		this.value = value;
	}

	@Override
	public Dictionary<String, Object> getAttributesForInsertion() {
		Dictionary<String, Object> attributes = createJustAttributesForInsertion();

		for (DictionaryEntry<String, Object> entry :
				new DictionaryIterator<String, Object>(getAttributes())) {
			DictionaryUtilities.addIfNotNull(attributes, entry.getKey(), entry.getValue());
		}

		return attributes;
	}

	@Override
	public Dictionary<String, Object> createJustAttributesForInsertion() {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		ColumnDescriptor column = getColumnDescriptors().iterator().next();
		DictionaryUtilities.addIfNotNull(
			attributes, column.getNameForDatabase(), interpretValue(this.value, column));

		return attributes;
	}

	private static Dictionary<String, Object> createAttributes() {
		return new Hashtable<String, Object>();
	}
}