package edu.iu.nwb.analysis.discretenetworkdynamics;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import edu.iu.nwb.analysis.discretenetworkdynamics.components.CreateStateSpaceGraph;
import edu.iu.nwb.analysis.discretenetworkdynamics.components.ParseDependencyGraphs;
import edu.iu.nwb.analysis.discretenetworkdynamics.parser.FunctionFormatException;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;


public class DVDAlgorithm implements Algorithm, ProgressTrackable {
	Data[] data;
	Dictionary parameters;
	CIShellContext context;
	LogService logger;
	ProgressMonitor monitor;

	public ProgressMonitor getProgressMonitor() {
		// TODO Auto-generated method stub
		return this.monitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		this.monitor = monitor;
	}

	public DVDAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;
		this.logger = (LogService)context.getService(LogService.class.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException{
		final Table functionTable = (Table)this.data[0].getData();
		CreateStateSpaceGraph cssg;
		Graph dependencyGraph;
		Graph pseudoGraph;
		Graph stateSpace;


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
		String[] schedule = handleOptionalData(isSynchronous, updateSchedule, "Sequential updating requires an update schedule.\n");


		boolean evaluateAll = intStringToBoolean(stateSpaceSpec);
		String[] initCondition = handleOptionalData(evaluateAll,initialCondition,"Evaluating a single trajectory requires an initial condition to be specified.\n");

		

		try{
			dependencyGraph = ParseDependencyGraphs.constructDependencyGraph(functionLabel,nodeLabel,functionTable);
			monitor.start(ProgressMonitor.WORK_TRACKABLE, (int)(Math.pow(numberOfStates,dependencyGraph.getNodeCount())));
			pseudoGraph = ParseDependencyGraphs.constructPseudoGraph(functionLabel, nodeLabel, functionTable);
			final Data outputData1 = constructData(this.data[0],dependencyGraph,prefuse.data.Graph.class.getName(),DataProperty.NETWORK_TYPE,"Created Dependency Graph");
			final Data outputData2 = constructData(this.data[0],pseudoGraph,prefuse.data.Graph.class.getName(),DataProperty.NETWORK_TYPE,"Created Dependency Graph with Function Pseudonodes");
			
			
			if(!DVDAlgorithm.verifyInitialConditions(initCondition, dependencyGraph.getNodeCount(), numberOfStates)){
				this.logger.log(LogService.LOG_WARNING, "The provided initial condition is invalid. Please provide a series of numbers or *'s separated" +
						" by spaces. There should be as many numbers or *'s as functions in your function file. Each number provided must be at least 0 and no greater" +
				" than the number of states less one. The state space has not been generated.");
				monitor.done();
				return new Data[] {outputData1,outputData2};
			}
			
			if(!DVDAlgorithm.verifyUpdateSchedule(schedule, dependencyGraph.getNodeCount())){
				this.logger.log(LogService.LOG_WARNING, "The provided update schedule is invalid. Please provide a series of unique numbers, separated by spaces, " +
						"corresponding to the function rows in your file.\n");
				return new Data[] {outputData1,outputData2};
			}
			

				cssg = new CreateStateSpaceGraph(this.getProgressMonitor());
				stateSpace = cssg.createStateSpace(dependencyGraph, numberOfStates, functionLabel, 
						isPolynomial,initCondition,intArrayFromStringArray(schedule));
				
				
					final Data outputData3 = constructData(this.data[0],generateStateSpaceFile(stateSpace,cssg),"file:text/nwb",DataProperty.NETWORK_TYPE, "Generated State Space Graph");
					monitor.done();
					return new Data[] {outputData1, outputData2, outputData3};
				
			
		}catch(FunctionFormatException ffe){
			throw new AlgorithmExecutionException(ffe.getMessage());
		}catch(InterruptedException ie){
			throw new AlgorithmExecutionException("Creation of state space graph was interrupted",ie);
		}		
	}

	private static Data constructData(Data d, Object obj, String className, String type, String label){
		Data outputData = new BasicData(obj,className);
		Dictionary dataAttributes = outputData.getMetadata();
		dataAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		dataAttributes.put(DataProperty.PARENT, d);
		dataAttributes.put(DataProperty.TYPE, type);
		dataAttributes.put(DataProperty.LABEL,label);

		return outputData;
	}

	private String getParameter(String s){
		try{
			return this.parameters.get(s).toString();
		}catch(NullPointerException npe){
			return null;
		}
	}

	private static boolean intStringToBoolean(String s){
		int value = new Integer(s).intValue();
		if(value >= 1){
			return true;
		}
		return false;
	}

	private static String[] handleOptionalData(Boolean b, String s, String errorMessage) throws AlgorithmExecutionException{
		String[] returnValue = null;
		if(!b){
			if(s.trim().equals("") || s == null){
				throw new AlgorithmExecutionException(errorMessage);
			}
			returnValue = s.split("\\s+");
		}
		return returnValue;
	}

	private static boolean verifyInitialConditions(String[] s, int numberOfNodes, int nodeStates) throws AlgorithmExecutionException{
		String numbers = "\\d+";
		String wildcard = "\\*";
		int value;
		
		if(s == null)
			return true;

		if(s.length != numberOfNodes){
			return false;
		}
		for(int i = 0; i < s.length; i++){
			if(s[i].matches(numbers)){
				value = new Integer(s[i]).intValue();
				if(value < 0 || value > (nodeStates-1)){
					return false;
				}
			}
			else if(s[i].matches(wildcard)){
	
			}
			else{
			
				return false;
			}
		}
		return true;
	}
	
	private static boolean verifyUpdateSchedule(String[] s, int numberOfNodes){
		Integer value;
		LinkedHashSet seenValues = new LinkedHashSet();
		if(s == null)
			return true;
		if(s.length != numberOfNodes){
			return false;
		}
		for(int i = 0; i < s.length; i++){
			try{
				value = new Integer(s[i]);
				if(seenValues.add(value)){
					
				if(value.intValue() < 1 || value.intValue() > numberOfNodes)
					return false;
				}
				else{
					return false;
				}
			
			}catch(NumberFormatException nfe){
				return false;
			}
		}
		
		return true;
	}

	private static int[] intArrayFromStringArray(String[] stringArray){
		if(stringArray == null)
			return null;
		int[] returnValue = new int[stringArray.length];

		for(int i = 0; i < stringArray.length; i++){
			returnValue[i] = new Integer(stringArray[i]).intValue();
		}
		
		return returnValue;
	}
	
	private static File generateStateSpaceFile(final Graph g, CreateStateSpaceGraph cssg) throws AlgorithmExecutionException{
		try{
			File stateSpaceFile = File.createTempFile("NWB-Session-StateSpace-", ".nwb");
			NWBFileWriter stateSpaceWriter = new NWBFileWriter(stateSpaceFile);
			LinkedHashMap nodeSchema = NWBFileWriter.getDefaultNodeSchema();
			LinkedHashMap edgeSchema = NWBFileWriter.getDefaultEdgeSchema();
			
			nodeSchema = generateSchema(g.getNodeTable(),nodeSchema);
			edgeSchema = generateSchema(g.getEdgeTable(),edgeSchema);
			
			stateSpaceWriter.setNodeSchema(nodeSchema);
			writeNodes(g,stateSpaceWriter, cssg);
			stateSpaceWriter.setDirectedEdgeSchema(edgeSchema);
			writeEdges(g,stateSpaceWriter,cssg);
			
			stateSpaceWriter.haltParsingNow();
			return stateSpaceFile;
		}catch(IOException ioe){
			throw new AlgorithmExecutionException("Error creating and writing the state space graph file.\n",ioe);
		}
	}
	
	private static LinkedHashMap generateSchema(final Table t, LinkedHashMap schema){
		for(int i = 0; i < t.getColumnCount(); i++){
			if(schema.get(t.getColumnName(i)) == null){
				schema.put(t.getColumnName(i), t.getColumnType(i).toString().toLowerCase());
			}
		}
		
		return schema;
	}
	
	private static void writeNodes(final Graph g, NWBFileWriter nfw, CreateStateSpaceGraph cssg){
		int i = 0;
		for(Iterator it = g.nodes(); it.hasNext();){
			i++;
			HashMap columnValues = new HashMap();
			Node n = (Node)it.next();
			
			columnValues.put("attractor", n.get("attractor"));
			nfw.addNode(n.getRow()+1, n.getString("label"), columnValues);
			if(i%80==0){
				CreateStateSpaceGraph.updateCalculatedStates(cssg, 20);
			}
		}
	}
	
	private static void writeEdges(final Graph g, NWBFileWriter nfw, CreateStateSpaceGraph cssg){
		int i = 0;
		for(Iterator it = g.edges(); it.hasNext();){
			i++;
			Edge e = (Edge)it.next();
			
		
			nfw.addDirectedEdge(e.getSourceNode().getRow()+1, e.getTargetNode().getRow()+1,null);
			if(i%80==0)
				CreateStateSpaceGraph.updateCalculatedStates(cssg, 20);
		}
	}

}