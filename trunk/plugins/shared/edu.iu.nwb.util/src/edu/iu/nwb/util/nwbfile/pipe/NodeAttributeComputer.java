package edu.iu.nwb.util.nwbfile.pipe;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;

class NodeAttributeComputer extends NodeAttributeAdder {
	private final FieldMakerFunction computer;
	
	public NodeAttributeComputer(NWBFileParserHandler delegate, String fieldName, 
			String fieldType, FieldMakerFunction computer) {
		super(delegate, fieldName, fieldType);
		this.computer = computer;
	}

	// XXX is it slow to make so many copies?  :-(
	@Override
	public void addNode(int id, String label, Map<String, Object> attributes) {
		HashMap<String, Object> copy = Maps.newHashMap(attributes);
		copy.put(getFieldName(), computer.compute(getFieldName(), Collections.unmodifiableMap(copy)));
		super.addNode(id, label, copy);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("name", getFieldName())
				.add("type", getFieldType())
				.add("computer", computer)
				.add("next", nextToString())
				.toString();
	}
}
