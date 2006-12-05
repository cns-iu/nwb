package edu.iu.nwb.analysis.pathfindergraphnetworkscaling;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cishell.framework.data.Data;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

import edu.uci.ics.jung.graph.ArchetypeEdge;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgeWeightLabeller;

public class GraphMetadataMemory {

	private Graph graph;
	private DoubleMatrix2D matrix;
	private List vertices = new ArrayList();

	public GraphMetadataMemory(Graph graph) {
		this.graph = graph;
		this.matrix = constructMatrix();
	}

	private DoubleMatrix2D constructMatrix() {
		//for now, this only deals with undirected graphs
		//use enforcesDirected and its pair later to deal with the other case
		
		int numVertices = graph.numVertices();
		
		//assume sparcity for now. This should later get some smarts.
		DoubleMatrix2D graphMatrix = new SparseDoubleMatrix2D(numVertices, numVertices);
		
		EdgeWeightLabeller weights = null;
		
		if(EdgeWeightLabeller.hasWeightLabeller(graph)) {
			weights = EdgeWeightLabeller.getLabeller(graph);
		}
		
		
		vertices = new ArrayList(this.graph.getVertices());
		
		Iterator vertexIterator = vertices.iterator();
		
		while(vertexIterator.hasNext()) {
			
			//we're guaranteed this cast and later to Edge will work because this is Graph, not an ArchetypeGraph
			Vertex vertex = (Vertex) vertexIterator.next();
			
			Iterator edgeIterator = vertex.getIncidentEdges().iterator();
			while(edgeIterator.hasNext()) {
				Edge edge = (Edge) edgeIterator.next();
				int weight = weights != null ? weights.getWeight(edge) : 1;
				
				Vertex connectedVertex = edge.getOpposite(vertex);
				
				graphMatrix.setQuick(vertices.indexOf(vertex), vertices.indexOf(connectedVertex), weight);
			}
		}
		
		return graphMatrix;
	}

	public DoubleMatrix2D getMatrix() {
		System.out.println(matrix.toString());
		return matrix;
	}

	public Graph reconstructMetadata(DoubleMatrix2D matrix) {
		System.out.println(matrix.toString());
		Graph graph = null;
		try {
			graph = (Graph) this.graph.clone();
		} catch (CloneNotSupportedException e) {
			//Big problem!
		}
		
		if(EdgeWeightLabeller.hasWeightLabeller(graph)) {
			EdgeWeightLabeller weights = EdgeWeightLabeller.getLabeller(graph);
			for(int row = 0; row < matrix.rows(); row++) {
				//begin is a bit of a misnomer until this can handle directed graphs
				Vertex begin = (Vertex) vertices.get(row);
				for(int column = 0; column < matrix.columns(); column++) {
					Vertex end = (Vertex) vertices.get(column);
					//round or drop? for now, drop, on the assumption that these should still be ints anyways
					int weight = (int) matrix.getQuick(row, column);
					Edge edge = begin.findEdge(end);
					if(edge != null) {
						if(weight == 0) {
							System.out.println("Tossing edge " + edge.toString());
							graph.removeEdge(edge);
						} else {
							System.out.println("Setting edge weight: " + edge.toString());
							weights.setWeight(edge, weight);
						}
					} else if(weight != 0) {
						//for now do nothing, later create the appropriate directed or undirected edge and add it to the graph
					}
				}
			}
		} else {
			for(int row = 0; row < matrix.rows(); row++) {
				Vertex begin = (Vertex) vertices.get(row);
				for(int column = 0; column < matrix.columns(); column++) {
					Vertex end = (Vertex) vertices.get(column);
					boolean connected = matrix.get(row, column) != 0;
					Edge edge = begin.findEdge(end);
					if(edge != null) {
						if(!connected) {
							System.out.println("Tossing edge " + edge.toString());
							graph.removeEdge(edge);
						}
					} else if(connected){
						//see above note about creating the appropriate edge
					}
				}
			}
		}
		
		return graph;
	}

}
