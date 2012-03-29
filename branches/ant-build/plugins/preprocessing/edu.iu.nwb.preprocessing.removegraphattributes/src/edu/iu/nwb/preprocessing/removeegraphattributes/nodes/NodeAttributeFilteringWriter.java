package edu.iu.nwb.preprocessing.removeegraphattributes.nodes;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.iu.nwb.preprocessing.removeegraphattributes.nwbIO.AttributeFilteringNWBWriter;
import edu.iu.nwb.preprocessing.removeegraphattributes.util.LinkedHashMapUtilities;

public class NodeAttributeFilteringWriter extends AttributeFilteringNWBWriter {
	public NodeAttributeFilteringWriter(File file, Collection<String> keysToRemove)
			throws IOException {
		super(file, keysToRemove);
	}

	public void setNodeSchema(LinkedHashMap<String, String> schema) {
		LinkedHashMap<String, String> prunedSchema =
			LinkedHashMapUtilities.excludeKeysFromMap(schema, keysToRemove);
		super.setNodeSchema(prunedSchema);
	}
	
	public void addNode(int id, String label, Map<String, Object> attributes) {
		LinkedHashMap<String, Object> prunedAttributes =
			LinkedHashMapUtilities.excludeKeysFromMap(attributes, keysToRemove);
		super.addNode(id, label, prunedAttributes);
	}
}
