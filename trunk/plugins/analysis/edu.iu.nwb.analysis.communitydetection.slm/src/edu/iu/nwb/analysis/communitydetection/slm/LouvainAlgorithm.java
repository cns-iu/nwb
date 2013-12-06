package edu.iu.nwb.analysis.communitydetection.slm;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;

public class LouvainAlgorithm extends AbstractVosAlgorithm {
	public static class Factory extends AbstractVosAlgorithmFactory {
		@Override
		public Algorithm createAlgorithm(Data[] data,
				 Dictionary<String, Object> parameters,
				 CIShellContext ciShellContext) {
			parameters.put(AbstractVosAlgorithm.ALGORITHM_FIELD_ID, "Louvain Algorithm");
			return new LouvainAlgorithm(data, parameters, ciShellContext);
		}
	}
	
	public LouvainAlgorithm(Data[] data, Dictionary<String, Object> parameters,
			CIShellContext ciShellContext) {
		super(data, parameters, ciShellContext);
	}
}