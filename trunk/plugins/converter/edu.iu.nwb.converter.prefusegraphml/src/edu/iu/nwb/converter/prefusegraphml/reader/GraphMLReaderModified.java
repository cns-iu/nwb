package edu.iu.nwb.converter.prefusegraphml.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.io.AbstractGraphReader;
import prefuse.data.io.DataIOException;
import prefuse.data.parser.DataParseException;
import prefuse.data.parser.DataParser;
import prefuse.data.parser.ParserFactory;
import prefuse.util.collections.IntIterator;


/**
 * GraphReader instance that reads in graph file formatted using the
 * GraphML file format. GraphML is an XML format supporting graph
 * structure and typed data schemas for both nodes and edges. For more
 * information about the format, please see the
 * <a href="http://graphml.graphdrawing.org/">GraphML home page</a>.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class GraphMLReaderModified extends AbstractGraphReader {
	private boolean cleanForGUESS;

	public GraphMLReaderModified(boolean cleanForGUESS) {
		this.cleanForGUESS = cleanForGUESS;
	}

    /**
     * @see prefuse.data.io.GraphReader#readGraph(java.io.InputStream)
     */
    public Graph readGraph(InputStream input) throws DataIOException {       
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
		    SAXParser saxParser = factory.newSAXParser();
			GraphMLHandler handler = new GraphMLHandler(this.cleanForGUESS);
	        saxParser.parse(input, handler);

	        return handler.getGraph();
		} catch (ParserConfigurationException e) {
			throw new DataIOException(e.getMessage(), e);
		} catch (SAXException e) {
			throw new DataIOException(e.getMessage(), e);
		} catch (IOException e) {
			throw new DataIOException(e.getMessage(), e);
		}
    }
    
    /**
     * String tokens used in the GraphML format.
     */
    public interface Tokens {
        public static final String ID = "id";
        public static final String GRAPH = "graph";
        public static final String EDGE_DEFINITION = "edgedefault";
        public static final String DIRECTED = "directed";
        public static final String UNDIRECTED = "undirected";

        public static final String KEY = "key";
        public static final String FOR = "for";
        public static final String ALL = "all";
        public static final String ATTRIBUTE_NAME = "attr.name";
        public static final String ATTRIBUTE_TYPE = "attr.type";
        public static final String DEFAULT = "default";
        
        public static final String NODE = "node";
        public static final String EDGE = "edge";
        public static final String SOURCE = "source";
        public static final String TARGET = "target";
        public static final String DATA = "data";
        public static final String TYPE = "type";
        
        public static final String INT = "int";
        public static final String INTEGER = "integer";
        public static final String LONG = "long";
        public static final String FLOAT = "float";
        public static final String DOUBLE = "double";
        public static final String REAL = "real";
        public static final String BOOLEAN = "boolean";
        public static final String STRING = "string";
        public static final String DATE = "date";
    }
    
    /**
     * A SAX Parser for GraphML data files.
     */
    public static class GraphMLHandler extends DefaultHandler implements Tokens
    {
    	public static final String SOURCE = Graph.DEFAULT_SOURCE_KEY;
        public static final String TARGET = Graph.DEFAULT_TARGET_KEY;
        public static final String SOURCE_ID = SOURCE + '_' + ID;
        public static final String TARGET_ID = TARGET + '_' + ID;

        protected ParserFactory parserFactory = ParserFactory.getDefaultFactory();
        
        protected Schema nodeSchema = new Schema();
        protected Schema edgeSchema = new Schema();
        
        protected String graphID;
        protected Graph graph;
        protected Table nodes;
        protected Table edges;
        
        // Schema parsing.
        protected String idAttribute;
        protected String forAttribute;
        protected String nameAttribute;
        protected String typeAttribute;
        protected String defaultValue;
        
        protected StringBuffer elementContents = new StringBuffer();
        
        // node, edge, data parsing
        private String currentKey;
        private int nodeRow = -1;
        private Table currentTable = null;
        protected HashMap<String, Integer> nodeIDsToRowNumbers = new HashMap<String, Integer>();
        protected HashMap idMap = new HashMap();
        
        private boolean isDirected = false;
        private boolean inSchema;

        private boolean cleanForGUESS;

        public GraphMLHandler(boolean cleanForGUESS) {
        	this.cleanForGUESS = cleanForGUESS;
        }
        
        public void startDocument() {
            nodeIDsToRowNumbers.clear();
            inSchema = true;
            
            this.edgeSchema.addColumn(SOURCE, int.class);
            this.edgeSchema.addColumn(TARGET, int.class);
            this.edgeSchema.addColumn(SOURCE_ID, String.class);
            this.edgeSchema.addColumn(TARGET_ID, String.class);
        }
        
        public void endDocument() throws SAXException {        	
        	/*NOTE: Inserted from 1.5 version of this file.
        	 * The fix is not available in the current release, but was
        	 * merged in from the repository
        	 */
        	
        	// Initialize the schemas just in case there are no nodes or edges
        	schemaCheck();
        	
            // Set up the edges
            IntIterator rows = edges.rows();
            while (rows.hasNext()) {
                int rowIndex = rows.nextInt();

                String source = edges.getString(rowIndex, SOURCE_ID);
                if (!nodeIDsToRowNumbers.containsKey(source)) {
                    throw new SAXException(
                        "Tried to create edge with source node id = " + source
                        + " which does not exist.");
                }

                int s = nodeIDsToRowNumbers.get(source);
                edges.setInt(rowIndex, SOURCE, s);

                String target = edges.getString(rowIndex, TARGET_ID);
                if (!nodeIDsToRowNumbers.containsKey(target)) {
                    throw new SAXException(
                        "Tried to create edge with target node this.idAttribute=" + target
                        + " which does not exist.");
                }
                int t = ((Integer) nodeIDsToRowNumbers.get(target)).intValue();
                edges.setInt(rowIndex, TARGET, t);
            }
            System.err.println("Node schema:");
            for (int ii = 0; ii < nodes.getColumnCount(); ii++) {
            	System.err.println("\t" + nodes.getColumnName(ii) + ": " + nodes.getColumnType(ii));
            }
            edges.removeColumn(SOURCE_ID);
            edges.removeColumn(TARGET_ID);

            // Now create the graph
            graph = new Graph(nodes, edges, isDirected);
            if (graphID != null)
                graph.putClientProperty(ID, graphID);
        }

        private String formNonConflictingNameForGUESS(String name) {
        	return "original" + name;
        }

        public static final String GUESS_ATTRIBUTE_COLOR = "color";
        public static final String GUESS_ATTRIBUTE_FIXED = "fixed";
        public static final String GUESS_ATTRIBUTE_HEIGHT = "height";
        public static final String GUESS_ATTRIBUTE_IMAGE = "image";
        public static final String GUESS_ATTRIBUTE_LABEL = "label";
        public static final String GUESS_ATTRIBUTE_LABEL_COLOR = "labelcolor";
        public static final String GUESS_ATTRIBUTE_LABEL_SIZE = "labelsize";
        public static final String GUESS_ATTRIBUTE_LABEL_VISIBLE = "labelvisible";
        public static final String GUESS_ATTRIBUTE_NAME = "name";
        public static final String GUESS_ATTRIBUTE_ORIGINAL_LABEL = "originallabel";
        public static final String GUESS_ATTRIBUTE_STROKE_COLOR = "strokecolor";
        public static final String GUESS_ATTRIBUTE_STYLE = "style";
        public static final String GUESS_ATTRIBUTE_VISIBLE = "visible";
        public static final String GUESS_ATTRIBUTE_WIDTH = "width";
        public static final String GUESS_ATTRIBUTE_X = "x";
        public static final String GUESS_ATTRIBUTE_Y = "y";

        public static final String GUESS_ATTRIBUTE_EDGE_ID = "__edgeid";
        public static final String GUESS_ATTRIBUTE_DIRECTED = "directed";
        public static final String GUESS_ATTRIBUTE_NODE_1 = "node1";
        public static final String GUESS_ATTRIBUTE_NODE_2 = "node2";
        public static final String GUESS_ATTRIBUTE_WEIGHT = "weight";

        public static final Collection<String> DEFAULT_GUESS_NODES_ATTRIBUTES =
        	Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
        		ID,
        		GUESS_ATTRIBUTE_COLOR,
        		GUESS_ATTRIBUTE_FIXED,
        		GUESS_ATTRIBUTE_HEIGHT,
        		GUESS_ATTRIBUTE_IMAGE,
//        		GUESS_ATTRIBUTE_LABEL,
        		GUESS_ATTRIBUTE_LABEL_COLOR,
        		GUESS_ATTRIBUTE_LABEL_SIZE,
        		GUESS_ATTRIBUTE_LABEL_VISIBLE,
        		GUESS_ATTRIBUTE_NAME,
        		GUESS_ATTRIBUTE_ORIGINAL_LABEL,
        		GUESS_ATTRIBUTE_STROKE_COLOR,
        		GUESS_ATTRIBUTE_STYLE,
        		GUESS_ATTRIBUTE_VISIBLE,
        		GUESS_ATTRIBUTE_WIDTH,
        		GUESS_ATTRIBUTE_X,
        		GUESS_ATTRIBUTE_Y)));

        public static final Collection<String> DEFAULT_GUESS_EDGE_ATTRIBUTES =
        	Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
        		GUESS_ATTRIBUTE_EDGE_ID,
        		GUESS_ATTRIBUTE_COLOR,
        		GUESS_ATTRIBUTE_DIRECTED,
//        		GUESS_ATTRIBUTE_LABEL,
        		GUESS_ATTRIBUTE_LABEL_COLOR,
        		GUESS_ATTRIBUTE_LABEL_SIZE,
        		GUESS_ATTRIBUTE_LABEL_VISIBLE,
        		GUESS_ATTRIBUTE_NODE_1,
        		GUESS_ATTRIBUTE_NODE_2,
        		GUESS_ATTRIBUTE_ORIGINAL_LABEL,
        		GUESS_ATTRIBUTE_VISIBLE,
//        		GUESS_ATTRIBUTE_WEIGHT,
        		GUESS_ATTRIBUTE_WIDTH)));
        
        public void startElement(
        		String namespaceURI, String localName, String attributeName, Attributes attributes)
        		throws SAXException {
            // Clear the character buffer
            this.elementContents.delete(0, this.elementContents.length());
            
            if (attributeName.equals(GRAPH)) {
                // Parse directedness default
                String edgeDefinition = attributes.getValue(EDGE_DEFINITION);
                isDirected = DIRECTED.equalsIgnoreCase(edgeDefinition);
                graphID = attributes.getValue(ID);
            } else if (attributeName.equals(KEY)) {
                if (!inSchema) {
                	throw new SAXException(
                		"\"" + KEY + "\" elements can not"
                        + " occur after the first node or edge declaration.");
                }

                this.forAttribute = attributes.getValue(FOR);
                this.idAttribute = attributes.getValue(ID);
                this.nameAttribute = attributes.getValue(ATTRIBUTE_NAME);

                if (this.cleanForGUESS) {
                	if (DEFAULT_GUESS_NODES_ATTRIBUTES.contains(this.idAttribute) ||
                			DEFAULT_GUESS_EDGE_ATTRIBUTES.contains(this.idAttribute)) {
                		this.idAttribute = formNonConflictingNameForGUESS(this.idAttribute);
                	}

                	if (DEFAULT_GUESS_NODES_ATTRIBUTES.contains(this.nameAttribute) ||
                			DEFAULT_GUESS_EDGE_ATTRIBUTES.contains(this.nameAttribute)) {
                		this.nameAttribute = formNonConflictingNameForGUESS(this.nameAttribute);
                	}
                }

                this.typeAttribute = attributes.getValue(ATTRIBUTE_TYPE);
            } else if (attributeName.equals(NODE)) {
                schemaCheck();

                nodeRow = nodes.addRow();
                
                String id = attributes.getValue(ID);
                nodeIDsToRowNumbers.put(id, new Integer(nodeRow));
                this.currentTable = this.nodes;
            } else if (attributeName.equals(EDGE)) {
                schemaCheck();

                nodeRow = edges.addRow();
                edges.setString(nodeRow, SOURCE_ID, attributes.getValue(SOURCE));
                edges.setString(nodeRow, TARGET_ID, attributes.getValue(TARGET));

                this.currentTable = this.edges;
            } else if (attributeName.equals(DATA)) {
                this.currentKey = attributes.getValue(KEY);

                if (this.cleanForGUESS) {
                	if (DEFAULT_GUESS_NODES_ATTRIBUTES.contains(this.currentKey) ||
                			DEFAULT_GUESS_EDGE_ATTRIBUTES.contains(this.currentKey)) {
            			this.currentKey = formNonConflictingNameForGUESS(this.currentKey);
                	}
                }
            }
        }

        public void endElement(
        		String namespaceURI, String localName, String attributeName) throws SAXException {
            if (attributeName.equals(DEFAULT)) {
                // Value is in the buffer.
                this.defaultValue = this.elementContents.toString();
            } else if (attributeName.equals(KEY)) {
                // Time to add to the proper schema(s).
                addToSchema();
            } else if (attributeName.equals(DATA)) {
                // Value is in the buffer.
                String contents = this.elementContents.toString();
                String name = (String) idMap.get(this.currentKey);
                Class type = currentTable.getColumnType(name);

                try {
                    Object val = parse(contents, type);
                    currentTable.set(nodeRow, name, val);
                } catch (DataParseException e) {
                	throw new SAXException(e);
                }
            } else if (NODE.equals(attributeName) || EDGE.equals(attributeName)) {
                nodeRow = -1;
                currentTable = null;
            }
        }
        
        public void characters(char[] ch, int start, int length)  {
            this.elementContents.append(ch, start, length);
        }

        // --------------------------------------------------------------------
        
        protected void schemaCheck() {
            if ( inSchema ) {
                this.nodeSchema.lockSchema();
                this.edgeSchema.lockSchema();
                nodes = this.nodeSchema.instantiate();
                edges = this.edgeSchema.instantiate();
                inSchema = false;
            }
        }
        
        protected void addToSchema() throws SAXException {
            if ((this.nameAttribute == null) || (this.nameAttribute.length() == 0)) {
                throw new SAXException("Empty " + KEY + " name.");
            }

            if ((this.typeAttribute == null) || (this.typeAttribute.length() == 0)) {
            	throw new SAXException("Empty " + KEY + " type.");
            }
            
            try {
                Class type = parseType(this.typeAttribute);
                Object defaultValue =
                	(this.defaultValue == null ? null : parse(this.defaultValue, type));
                
                if ((this.forAttribute == null) || this.forAttribute.equals(ALL)) {
                    this.nodeSchema.addColumn(this.nameAttribute, type, defaultValue);
                    this.edgeSchema.addColumn(this.nameAttribute, type, defaultValue);
                } else if (this.forAttribute.equals(NODE)) {
                	if (this.cleanForGUESS) {
                		System.err.println("adding " + this.nameAttribute);
                	}
                    this.nodeSchema.addColumn(this.nameAttribute, type, defaultValue);
                } else if (this.forAttribute.equals(EDGE)) {
                    this.edgeSchema.addColumn(this.nameAttribute, type, defaultValue);
                } else {
                	throw new SAXException(
            			"Unrecognized \"" + FOR + "\" value: " + this.forAttribute);
                }

                idMap.put(this.idAttribute, this.nameAttribute);
                
                this.defaultValue = null;
            } catch (DataParseException e) {
            	throw new SAXException(e);
            }
        }
        
        protected Class parseType(String type) throws SAXException {
            type = type.toLowerCase();
            if ( type.equals(INT) || type.equals(INTEGER) ) {
                return int.class;
            } else if ( type.equals(LONG) ) {
                return long.class;
            } else if ( type.equals(FLOAT) ) {
                return float.class;
            } else if ( type.equals(DOUBLE) || type.equals(REAL)) {
                return double.class;
            } else if ( type.equals(BOOLEAN) ) {
                return boolean.class;
            } else if ( type.equals(STRING) ) {
                return String.class;
            } else if ( type.equals(DATE) ) {
                return Date.class;
            } else {
                throw new SAXException("Unrecognized data type: "+type);
            }
        }

        // This parses the contents of an element as (the) type of data.
        protected Object parse(String input, Class type) throws DataParseException {
            DataParser parser = this.parserFactory.getParser(type);

            return parser.parse(input);
        }

        public Graph getGraph() {
            return graph;
        }        
    }
}

