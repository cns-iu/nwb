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

		BigInteger test1 = new BigInteger("2");
		BigInteger test2 = new BigInteger("3");
		
		BigInteger test3 = test1.subtract(test2);
		System.out.println(test3.toString(4));
		
		String functionLabel = this.parameters.get(ParameterDefinitions.FUNCTIONID).toString();
		String nodeLabel = this.parameters.get(ParameterDefinitions.NODELABEL).toString();
		int nodeStates = new Integer(this.parameters.get(ParameterDefinitions.NODESTATES).toString()).intValue();
		boolean isPolynomial = new Boolean(this.parameters.get(ParameterDefinitions.INPUTFORMAT).toString()).booleanValue();
		try{
		dependencyGraph = ParseDependencyGraphs.constructDependencyGraph(functionLabel,nodeLabel,functionTable);
		pseudoGraph = ParseDependencyGraphs.constructPseudoGraph(functionLabel, nodeLabel, functionTable);
		}catch(FunctionFormatException ffe){
			throw new AlgorithmExecutionException(ffe.getMessage());
		}
		File stateSpace = CreateStateSpaceGraph.createStateSpace(dependencyGraph, nodeStates, isPolynomial);
		
		final Data outputData1 = constructData(dependencyGraph,prefuse.data.Graph.class.getName(),DataProperty.NETWORK_TYPE,"Created Dependency Graph");
		final Data outputData2 = constructData(pseudoGraph,prefuse.data.Graph.class.getName(),DataProperty.NETWORK_TYPE,"Created Dependency Graph with Function Pseudonodes");
		final Data outputData3 = constructData(stateSpace,NWBFileProperty.NWB_MIME_TYPE,DataProperty.NETWORK_TYPE,"Generated State Space Graph");
	
		
		return new Data[] {outputData1, outputData2, outputData3};
	}

	private void validateParameters(){
		
	}
	
	private static Data constructData(Object obj, String className, String type, String label){
		Data outputData = new BasicData(obj,className);
		Dictionary dataAttributes = outputData.getMetadata();
		dataAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		dataAttributes.put(DataProperty.TYPE, type);
		dataAttributes.put(DataProperty.LABEL,label);
		
		return outputData;
	}
	
}