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

	Vector<String> nonNumericAttributes;
	Vector<String> numericAttributes;

	SelfLoopsParallelEdges selfLoopsParallelEdges;
	
	private Graph edgeGraph;

	@Override
	public void run() {
		initializeAdditionalAttributes(this.edgeGraph.getEdgeTable(), this);

		CascadedTable derivativeTable = new CascadedTable(this.edgeGraph.getEdgeTable());
		derivativeTable.addColumn("visited", boolean.class, new Boolean(false));

		int additionalNumericAttributes = this.numericAttributes.size();
		
		if (additionalNumericAttributes > 0) {
			this.weightedDensitySum = new double[additionalNumericAttributes];
			this.meanValues = new double[additionalNumericAttributes];
			this.maxValues = new double[additionalNumericAttributes];
			this.minValues = new double[additionalNumericAttributes];

			java.util.Arrays.fill(this.maxValues, Double.MIN_VALUE);
			java.util.Arrays.fill(this.minValues, Double.MAX_VALUE);
		}

		this.calculateEdgeStats(derivativeTable);
	}
	
	public static EdgeStats constructEdgeStats(Graph graph) {
		EdgeStats edgeStats = new EdgeStats();
		
		edgeStats.numberOfEdges = graph.getEdgeCount();
		edgeStats.selfLoopsParallelEdges = new SelfLoopsParallelEdges(graph);
		edgeStats.edgeGraph = graph;

		return edgeStats;
	}
	
	private static EdgeStats initializeAdditionalAttributes(Table edgeTable, EdgeStats edgeStats) {
		edgeStats.nonNumericAttributes = new Vector<String>();
		edgeStats.numericAttributes = new Vector<String>();
		int numberOfAdditionalAttributes = edgeTable.getColumnCount() - 2;

		if (numberOfAdditionalAttributes > 0) {
			for (int ii = 0; ii < edgeTable.getColumnCount(); ii++) {
				String columnName = edgeTable.getColumnName(ii);

				if (!("source".equals(columnName) || "target".equals(columnName))) {
					if (edgeTable.getColumn(ii).canGet(Number.class)) {
						edgeStats.numericAttributes.add(columnName);
					} else {
						edgeStats.nonNumericAttributes.add(columnName);
					}
				}
			}
		}

		if (edgeStats.nonNumericAttributes.size() > 0) {
			edgeStats.characteristicValues = new String[edgeStats.nonNumericAttributes.size()];
		}

		if (edgeStats.numericAttributes.size() > 0) {
			edgeStats.isValuedNetwork = true;
		}

		return edgeStats;
	}

	private void addEdge(
			Edge edge, CascadedTable derivativeTable, HashSet<Object> observedValues) {
		if (!derivativeTable.getBoolean(
				edge.getRow(), derivativeTable.getColumnNumber("visited"))) {
			processEdgeAttributes(edge, this.nonNumericAttributes, observedValues, false);
			processEdgeAttributes(edge, this.numericAttributes, observedValues, true);
			derivativeTable.setBoolean(
				edge.getRow(), derivativeTable.getColumnNumber("visited"), true);
		}
	}

	private void processEdgeAttributes(
			Edge edge,
			Vector<String> attributeNames,
			HashSet<Object> observedValues,
			boolean isNumeric) {
		for (int ii = 0; ii < attributeNames.size(); ii++){
			String columnName = attributeNames.get(ii);

			if (isNumeric) {
				double value = ((Number) edge.get(columnName)).doubleValue();
				this.meanValues[ii] += value / edge.getGraph().getEdgeCount();
				this.weightedDensitySum[ii] += value;

				if (value > this.maxValues[ii]) {
					this.maxValues[ii] = value;
				}

				if (value < this.minValues[ii]) {
					this.minValues[ii] = value;
				}

				if (attributeNames.size() == 1) {
					observedValues.add(edge.get(columnName));
				}
			} else {
				Object cell = edge.get(columnName);

				if (cell != null) {
					this.characteristicValues[ii] = edge.get(columnName).toString();
				}
			}
		}
	}

	public void calculateEdgeStats(CascadedTable visitedTable) {
		HashSet<Object> observedValues = null;

		if (numericAttributes.size() == 1) {
			observedValues = new HashSet<Object>();
		}

		for (Iterator<?> edgeIt = edgeGraph.edges(); edgeIt.hasNext();) {
			Edge e = (Edge) edgeIt.next();
			
			addEdge(e, visitedTable, observedValues);
		}

		if (numericAttributes.size() == 1) {
			if (observedValues.size() > 1) {
				isValuedNetwork = true;
			}
		}
		
	}

	public SelfLoopsParallelEdges getSelfLoopsParallelEdges() {
		return this.selfLoopsParallelEdges;
	}

	public int getNumberOfSelfLoops() {
		return this.selfLoopsParallelEdges.getNumSelfLoops();
	}

	public int getNumberOfParallelEdges() {
		return this.selfLoopsParallelEdges.getNumParallelEdges();
	}

	public double[] getWeightedDensitySumArray() {
		return this.weightedDensitySum;
	}

	public double[] getMaxValueArray() {
		return this.maxValues;
	}
	
	
	protected String[] getAdditionalNumericAttributes() {
		String[] numericAttributeNames = new String[this.numericAttributes.size()];

		return (String[]) this.numericAttributes.toArray(numericAttributeNames);
	}

	protected String appendParallelEdgeInfo() {
		StringBuffer stringBuffer = new StringBuffer();
		int parallelEdges = this.getNumberOfParallelEdges();

		if (parallelEdges > 0) {
			stringBuffer.append(String.format(
				"There are: %d parallel edges.%nThey are as follows:%n%s%n",
				parallelEdges,
				this.getSelfLoopsParallelEdges().printParallelEdges()));
		} else {
			stringBuffer.append(String.format("No parallel edges were discovered.%n"));
		}

		return stringBuffer.toString();
	}

	protected String selfLoopInfo() {
		StringBuffer stringBuffer = new StringBuffer();
		if (this.getNumberOfSelfLoops() > 0) {
			stringBuffer.append(String.format(
				"There are: %d self loops.%nThey are as follows:%n%s%n",
				this.getNumberOfSelfLoops(),
				this.getSelfLoopsParallelEdges().printSelfLoops()));
		} else {
			stringBuffer.append(String.format("No self loops were discovered.%n"));
		}

		return stringBuffer.toString();
	}

	protected String appendEdgeInfo() {
		StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append(String.format(
			"Edges: %d%n%s%s%n", this.numberOfEdges, selfLoopInfo(), appendParallelEdgeInfo()));

		int nonNumericAttributesSize = this.nonNumericAttributes.size();
		int numericAttributesSize = this.numericAttributes.size();

		if ((nonNumericAttributesSize + numericAttributesSize) > 0) {
			stringBuffer.append(String.format("Edge attributes:%n"));

			if (nonNumericAttributesSize > 0) {
				stringBuffer.append(String.format(
					"\tNonnumeric attributes:%n%s",
					printEdgeAttributes(this.nonNumericAttributes, false)));
			} else {
				stringBuffer.append("\tDid not detect any nonnumeric attributes.");
			}

			stringBuffer.append(System.getProperty("line.separator"));

			if (numericAttributesSize > 0) {
				stringBuffer.append(String.format(
					"\tNumeric attributes:%n%s",
					printEdgeAttributes(this.numericAttributes,true)));
			} else {
				stringBuffer.append("\tDid not detect any numeric attributes.");
			}

			stringBuffer.append(System.getProperty("line.separator"));
		} else {
			stringBuffer.append(String.format("\tDid not detect any edge attributes.%n"));
		}

		if (this.isValuedNetwork) {
			stringBuffer.append(String.format("\tThis network seems to be valued.%n"));
		} else {
			stringBuffer.append(String.format(
				"\tThis network does not seem to be a valued network.%n"));
		}

		return stringBuffer.toString();
	}

	private String printEdgeAttributes(Vector<String> attributeNames, boolean isNumeric) {
		StringBuffer stringBuffer = new StringBuffer();
		DecimalFormat densityFormatter = null;
		
		if (isNumeric) {
			stringBuffer.append("\t\t\t\tmin\tmax\tmean");
			stringBuffer.append(System.getProperty("line.separator"));
			densityFormatter = new DecimalFormat("#.#####");
		} else {
			stringBuffer.append("\t\t\t\tExample value");
			stringBuffer.append(System.getProperty("line.separator"));
		}
		
		for (int ii = 0; ii < attributeNames.size(); ii++) {
			String attributeName = attributeNames.get(ii);
			if (attributeName.length() > 7) {
				attributeName = attributeName.substring(0, 7);
				attributeName += "...";
			} else {
				String spacer = "          ";
				attributeName +=
					spacer.substring(0, (1 + spacer.length() - attributeName.length()));
			}
			
			stringBuffer.append("\t\t" + attributeName + "\t");
			
			if (isNumeric) {
				stringBuffer.append(densityFormatter.format(this.minValues[ii])
						+ "\t" + densityFormatter.format(this.maxValues[ii])
						+ "\t" + densityFormatter.format(this.meanValues[ii]));
			} else {
				stringBuffer.append(normalize(this.characteristicValues[ii]));
			}
			
			stringBuffer.append(System.getProperty("line.separator"));				
		}
		
		return stringBuffer.toString();
	}

	private String normalize(String value) {
		if (value == null) {
			return "N/A";
		} else {
			return value;
		}
	}
}