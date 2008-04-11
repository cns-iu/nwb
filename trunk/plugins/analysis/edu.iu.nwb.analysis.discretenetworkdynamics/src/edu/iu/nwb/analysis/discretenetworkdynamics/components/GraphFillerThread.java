package edu.iu.nwb.analysis.discretenetworkdynamics.components;

import java.math.BigInteger;

import prefuse.data.Graph;
import edu.iu.nwb.analysis.discretenetworkdynamics.parser.FunctionContainer;

public class GraphFillerThread extends Thread{
	CreateStateSpaceGraph threadListener;
	BigInteger Start;
	BigInteger End;
	Graph g;
	int numNodes;
	int nodeStates;
	FunctionContainer[] updateExpressions;
	
	public GraphFillerThread(CreateStateSpaceGraph cssg, BigInteger startIndex, BigInteger endIndex, Graph stateGraph, int numberOfNodes, int numberOfStates, FunctionContainer[] updateRules){
		super();
		this.threadListener = cssg;
		this.Start = startIndex;
		this.End = endIndex;
		this.g = stateGraph;
		this.numNodes = numberOfNodes;
		this.nodeStates = numberOfStates;
		this.updateExpressions = updateRules;
	}

	public void run() {
		int[] currentState = new int[this.numNodes];
		int[] nextState = new int[this.numNodes];
		String currentStateString;
		String nextStateString;
		BigInteger nextStateValue;
		BigInteger radix = new BigInteger(new Integer(nodeStates).toString());
		int calculated = 0;
		int onePercent;
	
		if((onePercent = (End.subtract(Start)).intValue()/100) == 0)
			onePercent = 1;
		
		
		for(BigInteger enumerate = Start; enumerate.compareTo(End) <= 0; enumerate = enumerate.add(BigInteger.ONE)){
			calculated++;	
			
			if(calculated%onePercent == 0){
					CreateStateSpaceGraph.updateCalculatedStates(this.threadListener, calculated);
					calculated = 0;
			}
				
			
				currentState = convertBigIntToIntArray(enumerate,currentState,nodeStates);
			
				currentStateString = convertIntArrayToString(currentState);
			
				
				//g.getNodeTable().set(enumerate.intValue(), "id", enumerate.intValue());
				g.getNodeTable().set(enumerate.intValue(), "label", currentStateString);
			
				nextState = evaluateFunctions(updateExpressions,currentState,null,radix);
				nextStateString = convertIntArrayToString(nextState,this.nodeStates);
				nextStateValue = new BigInteger(nextStateString,this.nodeStates);
					if(enumerate.compareTo(nextStateValue) == 0){
						g.getNodeTable().set(enumerate.intValue(), "attractor", 10);
					}
				
					g.getEdgeTable().set(enumerate.intValue(), "source", enumerate.intValue());
					g.getEdgeTable().set(enumerate.intValue(),"target",nextStateValue.intValue());
					
				
		}
	
		CreateStateSpaceGraph.updateCalculatedStates(this.threadListener,calculated);
	}
	
	private static int[] convertBigIntToIntArray(BigInteger bi, int[] stateSpace, int nodeStates){
		int pos = stateSpace.length;
		String biAsString = bi.toString(nodeStates);
		biAsString = biAsString.trim();
		
		String[] biToStateSpace;
		
		biToStateSpace = biAsString.split("");
		
		pos = stateSpace.length - (biToStateSpace.length-1);
		for(int i = 0; i < pos; i++){
			stateSpace[i] = 0;
		}

		for(int i = 1; i < biToStateSpace.length; i++){
			int digit = Character.digit(biToStateSpace[i].charAt(0),nodeStates);
			
			stateSpace[(pos-1)+i] = new Integer(digit).intValue();
		}
		
		return stateSpace;
	}

	private static String convertIntArrayToString(final int[] stateSpace){
		String s = "";
		for(int i = 0; i < stateSpace.length; i++){
			s += stateSpace[i]+ " ";		
		}	
		return s;
	}
	
	private static String convertIntArrayToString(final int[] stateSpace, int numberOfStates){
		String s = "";
		for(int i = 0; i < stateSpace.length; i++){
			s += (Character.forDigit(stateSpace[i], numberOfStates));
		}
		return s;
	}
	

	private static int[] evaluateFunctions(final FunctionContainer[] functions, final int[] stateSpace, final int[] order, BigInteger numberOfStates){
		int[] nextState = new int[stateSpace.length];
		for(int i = 0; i < stateSpace.length; i++){
			nextState[i] = functions[i].evaluate(stateSpace, numberOfStates);
		}	
		return nextState;
	}
	
}
