package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import prefuse.data.CascadedTable;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Table;

public class EdgeStats extends Thread {	
	double[] meanValues;
	double[] maxValues;
	double[] minValues;
	double[] weightedDensitySum;
	String[] characteristicValues;
	
	boolean isValuedNetwork = false;

	int numberOfEdges;

	Vector nonNumericAttributes;
	Vector numericAttributes;

	SelfLoopsParallelEdges selfLoopsParallelEdges;
	
	private Graph edgeGraph;
	
	public void run(){
		
		initializeAdditionalAttributes(this.edgeGraph.getEdgeTable(),this);
		
		CascadedTable derivativeTable = new CascadedTable(this.edgeGraph.getEdgeTable());
		derivativeTable.addColumn("visited", boolean.class, new Boolean(false));
		
		
		int additionalNumericAttributes = this.numericAttributes.size();
		
		if(additionalNumericAttributes > 0){
		  this.weightedDensitySum = new double[additionalNumericAttributes];
		  this.meanValues = new double[additionalNumericAttributes];
		  this.maxValues = new double[additionalNumericAttributes];
		  this.minValues = new double[additionalNumericAttributes];
		  
		  java.util.Arrays.fill(this.maxValues, Double.MIN_VALUE);
		  java.util.Arrays.fill(this.minValues, Double.MAX_VALUE);
		}
		
		this.calculateEdgeStats(derivativeTable);
	}
	
	public static EdgeStats constructEdgeStats(final Graph graph){
		EdgeStats edgeStats = new EdgeStats();
		
		edgeStats.numberOfEdges = graph.getEdgeCount();
		edgeStats.selfLoopsParallelEdges = new SelfLoopsParallelEdges(graph);
		edgeStats.edgeGraph = graph;
	
		
		return edgeStats;
	}
	
	private static EdgeStats initializeAdditionalAttributes(final Table edgeTable, EdgeStats es){
		es.nonNumericAttributes = new Vector();
		es.numericAttributes = new Vector();
		int numberOfAdditionalAttributes = edgeTable.getColumnCount()-2;
		if(numberOfAdditionalAttributes > 0){
			for(int i = 0; i < edgeTable.getColumnCount(); i++){
				String columnName = edgeTable.getColumnName(i);
				if(!(columnName.equals("source") || columnName.equals("target"))){
					if(edgeTable.getColumn(i).canGet(Number.class)){
						es.numericAttributes.add(columnName);
					}else{
						es.nonNumericAttributes.add(columnName);
					}
				}
			}
		}
		
		if(es.nonNumericAttributes.size() > 0){
			es.characteristicValues = new String[es.nonNumericAttributes.size()];
		}
		if(es.numericAttributes.size() > 0){
			es.isValuedNetwork = true;
		}
		
		return es;
	}

	private void addEdge(final Edge e, CascadedTable derivativeTable, HashSet observedValues){
		if(!derivativeTable.getBoolean(e.getRow(), derivativeTable.getColumnNumber("visited"))){
				processEdgeAttributes(e,this.nonNumericAttributes,observedValues,false);
				processEdgeAttributes(e,this.numericAttributes,observedValues,true);
				derivativeTable.setBoolean(e.getRow(), derivativeTable.getColumnNumber("visited"), true);
		}
	}
	
	private void processEdgeAttributes(final Edge e, final Vector attributeNames, HashSet observedValues, boolean isNumeric){
		for(int i = 0; i < attributeNames.size(); i++){
			String columnName = (String)attributeNames.get(i);
			if(isNumeric){
				double value = ((java.lang.Number)e.get(columnName)).doubleValue();
				this.meanValues[i] += (value)/e.getGraph().getEdgeCount();
				this.weightedDensitySum[i] += value;
				if(value > this.maxValues[i])
					this.maxValues[i] = value;
				if(value < this.minValues[i])
					this.minValues[i] = value;
				
				if(attributeNames.size() == 1){
					
					observedValues.add(e.get(columnName));
				}
			}
			else{
				this.characteristicValues[i] = e.get(columnName).toString();
			}
		}
	}


	public void calculateEdgeStats(CascadedTable visitedTable){
		HashSet observedValues = null;
		if (numericAttributes.size() == 1) {
			observedValues = new HashSet();
		}
		
		for (Iterator edgeIt = edgeGraph.edges(); edgeIt.hasNext();){
			Edge e = (Edge) edgeIt.next();
			
			addEdge(e, visitedTable, observedValues);
		}
		
		if (numericAttributes.size() == 1) {
			if(observedValues.size() > 1) {
				isValuedNetwork = true;
			}
		}
		
	}

	public SelfLoopsParallelEdges getSelfLoopsParallelEdges(){
		return this.selfLoopsParallelEdges;
	}

	public int getNumberOfSelfLoops(){
		return this.selfLoopsParallelEdges.getNumSelfLoops();
	}

	public int getNumberOfParallelEdges(){
		return this.selfLoopsParallelEdges.getNumParallelEdges();
	}

	public double[] getWeightedDensitySumArray(){
		return this.weightedDensitySum;
	}

	public double[] getMaxValueArray(){
		return this.maxValues;
	}
	
	
	protected String[] getAdditionalNumericAttributes(){
		String[] numericAttributeNames = new String[this.numericAttributes.size()];
		return (String[])this.numericAttributes.toArray(numericAttributeNames);
	}

	protected String appendParallelEdgeInfo(){
		StringBuffer sb = new StringBuffer();
		int parallelEdges = this.getNumberOfParallelEdges();
		if(parallelEdges > 0){
			sb.append("There are: " + parallelEdges + " parallel edges.\n" + 
			"They are as follows:");
			sb.append(System.getProperty("line.separator"));
			sb.append(this.getSelfLoopsParallelEdges().printParallelEdges());
			sb.append(System.getProperty("line.separator"));
		}
		else{
			sb.append("No parallel edges were discovered.");
			sb.append(System.getProperty("line.separator"));
		}

		return sb.toString();
	}

	protected String selfLoopInfo(){
		StringBuffer sb = new StringBuffer();
		if(this.getNumberOfSelfLoops() > 0){
			sb.append("There are: " + this.getNumberOfSelfLoops() + " self-loops. \n" + 
			"They are as follows:");
			sb.append(System.getProperty("line.separator"));
			sb.append(this.getSelfLoopsParallelEdges().printSelfLoops());
			sb.append(System.getProperty("line.separator"));
		}
		else{
			sb.append("No self loops were discovered.");
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	protected String appendEdgeInfo(){
		StringBuffer sb = new StringBuffer();
		
		sb.append("Edges: " + this.numberOfEdges);
		sb.append(System.getProperty("line.separator"));
		sb.append(this.selfLoopInfo());
		sb.append(this.appendParallelEdgeInfo());
		sb.append(System.getProperty("line.separator"));
		
		int nonNumericAttributesSize = this.nonNumericAttributes.size();
		int numericAttributesSize = this.numericAttributes.size();
		if((nonNumericAttributesSize+numericAttributesSize) > 0){
		sb.append("Edge attributes:");
		sb.append(System.getProperty("line.separator"));
		if(nonNumericAttributesSize > 0){
			sb.append("\tNonnumeric attributes:");
			sb.append(System.getProperty("line.separator"));
			sb.append(printEdgeAttributes(this.nonNumericAttributes,false));
		}
		else{
			sb.append("\tDid not detect any nonnumeric attributes");
		}
		sb.append(System.getProperty("line.separator"));
		
		if(numericAttributesSize > 0){
			sb.append("\tNumeric attributes:");
			sb.append(System.getProperty("line.separator"));
			sb.append(printEdgeAttributes(this.numericAttributes,true));
		}
		else{
			sb.append("\tDid not detect any numeric attributes");
		}
		
		sb.append(System.getProperty("line.separator"));
		}
		else{
			sb.append("\tDid not detect any edge attributes");
			sb.append(System.getProperty("line.separator"));
		}
		if(this.isValuedNetwork){
			sb.append("\tThis network seems to be valued.");
			sb.append(System.getProperty("line.separator"));
		}
		else{
			sb.append("\tThis network does not seem to be a valued network.");
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
		
		private String printEdgeAttributes(Vector attributeNames, boolean isNumeric){
			StringBuffer sb = new StringBuffer();
			DecimalFormat densityFormatter = null;
			
			if (isNumeric) {
				sb.append("\t\t\t\tmin\tmax\tmean");
				sb.append(System.getProperty("line.separator"));
				densityFormatter = new DecimalFormat("#.#####");
			} else {
				sb.append("\t\t\t\tExample value");
				sb.append(System.getProperty("line.separator"));
			}
			
			for (int i = 0; i < attributeNames.size(); i++) {
				String attributeName = (String) attributeNames.get(i);
				if (attributeName.length() > 7) {
					attributeName = attributeName.substring(0, 7);
					attributeName += "...";
				} else {
					String spacer = "          ";
					attributeName +=
						spacer.substring(
								0,
								1 + spacer.length() - attributeName.length());
				}
				
				sb.append("\t\t" + attributeName + "\t");
				
				if (isNumeric) {
					sb.append(densityFormatter.format(this.minValues[i])
							+ "\t" + densityFormatter.format(this.maxValues[i])
							+ "\t" + densityFormatter.format(this.meanValues[i]));
				} else {
					sb.append(normalize(this.characteristicValues[i]));
				}
				
				sb.append(System.getProperty("line.separator"));				
			}
			
			return sb.toString();
		}

		private String normalize(String value) {
			if(value == null) {
				return "N/A";
			}
			else {
				return value;
			}
		}

}