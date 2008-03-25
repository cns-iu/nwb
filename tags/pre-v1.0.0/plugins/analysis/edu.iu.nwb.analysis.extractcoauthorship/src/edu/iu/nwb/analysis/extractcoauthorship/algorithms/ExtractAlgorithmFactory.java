package edu.iu.nwb.analysis.extractcoauthorship.algorithms;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.metatype.MetaTypeProvider;

public class ExtractAlgorithmFactory implements AlgorithmFactory {
	// private BundleContext bContext;

	public Algorithm createAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		return new ExtractAlgorithm(data, parameters, context);
	}

	public MetaTypeProvider createParameters(Data[] data) {
		// TODO Auto-generated method stub
		return null;
	}

}
