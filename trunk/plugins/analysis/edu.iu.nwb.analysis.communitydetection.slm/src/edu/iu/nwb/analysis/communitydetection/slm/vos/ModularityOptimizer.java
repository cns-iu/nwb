package edu.iu.nwb.analysis.communitydetection.slm.vos;

/**
 * ModularityOptimizer
 *
 * @author Ludo Waltman
 * @author Nees Jan van Eck
 * @version 1.0.0, 08/23/13
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class ModularityOptimizer {
	boolean printOutput = true, update;
	double modularity, maxModularity, resolution, resolution2;
	int algorithm, i, j, nClusters, nIterations, nRandomStarts;
	long beginTime, endTime, randomSeed;
	Network network;
	Random random;
	
	public ModularityOptimizer(int algorithm, int randomStart, int randomSeed, int iterations, double resolution) {
		this.resolution = resolution;
		this.algorithm = algorithm;
		this.nRandomStarts = randomStart;
		this.nIterations = iterations;
		this.randomSeed = randomSeed;
	}
	
	public ModularityOptimizer() {
		this.resolution = 1.0;
		this.algorithm = 3;
		this.nRandomStarts = 10;
		this.nIterations = 10;
		this.randomSeed = 0;
	}

	public void OptimizeModularity(File inputFile, File outputFile) throws IOException {

		if (printOutput) {
			System.out
					.println("Modularity Optimizer version 1.0.0 by Ludo Waltman and Nees Jan van Eck");
		}

		network = readInputFile(inputFile);

		if (printOutput) {
			System.out.format("Number of nodes: %d%n", network.getNNodes());
			System.out.format("Number of edges: %d%n", network.getNEdges() / 2);
			System.out.println();
			System.out
					.println("Running "
							+ ((algorithm == 1) ? "Louvain algorithm"
									: ((algorithm == 2) ? "Louvain algorithm with multilevel refinement"
											: "smart local moving algorithm"))
							+ "...");
			System.out.println();
		}

		resolution2 = resolution / network.getTotalEdgeWeight();

		beginTime = System.currentTimeMillis();
		int[] cluster = null;
		nClusters = -1;
		maxModularity = Double.NEGATIVE_INFINITY;
		random = new Random(randomSeed);
		for (i = 0; i < nRandomStarts; i++) {
			if (printOutput && (nRandomStarts > 1))
				System.out.format("Random start: %d%n", i + 1);

			network.initSingletonClusters();

			j = 0;
			update = true;
			do {
				if (algorithm == 1)
					update = network.runLouvainAlgorithm(resolution2, random);
				else if (algorithm == 2)
					update = network
							.runLouvainAlgorithmWithMultilevelRefinement(
									resolution2, random);
				else if (algorithm == 3)
					network.runSmartLocalMovingAlgorithm(resolution2, random);
				j++;
			} while ((j < nIterations) && update);

			modularity = network.calcQualityFunction(resolution2);
			if (modularity > maxModularity) {
				network.orderClustersByNNodes();
				cluster = network.getClusters();
				nClusters = network.getNClusters();
				maxModularity = modularity;
			}

			if (printOutput && (nRandomStarts > 1)) {
				System.out.format("Modularity: %.4f%n", modularity);
				System.out.println();
			}
		}
		endTime = System.currentTimeMillis();

		writeOutputFile(outputFile, cluster);

		if (printOutput) {
			if (nRandomStarts == 1)
				System.out.format("Modularity: %.4f%n", maxModularity);
			else
				System.out.format(
						"Maximum modularity in %d random starts: %.4f%n",
						nRandomStarts, maxModularity);
			System.out.format("Number of clusters: %d%n", nClusters);
			System.out.format("Elapsed time: %d seconds%n",
					Math.round((endTime - beginTime) / 1000.0));
			System.out.println();
		}
	}

	private static Network readInputFile(File file) throws IOException {
		class Edge implements Comparable<Edge> {
			public int node1;
			public int node2;
			public int weight;

			public Edge(int node1, int node2, int weight) {
				this.node1 = node1;
				this.node2 = node2;
				this.weight = weight;
			}

			public int compareTo(Edge edge) {
				return (node1 == edge.node1) ? (node2 - edge.node2)
						: (node1 - edge.node1);
			}
		}

		ArrayList<Edge> edgeArrayList1 = new ArrayList<Edge>();
		Scanner fileScanner = null;

		try {
			fileScanner = new Scanner(new FileReader(file));
			Scanner lineScanner;
			while (fileScanner.hasNext()) {
				lineScanner = new Scanner(fileScanner.nextLine());
				int node1 = lineScanner.nextInt();
				int node2 = lineScanner.nextInt();
				int edgeWeight = lineScanner.hasNextInt() ? lineScanner.nextInt() : 1;
				if (node1 != node2) {
					edgeArrayList1.add(new Edge(node1, node2, edgeWeight));
					edgeArrayList1.add(new Edge(node2, node1, edgeWeight));
				}
			}
		} finally {
			if (fileScanner != null) {
				fileScanner.close();
			}
		}

		Collections.sort(edgeArrayList1);

		ArrayList<Edge> edgeArrayList2 = new ArrayList<Edge>();
		int node1 = -1;
		int node2 = -1;
		int edgeWeight = 0;
		for (int i = 0; i < edgeArrayList1.size(); i++) {
			Edge edge = edgeArrayList1.get(i);
			if ((edge.node1 == node1) && (edge.node2 == node2))
				edgeWeight += edge.weight;
			else {
				if (i > 0)
					edgeArrayList2.add(new Edge(node1, node2, edgeWeight));
				node1 = edge.node1;
				node2 = edge.node2;
				edgeWeight = edge.weight;
			}
		}
		edgeArrayList2.add(new Edge(node1, node2, edgeWeight));

		int[][] edge2 = new int[edgeArrayList2.size()][2];
		int[] edgeWeight2 = new int[edgeArrayList2.size()];
		for (int i = 0; i < edgeArrayList2.size(); i++) {
			Edge edge = edgeArrayList2.get(i);
			edge2[i][0] = edge.node1;
			edge2[i][1] = edge.node2;
			edgeWeight2[i] = edge.weight;
		}

		int nNodes = -1;
		for (int j = 0; j < edge2.length; j++)
			if (edge2[j][0] > nNodes)
				nNodes = edge2[j][0];
		nNodes++;

		int[] nodeWeight = new int[nNodes];
		for (int i = 0; i < edge2.length; i++)
			nodeWeight[edge2[i][0]] += edgeWeight2[i];

		double[] edgeWeightDouble = new double[edgeWeight2.length];
		for (int i = 0; i < edgeWeight2.length; i++)
			edgeWeightDouble[i] = edgeWeight2[i];

		return new Network(nNodes, edge2, edgeWeightDouble, nodeWeight);
	}

	private static void writeOutputFile(File file, int[] cluster) throws IOException {
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(file));

			for (int i = 0; i < cluster.length; i++) {
				bufferedWriter.write(i + " " + Integer.toString(cluster[i]));
				bufferedWriter.newLine();
			}

			bufferedWriter.close();
		} finally {
			if(bufferedWriter != null) {
				bufferedWriter.close();
			}
			
		}
	}
}
