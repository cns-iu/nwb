package edu.iu.nwb.preprocessing.extractnodesandedges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;

public class AddNumericAttributeParamMutator {

	public static ObjectClassDefinition mutateForNodes(
			Graph graph, ObjectClassDefinition oldParameters) throws NoNumericAttributesException {
		String[] numericAttributes = extractNodeNumericAttributes(graph);

		return genericMutate(numericAttributes, oldParameters);
	}

	private static ObjectClassDefinition genericMutate(
			String[] numericAttributes, ObjectClassDefinition oldParameters) {
		BasicObjectClassDefinition newParameters;
		try {
			newParameters = new BasicObjectClassDefinition(oldParameters.getID(), oldParameters.getName(), oldParameters.getDescription(), oldParameters.getIcon(16));
		} catch (IOException e) {
			newParameters = new BasicObjectClassDefinition(oldParameters.getID(), oldParameters.getName(), oldParameters.getDescription(), null);
		}
		
		//make the new parameter object the same as the old one...
		//except fill the column name attribute with values for the numeric attributes in the table
		
		AttributeDefinition[] paramAttributes = oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL);
		for(int ii = 0; ii < paramAttributes.length; ii++) {
			AttributeDefinition paramAttribute = paramAttributes[ii];
			if(paramAttribute.getID().equals("numericAttribute")) {
				newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(paramAttribute.getID(), paramAttribute.getName(), paramAttribute.getDescription(), paramAttribute.getType()
								, numericAttributes , numericAttributes));
			}  else {
				newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED, paramAttributes[ii]);
			}
		}
		
		//return our new parameters
		return newParameters;
	}
	
	public static ObjectClassDefinition mutateForEdges(Graph graph, ObjectClassDefinition ocd)
			throws NoNumericAttributesException {
		String[] numericAttributes = extractEdgeNumericAttributes(graph);

		return genericMutate(numericAttributes, ocd);
	}
	
	private static String[] extractNodeNumericAttributes(Graph graph)
			throws NoNumericAttributesException {
		Set<String> numericAttributes = new HashSet<String>();

		for (Object nodeObject : graph.getVertices()) {
			Vertex node = (Vertex) nodeObject;

			for (Iterator nodeAttributes = node.getUserDatumKeyIterator();
					nodeAttributes.hasNext();) {
				Object key = nodeAttributes.next();
				// Make sure value is numeric before we add it.
				Object value = node.getUserDatum(key);

				if (value instanceof Number) {
					numericAttributes.add(key.toString());
				}
			}
		}
		
		if (numericAttributes.size() == 0) {
			throw new NoNumericAttributesException();
		}

		return numericAttributes.toArray(new String[0]);
	}
	
	private static String[] extractEdgeNumericAttributes(Graph graph)
			throws NoNumericAttributesException {
		// Get the attributes from the edges which are numeric, and not "target" or "source".
		List attributeList = new ArrayList();

		for (Object edgeObject : graph.getEdges()) {
			Edge edge = (Edge) edgeObject;
			
			for (Iterator numericAttributesIt = edge.getUserDatumKeyIterator();
					numericAttributesIt.hasNext(); ) {
				Object key = numericAttributesIt.next();

				if (!(key.equals("target") || key.equals("source"))) {
					// Make sure value is numeric before we add it.
					Object value = edge.getUserDatum(key);
					
					if (value instanceof Number) {
						attributeList.add(key);
					}
				}
			}
		}

		if (attributeList.size() == 0) {
			throw new NoNumericAttributesException();
		}

		// Convert the attributes to Strings.
		String[] numericAttributes = new String[attributeList.size()];

		for (int ii = 0; ii < attributeList.size(); ii++) {
			numericAttributes[ii] = attributeList.get(ii).toString();
		}

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
