package edu.iu.nwb.analysis.java.undirectedknn.components;

import java.util.Iterator;
import java.util.Map;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;

public class KNNCalculatorThread extends Thread{
	private final Graph originalGraph;
	private Graph targetGraph;
	private int start;
	private int end;
	private KNNCalculator mainCalculator;
	private Map degreeToKNN;
	private Map degreeToCountDegree;

	public KNNCalculatorThread(final Graph orgGraph, Graph trgtGraph, int start, int end, KNNCalculator kCalc, Map sumNeighbor, Map countDegreeMap){
		super();
		this.originalGraph = orgGraph;
		this.targetGraph = trgtGraph;
		this.start = start;
		this.end = end;
		this.mainCalculator = kCalc;
		this.degreeToKNN = sumNeighbor;
		this.degreeToCountDegree = countDegreeMap;
	}

	public void run() {
		super.run();
		String label = "knn";
		Table sourceNodeTable = this.originalGraph.getNodeTable();
		Table targetNodeTable = this.targetGraph.getNodeTable();
		int tenPercent = (int)Math.ceil((end-start)/10);
		if(tenPercent == 0){
			tenPercent = 1;
		}
		int counter = 1;
		for(int i = this.start; i < this.end; i++){

			if(counter % tenPercent == 0){
				KNNCalculator.updateProgress(this.mainCalculator, counter);
			}

			for(int j = 0; j < sourceNodeTable.getColumnCount();j++){
				targetNodeTable.set(i, j, sourceNodeTable.get(i, j));
			}

			Node retrievedNode = this.originalGraph.getNode(i);
			float knn = calculateNeighborsAvgDegree(retrievedNode);
			targetNodeTable.setFloat(i, "knn", knn);
			updateMaps(retrievedNode.getDegree(),knn);


		}

	}

	private float calculateNeighborsAvgDegree(Node n){
		float answer = 0;
		if(n.getDegree() == 0)
			return 0;
		
		for(Iterator i = n.neighbors(); i.hasNext();){
			Node neighbor = (Node)i.next();
			answer += neighbor.getDegree();
		}

		
		
		return answer/n.getDegree();
	}

	private void updateMaps(int degree, float neighborsDegreeCalculation){
		Float knnValue = (Float)this.degreeToKNN.get(new Integer(degree));

		if(knnValue == null){
			this.degreeToKNN.put(new Integer(degree), new Float(neighborsDegreeCalculation));
			this.degreeToCountDegree.put(new Integer(degree), new Integer(1));
		}else{
			knnValue = new Float(knnValue.floatValue()+neighborsDegreeCalculation);
			this.degreeToKNN.put(new Integer(degree), knnValue);
			int count = ((Integer)this.degreeToCountDegree.get(new Integer(degree))).intValue();
			this.degreeToCountDegree.put(new Integer(degree), new Integer(count+1));
		}
	}


}
