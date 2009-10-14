package edu.iu.nwb.preprocessing.duplicatenodedetector;

import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Graph;
import prefuse.data.Table;
import edu.iu.nwb.preprocessing.duplicatenodedetector.util.GraphUtil;


public class DuplicateNodeDetectorAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		return new DuplicateNodeDetectorAlgorithm(data, parameters);
	}

	//allow the user to pick a node attribute from those available on the graph node.
	//Use the contents of this attribute for each node to determine node's similarities to one another
	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {

		//extract node attributes from the input graph

		Graph inputGraph = (Graph) data[0].getData();
		Table nodeTable = inputGraph.getNodeTable();
		String[] nodeAttributes = GraphUtil.getColumnNames(nodeTable);

		//make a new parameter object, so far identical to the old one.

		BasicObjectClassDefinition newParameters;
		try {
			newParameters = new BasicObjectClassDefinition(parameters.getID(), parameters.getName(), parameters.getDescription(), parameters.getIcon(16));
		} catch (IOException e) {
			newParameters = new BasicObjectClassDefinition(parameters.getID(), parameters.getName(), parameters.getDescription(), null);
		}

		//make the new parameter object the same as the old one...
		//except fill the column name attribute with values for all the node attributes in the graph

		AttributeDefinition[] paramAttributes = parameters.getAttributeDefinitions(ObjectClassDefinition.ALL);
		for(int ii = 0; ii < paramAttributes.length; ii++) {
			AttributeDefinition paramAttribute = paramAttributes[ii];
			if(paramAttribute.getID().equals("compareAttribute")) {
				newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(paramAttribute.getID(), paramAttribute.getName(), paramAttribute.getDescription(), paramAttribute.getType()
								, nodeAttributes, nodeAttributes));
			}  else {
				newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED, paramAttributes[ii]);
			}
		}

		//return our new parameters

		return newParameters;
	}
}