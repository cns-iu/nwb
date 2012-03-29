package edu.iu.nwb.analysis.sampling.snowball;

import edu.iu.nwb.analysis.sampling.common.JungAlgorithmFactory;
import edu.iu.nwb.analysis.sampling.common.JungSampler;

public class SnowballAlgorithmFactory extends JungAlgorithmFactory {

	protected JungSampler getSampler() {
		return new SnowballSampler();
	}

}
