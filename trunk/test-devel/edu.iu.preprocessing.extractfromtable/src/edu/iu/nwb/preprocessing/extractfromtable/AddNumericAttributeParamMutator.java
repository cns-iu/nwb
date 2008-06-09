package edu.iu.nwb.preprocessing.extractfromtable;

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
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;

public class AddNumericAttributeParamMutator {

	public static ObjectClassDefinition mutateForTopNodesOrEdges(Graph graph, ObjectClassDefinition ocd) throws AlgorithmExecutionException {
		String[] numericAttributes = extractNumericAttributes(graph);
		System.out.println("EXTRACTED NUMERIC ATTRIBUTES");
		
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
	
	private static String[] extractNumericAttributes(Graph g) throws AlgorithmExecutionException {
		//obtain a vertex to extract attribute information from
		Vertex exemplaryVertex = extractExemplaryVertex(g);
		
		//get the attributes from the vertex
		Iterator numericAttributesIt = exemplaryVertex.getUserDatumKeyIterator();
		List attributeList = new ArrayList();
		while (numericAttributesIt.hasNext()) {
			attributeList.add(numericAttributesIt.next());
		}
		
		//convert the attributes to Strings
		String[] numericAttributes = new String[attributeList.size()];
		for (int ii = 0; ii < attributeList.size(); ii++) {
			numericAttributes[ii] = attributeList.get(ii).toString();
		}
		
		//return the attributes
		return numericAttributes;
	}
	
	private static Vertex extractExemplaryVertex(Graph g) throws AlgorithmExecutionException {
		Vertex exemplaryVertex = null;
		Iterator vertexIt = g.getVertices().iterator();
		if (vertexIt.hasNext()) {
			exemplaryVertex = (Vertex) vertexIt.next();
			return exemplaryVertex;
		} else {
			throw new AlgorithmExecutionException("Cannot determine node attributes if there are no attributes in the graph.");
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
