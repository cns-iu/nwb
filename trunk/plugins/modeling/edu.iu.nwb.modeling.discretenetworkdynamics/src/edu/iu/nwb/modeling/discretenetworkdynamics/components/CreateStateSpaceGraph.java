package edu.iu.nwb.modeling.discretenetworkdynamics.components;

import java.math.BigInteger;
import java.util.Iterator;

import org.cishell.framework.algorithm.ProgressMonitor;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import edu.iu.nwb.modeling.discretenetworkdynamics.parser.FunctionContainer;
import edu.iu.nwb.modeling.discretenetworkdynamics.parser.FunctionFormatException;
import edu.iu.nwb.modeling.discretenetworkdynamics.parser.FunctionParser;

public class CreateStateSpaceGraph {
	int statesProcessed = 0;
	ProgressMonitor progMonitor;

	public CreateStateSpaceGraph(ProgressMonitor pm){
		this.progMonitor = pm;
	}

	public Graph createStateSpace(final Graph dependencyGraph, int nodeStates, String functionLabel, boolean isPolynomial, final String[] ic, final int[] updateSchedule) throws FunctionFormatException, InterruptedException{		
		
		
		int numberOfNodes = dependencyGraph.getNodeCount();

		FunctionContainer[] updateExpressions = CreateStateSpaceGraph.createUpdateExpressions(dependencyGraph,
				numberOfNodes, isPolynomial, functionLabel);
		BigInteger radix = new BigInteger(new Integer(nodeStates).toString());

		BigInteger totalSpace = radix;	
		totalSpace = totalSpace.pow(numberOfNodes);
		BigInteger start;
		BigInteger end;
		final int numProcessors = Math.min(totalSpace.intValue(),Math.max(Runtime.getRuntime().availableProcessors(),10));
		Thread[] pool = new Thread[numProcessors];

		int division = totalSpace.divideAndRemainder(new BigInteger(new Integer(numProcessors).toString()))[0].intValue();
		int remainder = totalSpace.divideAndRemainder(new BigInteger(new Integer(numProcessors).toString()))[1].intValue();

		Graph stateSpaceGraph = initializeStateSpaceGraph(totalSpace.intValue());

		for(int i = 0; i < numProcessors; i++){
			start = new BigInteger(new Integer(i*division).toString());
			end = new BigInteger(new Integer(((i+1)*division)-1).toString());
			if(i == numProcessors-1)
				end = end.add(new BigInteger(new Integer(remainder).toString()));

			pool[i] = new GraphFillerThread(this, start,end,stateSpaceGraph,numberOfNodes,nodeStates,updateExpressions,ic,updateSchedule);
			pool[i].start();			
		}

		for(int i = 0; i < numProcessors; i++){
			pool[i].join();
		}
/*
		NodeCleaningThread nct = new NodeCleaningThread(stateSpaceGraph);
		EdgeCleaningThread ect = new EdgeCleaningThread(stateSpaceGraph);
		nct.run();
		ect.run();
		nct.join();
		ect.join();*/
		
		return stateSpaceGraph;	
	}

	
	public static void updateCalculatedStates(CreateStateSpaceGraph cssg, int calculatedStates){
		cssg.statesProcessed += calculatedStates;
		
		cssg.progMonitor.worked(cssg.statesProcessed);
	}


	private static Graph initializeStateSpaceGraph(int numberOfNodes){
		Schema nodeSchema = new Schema();
		nodeSchema.addColumn("label", String.class);
		//nodeSchema.addColumn("attractor",int.class,new Integer(1));

		Schema edgeSchema = new Schema();
		edgeSchema.addColumn("source", int.class);
		edgeSchema.addColumn("target", int.class);

		return new Graph(nodeSchema.instantiate(numberOfNodes),edgeSchema.instantiate(numberOfNodes),true);
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




