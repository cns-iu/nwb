package edu.iu.nwb.util.nwbfile.pipe;

import java.util.LinkedHashMap;

import com.google.common.collect.Maps;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;

class NodeAttributeAdder extends ParserStage {
	private final String fieldName;
	private final String fieldType;

	public NodeAttributeAdder(NWBFileParserHandler delegate, String fieldName, String fieldType) {
		super(delegate);
		this.fieldName = fieldName;
		this.fieldType = fieldType;
	}

	@Override
	public void setNodeSchema(LinkedHashMap<String, String> schema) {
		LinkedHashMap<String, String> copy = Maps.newLinkedHashMap(schema);
		copy.put(getFieldName(), getFieldType());
		super.setNodeSchema(copy);
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

}