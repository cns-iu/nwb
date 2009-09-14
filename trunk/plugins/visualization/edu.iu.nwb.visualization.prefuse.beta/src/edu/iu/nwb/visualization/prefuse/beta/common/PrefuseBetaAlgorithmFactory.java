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
		private Data[] dm;
		private Dictionary parameters;
		private PrefuseBetaVisualization visualization;

		public VisualizationAlgorithm(Data[] dm, Dictionary parameters,
				CIShellContext ciContext, PrefuseBetaVisualization visualization) {
			this.dm = dm;
			this.parameters = parameters;
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
	
	protected AttributeDefinition[] createAttributeDefinitions(AttributeDefinition[] oldAttributeDefinitions,
			   												   String[] nodeAttributesArray,
			   												   String[] edgeAttributesArray)
	{
		final int numAttributeDefinitions = oldAttributeDefinitions.length;
		AttributeDefinition[] newAttributeDefinitions = new AttributeDefinition [numAttributeDefinitions];
		
		for (int ii = 0; ii < numAttributeDefinitions; ii++)
		{
			String id = oldAttributeDefinitions[ii].getID();
			
			if(id.equals(Constants.nodeSizeField))
			{
				newAttributeDefinitions[ii] = new BasicAttributeDefinition(Constants.nodeSizeField,
					"Node Size", "The label for the node size property", AttributeDefinition.STRING,
					nodeAttributesArray, nodeAttributesArray);
			}
			else if(id.equals(Constants.edgeSizeField))
			{
				newAttributeDefinitions[ii] = new BasicAttributeDefinition(Constants.edgeSizeField,
					"Edge Size", "The label for the edge size property",
					AttributeDefinition.STRING, edgeAttributesArray, edgeAttributesArray);
			}
			else if(id.equals(Constants.nodeColorField))
			{
				newAttributeDefinitions[ii] = new BasicAttributeDefinition(Constants.nodeColorField,
					"Node Color", "The label for the node color property",
					AttributeDefinition.STRING, nodeAttributesArray, nodeAttributesArray);
			}
			else if(id.equals(Constants.edgeColorField))
			{
				newAttributeDefinitions[ii] = new BasicAttributeDefinition(Constants.edgeColorField,
					"Edge Color", "The label for the edge color property",
					AttributeDefinition.STRING, edgeAttributesArray, edgeAttributesArray);
			}
			else if(id.equals(Constants.ringColorField))
			{
				newAttributeDefinitions[ii] = new BasicAttributeDefinition(Constants.ringColorField,
					"Ring Color", "The label for the ring color property",
					AttributeDefinition.STRING, nodeAttributesArray, nodeAttributesArray);
			}
			else if(id.equals(Constants.nodeShapeField))
			{
				newAttributeDefinitions[ii] = new BasicAttributeDefinition(Constants.nodeShapeField,
					"Node Shape", "The label for the node shape property",
					AttributeDefinition.STRING, nodeAttributesArray, nodeAttributesArray);
			}
			else
				newAttributeDefinitions[ii] = oldAttributeDefinitions[ii];
		}
		
		return newAttributeDefinitions;
	}

	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters)
	{
		Graph graph = (prefuse.data.Graph) data[0].getData();
		BasicObjectClassDefinition objectClassDefinition;
		
		try
		{
			objectClassDefinition = new BasicObjectClassDefinition(parameters.getID(),
					parameters.getName(), parameters.getDescription(), parameters.getIcon(16));
		}
		catch (IOException e)
		{
			objectClassDefinition = new BasicObjectClassDefinition(parameters.getID(),
					parameters.getName(), parameters.getDescription(), null);
		}

		String[] nodeAttributesArray = createKeyArray(graph.getNodeTable().getSchema());
		String[] edgeAttributesArray = createKeyArray(graph.getEdgeTable().getSchema());

		AttributeDefinition[] oldRequiredAttributes =
			parameters.getAttributeDefinitions(ObjectClassDefinition.REQUIRED);	
		AttributeDefinition[] newRequiredAttributes =
			createAttributeDefinitions(oldRequiredAttributes, nodeAttributesArray, edgeAttributesArray);
		
		AttributeDefinition[] oldOptionalAttributes =
			parameters.getAttributeDefinitions(ObjectClassDefinition.OPTIONAL);
		AttributeDefinition[] newOptionalAttributes =
			createAttributeDefinitions(oldOptionalAttributes, nodeAttributesArray, edgeAttributesArray);
		
		for (int ii = 0; ii < newRequiredAttributes.length; ii++)
			objectClassDefinition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, newRequiredAttributes[ii]);
		
		for (int ii = 0; ii < newOptionalAttributes.length; ii++)
			objectClassDefinition.addAttributeDefinition(ObjectClassDefinition.OPTIONAL, newOptionalAttributes[ii]);

		return objectClassDefinition;
	}
}
