/**
 * Copyright (c) 2004-2006 Regents of the University of California.
 * See "license-prefuse.txt" for licensing terms.
 */
package edu.iu.nwb.converter.prefusegraphml.writer;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

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
    public void writeGraph(Graph graph, OutputStream os) throws DataIOException
    {
    	boolean graphHasNodes = graph.nodes().hasNext();
    	boolean graphHasEdges = graph.edges().hasNext();
    	

        // first, check the schemas to ensure GraphML compatibility
    	Schema ns = null;
    	Schema es = null;
    	
    	if (graphHasNodes) {
    		ns = ((Node) graph.nodes().next()).getSchema();//graph.getNodeTable().getSchema();
    		checkGraphMLSchema(ns);
    	}
        
    	if (graphHasEdges) {
    		es = ((Edge) graph.edges().next()).getSchema();//graph.getEdgeTable().getSchema();
    		checkGraphMLSchema(es);
    	}

    	XMLWriter xml = null;
        try {
        	xml = new XMLWriter(new PrintWriter(new OutputStreamWriter(os, "UTF-8")));
        } catch (UnsupportedEncodingException e) {
        	String message =
        		"Error: The standard character encoding UTF-8 is not supported on this system.  "
        		+ "Please report this to the development team.";
        	throw new RuntimeException(message, e);
        }
        
        xml.begin(Tokens.GRAPHML_HEADER, 2);
        
        xml.comment("prefuse GraphML Writer | "
                + new Date(System.currentTimeMillis()));
        
        // print the graph schema
      
        if (graphHasNodes) {
        	printSchema(xml, Tokens.NODE, ns, null);
        }
        
        if (graphHasEdges) {
        	printSchema(xml, Tokens.EDGE, es, new String[] {
        			graph.getEdgeSourceField(), graph.getEdgeTargetField()
        	});
        }
        xml.println();
        
        // print graph contents
        xml.start(Tokens.GRAPH, Tokens.EDGEDEF,
            graph.isDirected() ? Tokens.DIRECTED : Tokens.UNDIRECTED);
        
        // print the nodes
        if (graphHasNodes) {
        	xml.comment("nodes");
        	Iterator nodes = graph.nodes();
        	while ( nodes.hasNext() ) {
        		Node n = (Node)nodes.next();
            
        		if ( ns.getColumnCount() > 0 ) {
        			xml.start(Tokens.NODE, Tokens.ID, "n" + String.valueOf(n.getRow()));
        			for ( int i=0; i<ns.getColumnCount(); ++i ) {
        				String field = ns.getColumnName(i);
        				
        				Object value = n.get(field);
        				if (value != null) {
        					xml.contentTag(Tokens.DATA, Tokens.KEY, field.toLowerCase(), value.toString());
        				}
        		}
                xml.end();
        		} else {
                xml.tag(Tokens.NODE, Tokens.ID, "n" + String.valueOf(n.getRow()));
            	}
        	}
        }
        // add a blank line
        xml.println();
        
        // print the edges
        if (graphHasEdges) {
        	String[] attr = new String[]{Tokens.ID, Tokens.SOURCE, Tokens.TARGET};
        	String[] vals = new String[3];
        
        	xml.comment("edges");
        	Iterator edges = graph.edges();
        	while ( edges.hasNext() ) {
        		Edge e = (Edge)edges.next();
        		vals[0] = "e" + String.valueOf(e.getRow());
        		vals[1] = "n" + String.valueOf(e.getSourceNode().getRow());
        		vals[2] = "n" + String.valueOf(e.getTargetNode().getRow());
            
        		if ( es.getColumnCount() > 2 ) {
        			xml.start(Tokens.EDGE, attr, vals, 3);
        			for ( int i=0; i<es.getColumnCount(); ++i ) {
        				String field = es.getColumnName(i);
        				if ( field.equals(graph.getEdgeSourceField()) ||
        						field.equals(graph.getEdgeTargetField()) )
        					continue;
                    
        				Object value = e.get(field);
        				
        				if (value != null) { // Suppress elements for null-valued edge attributes
	        				xml.contentTag(Tokens.DATA, Tokens.KEY, field.toLowerCase(), 
	                                   e.get(field).toString());
        				}
        			}
                xml.end();
        		} else {
                xml.tag(Tokens.EDGE, attr, vals, 3);
        		}
        	}
        }
        xml.end();
        
        // finish writing file
        xml.finish("</"+Tokens.GRAPHML+">\n");
    }
    
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