package edu.iu.nwb.analysis.discretenetworkdynamics;

import java.io.File;
import java.math.BigInteger;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.Table;
import edu.iu.nwb.analysis.discretenetworkdynamics.components.CreateStateSpaceGraph;
import edu.iu.nwb.analysis.discretenetworkdynamics.components.ParseDependencyGraphs;
import edu.iu.nwb.analysis.discretenetworkdynamics.parser.FunctionFormatException;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;


public class DVDAlgorithm implements Algorithm {
	Data[] data;
	Dictionary parameters;
	CIShellContext context;
	LogService logger;


	public DVDAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;
		this.logger = (LogService)context.getService(LogService.class.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException{
		final Table functionTable = (Table)this.data[0].getData();
		Graph dependencyGraph;
		Graph pseudoGraph;
		File stateSpace;

		

		String functionLabel = this.getParameter(ParameterDefinitions.FUNCTIONID);
		String nodeLabel = this.getParameter(ParameterDefinitions.NODELABEL);
		String nodeStates = this.getParameter(ParameterDefinitions.NODESTATES);
		String inputFormat = this.getParameter(ParameterDefinitions.INPUTFORMAT);
		String updateScheme = this.getParameter(ParameterDefinitions.UPDATESCHEME);
		String updateSchedule = this.getParameter(ParameterDefinitions.UPDATESCHEDULE);
		String stateSpaceSpec = this.getParameter(ParameterDefinitions.STATESPACESPEC);
		String initialCondition = this.getParameter(ParameterDefinitions.INITIALCONDITION);
		
		int numberOfStates = new Integer(nodeStates).intValue();
		if(numberOfStates < 2){
			throw new AlgorithmExecutionException("There must be at least two states to correctly analyze these expressions.\n");
		}	
		
		
		boolean isPolynomial = intStringToBoolean(inputFormat);
		if(!isPolynomial && numberOfStates != 2){
				logger.log(LogService.LOG_INFO, "Changing number of node states to 2.\n");	
			numberOfStates = 2;
		}
		
		
		boolean isSynchronous = intStringToBoolean(updateScheme);
		String[] schedule = null;
		if(!isSynchronous){
			if(updateSchedule == null){
				throw new AlgorithmExecutionException("Sequential updating requires an update schedule.\n");
			}
			schedule = updateSchedule.split("\\s+");
		}
		
		boolean evaluateAll = intStringToBoolean(stateSpaceSpec);
		String[] initCondition = null;
		if(!evaluateAll){
			if(initialCondition == null){
				throw new AlgorithmExecutionException("Evaluating a single trajectory requires an initial condition to be specified.\n");
			}
			initCondition = initialCondition.split("\\s+");
		}
		
		try{
			dependencyGraph = ParseDependencyGraphs.constructDependencyGraph(functionLabel,nodeLabel,functionTable);
			pseudoGraph = ParseDependencyGraphs.constructPseudoGraph(functionLabel, nodeLabel, functionTable);
			stateSpace = CreateStateSpaceGraph.createStateSpace(dependencyGraph, numberOfStates, functionLabel, isPolynomial);
		}catch(FunctionFormatException ffe){
			throw new AlgorithmExecutionException(ffe.getMessage());
		}

		final Data outputData1 = constructData(dependencyGraph,prefuse.data.Graph.class.getName(),DataProperty.NETWORK_TYPE,"Created Dependency Graph");
		final Data outputData2 = constructData(pseudoGraph,prefuse.data.Graph.class.getName(),DataProperty.NETWORK_TYPE,"Created Dependency Graph with Function Pseudonodes");
		final Data outputData3 = constructData(stateSpace,NWBFileProperty.NWB_MIME_TYPE,DataProperty.NETWORK_TYPE,"Generated State Space Graph");


		return new Data[] {outputData1, outputData2, outputData3};
	}

	private static Data constructData(Object obj, String className, String type, String label){
		Data outputData = new BasicData(obj,className);
		Dictionary dataAttributes = outputData.getMetadata();
		dataAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		dataAttributes.put(DataProperty.TYPE, type);
		dataAttributes.put(DataProperty.LABEL,label);

		return outputData;
	}
	
	private String getParameter(String s){
		return this.parameters.get(s).toString();
	}
	
	private static boolean intStringToBoolean(String s){
		int value = new Integer(s).intValue();
		if(value >= 1){
			return true;
		}
		return false;
	}

}