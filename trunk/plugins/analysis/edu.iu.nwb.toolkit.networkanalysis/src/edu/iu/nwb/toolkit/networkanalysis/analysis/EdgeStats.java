package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.util.Vector;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Table;

public class EdgeStats{	
	double[] meanValues;
	double[] maxValues;
	double[] minValues;
	boolean[] seenEdges;
	
	int numAdditionalAttributes;
	int numAdditionalNumericAttributes;
	
	Vector additionalAttributes;
	Vector additionalNumericAttributes;
	
	SelfLoopsParallelEdges selfLoopsParallelEdges;

	public EdgeStats(final Graph graph){
		this.selfLoopsParallelEdges = new SelfLoopsParallelEdges(graph.isDirected());
		seenEdges = new boolean[graph.getEdgeCount()];
		java.util.Arrays.fill(seenEdges, false);
		
			this.initializeAdditionalAttributes(graph);
		

			if(this.numAdditionalNumericAttributes > 0){
				this.meanValues = new double[this.numAdditionalNumericAttributes];			
			}
		
	}
	
	private void initializeAdditionalAttributes(final Graph graph){
		numAdditionalAttributes = graph.getEdgeTable().getColumnCount()-2;
		System.out.println(this.numAdditionalAttributes);
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

	public void addEdge(final Edge e, final Table t){
		if(!this.seenEdges[e.getRow()]){
			this.seenEdges[e.getRow()] = true;
			this.selfLoopsParallelEdges.addEdge(e);
			if(this.numAdditionalNumericAttributes > 0){
				for(int i = 0; i < this.additionalNumericAttributes.size(); i++){
					String columnName = (String)this.additionalNumericAttributes.get(i);
					Number n = (java.lang.Number)t.get(e.getRow(), columnName);
					this.meanValues[i] += (n.doubleValue())/e.getGraph().getEdgeCount();
				}
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

	
	

}