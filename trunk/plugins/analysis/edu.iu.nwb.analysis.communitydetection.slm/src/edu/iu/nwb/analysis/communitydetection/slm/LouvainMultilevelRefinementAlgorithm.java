package edu.iu.nwb.analysis.communitydetection.slm;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;

public class LouvainMultilevelRefinementAlgorithm extends AbstractVosAlgorithm {
	public static class Factory extends AbstractVosAlgorithmFactory {
		@Override
		public Algorithm createAlgorithm(Data[] data,
				 Dictionary<String, Object> parameters,
				 CIShellContext ciShellContext) {
			parameters.put(AbstractVosAlgorithm.ALGORITHM_FIELD_ID, "Louvain Agorithm With Multilevel Refinement");
			return new LouvainMultilevelRefinementAlgorithm(data, parameters, ciShellContext);
		}
	}
	
	public LouvainMultilevelRefinementAlgorithm(Data[] data, Dictionary<String, Object> parameters,
			CIShellContext ciShellContext) {
		super(data, parameters, ciShellContext);
	}
}