package edu.iu.nwb.analysis.discretenetworkdynamics.components;

import java.math.BigInteger;

import prefuse.data.CascadedTable;
import prefuse.data.Graph;
import prefuse.data.Node;
import edu.iu.nwb.analysis.discretenetworkdynamics.parser.FunctionContainer;

public class GraphFillerThread extends Thread{
	CreateStateSpaceGraph threadListener;
	BigInteger Start;
	BigInteger End;
	Graph g;
	int numNodes;
	int nodeStates;
	FunctionContainer[] updateExpressions;
	final int[] updateScheme;
	final String[] initialCondition;

	public GraphFillerThread(CreateStateSpaceGraph cssg, BigInteger startIndex, BigInteger endIndex, Graph stateGraph, int numberOfNodes, int numberOfStates, FunctionContainer[] updateRules, final String[] ic,final int[] updateSchedule){
		super();
		this.threadListener = cssg;
		this.Start = startIndex;
		this.End = endIndex;
		this.g = stateGraph;
		this.numNodes = numberOfNodes;
		this.nodeStates = numberOfStates;
		this.updateExpressions = updateRules;
		this.updateScheme = updateSchedule;
		this.initialCondition = ic;
	}


	public void run(){
		int[] currentState = new int[this.numNodes];
		int[] nextState = new int[this.numNodes];
		String currentStateString;
		String nextStateString;
		BigInteger nextStateValue;
		BigInteger radix = new BigInteger(new Integer(nodeStates).toString());
		int calculated = 0;
		int onePercent;
		Node n1;
		Node n2;
		CascadedTable visited = new CascadedTable(this.g.getNodeTable());
		visited.addColumn("visited", int.class, new Integer(-1));

		if((onePercent = (End.subtract(Start)).intValue()/100) == 0)
			onePercent = 1;


		for(BigInteger enumerate = Start; enumerate.compareTo(End) <= 0; enumerate = enumerate.add(BigInteger.ONE)){	
			currentState = convertBigIntToIntArray(enumerate,currentState,nodeStates);			
			if(checkInitialCondition(currentState)){
				n1 = g.getNode(enumerate.intValue());

				synchronized(n1){
					while(n1.getString("label") == null){
						currentStateString = convertIntArrayToString(currentState,this.nodeStates);
						calculated++;
						if(calculated%onePercent == 0){
							CreateStateSpaceGraph.updateCalculatedStates(this.threadListener, calculated);
							calculated = 0;
						}
						n1.set("label", convertIntArrayToString(currentState));
						

						nextState = evaluateFunctions(updateExpressions,currentState,this.updateScheme,radix);
						nextStateString = convertIntArrayToString(nextState,this.nodeStates);

						try{
							nextStateValue = new BigInteger(nextStateString,this.nodeStates);
						}catch(NumberFormatException nfe){
							System.err.println(nextStateString+ " " + this.nodeStates);
							nfe.printStackTrace(System.err);
							return;
						}

						n2 = g.getNode(nextStateValue.intValue());
						
						if(n1.equals(n2)){
							n1.set("attractor", 10);
						}
						
							g.getEdgeTable().set(n1.getRow(), "source", n1.getRow());
							g.getEdgeTable().set(n1.getRow(), "target", n2.getRow());


						currentState = nextState;
						n1 = g.getNode(nextStateValue.intValue());
					}
				}
			}
			
		}
		

		CreateStateSpaceGraph.updateCalculatedStates(this.threadListener,calculated);
	}


	protected static int[] convertBigIntToIntArray(BigInteger bi, int[] stateSpace, int nodeStates){
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

	protected static String convertIntArrayToString(final int[] stateSpace){
		String s = "";
		for(int i = 0; i < stateSpace.length; i++){
			s += stateSpace[i]+ " ";		
		}	
		return s.trim();
	}

	protected static String convertIntArrayToString(final int[] stateSpace, int numberOfStates){
		StringBuffer s = new StringBuffer();
		for(int i = 0; i < stateSpace.length; i++){
			s.append(Character.forDigit(stateSpace[i], numberOfStates));


		}
		return s.toString();
	}


	protected static int[] evaluateFunctions(final FunctionContainer[] functions, final int[] stateSpace, final int[] order, BigInteger numberOfStates){
		int[] nextState = new int[stateSpace.length];
		java.util.Arrays.fill(nextState, -1);
		int pos;
		if(order == null){
		for(int i = 0; i < stateSpace.length; i++){
			nextState[i] = functions[i].evaluate(stateSpace, nextState, numberOfStates);
		}	
		}
		else{
			for(int i = 0; i < order.length; i++){
				pos = order[i]-1;
				nextState[pos] = functions[pos].evaluate(stateSpace, nextState, numberOfStates);
			}
		}
		return nextState;
	}

	private boolean checkInitialCondition(int[] currentState){
		if(this.initialCondition == null)
			return true;
		int value;

		for(int i = 0; i < this.initialCondition.length; i++){
			if(!this.initialCondition[i].equals("*")){
				value = new Integer(this.initialCondition[i]).intValue();
				if(currentState[i] != value)
					return false;	
			}
		}
		return true;
	}

}
