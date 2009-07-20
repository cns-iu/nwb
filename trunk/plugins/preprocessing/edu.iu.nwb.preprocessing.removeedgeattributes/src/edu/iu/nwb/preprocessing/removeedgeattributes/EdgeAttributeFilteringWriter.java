package edu.iu.nwb.preprocessing.removeedgeattributes;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class EdgeAttributeFilteringWriter extends NWBFileWriter {
	private List keysToRemove;

	
	public EdgeAttributeFilteringWriter(File file, List keysToRemove)
			throws IOException {
		super(file);		
		this.keysToRemove = keysToRemove;
	}
	
	
	public void setDirectedEdgeSchema(LinkedHashMap schema) {
		LinkedHashMap newSchema = filterAttributes(schema, keysToRemove);
		super.setDirectedEdgeSchema(newSchema);
	}
	
	public void setUndirectedEdgeSchema(LinkedHashMap schema) {
		LinkedHashMap newSchema = filterAttributes(schema, keysToRemove);
		super.setUndirectedEdgeSchema(newSchema);
	}

	public void addDirectedEdge(int sourceNode,
								int targetNode,
								Map attributes) {
		LinkedHashMap newSchema = filterAttributes(attributes, keysToRemove);		
		super.addDirectedEdge(sourceNode, targetNode, newSchema);
	}
	
	public void addUndirectedEdge(int sourceNode,
									int targetNode,
									Map attributes) {
		LinkedHashMap newSchema = filterAttributes(attributes, keysToRemove);		
		super.addUndirectedEdge(sourceNode, targetNode, newSchema);
	}

	private LinkedHashMap filterAttributes(Map attributes, List keysToRemove) {
		LinkedHashMap newSchema = new LinkedHashMap();
		
		for (Iterator attributeIt = attributes.entrySet().iterator();
				attributeIt.hasNext(); ) {
			Entry attribute = (Entry) attributeIt.next();
			String attributeKey = (String) attribute.getKey();
			String attributeType = (String) attribute.getValue();
			
			if (keysToRemove.contains(attributeKey)) {
				/* Do nothing -- we are removing the edge attribute with this
				 * key by excluding it from attributes so that it will not be
				 * printed.
				 */
			}
			else {
				newSchema.put(attributeKey, attributeType);
			}
		}
		
		return newSchema;
	}
}
