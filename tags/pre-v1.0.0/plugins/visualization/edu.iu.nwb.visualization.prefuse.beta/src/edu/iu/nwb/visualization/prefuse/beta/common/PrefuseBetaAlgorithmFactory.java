package edu.iu.nwb.visualization.prefuse.beta.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicMetaTypeProvider;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeInformation;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.service.metatype.ObjectClassDefinition;


import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;




public abstract class PrefuseBetaAlgorithmFactory implements AlgorithmFactory {

	private MetaTypeInformation originalProvider;
	private String pid;

	protected void activate(ComponentContext ctxt) {
		//You may delete all references to metatype service if 
		//your algorithm does not require parameters and return
		//null in the createParameters() method
		MetaTypeService mts = (MetaTypeService)ctxt.locateService("MTS");
		pid = (String) ctxt.getServiceReference().getProperty(org.osgi.framework.Constants.SERVICE_PID);
		originalProvider = mts.getMetaTypeInformation(ctxt.getBundleContext().getBundle());
	}
	protected void deactivate(ComponentContext ctxt) {
		originalProvider = null;
	}

	private String[] createKeyArray(Schema schema) {
		String[] keys = new String[schema.getColumnCount() + 1];
		keys[0] = "";

		for(int ii = 1; ii <= schema.getColumnCount(); ii++) {
			keys[ii] = schema.getColumnName(ii - 1);
		}

		return keys;
	}

	/**
	 * @see org.cishell.framework.algorithm.AlgorithmFactory#createParameters(org.cishell.framework.data.Data[])
	 */
	public MetaTypeProvider createParameters(Data[] dm) {

		Graph graph = (Graph) dm[0].getData();

		String[] pids = originalProvider.getPids();
		Arrays.sort(pids);
		if(Arrays.binarySearch(pids, this.pid) >= 0) {
			ObjectClassDefinition oldDefinition = originalProvider.getObjectClassDefinition(this.pid, null);

			BasicObjectClassDefinition definition;
			try {
				definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), oldDefinition.getIcon(16));
			} catch (IOException e) {
				definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), null);
			}

			String[] nodeAttributesArray = createKeyArray(graph.getNodeTable().getSchema());
			String[] edgeAttributesArray = createKeyArray(graph.getEdgeTable().getSchema());

			//System.err.println(oldDefinition.getID());

			AttributeDefinition[] definitions = oldDefinition.getAttributeDefinitions(ObjectClassDefinition.ALL);

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

			MetaTypeProvider provider = new BasicMetaTypeProvider(definition);
			return provider;
		} else {
			return null;
		}
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
			Graph resultGraph = visualization.create(graph, parameters);
			if(resultGraph != null) {
				Data result = new BasicData(resultGraph, Graph.class.getName());
				return new Data[] { result };
			}
			return null;
		}
	}
}
