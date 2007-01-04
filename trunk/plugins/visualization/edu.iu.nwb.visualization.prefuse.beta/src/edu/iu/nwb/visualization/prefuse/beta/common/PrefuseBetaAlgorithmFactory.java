package edu.iu.nwb.visualization.prefuse.beta.common;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;

import prefuse.data.Graph;



/**
 * @author Weixia(Bonnie) Huang 
 */
public abstract class PrefuseBetaAlgorithmFactory implements AlgorithmFactory {

	private MetaTypeProvider provider;

	protected void activate(ComponentContext ctxt) {
		//You may delete all references to metatype service if 
		//your algorithm does not require parameters and return
		//null in the createParameters() method
		MetaTypeService mts = (MetaTypeService)ctxt.locateService("MTS");
		provider = mts.getMetaTypeInformation(ctxt.getBundleContext().getBundle());       
	}
	protected void deactivate(ComponentContext ctxt) {
		provider = null;
	}

	/**
	 * @see org.cishell.framework.algorithm.AlgorithmFactory#createParameters(org.cishell.framework.data.Data[])
	 */
	public MetaTypeProvider createParameters(Data[] dm) {
		return provider;
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
			if(label == null) {
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
