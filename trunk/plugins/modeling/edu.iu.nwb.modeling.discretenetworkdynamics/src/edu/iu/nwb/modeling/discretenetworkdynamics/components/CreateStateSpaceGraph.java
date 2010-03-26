package edu.iu.nwb.modeling.discretenetworkdynamics.components;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;

import prefuse.data.Graph;
import prefuse.data.Node;
import edu.iu.nwb.analysis.extractattractors.containers.BigBigIntegerArray;
import edu.iu.nwb.analysis.extractattractors.containers.FileBackedBitSet;
import edu.iu.nwb.modeling.discretenetworkdynamics.parser.FunctionContainer;
import edu.iu.nwb.modeling.discretenetworkdynamics.parser.FunctionFormatException;
import edu.iu.nwb.modeling.discretenetworkdynamics.parser.FunctionParser;

public class CreateStateSpaceGraph {
	int statesProcessed = 0;
	ProgressMonitor progMonitor;

	public CreateStateSpaceGraph(ProgressMonitor pm){
		this.progMonitor = pm;
	}



	public File createStateMap(final Graph dependencyGraph, int nodeStates, String functionLabel, boolean isPolynomial, final String[] ic, final int[] updateSchedule) throws FunctionFormatException, InterruptedException, AlgorithmExecutionException{		
		try{
			int systemSize = dependencyGraph.getNodeCount();
			FunctionContainer[] updateExpressions = CreateStateSpaceGraph.createUpdateExpressions(dependencyGraph,
					systemSize, isPolynomial, functionLabel);

			BigInteger totalSpace = new BigInteger(new Integer(nodeStates).toString()).pow(systemSize);
			File stateSpaceFile = File.createTempFile("DND-", ".edge");
			FileWriter output = new FileWriter(stateSpaceFile);
			BufferedWriter bufferedOutput = new BufferedWriter(output);

			byte runMode = calculateRunMode(totalSpace);

			runMultiThreaded(this,totalSpace,systemSize,nodeStates,updateExpressions,ic,updateSchedule,bufferedOutput);

			bufferedOutput.close();
			return stateSpaceFile;
		}
		catch(IOException ioe){
			throw new AlgorithmExecutionException("Error creating the state space file.\n",ioe);
		}
		catch(SecurityException se){
			throw new AlgorithmExecutionException("Security Manager does not allow the creation of the state space file.\n");
		}

	}

	public static byte calculateRunMode(BigInteger totalSpace){
		BigInteger maxInt = new BigInteger(new Integer(Integer.MAX_VALUE).toString());
		BigDecimal expectedMemoryUsage;
		byte runMode = 0;
		if(totalSpace.compareTo(maxInt) > 0){
			runMode |= RunningModes.LARGESIZE;
			expectedMemoryUsage = new BigDecimal(totalSpace).multiply(new BigDecimal(new Integer(8).toString()));
		}
		else{
			expectedMemoryUsage = new BigDecimal(totalSpace).multiply(new BigDecimal(new Integer(4).toString()));
		}
		BigDecimal memoryAvailable = new BigDecimal(new Long(Runtime.getRuntime().freeMemory()).toString());
		
		

		double percentageUsed = (expectedMemoryUsage.divide(memoryAvailable, 5, BigDecimal.ROUND_CEILING)).doubleValue();

		if(percentageUsed > .8){
			runMode |= RunningModes.FILEBASED;
		}



		return runMode;
	}


	public static void runMultiThreaded(CreateStateSpaceGraph cssg,BigInteger totalSpace,int systemSize, int nodeStates,FunctionContainer[] updateExpressions,String[] initialCondition,int[]updateSchedule,BufferedWriter bufferedOutput) throws IOException,InterruptedException{
		BigInteger start;
		BigInteger end;
		int numProcessors = Runtime.getRuntime().availableProcessors();
		
		//BigInteger totalSize = new BigInteger(new Integer(this.nodeStates).toString()).pow(systemSize);
		FileBackedBitSet seenNodesFile = null;
		BigBigIntegerArray bigSeenNodesArray = null;
		int[] smallSeenNodesArray = null;


		byte runMode = calculateRunMode(totalSpace);

		switch (runMode) {
		case 0:
			smallSeenNodesArray = new int[totalSpace.intValue()];
			java.util.Arrays.fill(smallSeenNodesArray, -1);
			
			break;
		case 1:
			if(initialCondition != null){
				seenNodesFile = new FileBackedBitSet(totalSpace);
				numProcessors = 1;
			}
			
			break;
		case 2:
			bigSeenNodesArray = new BigBigIntegerArray(totalSpace);
			BigBigIntegerArray.fill(bigSeenNodesArray, null);
			
			break;
		case 3:
			if(initialCondition != null){
				seenNodesFile = new FileBackedBitSet(totalSpace);
				numProcessors = 1;
			}
			break;

		}
		
		GraphFillerThread[] pool = new GraphFillerThread[numProcessors];
		BigInteger division = totalSpace.divideAndRemainder(new BigInteger(new Integer(numProcessors).toString()))[0];
		BigInteger remainder = totalSpace.divideAndRemainder(new BigInteger(new Integer(numProcessors).toString()))[1];


		for(int i = 0; i < numProcessors; i++){
			start = new BigInteger(new Integer(i).toString()).multiply(division);
			end = new BigInteger(new Integer((i+1)).toString()).multiply(division);
			end = end.subtract(BigInteger.ONE);
			if(i == numProcessors-1)
				end = end.add(remainder);

			pool[i] = new GraphFillerThread(cssg, start,end,systemSize,nodeStates,updateExpressions,initialCondition,updateSchedule);
			switch(runMode){
			case 0:
				pool[i].setSmallNodeArray(smallSeenNodesArray);
				break;
			case 1:
				pool[i].setOutput(bufferedOutput);
				if(initialCondition != null)
					pool[i].setBitSetFile(seenNodesFile);
				break;
			case 2:
				pool[i].setBigNodeArray(bigSeenNodesArray);
				break;
			case 3:
				pool[i].setOutput(bufferedOutput);
				if(initialCondition != null)
					pool[i].setBitSetFile(seenNodesFile);
			}
			pool[i].start();			
		}

		for(int i = 0; i < numProcessors; i++){
			pool[i].join();
		}

		if(runMode == 0){
			for(int i = 0; i < smallSeenNodesArray.length; i++){
				if(smallSeenNodesArray[i] > -1){
					bufferedOutput.write(i + " " + smallSeenNodesArray[i]+"\n");
				}
			}
		}
		if(runMode == 2){
			BigInteger alpha;
			BigInteger omega = bigSeenNodesArray.getSize();
			
			for(alpha = BigInteger.ZERO; alpha.compareTo(omega) < 0; alpha = alpha.add(BigInteger.ONE)){
				BigInteger val = bigSeenNodesArray.get(alpha);
				if(val != null){
					bufferedOutput.write(alpha + " " + val + "\n");
				}
			}
			
		}
		bufferedOutput.flush();
	}






	public static void updateCalculatedStates(CreateStateSpaceGraph cssg, int calculatedStates){
		cssg.statesProcessed += calculatedStates;

		cssg.progMonitor.worked(cssg.statesProcessed);
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




