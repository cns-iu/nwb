package edu.iu.nwb.visualization.prefuse.alpha.smallworld.action;

//Copyright (C) 2005 Andreas Noack
//
//This library is free software; you can redistribute it and/or
//modify it under the terms of the GNU Lesser General Public
//License as published by the Free Software Foundation; either
//version 2.1 of the License, or (at your option) any later version.
//
//This library is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public
//License along with this library; if not, write to the Free Software
//Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA 


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Simple program for computing graph layouts (positions of the nodes of a graph
 * in two- or three-dimensional space) for visualization and knowledge discovery.
 * Reads a graph from a file, computes a layout, writes the layout to a file,
 * and displays the layout in a dialog.
 * The program can be used to identify clusters (groups of densely connected 
 * nodes) in graphs, like groups of friends or collaborators in social networks,
 * related documents in hyperlink structures (e.g. web graphs),
 * cohesive subsystems in software models, etc.
 * With a change of a parameter in the <code>main</code> method,
 * it can also compute classical "nice" (i.e. readable) force-directed layouts.
 * The program is intended as a demo of the use of its core layouter class 
 * <code>MinimizerBarnesHut</code>.  See this class for details about layouts.
 * 
 * @author Andreas Noack (an@informatik.tu-cottbus.de)
 * @version 28.09.2005
 */
public class LinLogLayout {
	
	/**
	 * Reads and returns a graph from the specified file.
	 * The graph is returned as a nested map: Each source node 
	 * of an edge is mapped to a map representing its outgoing edges.  
	 * This map maps each target node of the outgoing edges to the edge weight
	 * (the weight of the edge from the source node to the target node).
	 * Schematically, source -> target -> edge weight.
	 * 
	 * @param filename name of the file to read from.
	 * @return read graph.
	 */
	private static Map<String,Map<String,Float>> readGraph(String filename) {
		Map<String,Map<String,Float>> result = new HashMap<String,Map<String,Float>>();
		try {
			BufferedReader file = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = file.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				String source = st.nextToken();
				String target = st.nextToken();
				float weight = st.hasMoreTokens() ? Float.parseFloat(st.nextToken()) : 1.0f;
				if (result.get(source) == null) result.put(source, new HashMap<String,Float>());
				result.get(source).put(target, weight);
			}
			file.close();
		} catch (IOException e) {
		      System.err.println("Exception while reading the graph:"); 
			  System.err.println(e);
			  System.exit(1);
		}
		return result;
	}
	
	/**
	 * Returns a symmetric version of the given graph.
	 * A graph is symmetric if and only if for each pair of nodes,
	 * the weight of the edge from the first to the second node
	 * equals the weight of the edge from the second to the first node.
	 * Here the symmetric version is obtained by adding to each edge weight
	 * the weight of the inverse edge.
	 * 
	 * @param graph  possibly unsymmetric graph.
	 * @return symmetric version of the given graph.
	 */
	public static Map<String,Map<String,Float>> makeSymmetricGraph
			(Map<String,Map<String,Float>> graph) {
		Map<String,Map<String,Float>> result = new HashMap<String,Map<String,Float>>();
		for (String source : graph.keySet()) {
			for (String target : graph.get(source).keySet()) {
				float weight = graph.get(source).get(target);
				float revWeight = 0.0f;
				if (graph.get(target) != null && graph.get(target).get(source) != null) {
					revWeight = graph.get(target).get(source);
				}
				if (result.get(source) == null) result.put(source, new HashMap<String,Float>());
				result.get(source).put(target, weight+revWeight);
				if (result.get(target) == null) result.put(target, new HashMap<String,Float>());
				result.get(target).put(source, weight+revWeight);
			}
		}
		return result;
	}
	
	/**
	 * Returns a map from each node of the given graph 
	 * to a unique number from 0 to (number of nodes minus 1). 
	 * 
	 * @param graph the graph.
	 * @return map from each node of the given graph 
	 *         to a unique number from 0 to (number of nodes minus 1).
	 */
	public static Map<String,Integer> makeIds(Map<String,Map<String,Float>> graph) {
		Map<String,Integer> result = new HashMap<String,Integer>();
		int cnt = 0;
		for (String node : graph.keySet()) {
			result.put(node, cnt);
			cnt++;
		}
		return result;
	}
	
	/**
	 * Returns a map from each node of the given graph 
	 * to a random initial position in three-dimensional space. 
	 * 
	 * @param graph the graph.
	 * @return map from each node of the given graph 
	 * 		   to a random initial position in three-dimensional space.
	 */
	public static float[][] makeInitialPositions(Map<String,Map<String,Float>> graph) {
		float[][] result = new float[graph.size()][3];
		for (int i = 0; i < result.length; i++) {
			result[i][0] = (float)Math.random() - 0.5f;
			result[i][1] = (float)Math.random() - 0.5f;
			result[i][2] = 0.0f; // set to 0.0f for 2D layouts,
			                     // and to a random number for 3D.
		}
		return result;
	}
	
	/**
	 * Converts the edge weights of the given graph into the adjacency list 
	 * format expected by <code>MinimizerBarnesHut</code>.
	 * 
	 * @param graph    the graph.
	 * @param nodeToId unique ids of the graph nodes.
	 * @return array of adjacency lists for <code>MinimizerBarnesHut</code>.
	 */
	public static float[][] makeAttrWeights
			(Map<String,Map<String,Float>> graph, Map<String,Integer> nodeToId) {
		float[][] result = new float[graph.size()][];
		for (String source : graph.keySet()) {
			int sourceId = nodeToId.get(source);
			result[sourceId] = new float[graph.get(source).size()];
			int cnt = 0;
			for (String target : graph.get(source).keySet()) {
				result[sourceId][cnt] = graph.get(source).get(target);
				cnt++;
			}
		}
		return result;
	}
	
	/**
	 * Converts the edges of the given graph into the adjacency list 
	 * format expected by <code>MinimizerBarnesHut</code>.
	 * 
	 * @param graph    the graph.
	 * @param nodeToId unique ids of the graph nodes.
	 * @return array of adjacency lists for <code>MinimizerBarnesHut</code>.
	 */
	public static int[][] makeAttrIndexes
			(Map<String,Map<String,Float>> graph, Map<String,Integer> nodeToId) {
		int[][] result = new int[graph.size()][];
		for (String source : graph.keySet()) {
			int sourceId = nodeToId.get(source);
			result[sourceId] = new int[graph.get(source).size()];
			int cnt = 0;
			for (String target : graph.get(source).keySet()) {
				result[sourceId][cnt] = nodeToId.get(target);
				cnt++;
			}
		}
		return result;
	}

	/**
	 * Computes the repulsion weights of the nodes  
	 * for <code>MinimizerBarnesHut</code>.
	 * In the edge repulsion LinLog energy model, the repulsion weight
	 * of each node is its degree, i.e. the sum of the weights of its edges.
	 * 
	 * @param graph    the graph.
	 * @param nodeToId unique ids of the graph nodes.
	 * @return array of repulsion weights for <code>MinimizerBarnesHut</code>.
	 */
	public static float[] makeRepuWeights
			(Map<String,Map<String,Float>> graph, Map<String,Integer> nodeToId) {
		float[] result = new float[graph.size()];
		for (String source : graph.keySet()) {
			int sourceId = nodeToId.get(source);
			result[sourceId] = 0.0f;
			for (Float weight : graph.get(source).values()) {
				result[sourceId] += weight;
			}
		}
		return result;
	}

	/**
	 * Converts the array of node positions from <code>MinimizerBarnesHut</code>
	 * into a map from each node to its position.
	 * 
	 * @param positions array of node positions.
	 * @param nodeToId unique ids of the graph nodes.
	 * @return map from each node to its positions.
	 */
	public static Map<String,float[]> convertPositions
			(float[][] positions, Map<String,Integer> nodeToId) {
		Map<String,float[]> result = new HashMap<String,float[]>();
		for (String node : nodeToId.keySet()) {
			result.put(node, positions[nodeToId.get(node)]);
		}
		return result;
	}
	
	/**
	 * Computes a map from each node to the diameter of the circle
	 * that represents the node in the visualization.
	 * Here the square root of the degree (the total weight of the edges)
	 * of the node is used as diameter, thus the area of the circle
	 * is proportional to the degree.  
	 * 
	 * @param graph the graph.
	 * @return map from each node to its diameter in the visualization.
	 */
	public static Map<String,Float> computeDiameters(Map<String,Map<String,Float>> graph) {
		Map<String,Float> result = new HashMap<String,Float>();
		for (String source : graph.keySet()) {
			float degree = 0.0f;
			for (Float weight : graph.get(source).values()) degree += weight;
			result.put(source, (float)Math.sqrt(degree));
		}
		return result;
	}

	/**
	 * Writes the given layout (node positions) into the specified file.
	 * 
	 * @param nodeToPosition map from the nodes to their positions.
	 * @param filename name of the file to write into.
	 */
	public static void writePositions(Map<String,float[]> nodeToPosition, String filename) {
		try {
			BufferedWriter file = new BufferedWriter(new FileWriter(filename));
			for (String node : nodeToPosition.keySet()) {
				float[] position = nodeToPosition.get(node);
				file.write(node + " " + position[0] + " " + position[1] + "\n");
			}
			file.close();
		} catch (IOException e) {
		      System.err.println("Exception while writing the graph:"); 
			  System.err.println(e);
			  System.exit(1);
		}
	}
	
	/**
	 * Reads a graph from the specified input file, computes a layout 
	 * for this graph, writes the layout into the specified output file, 
	 * and displays the layout. 
	 * 
	 * @param args name of the input file and of the output file.
	 * 	 If <code>args.length != 2</code>, the method outputs a help message.
	 */
	public static void main(final String[] args) {
		if (args.length != 2) {
			System.out.println(
				  "Usage: java LinLogLayout <inputfile> <outputfile>\n"
				+ "Computes a layout for the graph in <inputfile>\n"
				+ "writes the layout into <outputfile>, and displays the layout.\n\n"
				+ "Input file format:\n"
				+ "Each line represents an edge and has the format:\n"
				+ "<source> <target> <nonnegative real weight>\n"
				+ "The weight is optional, the default value is 1.0.\n\n"
				+ "Output file format:\n"
				+ "<node> <x-coordinate> <y-coordinate>"
			);
			System.exit(0);
		}
		
		Map<String,Map<String,Float>> graph = readGraph(args[0]);
		graph = makeSymmetricGraph(graph);
		Map<String,Integer> nodeToId = makeIds(graph);
		float[][] positions = makeInitialPositions(graph);
		// see class MinimizerBarnesHut for a description of the parameters
		MinimizerBarnesHut minimizer = new MinimizerBarnesHut(
			makeAttrIndexes(graph, nodeToId), makeAttrWeights(graph, nodeToId), 
			makeRepuWeights(graph, nodeToId),
			1.0f, 0.0f, 0.01f, positions, null);
		minimizer.minimizeEnergy(100);
		Map<String,float[]> nodeToPosition = convertPositions(positions, nodeToId);
		writePositions(nodeToPosition, args[1]);
		Map<String,Float> nodeToSize = computeDiameters(graph);
		(new GraphFrame(nodeToPosition, nodeToSize)).setVisible(true);
	}

}
