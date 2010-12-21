package edu.iu.nwb.preprocessing.extractnodesandedges.extractnodes.abovebelow;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;

import edu.iu.nwb.preprocessing.extractnodesandedges.GraphDataFormatter;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.filters.impl.NumericDecorationFilter;

public class ExtractNodesAboveBelowAlgorithm implements Algorithm {
	Data[] data;
	Dictionary parameters;
	CIShellContext context;

	private Double fromThisNum;
	private Boolean belowInstead;
	private String numericAttribute;

	boolean noParams = false;

	public ExtractNodesAboveBelowAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;

		// if parameter values are not defined...
		if (parameters.get("fromThisNum") == null) {
			// skip initialization and prepare to not execute
			noParams = true;
			return;
		}

		this.fromThisNum = (Double) parameters.get("fromThisNum");
		this.belowInstead = (Boolean) parameters.get("belowInstead");
		this.numericAttribute = (String) parameters.get("numericAttribute");
	}

	public Data[] execute() throws AlgorithmExecutionException {
		if (noParams)
			return null;

		// get the input graph
		Graph originalGraph = (Graph) data[0].getData();

		// make a new graph by filtering out nodes from the original graph
		Graph newGraph = filter(originalGraph);

		// format the resulting graph
		Data[] newGraphData = formatAsData(newGraph);

		// return the resulting graph
		return newGraphData;
	}

	// returns a new graph that contains all the nodes whose attribute is either
	// above or below a given threshold
	private Graph filter(Graph originalGraph)
			throws AlgorithmExecutionException {
		NumericDecorationFilter filter = null;
		if (belowInstead.booleanValue() == false) {
			filter = new NumericDecorationFilter();
		} else {
			filter = new ReverseOfNumericDecorationFilter();
		}

		filter.setDecorationKey(numericAttribute);
		filter.setThreshold(fromThisNum.doubleValue());

		try {
			Graph newGraph = filter.filter(originalGraph).assemble();
			return newGraph;
		} catch (Exception e) {
			throw new AlgorithmExecutionException(e);
		}

	}

	private Data[] formatAsData(Graph extractedGraph) {
		StringBuilder label = new StringBuilder();
		label.append("all nodes with " + this.numericAttribute);
		if (this.belowInstead.booleanValue() == false) {
			label.append(" above ");
		} else {
			label.append(" below or equal to ");
		}
		label.append("" + this.fromThisNum);
		Data[] data = GraphDataFormatter.formatExtractedGraphAsData(
				extractedGraph, label.toString(), this.data[0]);
		return data;
	}
}