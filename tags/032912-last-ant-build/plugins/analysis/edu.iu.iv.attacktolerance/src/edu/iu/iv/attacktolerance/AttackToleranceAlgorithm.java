package edu.iu.iv.attacktolerance;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.uci.ics.jung.graph.Graph;

public class AttackToleranceAlgorithm implements Algorithm {

	private Data[] data;
	private int numNodesToDelete;

	public AttackToleranceAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.numNodesToDelete = ((Integer) parameters.get("numNodes")).intValue();
	}

	public Data[] execute() {
		Graph inputGraph = (Graph) (data[0].getData());

		Graph outputGraph = AttackTolerance.testAttackTolerance(inputGraph, numNodesToDelete);

		Data outputData = prepareOutputData(outputGraph);

		return new Data[] { outputData };
	}

	private Data prepareOutputData(Graph outputGraph) {
		Data outputData = new BasicData(outputGraph, Graph.class.getName());

		Dictionary metadata = outputData.getMetadata();
		metadata.put(DataProperty.PARENT, data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		metadata.put(DataProperty.LABEL, "High Degree Node Deletion (Attack Tolerance)");

		return outputData;
	}
}
