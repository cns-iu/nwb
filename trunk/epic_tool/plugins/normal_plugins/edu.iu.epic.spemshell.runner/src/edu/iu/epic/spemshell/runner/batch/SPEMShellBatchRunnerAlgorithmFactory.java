package edu.iu.epic.spemshell.runner.batch;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.epic.spemshell.runner.single.SPEMShellSingleRunnerAlgorithmFactory;

public class SPEMShellBatchRunnerAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	public static final String NUMBER_OF_RUNS_PARAMETER_ID = "numberOfRuns";
	
	private static BundleContext bundleContext;
	protected void activate(ComponentContext componentContext) {
		SPEMShellBatchRunnerAlgorithmFactory.bundleContext =
			componentContext.getBundleContext();
	}
	protected static BundleContext getBundleContext() {
		return bundleContext;
	}
	
	
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
		return new SPEMShellBatchRunnerAlgorithm(data, parameters, context, getBundleContext());
	}

	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		ParameterMutator singleAlgorithmFactory = new SPEMShellSingleRunnerAlgorithmFactory();
		ObjectClassDefinition singleParameters =
			singleAlgorithmFactory.mutateParameters(data, oldParameters);

		return singleParameters;
	}
}
