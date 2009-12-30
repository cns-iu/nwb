package edu.iu.cns.database.loader.framework;

import java.util.Dictionary;

public abstract class RowItem<T extends RowItem<?>> {
	public static final int BASE_HASH_MULTIPLIER = 31;

	private Dictionary<String, Comparable<?>> attributes;

	public RowItem(Dictionary<String, Comparable<?>> attributes) {
		this.attributes = attributes;
	}

	public final Dictionary<String, Comparable<?>> getAttributes() {
		return this.attributes;
	}

	public abstract boolean shouldMerge(T otherItem);

	/**
	 * merge assumes that shouldMerge(otherAddress) would return true.
	 */
	public abstract void merge(T otherItem);

	public Dictionary<String, Comparable<?>> getAttributesForQuery() {
		return this.attributes;
	}
}