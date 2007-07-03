package edu.iu.nwb.converter.nwbgraphml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.service.log.LogService;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.java.browser.dom.DOMAccessException;

import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;


/**
 * Converts from GraphML to NWB file format via the DOM libraries
 * @author M Felix Terkhorn  
 */
public class GraphMLToNWBbyDOM implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext ciContext;
    LogService logger;
    GUIBuilderService guiBuilder;
    private Document dom;
    private HashMap vertexAttrMap;
    private HashMap edgeAttrMap;
    private String vertexHeaderLine;
    private String edgeHeaderLine;
    private String vertexAttrLine;
    private String edgeAttrLine;
    private String edgeDefault;
    private HashMap verticesByID; // map String id to Node vertex
    private HashMap edgesByID;    // map String id to Node edge
    private ArrayList invalidVertices; // buffer for vertex nodes with invalid IDs
    
    private ArrayList invalidEdges;   // buffer for edge nodes with invalid IDs, sources, or targets
    
    
    private HashMap newVertexIdMap; /*  String oldID -> String newID.  
    								 *  This HashMap is used to resolve broken vertex ID values as they
    								 *  appear in vertex node IDs, and edge node sources and targets 
    								 */
    
    private HashMap newEdgeIdMap;  /* String oldID -> String newID.
    							    * This HashMap resolves broken edge ID values to valid ones.
    							    */
    
	private StringBuffer errorMessages = new StringBuffer();

	private ArrayList vertexAttrLineOrder;
	private ArrayList edgeAttrLineOrder;
	
	
    
    /**
     * Intializes the algorithm
     * @param data List of Data objects to convert
     * @param parameters Parameters passed to the converter
     * @param context Provides access to CIShell services
     * @param transformer 
     */
    public GraphMLToNWBbyDOM(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.ciContext = context;
        this.logger = (LogService)ciContext.getService(LogService.class.getName());
        this.vertexAttrMap = new HashMap();
        this.edgeAttrMap = new HashMap();
        this.edgesByID = new HashMap();  
        this.verticesByID = new HashMap(); 
        this.invalidEdges = new ArrayList(); 
        this.invalidVertices = new ArrayList();
        this.newVertexIdMap = new HashMap();
        this.newEdgeIdMap = new HashMap();
        this.vertexAttrLineOrder = new ArrayList();
        this.edgeAttrLineOrder = new ArrayList();
        
    }

    private void parseXmlFile(File xmlFile) {
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	dbf.setCoalescing(true);
    	try {
    		// get an instance of the document builder
    		DocumentBuilder db = dbf.newDocumentBuilder();
    		
    		// parse using builder to get DOM representation of the XML file
    		
    		String fullpath = "file:/"+xmlFile.toString();
    		System.out.println(">>parse file: "+xmlFile+"\n");
    		
    		dom = db.parse(fullpath);
    		
    	} catch (ParserConfigurationException pce) {
    		pce.printStackTrace();
    	} catch (SAXException se) {
    		se.printStackTrace();
    	} catch (IOException ioe) {
    		ioe.printStackTrace();
    	}
    }
    
    /**
     * Executes the conversion
     * 
     * @return A single java file object
     */
    public Data[] execute() {
		Object inFile = data[0].getData();
    	int q;
		
		
		if (inFile instanceof File){
			
			
			
			Source graphml = new StreamSource((File) inFile);
			parseXmlFile((File)inFile);
			Element docEle = dom.getDocumentElement();
			
			//System.out.println(">>root localname:  "+docEle.getLocalName());
			//System.out.println(">>root node name:  "+docEle.getNodeName()); // is graphml
			
			
			
			NodeList topGraphList = docEle.getChildNodes();
			if (topGraphList != null && topGraphList.getLength() > 0) {
				//System.out.println(">>child node method worked.  length ="+ topGraphList.getLength()+"\n");
				for (int j = 0; j < topGraphList.getLength();j++) {
					//System.out.println("fooey "+j);
					//System.out.println(">>toplevel child node name: "+((Element)topGraphList.item(j)).getNodeName()+"\n");
					Node node = topGraphList.item(j);
					
					//System.out.println(">>toplevel child node name: "+el.getNodeName()+"\n");
					processGraph(node);
				}
			}
			
			//System.out.println(">> Length of vertexAttrMap is now "+vertexAttrMap.size());
			//System.out.println(">> Length of edgeAttrMap is now "+edgeAttrMap.size());
			
			vertexAttrLine = makeVertexAttrLine();
			edgeAttrLine = makeEdgeAttrLine();
			
			vertexHeaderLine = makeVertexHeaderLine();
			edgeHeaderLine = makeEdgeHeaderLine();
			
			
			System.out.println("--> Headers <--");
			System.out.println(">> vert header : "+vertexHeaderLine);
			System.out.println(">> vert attrs  : "+vertexAttrLine);
			System.out.println(">> edge header : "+edgeHeaderLine);
			System.out.println(">> edgeAttrLine: "+edgeAttrLine);
			
			
			
			File nwbFile = getTempFile();
			String currentLine;
			String element;
			
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(nwbFile));
				try {
					
					writer.write(vertexHeaderLine);
					writer.write(vertexAttrLine);
					Iterator vIt = verticesByID.keySet().iterator();
					
					while (vIt.hasNext()) {
						currentLine = "";
						String vNextID = (String)vIt.next();
						Node vNextNode = (Node)verticesByID.get(vNextID);
						System.out.println("#### NEXT VERTEX ID : "+vNextID);
						// write info for each vertex
						
						for (q = 0;q < vertexAttrLineOrder.size();q++) {
							if (vertexAttrLineOrder.get(q).equals("id")) {
								//currentLine += vNextNode.getAttributes().getNamedItem("label").getNodeValue();
								currentLine += (vNextID + " ");
							} else {
								// look in data element
								//System.out.println("Attempting to match node with data key "+(String)(vertexAttrLineOrder.get(q)));
								element = matchData(vNextNode,(String)(vertexAttrLineOrder.get(q)));
								System.out.println("found element: "+element);
								currentLine += element + " ";
							}
						}
						
						currentLine+="\n";
						System.out.println("WRITING LINE: "+currentLine);
						writer.write(currentLine);
					}
					
					System.out.println("Writing line: "+edgeHeaderLine);
					writer.write(edgeHeaderLine);
					System.out.println("Writing line: "+edgeAttrLine);
					writer.write(edgeAttrLine);
					Iterator eIt = edgesByID.keySet().iterator();
					while (eIt.hasNext()) {
						String eNextID = (String)eIt.next();
						//System.out.println("#### NEXT EDGE ID : "+eNextID);
						// write info for each edge
					}
					
					writer.flush();
					writer.close();
					
					// We need to do the conversion here.
					return new Data[] {new BasicData(nwbFile, "file:text/nwb")};
				} catch(Exception exception) {
					logger.log(LogService.LOG_ERROR, "Problem executing transformation from GraphML to NWB");
					exception.printStackTrace();
					return null;
				}
		
			} catch (IOException ioe ) {
				logger.log(LogService.LOG_ERROR, "Problem executing transformation from GraphML to NWB: IO Exception creating bufferedWriter output file");
				System.out.println ("IO Exception creating bufferedWriter output file");
				ioe.printStackTrace();
				return null;
			}
			
			
		} else {
			return null;
		}
    }
    
    private String matchData(Node n,String k) {
    	int i;
    	
    	
    	NodeList kids = n.getChildNodes(); 
    	for (i = 0;i < kids.getLength();i++) {
    		
    		if (kids.item(i).getNodeName().toLowerCase().equals("data")) {
    			//System.out.println(">> Child #"+i+" has name "+kids.item(i).getNodeName());
    			//System.out.println(">> data keys length = "+kids.item(i).getAttributes().getLength());
    			
    			for (int j = 0; j < kids.item(i).getAttributes().getLength();j++) {
    				//System.out.println("attribute..."+kids.item(i).getAttributes().item(j).getNodeValue());
    				//kids.item(i).getAttributes().\
    				if (	kids.item(i).getAttributes().item(j).getNodeValue().equals(k)) {
    					//System.out.println("not null");
    					return kids.item(i).getTextContent();

    				}
    			}
    		}
    	}
    	return "*";
    }
    
    private String makeVertexHeaderLine() {
    	return "*Nodes "+verticesByID.size()+"\n";
    }
    
    private String makeEdgeHeaderLine() {
    	return "*"+edgeDefault+" "+edgesByID.size()+"\n";
    }
    
    private String makeVertexAttrLine() {
    	
    	vertexAttrLine = "";
    	if (vertexAttrMap.get("id") != null ) {
    		vertexAttrLine += "id*"+vertexAttrMap.get("id")+" ";
    	} else {  // no id key specified
    		vertexAttrLine += "id*string ";
    	}
    	vertexAttrLineOrder.add("id");
    	
    	if (vertexAttrMap.get("label") != null ) {
    		vertexAttrLine += "label*"+vertexAttrMap.get("label")+" ";
    	} else { // no label key specified 
    		vertexAttrLine += "label*string ";
    	}
    	
    	vertexAttrLineOrder.add("label");
    	
    	Iterator it = vertexAttrMap.keySet().iterator();
    	
    	while (it.hasNext()) {
    		String str = (String)it.next();
    		
    		if (str.toLowerCase().equals("id") || str.toLowerCase().equals("label")) {
    		
    			continue;
    		} else {
    		
    			vertexAttrLine += str+"*"+vertexAttrMap.get(str)+" ";
    			vertexAttrLineOrder.add(str);
    		}
    	}
    	
    	
    	return vertexAttrLine+"\n";
    }
    
    private String makeEdgeAttrLine() {
    	//System.out.println(">> inside makeNodeAttrLine()");
    	edgeAttrLine = "";
    	if (edgeAttrMap.get("source") != null ) {
    		edgeAttrLine += "source*"+edgeAttrMap.get("source")+" ";
    	} else {  // no id key specified
    		edgeAttrLine += "source*string ";
    	}
    	
    	edgeAttrLineOrder.add("source");
    	
    	if (edgeAttrMap.get("target") != null ) {
    		edgeAttrLine += "target*"+edgeAttrMap.get("target")+" ";
    	} else { // no label key specified 
    		edgeAttrLine += "target*string ";
    	}
    	edgeAttrLineOrder.add("target");
    	
    	//System.out.println(">>halfway thru makeedgeAttrLine");
    	
    	Iterator it = edgeAttrMap.keySet().iterator();
    	
    	while (it.hasNext()) {
    		String str = (String)it.next();
    		//System.out.println(">> next key = "+str+", next value = "+edgeAttrMap.get(str));
    		if (str.toLowerCase().equals("id") || str.toLowerCase().equals("label")) {
    	
    			continue;
    		} else {
    	
    			edgeAttrLine += str+"*"+edgeAttrMap.get(str)+" ";
    			edgeAttrLineOrder.add(str);
    		}
    	}
    	
    	//System.out.println(">> returning makeedgeAttrLine");
    	return edgeAttrLine+"\n";
    
    }
    
    
    private void processGraph(Node n) {
    	//System.out.println(">>baseURI: "+el.getBaseURI()+"\n");
    	System.out.println(">>toplevel child node name: "+n.getNodeName()+"\n");
    	if (n.getNodeName() == "key") {
    		processGraphKey(n);
    	} if (n.getNodeName() == "graph") {
    		processGraphStructure(n);
    	}
    	
    	//NodeList topGraphNodes = el.getElementsByTagName("graph");
    	//for (int i=0;i<topGraphNodes.getLength();i++) {
    	//	System.out.println("node "+i+" at top level = "+topGraphNodes.item(i).toString()+"\n");
    	//}
    }
    
    private void processGraphStructure(Node n) {
    	NodeList children;
    	//check attrs for Node n.  there should be an edgedefault attr which specifies directed or undirected
    	n.getAttributes();
    	if (n.getAttributes().getNamedItem("edgedefault") != null) {
    		if (n.getAttributes().getNamedItem("edgedefault").getTextContent().toLowerCase().equals("undirected")) {
    			System.out.println(">> edgedefault is undirected");
    			edgeDefault = "UndirectedEdges";
    		} else if (n.getAttributes().getNamedItem("edgedefault").getTextContent().toLowerCase().equals("directed")) {
    			System.out.println(">> edgedefault is directed");
    			edgeDefault = "DirectedEdges";
    		} else {
    			System.out.println(">> unknown edgedefault");
    			edgeDefault = "UndirectedEdges";
    		}
    	} else {
    		System.out.println(">> no edgedefault attribute found in graph structure, defaulting to undirected");
    		edgeDefault = "UndirectedEdges";
    	}
    	
    	if (edgeDefault == null) {
    		// shouldn't ever reach this
    		edgeDefault = "UndirectedEdges";
    	}
    	
    	

		// We need to check for invalid edge ID vals, edge source vals, edge target vals, vertex ID vals (IE, values <= 0)
		// This should be done in processEdge, processVertex
		// From within processVertex, for example, when we find a bad ID val, we can push this vertex onto a buffer for completion after all
		// other vertices are processed
		// We can have another HashMap which maps invalid IDs to valid IDs.  This should help in dealing with the source and target
		// attribute values of edges.
		// Invalid source/target vals in an edge node will force that edge node to be processed. 
		
    	
    	String iName;
    	Node item;
    	children = n.getChildNodes();
    	for (int i = 0; i < children.getLength();i++) {
    		if ((iName = (item = children.item(i)).getNodeName()).startsWith("#")) 
    			continue;
    		//System.out.println(">>found child:   "+iName);
    		if (iName.toLowerCase().equals("edge")) {
    			try {
    				processEdge(item);
    			} catch (DOMAccessException dae) {
    				System.out.println(">> DOM Access Exception: "+dae.getMessage()+"\nSkipping this edge data.");
    				continue;
    			}
    		} else if (iName.toLowerCase().equals("node")) {
    			try {
    				processVertex(item);
    			} catch (DOMAccessException dae) {
    				System.out.println(">> DOM Access Exception: "+dae.getMessage()+"\nSkipping this vertex data.");
    				continue;
    			}
    		}
    		
    		//System.out.println(">> found element \""+ children.item(i).getNodeName() + "\", id =  "+children.item(i).getAttributes().getNamedItem("id"));
    		
    	}
    	
    	System.out.println(">> Edges and nodes processed.\n>> Total edges mapped: "+edgesByID.size());
		System.out.println(">> Total nodes mapped: "+verticesByID.size());
	
		System.out.println(">> Total unprocessed vertices: "+invalidVertices.size());
		System.out.println(">> Total unprocessed edges: "+invalidEdges.size());
		
		// We need to first visit all invalidVertices and add mappings from the bad ID vals to new, happy ID vals
		
		Iterator it = invalidVertices.iterator();
		while (it.hasNext()) {
			Node current = (Node)it.next();
			String oldIdentifier = ((NamedNodeMap)current.getAttributes()).getNamedItem("id").getNodeValue();
			
			String newIdentifier = findUnusedVertexId();
			((NamedNodeMap)current.getAttributes()).getNamedItem("id").setNodeValue(newIdentifier);
			System.out.println("    >> Mapping old vertex ID "+oldIdentifier +" to "+newIdentifier);
			newVertexIdMap.put(oldIdentifier, newIdentifier);
			verticesByID.put(newIdentifier, current);
			//invalidVertices.remove(current);
			// the above lined caused problems
			
		}
		
		/*
		String kNext;
		Iterator kIt = newVertexIdMap.keySet().iterator();
		while (kIt.hasNext()) {
			kNext = kIt.next().toString();
			System.out.println("---->> old ID "+kNext+ " maps to "+newVertexIdMap.get(kNext));
		}
		*/
		
		Iterator eit = invalidEdges.iterator();
		while (eit.hasNext()) {
		
			Node cur = (Node)eit.next();
			String oldEID = ((NamedNodeMap)cur.getAttributes()).getNamedItem("id").getNodeValue();
			
			boolean checkA = edgeIdIsBad(oldEID);
			boolean checkB = edgeReferencesAreBad(cur);
			
			
			if (checkA) {
				
				String newEID = findUnusedEdgeId();
				((NamedNodeMap)cur.getAttributes()).getNamedItem("id").setNodeValue(newEID);
				System.out.println("    >> Mapping old edge ID "+oldEID+" to "+newEID);
				newEdgeIdMap.put(oldEID,newEID);
			} 
			
			if (checkB) { // the edge ID is ok, but the vertex referenced by source, or target, or both is not
				
				System.out.println("    >> edge refs are bad at edge "+cur.getAttributes().getNamedItem("id").getNodeValue());
				correctEdgeReferences(cur);
				
				// correct references to invalid sources and targets
			}
			
			if (!checkA && !checkB) {
				// if neither of these tests failed, what's the problem?
				// error...
				errorMessages.append(">> error in Logic at Invalid Edges");
				System.out.println(">> error in Logic at Invalid Edges.  Continuing to next edge");
				continue;
			}
			
			// Everything should be OK now.
			if ( edgesByID.containsKey(oldEID)) {
				edgesByID.remove(oldEID);
				edgesByID.put(oldEID,cur);
				
			} else {
				
			
				edgesByID.put(cur.getAttributes().getNamedItem("id").getNodeValue(),cur);
			}
		}
		System.out.println(">> Done with graph structure!");
		System.out.println(">> vertices listed in HashMap: "+verticesByID.size());
		System.out.println(">> edges listed in HashMap: "+edgesByID.size());
		
    }
    
    private void correctEdgeReferences(Node n) {
    	Node nSourceNode = n.getAttributes().getNamedItem("source");
    	Node nTargetNode = n.getAttributes().getNamedItem("target");
    	String nSourceVal = nSourceNode.getNodeValue();
    	String nTargetVal = nTargetNode.getNodeValue();
    	
    	String tryLookup = null;
    	
    	int srcInt, trgInt;
    	
    	try {
    		srcInt = Integer.parseInt(nSourceVal);
    		if (srcInt <= 0) {
    			tryLookup = (String)newVertexIdMap.get(nSourceVal);
    			if (tryLookup != null ) {
        			
        			nSourceNode.setNodeValue(tryLookup);
        			System.out.println(">>reset node val");
        		}else {
        			errorMessages.append(">>ERROR:  lookup failed.1");
        		}
    		}
    		
    	} catch (NumberFormatException nfe) {
    		tryLookup = (String)newVertexIdMap.get(nSourceVal);
    		if (tryLookup != null ) {
    			
    			nSourceNode.setNodeValue(tryLookup);
    			System.out.println(">>reset node val");
    		}else {
    			errorMessages.append(">>ERROR:  lookup failed.2");
    		}
    	}
    	
    	tryLookup = null;
    	
    	try {
    		trgInt = Integer.parseInt(nTargetVal);
    		if (trgInt <=0) {
    			tryLookup = (String)newVertexIdMap.get(nTargetVal);

    			if (tryLookup != null ) {

    				nTargetNode.setNodeValue(tryLookup);
    				System.out.println(">>reset node val");
    			} else {
    				errorMessages.append(">>ERROR:  lookup failed.3");
    			}
    		}
    	} catch (NumberFormatException nfe) {
    		tryLookup = (String)newVertexIdMap.get(nTargetVal);
    		if (tryLookup != null ) {
    			
    			nTargetNode.setNodeValue(tryLookup);
    			System.out.println(">>reset node val");
    		} else {
    			errorMessages.append(">>ERROR:  lookup failed.4");
    		}
    	}
    	
    	tryLookup = null;
    	
    	
    	
    }
    
    private boolean edgeReferencesAreBad(Node n) {
    	
    	String snv = n.getAttributes().getNamedItem("source").getNodeValue();
    	String tnv = n.getAttributes().getNamedItem("target").getNodeValue();
    	int src,trg;
    	
    	try {
    		src = Integer.parseInt(snv);
    		trg = Integer.parseInt(tnv);
    	} catch (NumberFormatException nfe) {
    		return true;
    	}
    	
    	if (src <= 0 || trg <= 0) {
    		return true;
    	} else {
    		return false;
    	}
    	
    }
    
    /*
     * function returns true if the string passed is not convertible to an Integer,
     * or if the resulting Integer is <= 0.
     * @author Felix Terkhorn
     */
    private boolean edgeIdIsBad(String sid) {
    	
    	int id;
    	try {
    		
    		id = Integer.parseInt(sid);
    		if (id <= 0) {
    			return true;
    		} else {
    			return false;	
    		}
    		
    		
    	} catch (NumberFormatException nfe) {
    		return true;
    	}
    	
    }
    
    private String findUnusedVertexId () {
    	int z = 1;
    	while (verticesByID.get((new Integer(z)).toString()) != null ) {
    		z++;
    	}
    	
    	return (new Integer(z)).toString();
    }
    
    private String findUnusedEdgeId () {
    	int z = 1;
    	while (edgesByID.get((new Integer(z)).toString()) != null ) {
    		z++;
    	}
    	
    	return (new Integer(z)).toString();
    }
    
    
    private void processEdge(Node n) throws DOMAccessException {
    	NamedNodeMap nodeAttrMap = n.getAttributes();
    	Node idAttr;
    	String idVal;
    	Node sourceAttr;
    	Node targetAttr;
    	String sourceVal;
    	String targetVal;
    	
    	// children should correspond to keys given in edgeAttrMap.values()

    	if (nodeAttrMap == null ) {
    		//error
    		throw (new DOMAccessException("edge data has no attributes"));
    	}
    	
    	idAttr =nodeAttrMap.getNamedItem("id");
    	
    	
    	if (idAttr == null ) {
    		throw (new DOMAccessException("edge data has no ID attribute"));
    	}
    	
    	idVal = idAttr.getNodeValue();

    	if (idVal == null ) {
    		throw (new DOMAccessException("id attribute for this edge has no value"));
    	}
    	
    	// edges should have source and target
    	// if they don't, error
    	sourceAttr = nodeAttrMap.getNamedItem("source");
    	targetAttr = nodeAttrMap.getNamedItem("target");
    	if (sourceAttr == null ) {
    		throw (new DOMAccessException("edge data has no source attribute"));
    	}
    	
    	if (targetAttr == null) {
    		throw (new DOMAccessException("edge data has no target attribute"));
    	}
    	
    	sourceVal = sourceAttr.getNodeValue();
    	targetVal = targetAttr.getNodeValue();
    	
    	if (sourceVal == null ) {
    		throw (new DOMAccessException("source attribute for this edge has no value"));
    	}
    	
    	if (targetVal == null) {
    		throw (new DOMAccessException("target attribute for this edge has no value"));
    	}
    	
    	
    	
    	
    	// At this point, we know we have an edge with a reasonably well-defined ID, source, and target.
    	
    	if (checkBadEdge (n)) {
    		System.out.println(">> adding bad edge");
    		invalidEdges.add(n);  // if the edge has bad ID val, source val, or target val, push it onto the buffer
    		                      // we can revisit this edge and correct the offending values after HashMap newIDs is built
    		if (edgeReferencesAreBad(n) && !edgeIdIsBad(n.getAttributes().getNamedItem("id").getNodeValue())) {
    			edgesByID.put(idVal, n);
    			
    		}
    	} else {
//  		Add this edge to the class's private HashMap, edgesByID
//  		edgesByID:   String id -> Node edge
        	//System.out.println(">> this edge is ok, putting edge "+idVal);
    		edgesByID.put(idVal, n);	
    	}
    	
    	
    	
    //id->edge mapping succeeded
    	
    	
    }
    /*
     * checkBadEdge returns true if the given DOM Node representing this edge has:
     *     	  
     * An ID attr <= 0.
     * An ID that does not look like an integer.
     * A source attr <= 0.
     * A source that doesn't look like an int.
     * A target attr <= 0.
     * A target that doesn't look like an int.
     * 
     * @author Felix Terkhorn
     */
    private boolean checkBadEdge(Node n) {
    	int id,src,targ;
    	try {
    		
    		id = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());
    		src = Integer.parseInt(n.getAttributes().getNamedItem("source").getNodeValue());
    		targ = Integer.parseInt(n.getAttributes().getNamedItem("target").getNodeValue());
    	} catch (NumberFormatException nfe) {
    		return true;
    	}
    	if (id <= 0 || src <= 0 || targ <= 0) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    
    private void processVertex(Node n) throws DOMAccessException {
    	NamedNodeMap nodeAttrMap = n.getAttributes();
    	// children should correspond to keys given in nodeAttrMap.values()
    	//System.out.println(">> Entering processVertex with : "+n.getNodeName()+ "/"+ ((NamedNodeMap)n.getAttributes()).getNamedItem("id")+"which has "+((NodeList)n.getChildNodes()).getLength()+" children");

    	
    	Node idAttr;
    	String idVal;
    	
    	// children should correspond to keys given in edgeAttrMap.values()

    	if (nodeAttrMap == null ) {
    		//error
    		throw (new DOMAccessException("edge data has no attributes"));
    	}
    	
    	idAttr =nodeAttrMap.getNamedItem("id");
    	
    	
    	if (idAttr == null ) {
    		throw (new DOMAccessException("edge data has no ID attribute"));
    	}
    	
    	idVal = idAttr.getNodeValue();

    	if (idVal == null ) {
    		throw (new DOMAccessException("id attribute for this edge has no value"));
    	}
    	
    	// At this point, we know we have an edge with a reasonably well-defined ID, source, and target.

    	// We need to check for invalid IDs.  
    	// An ID is invalid if it is <= 0.
    	// An ID is invalid if it does not represent an integer.
    	
    	if (checkBadVertex(n)) {
    		// throw it on the buffer, where it will be processed later
    		invalidVertices.add(n);
    	} else {
    		
    		// Add this edge to the class's private HashMap, verticesByID
    		// verticesByID:   String id -> Node edge
    		verticesByID.put(idVal, n);

    		//System.out.println("    >> id->vertex mapping succeeded");

    	}
    }
    
    /*
     * checkBadVertex returns true if the given DOM Node representing this vertex has:
     *     	  
     * An ID <= 0.
     * An ID that does not look like an integer.
     * @author Felix Terkhorn
     */
    private boolean checkBadVertex(Node n) {
    	int i;
    	try {
    		
    		i = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());
    	} catch (NumberFormatException nfe) {
    		return true;
    	}
    	if (i <= 0) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
	private void processGraphKey(Node n) {
		NamedNodeMap keyAttrs;
		
		keyAttrs = n.getAttributes();
		Node forNode = keyAttrs.getNamedItem("for");
		Node attrNameNode = keyAttrs.getNamedItem("attr.name");
		Node attrTypeNode = keyAttrs.getNamedItem("attr.type");
		
		if (forNode != null) {
			//System.out.println("    >>for node's text content: "+forNode.getTextContent());
			if (attrNameNode != null && attrTypeNode != null) {
				if (forNode.getTextContent().equals("edge")) {
					// construct hashmap for edge attribute line
					if (attrNameNode.getTextContent().toLowerCase() == "source" || attrNameNode.getTextContent().toLowerCase() == "target") {
						edgeAttrMap.put(attrNameNode.getTextContent().toLowerCase(), attrTypeNode.getTextContent());
					} else {
						edgeAttrMap.put(attrNameNode.getTextContent(), attrTypeNode.getTextContent());
					}
					//System.out.println("      >> Looking up name node's value:  "+edgeAttrMap.get(attrNameNode.getTextContent()));
				} else if (forNode.getTextContent().equals("node")) {
					// construct hashmap for node attribute line
					if (attrNameNode.getTextContent().toLowerCase() == "id" || attrNameNode.getTextContent().toLowerCase() == "label") {
						vertexAttrMap.put(attrNameNode.getTextContent().toLowerCase(), attrTypeNode.getTextContent());
					} else {
						vertexAttrMap.put(attrNameNode.getTextContent(), attrTypeNode.getTextContent());
					}
				} else {
					System.out.println("      >>else???!!?!");
				}
			} else { // attrNameNode or attrTypeNode is null
				// this should be handled ok as it is
			}
			
		}
		
		

	}
    
    /**
     * Creates a temporary file for the NWB file
     * @return The temporary file
     */
	private File getTempFile(){
		File tempFile;
    
		String tempPath = System.getProperty("java.io.tmpdir");
		File tempDir = new File(tempPath+File.separator+"temp");
		if(!tempDir.exists())
			tempDir.mkdir();
		try{
			tempFile = File.createTempFile("NWB-Session-", ".nwb", tempDir);
		
		}catch (IOException e){
			logger.log(LogService.LOG_ERROR, e.toString());
			tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+"temp.nwb");

		}
		return tempFile;
	}
}