package edu.iu.nwb.preprocessing.removeegraphattributes.edges;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.iu.nwb.preprocessing.removeegraphattributes.nwbIO.AttributeFilteringNWBWriter;
import edu.iu.nwb.preprocessing.removeegraphattributes.util.LinkedHashMapUtilities;

public class EdgeAttributeFilteringWriter extends AttributeFilteringNWBWriter {
	public EdgeAttributeFilteringWriter(File file, Collection<String> keysToRemove)
			throws IOException {
		super(file, keysToRemove);
	}

	public void setDirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		LinkedHashMap<String, String> prunedSchema =
			LinkedHashMapUtilities.excludeKeysFromMap(schema, keysToRemove);
		super.setDirectedEdgeSchema(prunedSchema);
	}
	
	public void setUndirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		LinkedHashMap<String, String> prunedSchema =
			LinkedHashMapUtilities.excludeKeysFromMap(schema, keysToRemove);
		super.setUndirectedEdgeSchema(prunedSchema);
	}

	public void addDirectedEdge(
			int sourceNode, int targetNode, Map<String, Object> attributes) {
		LinkedHashMap<String, Object> prunedAttributes =
			LinkedHashMapUtilities.excludeKeysFromMap(attributes, keysToRemove);		
		super.addDirectedEdge(sourceNode, targetNode, prunedAttributes);
	}
	
	public void addUndirectedEdge(
			int sourceNode, int targetNode, Map<String, Object> attributes) {
		LinkedHashMap<String, Object> prunedAttributes =
			LinkedHashMapUtilities.excludeKeysFromMap(attributes, keysToRemove);		
		super.addUndirectedEdge(sourceNode, targetNode, prunedAttributes);
	}
}
