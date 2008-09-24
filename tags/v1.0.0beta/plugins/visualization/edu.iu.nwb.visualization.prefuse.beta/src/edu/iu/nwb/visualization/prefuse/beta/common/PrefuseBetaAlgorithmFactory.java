package edu.iu.nwb.visualization.prefuse.beta.common;

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
import prefuse.data.Schema;




public abstract class PrefuseBetaAlgorithmFactory implements AlgorithmFactory, ParameterMutator {


	private String[] createKeyArray(Schema schema) {
		String[] keys = new String[schema.getColumnCount() + 1];
		keys[0] = "";

		for(int ii = 1; ii <= schema.getColumnCount(); ii++) {
			keys[ii] = schema.getColumnName(ii - 1);
		}

		return keys;
	}

	

	/**
	 * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
	 */
	public Algorithm createAlgorithm(Data[] dm, Dictionary parameters,
			CIShellContext context) {
		return new VisualizationAlgorithm(dm, parameters, context, getVisualization());
	}

	protected abstract PrefuseBetaVisualization getVisualization() ;

	private class VisualizationAlgorithm implements Algorithm {
		Data[] dm;
		Dictionary parameters;
		CIShellContext ciContext;
		private PrefuseBetaVisualization visualization;

		public VisualizationAlgorithm(Data[] dm, Dictionary parameters,
				CIShellContext ciContext, PrefuseBetaVisualization visualization) {
			this.dm = dm;
			this.parameters = parameters;
			this.ciContext = ciContext;
			this.visualization = visualization;
		}

		public Data[] execute() {
			Graph graph = (Graph) dm[0].getData();
			Object label = parameters.get(Constants.label);
			if(label == null  || "".equals(label)) {
				parameters.put(Constants.label, Constants.label);
			}
			visualization.create(graph, parameters);
			return null;
		}
	}

	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {
		Graph graph = (prefuse.data.Graph) data[0].getData();
		
		BasicObjectClassDefinition definition;
		try {
			definition = new BasicObjectClassDefinition(parameters.getID(), parameters.getName(), parameters.getDescription(), parameters.getIcon(16));
		} catch (IOException e) {
			definition = new BasicObjectClassDefinition(parameters.getID(), parameters.getName(), parameters.getDescription(), null);
		}

		String[] nodeAttributesArray = createKeyArray(graph.getNodeTable().getSchema());
		String[] edgeAttributesArray = createKeyArray(graph.getEdgeTable().getSchema());

		//System.err.println(oldDefinition.getID());

		AttributeDefinition[] definitions = parameters.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for(int ii = 0; ii < definitions.length; ii++) {
			String id = definitions[ii].getID();
			//System.err.println(id);
			if(id.equals("nodeSize")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition("nodeSize", "Node Size", "The label for the node size property", AttributeDefinition.STRING, nodeAttributesArray, nodeAttributesArray));
			} else if(id.equals("edgeSize")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition("edgeSize", "Edge Size", "The label for the edge size property", AttributeDefinition.STRING, edgeAttributesArray, edgeAttributesArray));
			} else if(id.equals("nodeColor")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition("nodeColor", "Node Color", "The label for the node color property", AttributeDefinition.STRING, nodeAttributesArray, nodeAttributesArray));
			} else if(id.equals("edgeColor")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition("edgeColor", "Edge Color", "The label for the edge color property", AttributeDefinition.STRING, edgeAttributesArray, edgeAttributesArray));
			} else if(id.equals("ringColor")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition("ringColor", "Ring Color", "The label for the ring color property", AttributeDefinition.STRING, nodeAttributesArray, nodeAttributesArray));
			} else if(id.equals("nodeShape")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition("nodeShape", "Node Shape", "The label for the node shape property", AttributeDefinition.STRING, nodeAttributesArray, nodeAttributesArray));
			} else {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, definitions[ii]);
			}
		}

		return definition;
	}
}
