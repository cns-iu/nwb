package edu.iu.nwb.analysis.discretenetworkdynamics.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;

import prefuse.data.Graph;
import edu.iu.nwb.analysis.discretenetworkdynamics.functions.AbstractFunction;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class CreateStateSpaceGraph {
	
	public static File createStateSpace(final Graph dependencyGraph, int nodeStates, boolean isPolynomial){
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
		String currentStateString;
		String nextStateString;
		
	
		BigInteger totalSpace = new BigInteger(new Integer(nodeStates).toString());	
		totalSpace = totalSpace.pow(numberOfNodes);
		
		
		for(BigInteger enumerate = BigInteger.ZERO; enumerate.compareTo(totalSpace) < 0; enumerate = enumerate.add(BigInteger.ONE)){
			currentState = convertBigIntToIntArray(enumerate,numberOfNodes,nodeStates);
			currentStateString = convertIntArrayToString(currentState);
			nodeFileWriter.addNode(enumerate.add(BigInteger.ONE).intValue(), currentStateString, null);
			
			
			nextState = evaluateFunctions(null,currentState,null);
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
		int val;
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
	
	private static int evaluateFunction(AbstractFunction f){
		
		return 0;
	}
	
	private static int[] evaluateFunctions(final AbstractFunction[] functions, final int[] stateSpace, final int[] order){
		int[] nextState = new int[stateSpace.length];
		
		for(int i = 0; i < stateSpace.length; i++){
			nextState[i] = evaluateFunction(null);
		}
		
		return nextState;
	}

}
