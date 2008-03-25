package edu.iu.nwb.visualization.drl;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;

public class NWBToSimFileHandler extends NWBFileParserAdapter {
	private PrintWriter out;
	private String weightAttribute;
	
	public NWBToSimFileHandler(String weightAttribute, OutputStream output) {
		this.weightAttribute = weightAttribute;
		out = new PrintWriter(output);
	}
	
	public void setUndirectedEdgeSchema(LinkedHashMap schema) {
		if (!schema.containsKey(weightAttribute)) {			
			throw new RuntimeException("Weight attribute: "+weightAttribute+" is not in the edge schema!");
		} else if(schema.get(weightAttribute).equals(NWBFileProperty.TYPE_STRING)) {
			throw new RuntimeException("Weight attribute: "+weightAttribute+" is not a numerical attribute!");
		}
	}
	
	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		//ignore nodes without the weight attribute
		if (attributes.containsKey(weightAttribute)) {
			out.println(node1+"\t"+node2+"\t"+attributes.get(weightAttribute)+"\n");
		}
	}
	
	public void finishedParsing() {
		out.close();
	}
}
