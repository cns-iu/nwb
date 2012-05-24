package edu.iu.nwb.util.nwbfile.pipe;

import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;

class NodeAttributeDefaulter extends NodeAttributeAdder {
	private final String fieldName;
	private final Object defaultValue;

	public NodeAttributeDefaulter(NWBFileParserHandler delegate,
			String fieldName, String fieldType, Object defaultValue) {
		super(delegate, ImmutableMap.of(fieldName, fieldType));
		
		this.fieldName = fieldName;
		this.defaultValue = defaultValue;
	}

	@Override
	public void addNode(final int id, String label, final Map<String, Object> attributes) {
		Map<String, Object> copy = Maps.newHashMap(attributes);
		copy.put(fieldName, defaultValue);
		
		super.addNode(id, label, copy);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("fieldName", fieldName)
				.add("defaultValue", defaultValue)
				.add("next", nextToString())
				.toString();
	}
}
