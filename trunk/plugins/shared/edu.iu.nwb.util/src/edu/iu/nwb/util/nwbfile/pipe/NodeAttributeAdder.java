package edu.iu.nwb.util.nwbfile.pipe;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;

class NodeAttributeAdder extends ParserStage {
	private final ImmutableMap<String, String> schemaUpdates;

	public NodeAttributeAdder(NWBFileParserHandler delegate, Map<String, String> schemaUpdates) {
		super(delegate);
		
		this.schemaUpdates = ImmutableMap.copyOf(schemaUpdates);
	}

	@Override
	public void setNodeSchema(LinkedHashMap<String, String> schema) {
		LinkedHashMap<String, String> copy = Maps.newLinkedHashMap(schema);
		copy.putAll(schemaUpdates);
		
		super.setNodeSchema(copy);
	}
}