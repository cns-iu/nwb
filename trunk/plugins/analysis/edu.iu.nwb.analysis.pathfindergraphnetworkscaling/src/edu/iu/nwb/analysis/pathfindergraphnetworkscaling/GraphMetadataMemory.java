package edu.iu.nwb.analysis.pathfindergraphnetworkscaling;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cishell.framework.data.Data;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

import edu.uci.ics.jung.graph.ArchetypeEdge;
import edu.uci.ics.jung.graph.ArchetypeGraph;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgeWeightLabeller;
import edu.uci.ics.jung.utils.UserData;

public class GraphMetadataMemory {

	private Graph graph;
	private DoubleMatrix2D matrix;
	private boolean weighted = false;
	private List vertices = new ArrayList();

	public GraphMetadataMemory(Graph graph) {
		this.graph = graph;
		this.matrix = constructMatrix();
	}

	private DoubleMatrix2D constructMatrix() {
		//for now, this only deals with undirected graphs
		//use enforcesDirected and its pair later to deal with the other case
		
		int numVertices = graph.numVertices();
		
		//assume sparcity for now. This should later get some smarts, particularly if used with other algorithms
		DoubleMatrix2D graphMatrix = new SparseDoubleMatrix2D(numVertices, numVertices);
		
		vertices = new ArrayList(this.graph.getVertices());
		
		Iterator vertexIterator = vertices.iterator();
		
		while(vertexIterator.hasNext()) {
			
			//we're guaranteed this cast and later to Edge will work because this is Graph, not an ArchetypeGraph
			Vertex vertex = (Vertex) vertexIterator.next();
			
			Iterator edgeIterator = vertex.getIncidentEdges().iterator();
			while(edgeIterator.hasNext()) {
				Edge edge = (Edge) edgeIterator.next();
				String weightString = "1";
				if(edge.containsUserDatumKey("weight")) {
					weightString = (String) edge.getUserDatum("weight");
					weighted = true;
				}
				
				Vertex connectedVertex = edge.getOpposite(vertex);
				
				int weight = Integer.parseInt(weightString);
				graphMatrix.setQuick(vertices.indexOf(vertex), vertices.indexOf(connectedVertex), weight);
			}
		}
		
		return graphMatrix;
	}

	public DoubleMatrix2D getMatrix() {
		return matrix;
	}

	public Graph reconstructMetadata(DoubleMatrix2D matrix) {
		//we're copying a Graph, so we're guaranteed to get a Graph back
		Graph graphCopy = (Graph) this.graph.copy();
		
		if(weighted) {
			for(int row = 0; row < matrix.rows(); row++) {
				//begin is a bit of a misnomer until this can handle directed graphs
				Vertex begin = ((Vertex) vertices.get(row));
				begin = (Vertex) begin.getEqualVertex(graphCopy);
				for(int column = 0; column < matrix.columns(); column++) {
					Vertex end = (Vertex) vertices.get(column);
					end = (Vertex) end.getEqualVertex(graphCopy);
					//round or drop? for now, drop, on the assumption that these should still be ints anyways
					int weight = (int) matrix.getQuick(row, column);
					Edge edge = begin.findEdge(end);
					if(edge != null) {
						if(weight == 0) {
							graphCopy.removeEdge(edge);
						} else {
							edge.setUserDatum("weight", Integer.toString(weight), UserData.SHARED);
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
							graphCopy.removeEdge(edge);
						}
					} else if(connected){
						//see above note about creating the appropriate edge
					}
				}
			}
		}
		
		return graphCopy;
	}

}
