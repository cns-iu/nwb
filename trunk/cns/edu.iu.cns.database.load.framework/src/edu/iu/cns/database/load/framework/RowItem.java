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

	public Dictionary<String, Object> getAttributesForInsertion() {
		return this.attributes;
	}
}