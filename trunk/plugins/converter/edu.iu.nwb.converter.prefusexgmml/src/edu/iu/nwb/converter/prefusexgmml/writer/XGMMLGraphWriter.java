package edu.iu.nwb.converter.prefusexgmml.writer;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.io.AbstractGraphWriter;

/**
 * Adapted from version 1.0 of Jeffrey Heer's XMLGraphWriter
 * Retrieved 2009.05.22 from:
 * http://prefuse.cvs.sourceforge.net/viewvc/prefuse/prefuse/src/edu/berkeley/guir/prefuse/graph/io/XMLGraphWriter.java
 */
/* Carefully note these differing conventions between XML and XGMML:
 * 		In XML: elements, text, and attributes are all kinds of (XML) nodes.
 * 		In XGMML: nodes, edges, and atts are all kinds of (XML) elements.
 * Prefuse follows the XGMML convention:
 * - a Node is an XGMML node, which is a kind of XML element, which is a kind of XML node.
 * - an Edge is an XGMML edge, which is a kind of XML element, which is a kind of XML node.
 * As near as I can tell, Prefuse has no Att class(!), but avoid great pain by noting
 * that the Entity (of which Node and Edge are subinterfaces) methods getAttribute and
 * setAttribute refer to XGMML atts and _NOT_ XML attributes.
 * For example, edge.setAttribute("source", 0) will result not in:
 * 		'<edge source="0" ...>'
 * 		but in:
 * 		'<edge ...><att name="source" value="0"/>...</edge>'!
 * This apparent inability to speak of _XML_ attributes for a Graph means
 * this class is significantly more complicated than it logically need be,
 * and we are forced to manually write an unfortunate lot of XML (or XGMML).
 * 
 * For the sake of compatibility between our exports here and imports to Cytoscape,
 * the helper classes PrefuseNodeIDer, PrefuseNodeLabeler, and PrefuseEdgeLabeler
 * are used to ensure that each Node has an id and a label attribute, and that
 * each Edge has a label attribute.
 * 
 * Joseph Biberstine, 2009.05.26
 */
public class XGMMLGraphWriter extends AbstractGraphWriter {

	public static final String NODE   = "node";
	public static final String EDGE   = "edge";
	public static final String ATT    = "att";
	public static final String ID     = "id";
	public static final String LABEL  = "label";
	public static final String SOURCE = "source";
	public static final String TARGET = "target";
	public static final String WEIGHT = "weight";
	public static final String TYPE   = "type";
	public static final String NAME   = "name";
	public static final String VALUE  = "value";
	public static final String LIST   = "list";
	public static final String GRAPH  = "graph";
	public static final String DIRECTED = "directed";

	/* Attributes which should be recorded inside the parent element's
	 * start-tag rather than with a child "att" element.
	 */
	public static final String[] NODE_XML_ATTRIBUTES = { ID, NAME, LABEL, WEIGHT };
	public static final String[] EDGE_XML_ATTRIBUTES = { ID, NAME, LABEL, WEIGHT, SOURCE, TARGET };
	
	public static final String DEFAULT_GRAPH_LABEL = "Network";
	
	public static final String NODE_INDENT 	= 	"  ";
	public static final String EDGE_INDENT 	= 	"  ";
	public static final String ATT_INDENT	= "    ";
	
	public static final String NODES_LISTING_COMMENT = "<!-- nodes -->";
	public static final String EDGES_LISTING_COMMENT = "<!-- edges -->";
	
	public static final String XML_NAMESPACE_ATTRIBUTE_KEY = "xmlns";
	public static final String XGMML_NAMESPACE = "http://www.cs.rpi.edu/XGMML";

	@Override
	public void writeGraph(Graph graph, OutputStream os) {		
		PrefuseNodeIDer.assignMissingIDs(graph);
		PrefuseNodeLabeler.assignMissingNodeLabels(graph);
		PrefuseEdgeLabeler.assignMissingEdgeLabels(graph);
		
		writeGraphToFile(graph, os);
	}
	
	private void writeGraphToFile(Graph graph, OutputStream os) {
		PrintWriter printWriter = new PrintWriter(new BufferedOutputStream(os));
		printWriter.print(createGraphXML(graph));
		printWriter.flush();
	}
	
	private String createGraphXML(Graph graph) {
		String xml = "";
		
		xml += (createGraphStartElement(graph) + "\n");		
		xml += createNodesXML(graph);		
		xml += createEdgesXML(graph);
		xml += (createElementEndTag(GRAPH) + "\n");
		
		return xml;
	}
	
	private String createGraphStartElement(Graph graph) {
		String directed = graph.isDirected() ? "1" : "0";
		
		Map attributes = new HashMap();
		attributes.put(LABEL, DEFAULT_GRAPH_LABEL);
		attributes.put(DIRECTED, directed);
		attributes.put(XML_NAMESPACE_ATTRIBUTE_KEY, XGMML_NAMESPACE);
		
		return createElementStartTag(GRAPH, attributes);
	}
	
	private String createNodesXML(Graph graph) {
		String xml = "";
		
		xml += (NODE_INDENT + NODES_LISTING_COMMENT + "\n");
		
		Iterator nodes = graph.getNodes();
		while ( nodes.hasNext() ) {
			Node node = (Node) nodes.next();
			
			xml += createNodeXML(node);
		}
		
		return xml;
	}
	
	private String createNodeXML(Node node) {
		String xml = "";
		xml += (NODE_INDENT + createNodeStartTag(node) + "\n");
		xml += createAttsXML(node, NODE_XML_ATTRIBUTES);
		xml += (NODE_INDENT + createElementEndTag(NODE) + "\n");
		return xml;
	}
	
	private String createNodeStartTag(Node node) {
		Map xmlAttributesMap = getXMLAttributesMap(node, NODE_XML_ATTRIBUTES);		
		return createElementStartTag(NODE, xmlAttributesMap);
	}
	
	private String createEdgesXML(Graph graph) {
		String xml = "";
		
		xml += EDGE_INDENT + EDGES_LISTING_COMMENT + "\n";
		
		Iterator edges = graph.getEdges();
		while ( edges.hasNext() ) {
			Edge edge = (Edge) edges.next();
			
			xml += createEdgeXML(edge);
		}
		
		return xml;
	}
	
	private String createEdgeXML(Edge edge) {
		String xml = "";		
		xml += (EDGE_INDENT + createEdgeStartTag(edge) + "\n");		
		xml += createAttsXML(edge, EDGE_XML_ATTRIBUTES);	
		xml += (EDGE_INDENT + createElementEndTag(EDGE) + "\n");		
		return xml;
	}

	private String createEdgeStartTag(Edge edge) {
		Map xmlAttributesMap = getXMLAttributesMap(edge, EDGE_XML_ATTRIBUTES);
		return createElementStartTag(EDGE, xmlAttributesMap);
	}
	
	private Map getXMLAttributesMap(Entity e, String[] xmlAttributeKeys) {
		Map xmlAttributes = new HashMap();
		
		for( int i = 0; i < xmlAttributeKeys.length; i++ ) {
			String key = xmlAttributeKeys[i];
			String value = e.getAttribute(key);
			
			if ( value != null )
				xmlAttributes.put(key, value);
		}
		
		return xmlAttributes;
	}	
	
	/* xmlAttributeKeys are, for example, the "source" in '<edge source="0" ...>'.
	 * All other attributes are recorded not as XML attributes but with "att" elements,
	 *   per the XGMML spec,
	 *   for example the "rad" in
	 *   	'<edge><att type="integer" name="rad" value="30"/>...</edge>'. 
	 */
	private String createAttsXML(Entity entity, String[] xmlAttributeKeys) {
		String xml = "";
		
		Map attMap = entity.getAttributes();
		Iterator attsIterator = (Iterator) attMap.entrySet().iterator();
		while( attsIterator.hasNext() ) {
			Map.Entry att = (Map.Entry) attsIterator.next();
			
			/* If it this attribute key is an XML attribute, then it has already
			 * been recorded in this entity's start tag.
			 * If it is not, then create an att element. 
			 */
			boolean xmlAttribute = contains(xmlAttributeKeys, (String) att.getKey());
			if ( !xmlAttribute ) {
				xml += (ATT_INDENT + createAttStartTag(att));
				xml += (createElementEndTag(ATT) + "\n");
			}
		}
		
		return xml;
	}
	
	private String createAttStartTag(Map.Entry attBinding) {
		String key = (String) attBinding.getKey();
		String value = (String) attBinding.getValue();
		
		Map attributes = new HashMap();
		attributes.put(NAME, key);
		attributes.put(VALUE, value);
		
		return createElementStartTag(ATT, attributes);
	}
	
	private String createElementStartTag(String elementName, Map xmlAttributesMap) {
		String tag = "";
		tag += "<";
		tag += elementName;
		
		Iterator xmlAttributesMapIterator = xmlAttributesMap.entrySet().iterator();		
		while ( xmlAttributesMapIterator.hasNext() ) {
			Map.Entry xmlAttributeBinding = (Map.Entry) xmlAttributesMapIterator.next();
			
			tag += (" " + createAttributeString(xmlAttributeBinding));
		}
		
		tag += ">";
		
		return tag;
	}
	
	private String createElementEndTag(String elementName) {
		return "</" + elementName + ">";
	}
	
	// Returns "key="value"" where the value is escaped for XML.
	private String createAttributeString(Map.Entry attributePair) {
		String key 		= (String) attributePair.getKey();
		String value 	= (String) attributePair.getValue();
		
		return key + "=\"" + XMLLib.EscapeString(value) + "\"";
	}
	
	private boolean contains(String list[], String item) {
		for ( int i = 0; i < list.length; i++ ) {
			if ( list[i].equals(item) )
				return true;
		}
		return false;
	}
}