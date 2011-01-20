/**
 * Copyright (c) 2004-2006 Regents of the University of California.
 * See "license-prefuse.txt" for licensing terms.
 */
package edu.iu.nwb.converter.prefusegraphml.writer;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.io.AbstractGraphWriter;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;
import prefuse.util.io.XMLWriter;

/**
 * GraphWriter instance that writes a graph file formatted using the
 * GraphML file format. GraphML is an XML format supporting graph
 * structure and typed data schemas for both nodes and edges. For more
 * information about the format, please see the
 * <a href="http://graphml.graphdrawing.org/">GraphML home page</a>.
 * 
 * <p>The GraphML spec only supports the data types <code>int</code>,
 * <code>long</code>, <code>float</code>, <code>double</code>,
 * <code>boolean</code>, and <code>string</code>. An exception will
 * be thrown if a data type outside these allowed types is
 * encountered.</p>
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class GraphMLWriter extends AbstractGraphWriter {

    /**
     * String tokens used in the GraphML format.
     */
    public interface Tokens extends GraphMLReader.Tokens  {
        public static final String GRAPHML = "graphml";
        
        public static final String GRAPHML_HEADER =
            "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"\n" 
            +"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            +"  xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns\n"
            +"  http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">\n\n";
    }
    
    /**
     * Map containing legal data types and their names in the GraphML spec
     */
    private static final HashMap TYPES = new HashMap();
    static {
        TYPES.put(int.class, Tokens.INT);
        TYPES.put(Integer.class, Tokens.INT);
        TYPES.put(long.class, Tokens.LONG);
        TYPES.put(Long.class, Tokens.LONG);
        TYPES.put(float.class, Tokens.DOUBLE);
        TYPES.put(Float.class, Tokens.DOUBLE);
        TYPES.put(double.class, Tokens.DOUBLE);
        TYPES.put(Double.class, Tokens.DOUBLE);
        TYPES.put(boolean.class, Tokens.BOOLEAN);
        TYPES.put(Boolean.class, Tokens.BOOLEAN);
        TYPES.put(String.class, Tokens.STRING);
        TYPES.put(java.util.Date.class, Tokens.DATE);
        TYPES.put(java.sql.Date.class, Tokens.DATE);
    }
    
    /**
     * @see prefuse.data.io.GraphWriter#writeGraph(prefuse.data.Graph, java.io.OutputStream)
     */
    public void writeGraph(Graph graph, OutputStream output) throws DataIOException
    {
    	boolean graphHasNodes = graph.nodes().hasNext();
    	boolean graphHasEdges = graph.edges().hasNext();

        // First, check the schemas to ensure GraphML compatibility.
    	Schema nodeSchema = null;
    	Schema edgeSchema = null;

    	if (graphHasNodes) {
    		nodeSchema = ((Node) graph.nodes().next()).getSchema();
    		checkGraphMLSchema(nodeSchema);
    	}
        
    	if (graphHasEdges) {
    		edgeSchema = ((Edge) graph.edges().next()).getSchema();
    		checkGraphMLSchema(edgeSchema);
    	}

    	XMLWriter xml = null;

        try {
        	xml = new XMLWriter(new PrintWriter(new OutputStreamWriter(output, "UTF-8")));
        } catch (UnsupportedEncodingException e) {
        	String message =
        		"Error: The standard character encoding UTF-8 is not supported on this system.  "
        		+ "Please report this to the development team.";
        	throw new RuntimeException(message, e);
        }
        
        xml.begin(Tokens.GRAPHML_HEADER, 2);
        xml.comment("prefuse GraphML Writer | " + new Date(System.currentTimeMillis()));
        
        // Print the graph schema.
      
        if (graphHasNodes) {
        	printSchema(xml, Tokens.NODE, nodeSchema, null);
        }
        
        if (graphHasEdges) {
        	printSchema(
        		xml,
        		Tokens.EDGE,
        		edgeSchema,
        		new String[] { graph.getEdgeSourceField(), graph.getEdgeTargetField() });
        }

        xml.println();
        
        // Print graph contents.
        String directed = (graph.isDirected() ? Tokens.DIRECTED : Tokens.UNDIRECTED);
        xml.start(Tokens.GRAPH, Tokens.EDGEDEF, directed);
        
        // Print the nodes.
        if (graphHasNodes) {
//        	String idFieldName = determineIDFieldName(nodeSchema);
        	xml.comment("nodes");
        	Iterator nodes = graph.nodes();

        	while (nodes.hasNext()) {
        		Node node = (Node) nodes.next();
            
        		if (nodeSchema.getColumnCount() > 0) {
        			xml.start(Tokens.NODE, Tokens.ID, "n" + String.valueOf(node.getRow()));

        			for (int ii = 0; ii < nodeSchema.getColumnCount(); ii++) {
        				String field = nodeSchema.getColumnName(ii);
        				Object value = node.get(field);

        				if (value != null) {
    						xml.contentTag(
    							Tokens.DATA, Tokens.KEY, field.toLowerCase(), value.toString());
        				}
        			}

                	xml.end();
        		} else {
                	xml.tag(Tokens.NODE, Tokens.ID, "n" + String.valueOf(node.getRow()));
            	}
        	}
        }

        // Add a blank line.
        xml.println();
        
        // Print the edges.
        if (graphHasEdges) {
        	String[] attributeNames = new String[] { Tokens.ID, Tokens.SOURCE, Tokens.TARGET };
        	String[] attributeValues = new String[3];
        
        	xml.comment("edges");
        	Iterator edges = graph.edges();

        	while (edges.hasNext()) {
        		Edge edge = (Edge) edges.next();
        		attributeValues[0] = "e" + String.valueOf(edge.getRow());
        		attributeValues[1] = "n" + String.valueOf(edge.getSourceNode().getRow());
        		attributeValues[2] = "n" + String.valueOf(edge.getTargetNode().getRow());
            
        		if (edgeSchema.getColumnCount() > 2) {
        			xml.start(Tokens.EDGE, attributeNames, attributeValues, 3);

        			for (int ii=0; ii<edgeSchema.getColumnCount(); ii++) {
        				String field = edgeSchema.getColumnName(ii);

        				if (field.equals(graph.getEdgeSourceField()) ||
        						field.equals(graph.getEdgeTargetField()))
        				{
        					continue;
        				}
                    
        				Object value = edge.get(field);

        				// Suppress elements for null-valued edge attributes
        				if (value != null) {
	        				xml.contentTag(
	        					Tokens.DATA,
	        					Tokens.KEY,
	        					field.toLowerCase(), 
	        					edge.get(field).toString());
        				}
        			}

                	xml.end();
        		} else {
                	xml.tag(Tokens.EDGE, attributeNames, attributeValues, 3);
        		}
        	}
        }

        xml.end();
        
        // Finish writing file.
        xml.finish("</" + Tokens.GRAPHML + ">\n");
    }

//    private String determineIDFieldName(Schema nodeSchema) {
//    	String baseID = "idAttribute";
//    	Set<String> fieldNames = new HashSet<String>();
//
//    	// Get all of the field names out of the schema.
//    	for (int ii = 0; ii < nodeSchema.getColumnCount(); ii++) {
//    		fieldNames.add(nodeSchema.getColumnName(ii).toLowerCase());
//    	}
//
//    	if (fieldNames.contains(baseID)) {
//    		int idNumber = 2;
//    		String potentiallyUniqueID = baseID + idNumber;
//
//    		while (fieldNames.contains(potentiallyUniqueID)) {
//    			idNumber++;
//    		}
//
//    		return potentiallyUniqueID;
//    	} else {
//    		return null;
//    	}
//    }
    
    /**
     * Print a table schema to a GraphML file
     * @param xml the XMLWriter to write to
     * @param group the data group (node or edge) for the schema
     * @param s the schema
     */
    private void printSchema(XMLWriter xml, String group, Schema s,
                             String[] ignore)
    {
        String[] attr = new String[] {Tokens.ID, Tokens.FOR,
                Tokens.ATTRNAME, Tokens.ATTRTYPE };
        String[] vals = new String[4];

OUTER:
        for ( int i=0; i<s.getColumnCount(); ++i ) {
            vals[0] = s.getColumnName(i);
            
            for ( int j=0; ignore!=null && j<ignore.length; ++j ) {
                if ( vals[0].equals(ignore[j]) )
                    continue OUTER;
            }
            
            vals[0] = vals[0].toLowerCase();
            vals[1] = group;
            vals[2] = vals[0];
            vals[3] = (String)TYPES.get(s.getColumnType(i));
            Object dflt = s.getDefault(i);
            
            if ( dflt == null ) {
                xml.tag(Tokens.KEY, attr, vals, 4);
            } else {
                xml.start(Tokens.KEY, attr, vals, 4);
                xml.contentTag(Tokens.DEFAULT, dflt.toString());
                xml.end();
            }
        }
    }
    
    /**
     * Checks if all Schema types are compatible with the GraphML specification.
     * The GraphML spec only allows the types <code>int</code>,
     * <code>long</code>, <code>float</code>, <code>double</code>,
     * <code>boolean</code>, and <code>string</code>.
     * @param s the Schema to check
     */
    private void checkGraphMLSchema(Schema s) throws DataIOException {
        for ( int i=0; i<s.getColumnCount(); ++i ) {
            Class type = s.getColumnType(i);
            if ( TYPES.get(type) == null ) {
                throw new DataIOException(
                	"Data type unsupported by the "
                    + "GraphML format: " + type.getName());
            }
        }
    }
    
} // end of class GraphMLWriter