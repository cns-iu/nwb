package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.text.DecimalFormat;
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
	StringBuffer[] characteristicValues;



	boolean isValuedNetwork = false;

	int numberOfEdges;

	int numAdditionalNonNumericAttributes;
	int numAdditionalNumericAttributes;

	Vector additionalNonNumericAttributes;
	Vector additionalNumericAttributes;

	SelfLoopsParallelEdges selfLoopsParallelEdges;

	public EdgeStats(final Graph graph){
		this.numberOfEdges = graph.getEdgeCount();
		this.selfLoopsParallelEdges = new SelfLoopsParallelEdges(graph);

		this.initializeAdditionalAttributes(graph);

		if(this.numAdditionalNumericAttributes > 0){

			this.weightedDensitySum = new double[this.numAdditionalNumericAttributes];
			this.meanValues = new double[this.numAdditionalNumericAttributes];
			this.maxValues = new double[this.numAdditionalNumericAttributes];
			this.minValues = new double[this.numAdditionalNumericAttributes];

			java.util.Arrays.fill(this.maxValues, Double.MIN_VALUE);
			java.util.Arrays.fill(this.minValues, Double.MAX_VALUE);

		}
		
		this.calculateEdgeStats(graph);
	}

	private void initializeAdditionalAttributes(final Graph graph){
		numAdditionalNonNumericAttributes = graph.getEdgeTable().getColumnCount()-2;
		numAdditionalNumericAttributes = 0;

		if(numAdditionalNonNumericAttributes > 0){
			additionalNonNumericAttributes = new Vector(numAdditionalNonNumericAttributes);
			additionalNumericAttributes = new Vector();
			Table t = graph.getEdgeTable();

			for(int i = 0; i < t.getColumnCount(); i++){
				if(!(t.getColumnName(i).equalsIgnoreCase("source") || t.getColumnName(i).equalsIgnoreCase("target"))){
					additionalNonNumericAttributes.add(t.getColumnName(i));
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
		

		this.additionalNonNumericAttributes.removeAll(this.additionalNumericAttributes);
		this.numAdditionalNonNumericAttributes = this.additionalNonNumericAttributes.size();
		if(this.numAdditionalNonNumericAttributes > 0){
			this.characteristicValues = new StringBuffer[this.numAdditionalNonNumericAttributes];
		}
		}
		if(this.numAdditionalNumericAttributes > 1)
			this.isValuedNetwork = true;

		//if this is not the case, then we do futher calculations in calculateEdgeStats
		
	}

	public void addEdge(final Edge e, boolean[] seenEdges, HashSet observedValues){
		if(!seenEdges[e.getRow()]){
			seenEdges[e.getRow()] = true;
			
			if(this.numAdditionalNonNumericAttributes > 0){
				processEdgeAttributes(e,this.additionalNonNumericAttributes,observedValues,false);
			}
			if(this.numAdditionalNumericAttributes > 0){
				processEdgeAttributes(e,this.additionalNumericAttributes,observedValues,true);
			}
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
				
				if(this.numAdditionalNumericAttributes == 1){
					
					observedValues.add(e.get(columnName));
				}
			}
			else{
				if(this.characteristicValues[i] == null){
					this.characteristicValues[i] = new StringBuffer();
					this.characteristicValues[i].append(e.get(columnName).toString());
				}
			}
		}
	}


	public void calculateEdgeStats(final Graph graph){
		boolean[] seenEdges;
		seenEdges = new boolean[graph.getEdgeCount()];
		HashSet observedValues = null;
		if(this.numAdditionalNumericAttributes == 1)
			observedValues = new HashSet();
		for(Iterator it = graph.edges(); it.hasNext();){
			Edge e = (Edge)it.next();
			this.addEdge(e, seenEdges, observedValues);
		}
		
		if(this.numAdditionalNumericAttributes == 1){
			if(observedValues.size() > 1){
				this.isValuedNetwork = true;
			}
		}
		observedValues = null;
		seenEdges = null;
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
		for(int i = 0; i < this.numAdditionalNonNumericAttributes; i++){
			sb.append((String) this.additionalNonNumericAttributes.get(i) + " ");
		}
		return sb.toString();
	}

	public double[] getWeightedDensitySumArray(){
		return this.weightedDensitySum;
	}

	public double[] getMaxValueArray(){
		return this.maxValues;
	}
	
	
	protected String[] getAdditionalNumericAttributes(){
		String[] numericAttributeNames = new String[this.additionalNumericAttributes.size()];
		return (String[])this.additionalNumericAttributes.toArray(numericAttributeNames);
	}


	protected boolean getIsValued(){
		return this.isValuedNetwork;
	}

	protected String parallelEdgeInfo(){
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

	protected String edgeInfo(){
		StringBuffer sb = new StringBuffer();
		
		sb.append("Edges: " + this.numberOfEdges);
		sb.append(System.getProperty("line.separator"));
		sb.append(this.selfLoopInfo());
		sb.append(this.parallelEdgeInfo());
		
		if((this.numAdditionalNonNumericAttributes+this.numAdditionalNumericAttributes) > 0){
		sb.append("Edge attributes:");
		sb.append(System.getProperty("line.separator"));
		if(this.numAdditionalNonNumericAttributes > 0){
			sb.append("\tNonnumeric attributes:");
			sb.append(System.getProperty("line.separator"));
			sb.append(printEdgeAttributes(this.additionalNonNumericAttributes,false));
		}
		else{
			sb.append("\tDid not detect any nonnumeric attributes");
		}
		sb.append(System.getProperty("line.separator"));
		
		if(this.numAdditionalNumericAttributes > 0){
			sb.append("\tNumeric attributes:");
			sb.append(System.getProperty("line.separator"));
			sb.append(printEdgeAttributes(this.additionalNumericAttributes,true));
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
			if(isNumeric){
				sb.append("\t\t\t\tmin\tmax\tmean");
				sb.append(System.getProperty("line.separator"));
				densityFormatter = new DecimalFormat("#.#####");
			}
			else{
				sb.append("\t\t\t\tExample value");
				sb.append(System.getProperty("line.separator"));
			}
			for(int i = 0; i < attributeNames.size(); i++){
				String attributeName = (String)attributeNames.get(i);
				if(attributeName.length() > 7){
					attributeName = attributeName.substring(0, 7);
					attributeName += "...";
				}
				else{
					String spacer = "          ";
					attributeName += spacer.substring(0,1+spacer.length()-attributeName.length());
				}
				sb.append("\t\t"+attributeName+"\t");
				if(isNumeric){
					sb.append(densityFormatter.format(this.minValues[i])
							+ "\t" + densityFormatter.format(this.maxValues[i])
							+ "\t" + densityFormatter.format(this.meanValues[i]));
				}
				else{
					sb.append(this.characteristicValues[i].toString());
				}
				sb.append(System.getProperty("line.separator"));
				
		}
			return sb.toString();
		}

}