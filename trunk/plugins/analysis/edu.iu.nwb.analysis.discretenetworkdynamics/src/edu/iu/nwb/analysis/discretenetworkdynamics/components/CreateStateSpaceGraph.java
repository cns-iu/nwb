package edu.iu.nwb.analysis.discretenetworkdynamics.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Iterator;

import org.cishell.framework.algorithm.ProgressMonitor;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import edu.iu.nwb.analysis.discretenetworkdynamics.parser.FunctionContainer;
import edu.iu.nwb.analysis.discretenetworkdynamics.parser.FunctionFormatException;
import edu.iu.nwb.analysis.discretenetworkdynamics.parser.FunctionParser;

public class CreateStateSpaceGraph {
	int statesProcessed = 0;
	ProgressMonitor progMonitor;

	public CreateStateSpaceGraph(ProgressMonitor pm){
		this.progMonitor = pm;
	}

	public Graph createStateSpace(final Graph dependencyGraph, int nodeStates, String functionLabel, boolean isPolynomial, final int[] ic, final int[] updateSchedule) throws FunctionFormatException, InterruptedException{		
		final int numProcessors = Runtime.getRuntime().availableProcessors();
		int numberOfNodes = dependencyGraph.getNodeCount();

		FunctionContainer[] updateExpressions = CreateStateSpaceGraph.createUpdateExpressions(dependencyGraph,
				numberOfNodes, isPolynomial, functionLabel);
		BigInteger radix = new BigInteger(new Integer(nodeStates).toString());

		BigInteger totalSpace = radix;	
		totalSpace = totalSpace.pow(numberOfNodes);
		BigInteger start;
		BigInteger end;
		Thread[] pool = new Thread[numProcessors];

		int division = totalSpace.divideAndRemainder(new BigInteger(new Integer(numProcessors).toString()))[0].intValue();
		int remainder = totalSpace.divideAndRemainder(new BigInteger(new Integer(numProcessors).toString()))[1].intValue();

		Graph stateSpaceGraph = initializeStateSpaceGraph(totalSpace.intValue());

		for(int i = 0; i < numProcessors; i++){
			start = new BigInteger(new Integer(i*division).toString());
			end = new BigInteger(new Integer(((i+1)*division)-1).toString());
			if(i == numProcessors-1)
				end = end.add(new BigInteger(new Integer(remainder).toString()));

			pool[i] = new GraphFillerThread(this, start,end,stateSpaceGraph,numberOfNodes,nodeStates,updateExpressions,updateSchedule);
			pool[i].start();			
		}

		for(int i = 0; i < numProcessors; i++){
			pool[i].join();
		}

		return stateSpaceGraph;	
	}

	
	public static void updateCalculatedStates(CreateStateSpaceGraph cssg, int calculatedStates){
		cssg.statesProcessed += calculatedStates;
		cssg.progMonitor.worked(cssg.statesProcessed);
	}


	private static Graph initializeStateSpaceGraph(int numberOfNodes){
		Schema nodeSchema = new Schema();
		//nodeSchema.addColumn("id", int.class);
		nodeSchema.addColumn("label", String.class);
		nodeSchema.addColumn("weakCluster", int.class,1);
		nodeSchema.addColumn("attractor",int.class,1);

		Schema edgeSchema = new Schema();
		edgeSchema.addColumn("source", int.class);
		edgeSchema.addColumn("target", int.class);

		return new Graph(nodeSchema.instantiate(numberOfNodes),edgeSchema.instantiate(numberOfNodes),true);
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




