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
		LinkedHashMap newSchema = subtractKeysFromMap(keysToRemove, schema);
		super.setDirectedEdgeSchema(newSchema);
	}
	
	public void setUndirectedEdgeSchema(LinkedHashMap schema) {
		LinkedHashMap newSchema = subtractKeysFromMap(keysToRemove, schema);
		super.setUndirectedEdgeSchema(newSchema);
	}

	public void addDirectedEdge(int sourceNode,
								int targetNode,
								Map attributes) {
		LinkedHashMap newSchema = subtractKeysFromMap(keysToRemove, attributes);		
		super.addDirectedEdge(sourceNode, targetNode, newSchema);
	}
	
	public void addUndirectedEdge(int sourceNode,
									int targetNode,
									Map attributes) {
		LinkedHashMap newSchema = subtractKeysFromMap(keysToRemove, attributes);		
		super.addUndirectedEdge(sourceNode, targetNode, newSchema);
	}

	private LinkedHashMap subtractKeysFromMap(List keysToRemove, Map oldMap) {
		LinkedHashMap map = new LinkedHashMap();
		
		for (Iterator entryIt = oldMap.entrySet().iterator();
				entryIt.hasNext(); ) {
			Entry entry = (Entry) entryIt.next();
			String key = (String) entry.getKey();

			if (keysToRemove.contains(key)) {
				// Do nothing
			}
			else {
				map.put(key, entry.getValue());
			}
		}
		
		return map;
	}
}
