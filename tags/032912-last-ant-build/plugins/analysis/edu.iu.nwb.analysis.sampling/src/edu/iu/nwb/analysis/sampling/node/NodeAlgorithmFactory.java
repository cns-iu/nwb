package edu.iu.nwb.analysis.sampling.node;

import edu.iu.nwb.analysis.sampling.common.JungAlgorithmFactory;
import edu.iu.nwb.analysis.sampling.common.JungSampler;

public class NodeAlgorithmFactory extends JungAlgorithmFactory {

	protected JungSampler getSampler() {
		return new NodeSampler();
	}

}
