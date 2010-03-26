package edu.iu.nwb.analysis.extractattractors.components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Stack;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

import prefuse.data.Table;
import edu.iu.nwb.analysis.extractattractors.containers.BigArray;
import edu.iu.nwb.analysis.extractattractors.containers.BigBigIntegerArray;
import edu.iu.nwb.analysis.extractattractors.containers.BigHashSetVector;
import edu.iu.nwb.analysis.extractattractors.containers.BigStackVector;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class BasinConstructorThread extends Thread{
	ArrayList<File> basinGraphs = new ArrayList<File>();
	ArrayList<File> attractorTables = new ArrayList<File>();
	ArrayList<BigInteger> basinSizes = new ArrayList<BigInteger>();
	LinkedList<BigHashSetVector> bigNodesQueue = null;
	LinkedList<HashSet<Integer>> smallNodesQueue = null;
	SimpleStateSpaceGraph simpleStateGraph = null;
	static Table originalTable;
	final String labelColumn;
	ArrayList<Double> observedCoherencys = new ArrayList<Double>();
	BigInteger twentyPercent;
	BigInteger strongComponents = BigInteger.ZERO;
	ExtractAttractorBasins eab;
	BigInteger totalSize = BigInteger.ZERO;

	BigBigIntegerArray bigPreOrderColumn = null;
	BigBigIntegerArray bigStrongComponentColumn = null;

	int[] preOrderColumn = null;
	int[] strongComponentColumn = null;

	public BasinConstructorThread(SimpleStateSpaceGraph sssg, final Table originalTable, final String labelColumn, ExtractAttractorBasins eab){
		super();
		this.simpleStateGraph = sssg;
		this.originalTable = originalTable;
		this.labelColumn = labelColumn;
		this.eab = eab;
	}


	public void setSmallArrays(int[] preOrder, int[] strongComponent){
		this.preOrderColumn = preOrder;
		this.strongComponentColumn = strongComponent;
	}

	public void setBigArrays(BigBigIntegerArray preOrder, BigBigIntegerArray strongComponent){
		this.bigPreOrderColumn = preOrder;
		this.bigStrongComponentColumn = strongComponent;
	}

	public void queueSmallNodes(HashSet<Integer> nodes){
		if(this.smallNodesQueue == null)
			this.smallNodesQueue = new LinkedList<HashSet<Integer>>();
		this.smallNodesQueue.add(nodes);
		this.totalSize = this.totalSize.add(new BigInteger(new Integer(nodes.size()).toString()));
		this.observedCoherencys.add(new Double(0));
	}

	public void queueBigNodes(BigHashSetVector nodes){
		if(this.bigNodesQueue == null)
			this.bigNodesQueue = new LinkedList<BigHashSetVector>();
		this.bigNodesQueue.add(nodes);
		this.totalSize = this.totalSize.add(nodes.size());
		this.observedCoherencys.add(new Double(0));
	}

	public double getObservedCoherency(int basinNumber){
		return this.observedCoherencys.get(basinNumber).doubleValue();
	}

	public static int smallCalculations(HashSet<Integer> nodes, BasinConstructorThread bct, Table attractorTable, int basinNumber, NWBFileWriter fileWriter){
		Stack<Integer> firstStack = new Stack<Integer>();
		Stack<Integer> secondStack = new Stack<Integer>();
		Integer testCount = new Integer(0);
		int calculatedNodes = 0;
		int nodeID;
		for(Iterator<Integer> intIt = nodes.iterator(); intIt.hasNext();){

			nodeID = intIt.next().intValue();
			calculatedNodes++;
			if(calculatedNodes%bct.twentyPercent.intValue() == 0){
				bct.eab.updateProgress(bct.eab, 20);
				calculatedNodes = 0;
			}
			if(bct.preOrderColumn[nodeID] == -1){
				smallRecursiveStrongComponentCalculation(bct,nodes,nodeID,firstStack,secondStack,testCount,attractorTable,basinNumber,fileWriter);
			}
		}
		
		return calculatedNodes;
	}

	public static void bigCalculations(BigHashSetVector nodes, BasinConstructorThread bct, Table attractorTable, int basinNumber, HashMap<BigInteger,Integer> integerMappings,NWBFileWriter fileWriter){
		BigStackVector firstStack = new BigStackVector();
		BigStackVector secondStack = new BigStackVector();
		BigInteger testCount = BigInteger.ONE;
		BigInteger calculatedNodes = BigInteger.ZERO;
		BigInteger nodeID;
		for(Iterator<BigInteger> it = nodes.iterator(); it.hasNext();){
			nodeID = it.next();
			calculatedNodes = calculatedNodes.add(BigInteger.ONE);
			if(bct.bigPreOrderColumn.get(nodeID).compareTo(BigInteger.ZERO) == 0){
				bigRecursiveStrongComponentCalculation(bct,nodes,nodeID,firstStack,secondStack,testCount,attractorTable,basinNumber,integerMappings,fileWriter);
			}
		}

	}

	public void setTwentyPercent(BigInteger bi){
		this.twentyPercent = bi;
	}

	public void run() {
		try{
			if(this.smallNodesQueue != null)
				smallExecute(this);
			if(this.bigNodesQueue != null)
				bigExecute(this);


		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(AlgorithmExecutionException aee){
			aee.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}

	public static void smallExecute(BasinConstructorThread bct)throws IOException, AlgorithmExecutionException{
		int basinNumber = 0;
		int calculatedNodes = 0;
		while(!bct.smallNodesQueue.isEmpty()){
			HashSet<Integer> nodes = bct.smallNodesQueue.removeFirst();


			Table attractorTable = constructAttractorTable(bct.originalTable, bct.labelColumn,true);
			File stateSpaceFile = File.createTempFile("Attractors-StateSpace-", ".nwb");
			NWBFileWriter stateSpaceFileWriter = createNWBFileWriter(stateSpaceFile);

			calculatedNodes += BasinConstructorThread.smallCalculations(nodes, bct, attractorTable, basinNumber, stateSpaceFileWriter);
			if(calculatedNodes%bct.twentyPercent.intValue() == 0){
				bct.eab.updateProgress(bct.eab, 20);
				calculatedNodes = 0;
			}
			
			bct.eab.updateProgress(bct.eab, (calculatedNodes/bct.simpleStateGraph.maxSize.intValue())*100);
			stateSpaceFileWriter.setDirectedEdgeSchema(NWBFileWriter.getDefaultEdgeSchema());
			writeSmallEdges(bct,nodes,stateSpaceFileWriter);
			stateSpaceFileWriter.haltParsingNow();
			bct.basinGraphs.add(stateSpaceFile);
			bct.attractorTables.add(generateAttractorCSV(attractorTable));
			bct.basinSizes.add(new BigInteger(new Integer(nodes.size()).toString()));

			bct.observedCoherencys.set(basinNumber, new Double(new BigDecimal(bct.observedCoherencys.get(basinNumber).toString()).divide(new BigDecimal(bct.getBasinSize(basinNumber)),7,BigDecimal.ROUND_DOWN).doubleValue()));
			basinNumber++;
		}
	}

	public static void bigExecute(BasinConstructorThread bct)throws IOException, AlgorithmExecutionException{
		int basinNumber = 0;
		HashMap<BigInteger,Integer> integerMapping = null;
		while(!bct.bigNodesQueue.isEmpty()){
			BigHashSetVector nodes = bct.bigNodesQueue.removeFirst();

			Table attractorTable = constructAttractorTable(bct.originalTable,bct.labelColumn,true);
			File stateSpaceFile = null;
			NWBFileWriter stateSpaceFileWriter = null;

			if(nodes.size().compareTo(BigArray.maxInteger) < 0){
				stateSpaceFile = File.createTempFile("Attractors-StateSpace-", ".nwb");
				stateSpaceFileWriter = createNWBFileWriter(stateSpaceFile);
				integerMapping = new HashMap<BigInteger,Integer>(nodes.size().intValue());
				int i = 0;
				for(Iterator<BigInteger> it = nodes.iterator(); it.hasNext();){
					BigInteger key = it.next();
					integerMapping.put(key, new Integer(i));
					i++;
				}
			}

			
			BasinConstructorThread.bigCalculations(nodes,bct,attractorTable,basinNumber,integerMapping,stateSpaceFileWriter);

			if(stateSpaceFileWriter != null){
				stateSpaceFileWriter.setDirectedEdgeSchema(NWBFileWriter.getDefaultEdgeSchema());
				writeBigEdges(bct,nodes,integerMapping,stateSpaceFileWriter);
				stateSpaceFileWriter.haltParsingNow();
			}

			bct.basinGraphs.add(stateSpaceFile);
			bct.attractorTables.add(generateAttractorCSV(attractorTable));
			bct.basinSizes.add(nodes.size());
			bct.observedCoherencys.set(basinNumber, new Double(new BigDecimal(bct.observedCoherencys.get(basinNumber).toString()).divide(new BigDecimal(bct.getBasinSize(basinNumber)),7,BigDecimal.ROUND_DOWN).doubleValue()));
			basinNumber++;
		}
	}

	private static void bigRecursiveStrongComponentCalculation(BasinConstructorThread bct, BigHashSetVector nodes, BigInteger nodeNumber,BigStackVector firstStack, BigStackVector secondStack,BigInteger count, Table attractorTable, int basinNumber, HashMap<BigInteger,Integer>integerMapping,NWBFileWriter nfw){
		BigInteger v;
		count = count.add(BigInteger.ONE);
		bct.bigPreOrderColumn.put(nodeNumber, count);
		firstStack.push(nodeNumber);
		secondStack.push(nodeNumber);

		BigInteger outNode = bct.simpleStateGraph.getOutNeighbor(nodeNumber);
		if(bct.bigPreOrderColumn.get(outNode).compareTo(BigInteger.ZERO)==0){ 
			bigRecursiveStrongComponentCalculation(bct,nodes,outNode,firstStack,secondStack, count,attractorTable,basinNumber,integerMapping,nfw);
		}
		else if (bct.bigStrongComponentColumn.get(outNode).compareTo(BigInteger.ZERO) == 0){
			while (bct.bigPreOrderColumn.get(secondStack.peek()).compareTo(bct.bigPreOrderColumn.get(outNode)) > 0){ 
				secondStack.pop();
			}

		}


		if(secondStack.peek() == nodeNumber){ 
			secondStack.pop();
		}
		else{
			return;
		}

		BigStackVector componentMembers = new BigStackVector();

		do {
			v = firstStack.pop();
			componentMembers.push(v);
			bct.bigStrongComponentColumn.put(v, bct.strongComponents);
		} while (nodeNumber.compareTo(v) != 0);

		if(componentMembers.size().compareTo(BigInteger.ONE) > 1){
			while(!componentMembers.isEmpty()){
				BigInteger nOne = componentMembers.pop();
				double nodeRobustness;
				String label = getLabel(nOne,bct.simpleStateGraph.labelSize,bct.simpleStateGraph.nodeStates);
				nodeRobustness = calculateBigNodeRobustness(nOne,bct.simpleStateGraph.nodeStates,nodes,bct.simpleStateGraph.labelSize);
				bct.observedCoherencys.set(basinNumber, new Double(bct.observedCoherencys.get(basinNumber).doubleValue()+nodeRobustness)) ;
				if(nfw != null)
					writeBigNode(nOne,label,componentMembers.size(),nodeRobustness,integerMapping,nfw);
				annotateAttractorTable(attractorTable,label,true);
			}
		}
		else{

			BigInteger nOne = componentMembers.pop();
			BigInteger target = bct.simpleStateGraph.getOutNeighbor(nOne);
			double nodeRobustness;
			String label = getLabel(nOne,bct.simpleStateGraph.labelSize,bct.simpleStateGraph.nodeStates);
			nodeRobustness = calculateBigNodeRobustness(nOne,bct.simpleStateGraph.nodeStates,nodes,bct.simpleStateGraph.labelSize);

			bct.observedCoherencys.set(basinNumber, new Double(bct.observedCoherencys.get(basinNumber).doubleValue()+nodeRobustness)) ;
			if(target.compareTo(nOne) == 0){
				//this.simpleStateGraph.setAttractor(target, 1);
				annotateAttractorTable(attractorTable,label,true);
				if(nfw != null)
					writeBigNode(nOne,label,BigInteger.ONE,nodeRobustness,integerMapping,nfw);
			}
			else{
				if(nfw != null)
					writeBigNode(nOne,label,BigInteger.ZERO,nodeRobustness,integerMapping,nfw);
			}
		}

		bct.strongComponents = bct.strongComponents.add(BigInteger.ONE);

	}

	private static void smallRecursiveStrongComponentCalculation(BasinConstructorThread bct,HashSet<Integer> nodes,Integer nodeNumber,Stack<Integer> firstStack, Stack<Integer> secondStack, Integer count,Table attractorTable, int basinNumber,NWBFileWriter nfw){
		Integer v;
		count = new Integer(count+1);
		bct.preOrderColumn[nodeNumber.intValue()] = count;
		firstStack.push(nodeNumber);
		secondStack.push(nodeNumber);

		int outNode = bct.simpleStateGraph.getOutNeighbor(new BigInteger(nodeNumber.toString())).intValue();

		if(bct.preOrderColumn[outNode] == -1){
			smallRecursiveStrongComponentCalculation(bct,nodes,outNode,firstStack,secondStack, count,attractorTable,basinNumber,nfw);
		}
		else if(bct.strongComponentColumn[outNode]== -1){
			while(bct.preOrderColumn[secondStack.peek().intValue()] > bct.preOrderColumn[outNode]){	
				secondStack.pop();
			}

		}


		if(secondStack.peek() == nodeNumber){ 
			secondStack.pop();
		}
		else{
			return;
		}

		Stack<Integer> componentMembers = new Stack<Integer>();

		do {
			v = firstStack.pop();
			componentMembers.push(v);
			bct.strongComponentColumn[v.intValue()] = bct.strongComponents.intValue();
		} while (nodeNumber.compareTo(v) != 0);

		if(componentMembers.size() > 1){
			while(!componentMembers.isEmpty()){
				Integer n1 = componentMembers.pop();
				BigInteger nOne = new BigInteger(n1.toString());
				double nodeRobustness;
				String label = getLabel(nOne,bct.simpleStateGraph.labelSize,bct.simpleStateGraph.nodeStates);
				nodeRobustness = calculateSmallNodeRobustness(nOne,bct.simpleStateGraph.nodeStates,nodes,bct.simpleStateGraph.labelSize);
				bct.observedCoherencys.set(basinNumber, new Double(bct.observedCoherencys.get(basinNumber).doubleValue()+nodeRobustness)) ;
				writeSmallNode(nOne,label,new BigInteger(new Integer(componentMembers.size()).toString()),nodeRobustness,nfw);
				annotateAttractorTable(attractorTable,label,true);
			}
		}
		else{
			Integer n1 = componentMembers.pop();
			BigInteger nOne = new BigInteger(n1.toString());
			BigInteger target = bct.simpleStateGraph.getOutNeighbor(new BigInteger(n1.toString()));
			double nodeRobustness;
			String label = getLabel(new BigInteger(n1.toString()),bct.simpleStateGraph.labelSize,bct.simpleStateGraph.nodeStates);
			nodeRobustness = calculateSmallNodeRobustness(nOne,bct.simpleStateGraph.nodeStates,nodes,bct.simpleStateGraph.labelSize);

			bct.observedCoherencys.set(basinNumber, new Double(bct.observedCoherencys.get(basinNumber).doubleValue()+nodeRobustness)) ;
			if(target.compareTo(nOne) == 0){
				annotateAttractorTable(attractorTable,label,true);
				writeSmallNode(nOne,label,BigInteger.ONE,nodeRobustness,nfw);
			}
			else{
				writeSmallNode(nOne,label,BigInteger.ZERO,nodeRobustness,nfw);
			}
		}

		bct.strongComponents = bct.strongComponents.add(BigInteger.ONE);

	}

	private static String getLabel(BigInteger nodeID, int systemSize,int nodeStates){
		//BigInteger labelInteger = new BigInteger(new Integer(nodeID).toString());
		int[] labelArray = new int[systemSize];
		labelArray = convertBigIntToIntArray(nodeID,labelArray,nodeStates);
		String label = convertIntArrayToString(labelArray);

		return label;
	}

	private static Table constructAttractorTable(Table orgTable, String labelColumn, boolean isHorizontal){
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

	private static Table annotateAttractorTable(Table attractorTable, String value, boolean isHorizontal){
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



	protected static double calculateBigNodeRobustness(BigInteger nodeState, int nodeStates, BigHashSetVector nodes, int systemSize){
		int[] nodeStateArray = new int[systemSize];
		int[] checkStateArray = new int[systemSize];
		BigInteger checkState = BigInteger.ZERO;
		double counter = 0;
		//Integer checkStateInteger;
		nodeStateArray = convertBigIntToIntArray(nodeState,nodeStateArray,nodeStates);

		for(int i = 0; i < nodeStateArray.length; i++){
			checkStateArray = convertBigIntToIntArray(nodeState,checkStateArray,nodeStates);
			for(int j = 1; j < nodeStates; j++){

				checkStateArray[i] = ((nodeStateArray[i]+j) % nodeStates);
				checkState = new BigInteger(convertIntArrayToString(checkStateArray,nodeStates),nodeStates);
				//checkStateInteger = new Integer(checkState.intValue());
				if(nodes.contains(checkState)){
					counter++;
				}
			}
		}

		counter = counter/((double)(nodeStates-1)*(double)nodeStateArray.length);
		return counter;

	}

	protected static double calculateSmallNodeRobustness(BigInteger nodeState, int nodeStates, HashSet<Integer> nodes, int systemSize){
		int[] nodeStateArray = new int[systemSize];
		int[] checkStateArray = new int[systemSize];
		BigInteger checkState = BigInteger.ZERO;
		double counter = 0;
		//Integer checkStateInteger;
		nodeStateArray = convertBigIntToIntArray(nodeState,nodeStateArray,nodeStates);

		for(int i = 0; i < nodeStateArray.length; i++){
			checkStateArray = convertBigIntToIntArray(nodeState,checkStateArray,nodeStates);
			for(int j = 1; j < nodeStates; j++){

				checkStateArray[i] = ((nodeStateArray[i]+j) % nodeStates);
				checkState = new BigInteger(convertIntArrayToString(checkStateArray,nodeStates),nodeStates);
				//checkStateInteger = new Integer(checkState.intValue());
				if(nodes.contains(new Integer(checkState.intValue()))){
					counter++;
				}
			}
		}

		counter = counter/((double)(nodeStates-1)*(double)nodeStateArray.length);
		return counter;

	}

	protected static double calculateNodeRobustness(BigInteger nodeState, int nodeStates, HashSet<String> nodes, int systemSize){
		int[] nodeStateArray = new int[systemSize];
		int[] checkStateArray = new int[systemSize];
		BigInteger checkState = BigInteger.ZERO;
		double counter = 0.0;
		//Integer checkStateInteger;
		nodeStateArray = convertBigIntToIntArray(nodeState,nodeStateArray,nodeStates);

		for(int i = 0; i < nodeStateArray.length; i++){
			checkStateArray = convertBigIntToIntArray(nodeState,checkStateArray,nodeStates);
			for(int j = 1; j < nodeStates; j++){

				checkStateArray[i] = ((nodeStateArray[i]+j) % nodeStates);
				checkState = new BigInteger(convertIntArrayToString(checkStateArray,nodeStates),nodeStates);
				//checkStateInteger = new Integer(checkState.intValue());
				if(nodes.contains(checkState.toString())){
					counter++;
				}
			}
		}

		counter = counter/((double)(nodeStates-1)*(double)nodeStateArray.length);
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

	public File getAttractorBasin(int basinNumber){
		return this.basinGraphs.get(basinNumber);
	}



	public BigInteger getBasinSize(int basinNumber){
		return this.basinSizes.get(basinNumber);
	}


	public File getAttractorTable(int basinNumber){
		return this.attractorTables.get(basinNumber);
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
			File attractorTableFile = File.createTempFile("Attractors-StateSpace-", ".csv");
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

	private static void writeSmallNode(BigInteger id, String label, BigInteger attractor, double observedRobustness, NWBFileWriter nfw){
		

		HashMap columnValues = new HashMap();

		columnValues.put("attractor", new Integer(attractor.intValue()));
		columnValues.put("observedrobustness", new Double(observedRobustness));

		nfw.addNode(id.intValue()+1, label, columnValues);


	}
	
	private static void writeBigNode(BigInteger id, String label, BigInteger attractor, double observedRobustness, HashMap<BigInteger,Integer> integerMapping,NWBFileWriter nfw){
		HashMap columnValues = new HashMap();

		columnValues.put("attractor", new Integer(attractor.intValue()));
		columnValues.put("observedrobustness", new Double(observedRobustness));

		nfw.addNode(integerMapping.get(id).intValue()+1, label, columnValues);

	}
	
	

	private static void writeBigEdges(BasinConstructorThread bct, BigHashSetVector sources, HashMap<BigInteger,Integer> integerMapping,NWBFileWriter nfw){		
		for(Iterator<BigInteger> it = sources.iterator(); it.hasNext();){
			BigInteger source = it.next();
			BigInteger target = bct.simpleStateGraph.getOutNeighbor(source);

			if(integerMapping != null)
				nfw.addDirectedEdge(integerMapping.get(source).intValue()+1, integerMapping.get(target).intValue()+1,null);
			else
				nfw.addDirectedEdge(source.intValue(), target.intValue(), null);
		}
	}

	private static void writeSmallEdges(BasinConstructorThread bct, HashSet<Integer> sources, NWBFileWriter nfw){

		for(Iterator<Integer> it = sources.iterator(); it.hasNext();){
			Integer source = it.next();
			Integer target = new Integer(bct.simpleStateGraph.getOutNeighbor(new BigInteger(source.toString())).intValue());


			nfw.addDirectedEdge(source.intValue()+1, target.intValue()+1,null);

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
