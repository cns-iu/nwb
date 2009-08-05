package edu.iu.nwb.modeling.discretenetworkdynamics.components;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import edu.iu.nwb.analysis.extractattractors.containers.BigBigIntegerArray;
import edu.iu.nwb.analysis.extractattractors.containers.FileBackedBitSet;
import edu.iu.nwb.modeling.discretenetworkdynamics.parser.FunctionContainer;

public class GraphFillerThread extends Thread{
	CreateStateSpaceGraph threadListener;
	BigInteger Start;
	BigInteger End;
	FileBackedBitSet seenNodes = null;
	BigBigIntegerArray bigNodeArray = null;
	int[] smallNodeArray = null;
	BufferedWriter output = null;
	int systemSize;
	int nodeStates;
	FunctionContainer[] updateExpressions;
	final int[] updateScheme;
	final String[] initialCondition;
	ArrayList<Exception> exceptions = new ArrayList<Exception>();

	public GraphFillerThread(CreateStateSpaceGraph cssg, BigInteger startIndex, BigInteger endIndex, int systemSize, int numberOfStates, FunctionContainer[] updateRules, final String[] initialCondition,final int[] updateSchedule){
		super();
		this.threadListener = cssg;
		this.Start = startIndex;
		this.End = endIndex;
		this.initialCondition = initialCondition;
		this.systemSize = systemSize;
		this.nodeStates = numberOfStates;
		this.updateExpressions = updateRules;
		this.updateScheme = updateSchedule;


	}

	public void setOutput(BufferedWriter output){
		this.output = output;
	}

	public void setBitSetFile(FileBackedBitSet seenNodes){
		this.seenNodes = seenNodes;
	}

	public void setBigNodeArray(BigBigIntegerArray bigNodeArray){
		this.bigNodeArray = bigNodeArray;
	}

	public void setSmallNodeArray(int[] smallArray){
		this.smallNodeArray = smallArray;
	}


	public void run(){
		BigInteger tenPercent;
		tenPercent = (End.subtract(Start)).divide(BigInteger.TEN);
		if(tenPercent.compareTo(BigInteger.ZERO) == 0)
			tenPercent = BigInteger.ONE;
		try{
			if(this.initialCondition == null){
				if(this.output != null)
					GraphFillerThread.fileBasedFullStateMap(tenPercent,this);
				else if(this.smallNodeArray != null){
					GraphFillerThread.smallFullStateMap(tenPercent.intValue(),this);
				}
				else{
					GraphFillerThread.bigFullStateMap(tenPercent,this);
				}	
			}
			else{
				if(this.output != null)
					GraphFillerThread.fileBasedTrajectory(tenPercent,this);
				else if(this.smallNodeArray != null){
					GraphFillerThread.smallTrajectory(tenPercent.intValue(), this);
				}
				else{
					GraphFillerThread.bigTrajectory(tenPercent, this);
				}
			}
		}catch(Exception ex){
			this.exceptions.add(ex);
			return;
		}
	}

	public boolean hasExceptions(){
		return !this.exceptions.isEmpty();
	}

	protected static void smallFullStateMap(int tenPercentageOfNodes, GraphFillerThread gft) throws InterruptedException{
		int[] currentState = new int[gft.systemSize];
		int[] nextState = new int[gft.systemSize];
		//String currentStateString;
		String nextStateString;
		BigInteger nextStateValue;
		BigInteger radix = new BigInteger(new Integer(gft.nodeStates).toString());
		int calculated = 0;



		for(BigInteger enumerate = gft.Start; enumerate.compareTo(gft.End) <= 0; enumerate = enumerate.add(BigInteger.ONE)) {	
			currentState = convertBigIntToIntArray(enumerate,currentState,gft.nodeStates);			


			calculated++;
			if((calculated%tenPercentageOfNodes) == 0){
				CreateStateSpaceGraph.updateCalculatedStates(gft.threadListener, 5);
			}

			nextState = evaluateFunctions(gft.updateExpressions,currentState,gft.updateScheme,radix);
			nextStateString = convertIntArrayToString(nextState,gft.nodeStates);

			try{
				nextStateValue = new BigInteger(nextStateString,gft.nodeStates);

				gft.smallNodeArray[enumerate.intValue()] = nextStateValue.intValue();
			}catch(NumberFormatException nfe){
				System.err.println(nextStateString+ " " + gft.nodeStates);
				nfe.printStackTrace(System.err);
				throw new InterruptedException(nfe.getMessage());
			}
		}
	}

	protected static void bigFullStateMap(BigInteger tenPercentageOfNodes, GraphFillerThread gft) throws InterruptedException{
		int[] currentState = new int[gft.systemSize];
		int[] nextState = new int[gft.systemSize];
		//String currentStateString;
		String nextStateString;
		BigInteger nextStateValue;
		BigInteger radix = new BigInteger(new Integer(gft.nodeStates).toString());
		BigInteger calculated = BigInteger.ZERO;



		for(BigInteger enumerate = gft.Start; enumerate.compareTo(gft.End) <= 0; enumerate = enumerate.add(BigInteger.ONE)) {	
			currentState = convertBigIntToIntArray(enumerate,currentState,gft.nodeStates);			


			calculated = calculated.add(BigInteger.ONE);
			if(calculated.mod(tenPercentageOfNodes).compareTo(BigInteger.ZERO) == 0){
				CreateStateSpaceGraph.updateCalculatedStates(gft.threadListener, 5);
			}

			nextState = evaluateFunctions(gft.updateExpressions,currentState,gft.updateScheme,radix);
			nextStateString = convertIntArrayToString(nextState,gft.nodeStates);

			try{
				nextStateValue = new BigInteger(nextStateString,gft.nodeStates);

				gft.bigNodeArray.put(enumerate, nextStateValue);
			}catch(NumberFormatException nfe){
				System.err.println(nextStateString+ " " + gft.nodeStates);
				nfe.printStackTrace(System.err);
				throw new InterruptedException(nfe.getMessage());
			}
		}
	}

	protected static void fileBasedFullStateMap(BigInteger tenPercentageOfNodes, GraphFillerThread gft)throws InterruptedException{
		int[] currentState = new int[gft.systemSize];
		int[] nextState = new int[gft.systemSize];
		//String currentStateString;
		String nextStateString;
		BigInteger nextStateValue;
		BigInteger radix = new BigInteger(new Integer(gft.nodeStates).toString());
		BigInteger calculated = BigInteger.ZERO;



		for(BigInteger enumerate = gft.Start; enumerate.compareTo(gft.End) <= 0; enumerate = enumerate.add(BigInteger.ONE)) {	
			currentState = convertBigIntToIntArray(enumerate,currentState,gft.nodeStates);			


			calculated = calculated.add(BigInteger.ONE);
			if((calculated.mod(tenPercentageOfNodes)).compareTo(BigInteger.ZERO) == 0){
				CreateStateSpaceGraph.updateCalculatedStates(gft.threadListener, 5);
			}

			nextState = evaluateFunctions(gft.updateExpressions,currentState,gft.updateScheme,radix);
			nextStateString = convertIntArrayToString(nextState,gft.nodeStates);

			try{
				nextStateValue = new BigInteger(nextStateString,gft.nodeStates);

				gft.output.write(enumerate + " " + nextStateValue +"\n");
			}catch(NumberFormatException nfe){
				System.err.println(nextStateString+ " " + gft.nodeStates);
				nfe.printStackTrace(System.err);
				throw new InterruptedException(nfe.getMessage());
			}catch(IOException ioe){
				System.err.println("Error writing to file\n");
				ioe.printStackTrace();
				throw new InterruptedException(ioe.getMessage());
			}



		}
	}

	protected static void smallTrajectory(int tenPercent, GraphFillerThread gft) throws InterruptedException{
		int[] currentState = new int[gft.systemSize];
		int[] nextState = new int[gft.systemSize];
		//String currentStateString;
		String nextStateString;
		BigInteger nextStateValue =null;
		BigInteger tempState;
		BigInteger radix = new BigInteger(new Integer(gft.nodeStates).toString());
		int calculated = 0;

		for(BigInteger enumerate = gft.Start; enumerate.compareTo(gft.End) <= 0; enumerate = enumerate.add(BigInteger.ONE)){	
			currentState = convertBigIntToIntArray(enumerate,currentState,gft.nodeStates);			
			if(checkInitialCondition(gft.initialCondition,currentState)){		
				tempState = enumerate;
				while(gft.smallNodeArray[tempState.intValue()] < 0){
					calculated++;
					if(calculated %tenPercent  == 0){
						CreateStateSpaceGraph.updateCalculatedStates(gft.threadListener, 5);
					}
					
					nextState = evaluateFunctions(gft.updateExpressions,currentState,gft.updateScheme,radix);
					nextStateString = convertIntArrayToString(nextState,gft.nodeStates);

					try{
						nextStateValue = new BigInteger(nextStateString,gft.nodeStates);
						gft.smallNodeArray[tempState.intValue()] = nextStateValue.intValue();
						currentState = nextState;
						tempState = nextStateValue;		
					}catch(NumberFormatException nfe){
						System.err.println(nextStateString+ " " + gft.nodeStates);
						nfe.printStackTrace(System.err);
						return;
					}
				}
			}
		}
	}
	
	protected static void bigTrajectory(BigInteger tenPercent, GraphFillerThread gft){
		int[] currentState = new int[gft.systemSize];
		int[] nextState = new int[gft.systemSize];
		//String currentStateString;
		String nextStateString;
		BigInteger nextStateValue =null;
		BigInteger tempState;
		BigInteger radix = new BigInteger(new Integer(gft.nodeStates).toString());
		BigInteger calculated = BigInteger.ZERO;

		for(BigInteger enumerate = gft.Start; enumerate.compareTo(gft.End) <= 0; enumerate = enumerate.add(BigInteger.ONE)){	
			currentState = convertBigIntToIntArray(enumerate,currentState,gft.nodeStates);			
			if(checkInitialCondition(gft.initialCondition,currentState)){		
				tempState = enumerate;
				while(gft.smallNodeArray[tempState.intValue()] < 0){
					calculated = calculated.add(BigInteger.ONE);
					if(calculated.mod(tenPercent).compareTo(BigInteger.ZERO)  == 0){
						CreateStateSpaceGraph.updateCalculatedStates(gft.threadListener, 5);
					}
					
					nextState = evaluateFunctions(gft.updateExpressions,currentState,gft.updateScheme,radix);
					nextStateString = convertIntArrayToString(nextState,gft.nodeStates);

					try{
						nextStateValue = new BigInteger(nextStateString,gft.nodeStates);
						gft.bigNodeArray.put(tempState, nextStateValue);
						currentState = nextState;
						tempState = nextStateValue;		
					}catch(NumberFormatException nfe){
						System.err.println(nextStateString+ " " + gft.nodeStates);
						nfe.printStackTrace(System.err);
						return;
					}
				}
			}
		}
	}

	protected static void fileBasedTrajectory(BigInteger tenPercent,GraphFillerThread gft) throws InterruptedException{
		int[] currentState = new int[gft.systemSize];
		int[] nextState = new int[gft.systemSize];
		//String currentStateString;
		String nextStateString;
		BigInteger nextStateValue =null;
		BigInteger tempState;
		BigInteger radix = new BigInteger(new Integer(gft.nodeStates).toString());
		BigInteger calculated = BigInteger.ZERO;
		
		for(BigInteger enumerate = gft.Start; enumerate.compareTo(gft.End) <= 0; enumerate = enumerate.add(BigInteger.ONE)){	
			currentState = convertBigIntToIntArray(enumerate,currentState,gft.nodeStates);			
			if(checkInitialCondition(gft.initialCondition,currentState)){		
				tempState = enumerate;
				while(!gft.seenNodes.get(tempState)){
					boolean set = gft.seenNodes.set(tempState);
					calculated = calculated.add(BigInteger.ONE);
					if(calculated.mod(tenPercent).compareTo(BigInteger.ZERO) == 0){
						CreateStateSpaceGraph.updateCalculatedStates(gft.threadListener, 5);
					}

					nextState = evaluateFunctions(gft.updateExpressions,currentState,gft.updateScheme,radix);
					nextStateString = convertIntArrayToString(nextState,gft.nodeStates);

					try{
						nextStateValue = new BigInteger(nextStateString,gft.nodeStates);
						if(set){
							gft.output.write(tempState+" "+nextStateValue+"\n");
							currentState = nextState;
							tempState = nextStateValue;
						}
						else{
							break;
						}
					}catch(NumberFormatException nfe){
						System.err.println(nextStateString+ " " + gft.nodeStates);
						nfe.printStackTrace(System.err);
						return;
					}catch(IOException ioe){
						System.err.println("Error writing to file\n");
						ioe.printStackTrace();
						throw new InterruptedException(ioe.getMessage());
					}
				}
			}
		}
	}

	public static int[] convertBigIntToIntArray(BigInteger bi, int[] stateSpace, int nodeStates){
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

	public static String convertIntArrayToString(final int[] stateSpace, int numberOfStates){
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
				nextState[i] = functions[i].evaluate(stateSpace, nextState, numberOfStates, false);
			}	

		}
		else{
			for(int i = 0; i < order.length; i++){
				pos = order[i]-1;
				nextState[pos] = functions[pos].evaluate(stateSpace, nextState, numberOfStates,true);
			}
		}
		return nextState;
	}

	private static boolean checkInitialCondition(String[] initialCondition,int[] currentState){
		if(initialCondition == null)
			return true;
		int value;

		for(int i = 0; i < initialCondition.length; i++){
			if(!initialCondition[i].equals("*")){
				value = new Integer(initialCondition[i]).intValue();
				if(currentState[i] != value)
					return false;	
			}
		}
		return true;
	}

}
