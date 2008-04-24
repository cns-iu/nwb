package edu.iu.nwb.util.nwbfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A lightweight NWB File Writer. 
 * 
 * FIXME: Needs much more erorr handling/checking
 * 
 * @author Bruce Herr (bh2@bh2.net)
 */
public class NWBFileWriter implements NWBFileParserHandler {
	private PrintStream out;
	
	LinkedHashMap nodeSchema;
	LinkedHashMap directedEdgeSchema;
	LinkedHashMap undirectedEdgeSchema;
	
	int nodeCount = -1;
	int directedEdgeCount = -1;
	int undirectedEdgeCount = -1;

	public NWBFileWriter(String file) throws IOException {
		this(new File(file));
	}
	
	public NWBFileWriter(File file) throws IOException { 
		this(new FileOutputStream(file));
	}
	
	public NWBFileWriter(OutputStream output) throws IOException { 
		out = new PrintStream(output,true,"UTF-8");
	}

	public void addComment(String comment) {
		out.println("#"+comment);
	}
	
	public void finishedParsing() { 
		out.close();
	}
	
	public void addNode(int id, String label, Map attributes) {
		for (Iterator keys = nodeSchema.keySet().iterator(); keys.hasNext(); ) {
			String key = (String) keys.next();
			String value;
			
			if (key.equals(NWBFileProperty.ATTRIBUTE_ID)) {
				value = ""+id;
			} else if (key.equals(NWBFileProperty.ATTRIBUTE_LABEL)) {
				value = "\t\""+label+"\"";
			} else if (attributes.containsKey(key)) {
				if (nodeSchema.get(key).equals(NWBFileProperty.TYPE_STRING)) {
					value = "\t\""+attributes.get(key)+"\"";
				} else {
					value = "\t"+attributes.get(key);
				}
			} else {
				//if no value, then it needs to be the 'null' character:'*'
				value = "\t"+NWBFileProperty.PRESERVED_STAR;
			}
			
			out.print(value);
			if (!keys.hasNext()) {
				out.println();
			}
		}		
	}

	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {
		if (directedEdgeSchema != null) {
			printEdge(sourceNode,targetNode,attributes,directedEdgeSchema);
		} else {
			throw new RuntimeException("Edge creation before directed edge schema set.");
		}
	}
	
	public void addUndirectedEdge(int sourceNode, int targetNode, Map attributes) {
		if (undirectedEdgeSchema != null) {
			printEdge(sourceNode,targetNode,attributes,undirectedEdgeSchema);
		} else {
			throw new RuntimeException("Edge creation before undirected edge schema set.");
		}
	}
	
	private void printEdge(int sourceNode, int targetNode, Map attributes, LinkedHashMap schema) {
		for (Iterator keys = schema.keySet().iterator(); keys.hasNext(); ) {
			String key = (String) keys.next();
			String value;
			
			if (key.equals(NWBFileProperty.ATTRIBUTE_SOURCE)) {
				value = ""+sourceNode;
			} else if (key.equals(NWBFileProperty.ATTRIBUTE_TARGET)) {
				value = "\t"+targetNode;
			} else if (attributes.containsKey(key)) {				
				if (schema.get(key).equals(NWBFileProperty.TYPE_STRING)) {
					value = "\t\""+attributes.get(key)+"\"";
				} else {
					value = "\t"+attributes.get(key);
				}
			} else {
				//if no value, then it needs to be the 'null' character:'*'
				value = "\t"+NWBFileProperty.PRESERVED_STAR;
			}
			
			out.print(value);
			if (!keys.hasNext()) {
				out.println();
			}
		}
	}

	public void setNodeCount(int numberOfNodes) {
		nodeCount = numberOfNodes;
	}
	
	public void setDirectedEdgeCount(int numberOfEdges) {
		directedEdgeCount = numberOfEdges;
	}

	public void setUndirectedEdgeCount(int numberOfEdges) {
		undirectedEdgeCount = numberOfEdges;
	}
	
	public void setNodeSchema(LinkedHashMap schema) {
		nodeSchema = schema;
		
		out.print(NWBFileProperty.HEADER_NODE);
		if (nodeCount >= 0) {
			out.println(" "+nodeCount);
		} else { 
			out.println();
		}
		
		for (Iterator keys = schema.keySet().iterator(); keys.hasNext(); ) {
			String key = (String) keys.next();
			out.print(key+NWBFileProperty.PRESERVED_STAR+schema.get(key));
			
			if (keys.hasNext()) {
				out.print("\t");
			} else {
				out.println();
			}
		}
	}
	
	public void setDirectedEdgeSchema(LinkedHashMap schema) {
		directedEdgeSchema = schema;
		
		out.print(NWBFileProperty.HEADER_DIRECTED_EDGES);
		if (directedEdgeCount >= 0) {
			out.println(" "+directedEdgeCount);
		} else { 
			out.println();
		}
		
		for (Iterator keys = schema.keySet().iterator(); keys.hasNext(); ) {
			String key = (String) keys.next();
			out.print(key+NWBFileProperty.PRESERVED_STAR+schema.get(key));
			
			if (keys.hasNext()) {
				out.print("\t");
			} else {
				out.println();
			}
		}
	}
	
	public static LinkedHashMap getDefaultNodeSchema(){
		LinkedHashMap defaultNodeSchema = new LinkedHashMap();
		defaultNodeSchema.put(NWBFileProperty.ATTRIBUTE_ID, NWBFileProperty.TYPE_INT);
		defaultNodeSchema.put(NWBFileProperty.ATTRIBUTE_LABEL, NWBFileProperty.TYPE_STRING);
		return defaultNodeSchema;
	}
	
	public static LinkedHashMap getDefaultEdgeSchema(){
		LinkedHashMap defaultEdgeSchema = new LinkedHashMap();
		defaultEdgeSchema.put(NWBFileProperty.ATTRIBUTE_SOURCE, NWBFileProperty.TYPE_INT);
		defaultEdgeSchema.put(NWBFileProperty.ATTRIBUTE_TARGET, NWBFileProperty.TYPE_INT);
		return defaultEdgeSchema;
	}

	public void setUndirectedEdgeSchema(LinkedHashMap schema) {
		undirectedEdgeSchema = schema;
		
		out.print(NWBFileProperty.HEADER_UNDIRECTED_EDGES);
		if (undirectedEdgeCount >= 0) {
			out.println(" "+undirectedEdgeCount);
		} else { 
			out.println();
		}
		
		for (Iterator keys = schema.keySet().iterator(); keys.hasNext(); ) {
			String key = (String) keys.next();
			out.print(key+NWBFileProperty.PRESERVED_STAR+schema.get(key));
			
			if (keys.hasNext()) {
				out.print("\t");
			} else {
				out.println();
			}
		}
	}

	public boolean haltParsingNow() {
		return false;
	}
}
