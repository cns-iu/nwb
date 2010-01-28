package edu.iu.cns.database.load.framework;

import java.util.Dictionary;

public abstract class RowItem<T extends RowItem<?>> {
	private Dictionary<String, Object> attributes;
	
	public RowItem(Dictionary<String, Object> attributes) {
		this.attributes = attributes;
	}

	public final Dictionary<String, Object> getAttributes() {
		return this.attributes;
	}
	
//	public final Dictionary<String, Object> getAttributes() {
//		getAttributes();
//	
//		return new modified attributes thing
//		
//		return this.attributes;
//	}

	public abstract boolean shouldMerge(T otherItem);

	/**
	 * merge assumes that shouldMerge(otherAddress) would return true.
	 */
	public abstract void merge(T otherItem);
}