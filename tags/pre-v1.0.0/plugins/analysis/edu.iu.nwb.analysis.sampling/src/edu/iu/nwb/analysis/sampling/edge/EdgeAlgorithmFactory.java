package edu.iu.nwb.analysis.sampling.edge;

import edu.iu.nwb.analysis.sampling.common.JungAlgorithmFactory;
import edu.iu.nwb.analysis.sampling.common.JungSampler;

public class EdgeAlgorithmFactory extends JungAlgorithmFactory {

	protected JungSampler getSampler() {
		return new EdgeSampler();
	}

}
