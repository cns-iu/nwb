package edu.iu.nwb.preprocessing.extractnodesandedges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;

public class AddNumericAttributeParamMutator {

	public static ObjectClassDefinition mutateForNodes(Graph graph, ObjectClassDefinition ocd) throws NoNumericAttributesException {
		String[] numericAttributes = extractNodeNumericAttributes(graph);
		return genericMutate(numericAttributes, ocd);
	}
	
	
	
	
	private static ObjectClassDefinition genericMutate(String[] numericAttributes, ObjectClassDefinition ocd) {
		BasicObjectClassDefinition newOCD;
		try {
			newOCD = new BasicObjectClassDefinition(ocd.getID(), ocd.getName(), ocd.getDescription(), ocd.getIcon(16));
		} catch (IOException e) {
			newOCD = new BasicObjectClassDefinition(ocd.getID(), ocd.getName(), ocd.getDescription(), null);
		}
		
		//make the new parameter object the same as the old one...
		//except fill the column name attribute with values for the numeric attributes in the table
		
		AttributeDefinition[] paramAttributes = ocd.getAttributeDefinitions(ObjectClassDefinition.ALL);
		for(int ii = 0; ii < paramAttributes.length; ii++) {
			AttributeDefinition paramAttribute = paramAttributes[ii];
			if(paramAttribute.getID().equals("numericAttribute")) {
				newOCD.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(paramAttribute.getID(), paramAttribute.getName(), paramAttribute.getDescription(), paramAttribute.getType()
								, numericAttributes , numericAttributes));
			}  else {
				newOCD.addAttributeDefinition(ObjectClassDefinition.REQUIRED, paramAttributes[ii]);
			}
		}
		
		//return our new parameters
		return newOCD;
	}
	
	public static ObjectClassDefinition mutateForEdges(Graph graph, ObjectClassDefinition ocd) throws NoNumericAttributesException {
		String[] numericAttributes = extractEdgeNumericAttributes(graph);
		return genericMutate(numericAttributes, ocd);
	}
	
	private static String[] extractNodeNumericAttributes(Graph g) throws NoNumericAttributesException {
		//obtain a vertex to extract attribute information from
		Vertex exemplaryVertex = extractExemplaryVertex(g);
		
		//get the attributes from the vertex which are numeric
		Iterator numericAttributesIt = exemplaryVertex.getUserDatumKeyIterator();
		List attributeList = new ArrayList();
		while (numericAttributesIt.hasNext()) {
			Object key = numericAttributesIt.next();
			//make sure value is numeric before we add it
			Object value = exemplaryVertex.getUserDatum(key);
			if (value instanceof Number) {
				attributeList.add(key);
			}
		}
		
		if (attributeList.size() == 0) throw new NoNumericAttributesException();
		
		//convert the attributes to Strings
		String[] numericAttributes = new String[attributeList.size()];
		for (int ii = 0; ii < attributeList.size(); ii++) {
			numericAttributes[ii] = attributeList.get(ii).toString();
		}
		
		//return the attributes
		return numericAttributes;
	}
	
	private static String[] extractEdgeNumericAttributes(Graph g) throws NoNumericAttributesException {
		//obtain a vertex to extract attribute information from
		Edge exemplaryEdge = extractExemplaryEdge(g);
		System.out.println("Got the edge");
		//get the attributes from the edges which are numeric, and not "target" or "source"
		Iterator numericAttributesIt = exemplaryEdge.getUserDatumKeyIterator();
		List attributeList = new ArrayList();
		while (numericAttributesIt.hasNext()) {
			Object key = numericAttributesIt.next();
			if (! (key.equals("target") || key.equals("source"))) {
			//make sure value is numeric before we add it
			Object value = exemplaryEdge.getUserDatum(key);
			if (value instanceof Number) {
				attributeList.add(key);
			}
			}
		}
		System.out.println("attributeListSize: " + attributeList.size());
		if (attributeList.size() == 0) throw new NoNumericAttributesException();
		//convert the attributes to Strings
		String[] numericAttributes = new String[attributeList.size()];
		for (int ii = 0; ii < attributeList.size(); ii++) {
			numericAttributes[ii] = attributeList.get(ii).toString();
		}
		
		//return the attributes
		return numericAttributes;
	}
	
	private static Vertex extractExemplaryVertex(Graph g) throws NoNumericAttributesException {
		Vertex exemplaryVertex = null;
		Iterator vertexIt = g.getVertices().iterator();
		if (vertexIt.hasNext()) {
			exemplaryVertex = (Vertex) vertexIt.next();
			return exemplaryVertex;
		} else {
			throw new NoNumericAttributesException();
		}
		
	}
	
	
	private static Edge extractExemplaryEdge(Graph g) throws NoNumericAttributesException {
		Edge exemplaryEdge = null;
		Iterator edgeIt = g.getEdges().iterator();
		if (edgeIt.hasNext()) {
			exemplaryEdge = (Edge) edgeIt.next();
			return exemplaryEdge;
		} else {
			throw new NoNumericAttributesException();
		}
		
	}
	
	  private static void inspect(Graph g) {
	    	Set vertices = g.getVertices();
	    	int index = 0;
	    	for (Iterator vIt = vertices.iterator(); vIt.hasNext();) {
	    		Vertex v = (Vertex) vIt.next();
	    		System.out.println("Vertex " + index);
	    		Iterator userDatumKeys = v.getUserDatumKeyIterator();
	    		while (userDatumKeys.hasNext()) {
	    			Object key = (Object) userDatumKeys.next();
	    			Object val = v.getUserDatum(key);
	    			System.out.println("" + key.toString() + " :" + val.toString());
	    		}
	    		index++;
	    	}
	    }
	  
	public static ObjectClassDefinition mutateForTopNodesOrEdges(Table table, ObjectClassDefinition ocd) {
		String[] attributes = GraphUtil.getColumnNames(table);
		String[] numericAttributes = filterNumericAttributes(table, attributes);
		//make a new parameter object, so far identical to the old one.
		
		BasicObjectClassDefinition newOCD;
		try {
			newOCD = new BasicObjectClassDefinition(ocd.getID(), ocd.getName(), ocd.getDescription(), ocd.getIcon(16));
		} catch (IOException e) {
			newOCD = new BasicObjectClassDefinition(ocd.getID(), ocd.getName(), ocd.getDescription(), null);
		}
		
		//make the new parameter object the same as the old one...
		//except fill the column name attribute with values for the numeric attributes in the table
		
		AttributeDefinition[] paramAttributes = ocd.getAttributeDefinitions(ObjectClassDefinition.ALL);
		for(int ii = 0; ii < paramAttributes.length; ii++) {
			AttributeDefinition paramAttribute = paramAttributes[ii];
			if(paramAttribute.getID().equals("numericAttribute")) {
				newOCD.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(paramAttribute.getID(), paramAttribute.getName(), paramAttribute.getDescription(), paramAttribute.getType()
								, numericAttributes , numericAttributes));
			}  else {
				newOCD.addAttributeDefinition(ObjectClassDefinition.REQUIRED, paramAttributes[ii]);
			}
		}
		
		//return our new parameters
		return newOCD;
	}
	
	private static String[] filterNumericAttributes(Table nodeTable, String[] nodeAttributes) {
		List numericAttributes = new ArrayList();
		for (int ii = 0; ii < nodeAttributes.length; ii++) {
			if (nodeTable.canGet(nodeAttributes[ii], Number.class)) {
				numericAttributes.add(nodeAttributes[ii]);
			}
		}
		return (String[]) numericAttributes.toArray(new String[numericAttributes.size()]);
		}
}
