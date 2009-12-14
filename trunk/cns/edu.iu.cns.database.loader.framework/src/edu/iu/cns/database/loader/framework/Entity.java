package edu.iu.cns.database.loader.framework;

import java.util.Dictionary;

import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.cns.shared.utilities.DictionaryIterator;
import edu.iu.cns.shared.utilities.GenericPair;


public abstract class Entity implements PrimaryKeyContainer {
	private int primaryKey;
	private Dictionary<String, Object> attributes;

	public Entity(DatabaseTableKeyGenerator keyGenerator) {
		this.primaryKey = keyGenerator.getNextKey();
		this.attributes = createAttributes();

		// TODO: Hhmmm, is this the right thing to do?
		this.attributes.put(PrimaryKeyContainer.PRIMARY_KEY, this.primaryKey);
	}

	public final int getPrimaryKey() {
		return this.primaryKey;
	}

	public final Dictionary<String, Object> getAttributes() {
		return this.attributes;
	}

	public final String toString() {
		return Integer.toString(getPrimaryKey());
	}

	/**
	 * This is meant to be overridden.
	 * by default, it will do a naive equality check between all of the attributes of this
	 *  and otherEntity.
	 */
	public boolean equals(Entity otherEntity) {
		if (this == otherEntity) {
			return true;
		}

		Dictionary<String, Object> attributes = getAttributes();
		Dictionary<String, Object> otherEntityAttributes = otherEntity.getAttributes();

		if (attributes.size() != otherEntityAttributes.size()) {
			return false;
		}

		for (GenericPair<String, Object> dictionaryEntry :
				new DictionaryIterator<String, Object>(attributes)) {
			Object otherEntityValue = otherEntityAttributes.get(dictionaryEntry.getFirstObject());

			if (!dictionaryEntry.getSecondObject().equals(otherEntityValue)) {
				return false;
			}
		}

		return true;
	}

	// TODO: Override hashCode?

	public abstract Dictionary<String, Object> createAttributes();

	/*public String createValuesString() {
		return StringUtilities.impl
	}*/
}
