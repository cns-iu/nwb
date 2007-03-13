package edu.iu.nwb.analysis.sampling.common;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;

import edu.uci.ics.jung.graph.Graph;


public abstract class JungAlgorithmFactory implements AlgorithmFactory {

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
		return new SamplingAlgorithm(dm, parameters, context, getSampler());
	}

	protected abstract JungSampler getSampler();

	private class SamplingAlgorithm implements Algorithm {
		Data[] dm;
		Dictionary parameters;
		CIShellContext ciContext;
		private JungSampler sampler;

		public SamplingAlgorithm(Data[] dm, Dictionary parameters,
				CIShellContext ciContext, JungSampler sampler) {
			this.dm = dm;
			this.parameters = parameters;
			this.ciContext = ciContext;
			this.sampler = sampler;
		}

		public Data[] execute() {
			Graph graph = (Graph) dm[0].getData();
			Graph outputGraph = sampler.sample(graph, this.parameters);
			Data outputData = new BasicData(outputGraph, Graph.class.getName());
			
			
			Dictionary map = outputData.getMetaData();
    		map.put(DataProperty.MODIFIED,
                    new Boolean(true));
            map.put(DataProperty.PARENT, dm[0]);
            map.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
            map.put(DataProperty.LABEL, sampler.getSampleName());
			return new Data[] {outputData};
		}
	}
}
