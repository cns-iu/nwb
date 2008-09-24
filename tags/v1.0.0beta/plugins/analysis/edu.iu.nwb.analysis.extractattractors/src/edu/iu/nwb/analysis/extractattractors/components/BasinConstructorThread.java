package edu.iu.nwb.analysis.extractattractors.components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Stack;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

import prefuse.data.Table;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class BasinConstructorThread extends Thread{
	File basinGraph;
	File attractorTable;
	int basinSize;
	LinkedHashSet nodes;
	SimpleStateSpaceGraph simpleStateGraph = null;
	final Table originalTable;
	final String labelColumn;
	final int systemSize;
	final int nodeStates;
	ExtractAttractorBasins eab;
	double observedCoherency = 0;
	private int strongComponents = 0;
	private Stack componentMembers = new Stack();
	int[] preOrderColumn;
	int[] strongComponentColumn;
	
	public BasinConstructorThread(SimpleStateSpaceGraph sssg, final Table originalTable, final String labelColumn, LinkedHashSet nodes, int systemSize,int nodeStates,int[] preOrder, int[] strongComponent,ExtractAttractorBasins eab){
		super();
		this.simpleStateGraph = sssg;
		this.nodes = nodes;
		this.originalTable = originalTable;
		this.labelColumn = labelColumn;
		this.systemSize = systemSize;
		this.nodeStates = nodeStates;
		this.eab = eab;
		this.preOrderColumn = preOrder;
		this.strongComponentColumn = strongComponent;
	}
	
	
	public double getObservedCoherency(){
		return this.observedCoherency;
	}
	

	public void queueComponent(LinkedHashSet componentNodeIDs){
		//this.components.add(componentNodeIDs);
		this.nodes = componentNodeIDs;
	}

	public void run() {
		try{
		System.gc();
		Table attractorTable = constructAttractorTable(originalTable, labelColumn,true);
		File stateSpaceFile = File.createTempFile("NWB-Session-StateSpace-", ".nwb");
		NWBFileWriter stateSpaceFileWriter = createNWBFileWriter(stateSpaceFile);
		
		Stack firstStack = new Stack();
		firstStack.setSize(nodes.size());
		Stack secondStack = new Stack();
		secondStack.setSize(nodes.size());
		
		Integer testCount = new Integer(0);

		for(Iterator it = this.nodes.iterator(); it.hasNext();){
			int nodeID = ((Integer)it.next()).intValue();
			if(preOrderColumn[nodeID] == -1){
				recursiveStrongComponentCalculation(nodeID,strongComponentColumn,preOrderColumn,firstStack,secondStack,testCount,attractorTable, stateSpaceFileWriter);
			}
		}
		stateSpaceFileWriter.setDirectedEdgeSchema(NWBFileWriter.getDefaultEdgeSchema());
		writeEdges(simpleStateGraph.outNeighbor,this.nodes,stateSpaceFileWriter);
		stateSpaceFileWriter.haltParsingNow();
		
			this.basinGraph = stateSpaceFile;
			this.basinSize = nodes.size();
			this.attractorTable = generateAttractorCSV(attractorTable);
			this.observedCoherency = this.observedCoherency/(double)nodes.size();
			ExtractAttractorBasins.updateProgress(eab, nodes.size());
		
		
			System.gc();
		}catch(IOException ioe){
			
		}catch(AlgorithmExecutionException aee){
			
		}
	}
	
	private void recursiveStrongComponentCalculation(int nodeNumber, int[] strongComponentColumn,int[] preOrderColumn, Stack firstStack, Stack secondStack, Integer count, Table attractorTable, NWBFileWriter nfw){
		int v;
		count = new Integer(count.intValue()+1);
		preOrderColumn[nodeNumber] = count.intValue();
		firstStack.push(new Integer(nodeNumber));
		secondStack.push(new Integer(nodeNumber));

		int outNode = this.simpleStateGraph.getOutNeighbor(nodeNumber).intValue();
		if(preOrderColumn[outNode] == -1) 
			recursiveStrongComponentCalculation(outNode,strongComponentColumn,preOrderColumn,firstStack,secondStack, count,attractorTable,nfw);
		else if (strongComponentColumn[outNode]  == -1){
			while (preOrderColumn[((Integer)secondStack.peek()).intValue()] > preOrderColumn[outNode]){ 
				secondStack.pop();

			}

		}


		if(((Integer)secondStack.peek()).intValue() == nodeNumber){ 
			secondStack.pop();
		}
		else{
			return;
		}


		do {
			v = ((Integer)firstStack.pop()).intValue();
			componentMembers.push(new Integer(v));
			strongComponentColumn[v] = this.strongComponents;
		} while (nodeNumber != v);


		if(componentMembers.size() > 1){
			int componentSize = componentMembers.size();
			while(!componentMembers.isEmpty()){
				int n1 = ((Integer)componentMembers.pop()).intValue();
				double nodeRobustness;
				String label = getLabel(n1);
				BigInteger stateSpace = convertStringToBigInt(label,nodeStates);
				nodeRobustness = calculateNodeRobustness(stateSpace,nodeStates,nodes);
				this.observedCoherency+=nodeRobustness;
				writeNode(n1,label,componentSize,nodeRobustness,nfw);
				//this.simpleStateGraph.setAttractor(n1, componentSize);
				annotateAttractorTable(attractorTable,label,true);
			}
		}
		if(componentMembers.size() == 1){
			int n1 = ((Integer)componentMembers.pop()).intValue();
			int target = this.simpleStateGraph.getOutNeighbor(n1).intValue();
			double nodeRobustness;
			String label = getLabel(n1);
			BigInteger stateSpace = convertStringToBigInt(label,nodeStates);
			nodeRobustness = calculateNodeRobustness(stateSpace,nodeStates,nodes);
			this.observedCoherency += nodeRobustness;
			if(target == n1){
				//this.simpleStateGraph.setAttractor(target, 1);
				annotateAttractorTable(attractorTable,label,true);
				writeNode(n1,label,1,nodeRobustness,nfw);
			}
			else{
				writeNode(n1,label,0,nodeRobustness,nfw);
			}
		}

		componentMembers = new Stack();
		this.strongComponents++;
	}
	
	private String getLabel(int nodeID){
		BigInteger labelInteger = new BigInteger(new Integer(nodeID).toString());
		int[] labelArray = new int[this.simpleStateGraph.labelSize];
		labelArray = convertBigIntToIntArray(labelInteger,labelArray,nodeStates);
		String label = convertIntArrayToString(labelArray);
		
		return label;
	}
	
	private Table constructAttractorTable(Table orgTable, String labelColumn, boolean isHorizontal){
		Table attractorTable = new Table();
		if(!isHorizontal){
			attractorTable.addColumn("Label", String.class);
		}
		for(int i = 0; i < orgTable.getRowCount(); i++){
			if(isHorizontal){
				if(labelColumn == null || labelColumn.equals("")){
					attractorTable.addColumn("x" + (i+1), int.class);
				}else{
					attractorTable.addColumn(orgTable.getString(i, labelColumn), int.class);
				}
			}else{
				int rowNumber = attractorTable.addRow();
				if(labelColumn == null || labelColumn.equals("")){
					attractorTable.setString(rowNumber, "Label", "x"+(i+1));
				}
				else{
					attractorTable.setString(rowNumber, "Label", orgTable.getString(i, labelColumn));
				}
			}
		}
		return attractorTable;
	}
	
	private Table annotateAttractorTable(Table attractorTable, String value, boolean isHorizontal){
		String[] discreteValues = value.split("\\s+");
		if(isHorizontal){
			int rowNumber = attractorTable.addRow();
			for(int i = 0; i < discreteValues.length; i++){
				attractorTable.setInt(rowNumber, i, new Integer(discreteValues[i]).intValue());
			}
		}else{
			attractorTable.addColumn(new Integer(attractorTable.getColumnCount()).toString(), int.class);
			for(int i = 0; i < discreteValues.length; i++){
				attractorTable.setInt(0, attractorTable.getColumnCount()-1, new Integer(discreteValues[i]).intValue());
			}
		}
		return attractorTable;
	}

	public int getSystemSize(){
		return this.systemSize;
	}

	protected double calculateNodeRobustness(BigInteger nodeState, int nodeStates, LinkedHashSet nodes){
		int[] nodeStateArray = new int[this.systemSize];
		int[] checkStateArray = new int[this.systemSize];
		BigInteger checkState = BigInteger.ZERO;
		double counter = 0;
		Integer checkStateInteger;
		nodeStateArray = convertBigIntToIntArray(nodeState,nodeStateArray,nodeStates);

		for(int i = 0; i < nodeStateArray.length; i++){
			checkStateArray = convertBigIntToIntArray(nodeState,checkStateArray,nodeStates);
			for(int j = 1; j < nodeStates; j++){

				checkStateArray[i] = ((nodeStateArray[i]+j) % nodeStates);
				checkState = new BigInteger(convertIntArrayToString(checkStateArray,nodeStates),nodeStates);
				checkStateInteger = new Integer(checkState.intValue());
				if(nodes.contains(checkStateInteger)){
					counter++;
				}
			}
		}

		counter = counter/((nodeStates-1)*nodeStateArray.length);
		return counter;

	}

	protected static String convertIntArrayToString(final int[] stateSpace, int numberOfStates){
		StringBuffer s = new StringBuffer();
		for(int i = 0; i < stateSpace.length; i++){
			s.append(Character.forDigit(stateSpace[i], numberOfStates));
		}
		return s.toString();
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

	protected static int convertBigIntegerToCorrectInt(BigInteger bi, int[] stateSpace,int nodeStates){
		int[] convertedIntArray = convertBigIntToIntArray(bi,stateSpace,nodeStates);
		int returnValue = new Integer(convertIntArrayToString(convertedIntArray,nodeStates)).intValue();
		return returnValue;
	}

	public File getAttractorBasin(){
		return this.basinGraph;
	}



	public Integer getBasinSize(){
		return new Integer(this.basinSize);
	}


	public File getAttractorTable(){
		return this.attractorTable;
	}
	
	private static NWBFileWriter createNWBFileWriter(File stateSpaceFile) throws AlgorithmExecutionException{
		try{
		NWBFileWriter stateSpaceWriter = new NWBFileWriter(stateSpaceFile);
		LinkedHashMap nodeSchema = NWBFileWriter.getDefaultNodeSchema();
		nodeSchema.put("attractor", int.class);
		nodeSchema.put("observedrobustness","real");
		stateSpaceWriter.setNodeSchema(nodeSchema);
		
		return stateSpaceWriter;
		}catch(IOException ioe){
			throw new AlgorithmExecutionException("Error creating and writing the state space graph file.\n",ioe);
		}
	}

	private static File generateAttractorCSV(final Table t) throws AlgorithmExecutionException{
		try{
			File attractorTableFile = File.createTempFile("NWB-Session-StateSpace-", ".csv");
			FileOutputStream fis = new FileOutputStream(attractorTableFile);
			PrintWriter pw = new PrintWriter(fis);
			for(int i = 0; i < t.getColumnCount(); i++){
				pw.print("\""+t.getColumnName(i)+"\",");
			}
			pw.println();
			for(int i = 0; i < t.getRowCount(); i++){
				for(int j = 0; j < t.getColumnCount();j++){
					pw.print("\""+t.get(i,j)+"\",");
				}
				pw.println();
			}
			pw.flush();
			pw.close();

			return attractorTableFile;
		}catch(IOException ioe){
			throw new AlgorithmExecutionException("Error creating and writing the attractor table file.\n",ioe);
		}	
	}

	private static void writeNode(int id, String label, int attractor, double observedRobustness, NWBFileWriter nfw){

		
			HashMap columnValues = new HashMap();

			columnValues.put("attractor", new Integer(attractor));
			columnValues.put("observedrobustness", new Double(observedRobustness));

			nfw.addNode(id+1, label, columnValues);

		
	}

	private static void writeEdges(HashMap outNeighbors, LinkedHashSet sources, NWBFileWriter nfw){

		for(Iterator it = sources.iterator(); it.hasNext();){
			int source = ((Integer)it.next()).intValue();
			int target = ((Integer)outNeighbors.get(new Integer(source))).intValue();


			nfw.addDirectedEdge(source+1, target+1,null);

		}
	}
	
	protected static BigInteger convertStringToBigInt(String state,int nodeStates){
		String stateString = state.replaceAll("\\s+", "");
		BigInteger bi = new BigInteger(stateString,nodeStates);
		return bi;
	}
	

	protected static String convertIntArrayToString(final int[] stateSpace){
		String s = "";
		for(int i = 0; i < stateSpace.length; i++){
			s += stateSpace[i]+ " ";		
		}	
		return s.trim();
	}

}
