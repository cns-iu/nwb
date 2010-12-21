package edu.iu.cns.r;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

import edu.iu.cns.r.utility.RInstance;

public class RunRAlgorithmFactory implements AlgorithmFactory {
	private LogService logger;

	protected void activate(ComponentContext componentContext) {
		this.logger = (LogService) componentContext.locateService("LOG");
	}

	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		RInstance rInstance = (RInstance) data[0].getData();

		return new RunRAlgorithm(rInstance, this.logger);
	}
}