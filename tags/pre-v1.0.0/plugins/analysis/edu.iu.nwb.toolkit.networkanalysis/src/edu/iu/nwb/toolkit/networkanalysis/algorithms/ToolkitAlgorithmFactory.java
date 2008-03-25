package edu.iu.nwb.toolkit.networkanalysis.algorithms;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.metatype.MetaTypeProvider;

import edu.iu.nwb.toolkit.networkanalysis.utilities.Util;

public class ToolkitAlgorithmFactory implements AlgorithmFactory{

	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		Util.getInstance().setContext(context);
		return new ToolkitAlgorithm(data,parameters,context);
	}

	public MetaTypeProvider createParameters(Data[] data) {
		// TODO Auto-generated method stub
		return null;
	}

}
