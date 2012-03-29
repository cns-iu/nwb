package edu.iu.nwb.util.nwbfile.pipe;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;

class NodeAttributeRemover extends ParserStage {
	private final String attributeToRemove;
	
	public NodeAttributeRemover(NWBFileParserHandler delegate, String toRemove) {
		super(delegate);
		this.attributeToRemove = toRemove;
	}

	@Override
	public void setNodeSchema(LinkedHashMap<String, String> schema) {
		LinkedHashMap<String, String> ourCopy = Maps.newLinkedHashMap(schema);
		ourCopy.remove(attributeToRemove);
		super.setNodeSchema(ourCopy);
	}

	// XXX - Maybe possible to make this a no-op, since Writer doesn't care about extra attributes?
	@Override
	public void addNode(int id, String label, Map<String, Object> attributes) {
		HashMap<String, Object> ourCopy = Maps.newHashMap(attributes);
		ourCopy.remove(attributeToRemove);
		super.addNode(id, label, attributes);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("to remove", attributeToRemove)
				.add("next", nextToString())
				.toString();
	}
}
