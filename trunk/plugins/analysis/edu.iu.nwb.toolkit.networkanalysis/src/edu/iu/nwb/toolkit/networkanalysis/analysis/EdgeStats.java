package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Table;

public class EdgeStats{	
	double[] meanValues;
	double[] maxValues;
	double[] minValues;
	double[] weightedDensitySum;

	boolean[] seenEdges;
	boolean[] isValuedAttributeArray;
	
	boolean isValuedNetwork;
	
	int numberOfEdges;
	
	int numAdditionalAttributes;
	int numAdditionalNumericAttributes;
	
	Vector additionalAttributes;
	Vector additionalNumericAttributes;
	
	SelfLoopsParallelEdges selfLoopsParallelEdges;

	public EdgeStats(final Graph graph){
		this.numberOfEdges = graph.getEdgeCount();
		this.selfLoopsParallelEdges = new SelfLoopsParallelEdges(graph.isDirected());
		seenEdges = new boolean[graph.getEdgeCount()];
		java.util.Arrays.fill(seenEdges, false);
		
			this.initializeAdditionalAttributes(graph);
		

			if(this.numAdditionalNumericAttributes > 0){
				this.isValuedAttributeArray = new boolean[this.numAdditionalNumericAttributes];
				this.weightedDensitySum = new double[this.numAdditionalNumericAttributes];
				this.meanValues = new double[this.numAdditionalNumericAttributes];
				this.maxValues = new double[this.numAdditionalNumericAttributes];
				this.minValues = new double[this.numAdditionalNumericAttributes];
				java.util.Arrays.fill(this.isValuedAttributeArray, false);
				java.util.Arrays.fill(this.maxValues, Double.MIN_VALUE);
				java.util.Arrays.fill(this.minValues, Double.MAX_VALUE);
				
			}
		this.calculateEdgeStats(graph);
	}
	
	private void initializeAdditionalAttributes(final Graph graph){
		numAdditionalAttributes = graph.getEdgeTable().getColumnCount()-2;
		numAdditionalNumericAttributes = 0;
		
		if(numAdditionalAttributes > 0){
			additionalAttributes = new Vector(numAdditionalAttributes);
			additionalNumericAttributes = new Vector();
			Table t = graph.getEdgeTable();
			
			for(int i = 0; i < t.getColumnCount(); i++){
				if(!(t.getColumnName(i).equalsIgnoreCase("source") || t.getColumnName(i).equalsIgnoreCase("target"))){
					additionalAttributes.add(t.getColumnName(i));
					try{
						if(graph.getEdge(0).get(i) instanceof Number){
							numAdditionalNumericAttributes += 1;
							additionalNumericAttributes.add(t.getColumnName(i));
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
		
	}

	public void addEdge(final Edge e, HashSet[] observedValues){
		if(!this.seenEdges[e.getRow()]){
			this.seenEdges[e.getRow()] = true;
			this.selfLoopsParallelEdges.addEdge(e);
			if(this.numAdditionalNumericAttributes > 0){
				for(int i = 0; i < this.additionalNumericAttributes.size(); i++){
					String columnName = (String)this.additionalNumericAttributes.get(i);
					double value = ((java.lang.Number)e.get(columnName)).doubleValue();
					this.meanValues[i] += (value)/e.getGraph().getEdgeCount();
					this.weightedDensitySum[i] += value;
					if(value > this.maxValues[i])
						this.maxValues[i] = value;
					if(value < this.minValues[i])
						this.minValues[i] = value;
					
					if(!this.isValuedAttributeArray[i]){
						if(value != 0.0){
							observedValues[i].add(new Double(value));
							if(observedValues[i].size() > 1)
								this.isValuedAttributeArray[i] = true;
						}
					}
				}
			}
		}
	}
	
	public void calculateEdgeStats(final Graph graph){
		HashSet[] observedValues = new HashSet[this.numAdditionalNumericAttributes];
		java.util.Arrays.fill(observedValues, new HashSet());
		for(Iterator it = graph.edges(); it.hasNext();){
			Edge e = (Edge)it.next();
			this.addEdge(e, observedValues);
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

	public int getNumberOfEdges(){
		return this.numberOfEdges;
	}
	
	public String printEdgeAttributes(){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < this.numAdditionalAttributes; i++){
			sb.append((String) this.additionalAttributes.get(i) + " ");
		}
		return sb.toString();
	}
	
	public double[] getWeightedDensitySumArray(){
		return this.weightedDensitySum;
	}
	
	public double[] getMeanValueArray(){
		return this.meanValues;
	}
	
	public double[] getMaxValueArray(){
		return this.maxValues;
	}
	
	public double[] getMinValueArray(){
		return this.minValues;
	}
	
	protected String[] getAdditionalNumericAttributes(){
		String[] numericAttributeNames = new String[this.additionalNumericAttributes.size()];
		return (String[])this.additionalNumericAttributes.toArray(numericAttributeNames);
	}
	
	protected String[] getAdditionalAttributes(){
		String[] attributeNames = new String[this.numAdditionalAttributes];
		return (String[])this.additionalAttributes.toArray(attributeNames);
		
	}

}