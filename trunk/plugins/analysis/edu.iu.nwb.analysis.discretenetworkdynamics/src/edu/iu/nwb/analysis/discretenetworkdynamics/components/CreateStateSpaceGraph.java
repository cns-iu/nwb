package edu.iu.nwb.analysis.discretenetworkdynamics.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Iterator;

import prefuse.data.Graph;
import prefuse.data.Node;
import edu.iu.nwb.analysis.discretenetworkdynamics.parser.FunctionContainer;
import edu.iu.nwb.analysis.discretenetworkdynamics.parser.FunctionFormatException;
import edu.iu.nwb.analysis.discretenetworkdynamics.parser.FunctionParser;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class CreateStateSpaceGraph {
	
	public static File createStateSpace(final Graph dependencyGraph, int nodeStates, String functionLabel, boolean isPolynomial) throws FunctionFormatException{
		try{
			File tempNodeFile = File.createTempFile("NWB-Session-StateSpace-Nodes-", ".nwb");
			File tempEdgeFile = File.createTempFile("NWB-Session-StateSpace-Edges-", ".nwb");
			
			File stateSpaceFile;
			NWBFileWriter nodeFileWriter = new NWBFileWriter(tempNodeFile);
			nodeFileWriter.setNodeSchema(NWBFileWriter.getDefaultNodeSchema());
			
			NWBFileWriter edgeFileWriter = new NWBFileWriter(tempEdgeFile);
			edgeFileWriter.setDirectedEdgeSchema(NWBFileWriter.getDefaultEdgeSchema());
			
		int numberOfNodes = dependencyGraph.getNodeCount();
		int[] currentState;
		int[] nextState;
		FunctionContainer[] updateExpressions = CreateStateSpaceGraph.createUpdateExpressions(dependencyGraph,
				numberOfNodes, isPolynomial, functionLabel);
		BigInteger radix = new BigInteger(new Integer(nodeStates).toString());
		
		String currentStateString;
		String nextStateString;
		
	
		BigInteger totalSpace = radix;	
		totalSpace = totalSpace.pow(numberOfNodes);
		
		
		for(BigInteger enumerate = BigInteger.ZERO; enumerate.compareTo(totalSpace) < 0; enumerate = enumerate.add(BigInteger.ONE)){
			currentState = convertBigIntToIntArray(enumerate,numberOfNodes,nodeStates);
			currentStateString = convertIntArrayToString(currentState);
			nodeFileWriter.addNode(enumerate.add(BigInteger.ONE).intValue(), currentStateString, null);
			
			
			nextState = evaluateFunctions(updateExpressions,currentState,null,radix);
			nextStateString = convertIntArrayToString(nextState);
			edgeFileWriter.addDirectedEdge(convertStringToInt(currentStateString,nodeStates)+1, convertStringToInt(nextStateString,nodeStates)+1, null);
		}
		
		edgeFileWriter.finishedParsing();
		nodeFileWriter.finishedParsing();
		
		stateSpaceFile = mergeEdgesAndNodes(tempNodeFile,tempEdgeFile);
		
		
		return stateSpaceFile;
		
		}catch(IOException ioe){
			return null;  //throw runtime exception here.
		}
	}
	
	public static File mergeEdgesAndNodes(File nodeFile, final File edgeFile) throws IOException{
		PrintWriter mergeFile = null;
		BufferedReader inputStream = null;
		try{
		mergeFile = new PrintWriter(new FileWriter(nodeFile,true));
		inputStream = new BufferedReader(new FileReader(edgeFile));
		
		String line;
		while((line = inputStream.readLine()) != null){
			mergeFile.println(line);
		}
		}finally{
			if(mergeFile != null)
				mergeFile.close();
			if(inputStream != null)
				inputStream.close();
		}
		
		return nodeFile;
	}
	
	public static Graph createStateSpace(final Graph dependencyGraph, int nodeStates, final int[] initialCondition, boolean isPolynomial){
		Graph ssg = new Graph();
		int[] currentState = initialCondition;		
		return ssg;
	}
	
	
	
	private static int[] convertBigIntToIntArray(BigInteger bi, int numberOfNodes, int nodeStates){
		int[] stateSpace = new int[numberOfNodes];
		int pos = stateSpace.length;
		String biAsString = bi.toString(nodeStates);
		biAsString = biAsString.trim();
		
		String[] biToStateSpace = biAsString.split("");
		pos = stateSpace.length - (biToStateSpace.length-1);
		for(int i = 0; i < pos; i++){
			stateSpace[i] = 0;
		}
		
		for(int i = 1; i < biToStateSpace.length; i++){
			stateSpace[(pos-1)+i] = new Integer(biToStateSpace[i]).intValue();
		}
		
		return stateSpace;
	}
	
	private static String convertIntArrayToString(final int[] stateSpace){
		String s = "";
		for(int i = 0; i < stateSpace.length; i++){
			s += stateSpace[i];
		}
		return s;
	}
	
	private static int convertStringToInt(final String stateSpace, int nodeStates){
	
		int integerRepresentation = new BigInteger(stateSpace,nodeStates).intValue();
		
		return integerRepresentation;
	}
	
	private static int[] evaluateFunctions(final FunctionContainer[] functions, final int[] stateSpace, final int[] order, BigInteger numberOfStates){
		int[] nextState = new int[stateSpace.length];
		
		for(int i = 0; i < stateSpace.length; i++){
			nextState[i] = functions[i].evaluate(stateSpace, numberOfStates);
		}	
		return nextState;
	}
	
	
	private static FunctionContainer[] createUpdateExpressions(final Graph g, int numberOfNodes, boolean isPolynomial, final String columnName) throws FunctionFormatException{
		FunctionContainer[] updateExpressions = new FunctionContainer[numberOfNodes];
		
		int i = 0;
		for(Iterator it = g.nodes(); it.hasNext();){
			Node n = (Node)it.next();
			String expression = n.getString(columnName);
			updateExpressions[i] = FunctionParser.parseFunction(expression, isPolynomial);
			i++;
		}
		
		return updateExpressions;
	}

}
