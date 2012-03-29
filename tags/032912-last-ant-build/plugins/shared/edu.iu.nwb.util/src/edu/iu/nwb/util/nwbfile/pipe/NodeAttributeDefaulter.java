package edu.iu.nwb.util.nwbfile.pipe;

import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;

class NodeAttributeDefaulter extends NodeAttributeAdder {
	private final Object defaultValue;

	public NodeAttributeDefaulter(NWBFileParserHandler delegate,
			String fieldName, String fieldType, Object defaultValue) {
		super(delegate, fieldName, fieldType);
		this.defaultValue = defaultValue;
	}

	@Override
	public void addNode(final int id, String label, final Map<String, Object> attributes) {
		Map<String, Object> copy = Maps.newHashMap(attributes);
		copy.put(getFieldName(), defaultValue);
		super.addNode(id, label, attributes);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("name", getFieldName())
				.add("type", getFieldType())
				.add("default", defaultValue)
				.add("next", nextToString())
				.toString();
	}
}
