package edu.iu.nwb.analysis.pathfindergraphnetworkscaling;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.utils.UserData;

/**
 * @author Russell Duhon
 *
 */
public class GraphMetadataMemory {

	private Graph graph;
	private DoubleMatrix2D matrix;
	private boolean weighted = false;
	private List vertices = new ArrayList();

	/**
	 * Construct a memory for the graph that can generate a matrix representation of a graph
	 * as well as reconstruct the appropriate metadata for a new matrix representing a changed
	 * version of the graph.
	 * @param graph The graph to remember
	 */
	public GraphMetadataMemory(Graph graph) {
		this.graph = graph;
		this.vertices = new ArrayList(this.graph.getVertices());
		this.matrix = constructMatrix();
	}

	private DoubleMatrix2D constructMatrix() {
		//for now, this only deals with undirected graphs
		//use enforcesDirected and its pair later to deal with the other case
		
		int numVertices = graph.numVertices();
		
		//assume sparcity for now. This should later get some smarts, particularly if used with other algorithms
		DoubleMatrix2D graphMatrix = new SparseDoubleMatrix2D(numVertices, numVertices);
		
		Iterator vertexIterator = vertices.iterator();
		
		while(vertexIterator.hasNext()) {
			
			//we're guaranteed this cast and later to Edge will work because this is Graph, not an ArchetypeGraph
			Vertex vertex = (Vertex) vertexIterator.next();
			
			Iterator edgeIterator = vertex.getIncidentEdges().iterator();
			while(edgeIterator.hasNext()) {
				Edge edge = (Edge) edgeIterator.next();
				String weightString = "1";
				if(edge.containsUserDatumKey("weight")) {
					weightString = edge.getUserDatum("weight").toString();
					//bit of a hack, since we can't rely on anything other than the presence of this userdatum on an edge
					weighted = true;
				}
				
				//what's the other vertex connected to the edge
				Vertex connectedVertex = edge.getOpposite(vertex);
				
				//We're accepting doubles here because the matrix will just convert to doubles anyways. The output should be ints,
				//since weights on graphs should be ints
				//TODO: find out if some people use double weights on graphs
				double weight = Double.parseDouble(weightString);
				
				//fill in the correct value based on the indices of the vertices in our list of vertices
				graphMatrix.setQuick(vertices.indexOf(vertex), vertices.indexOf(connectedVertex), weight);
			}
		}
		
		return graphMatrix;
	}

	/**
	 * @return A matrix representation of the remembered matrix
	 */
	public DoubleMatrix2D getMatrix() {
		return matrix;
	}

	/**
	 * The changes to the graph can only be in edge presence and weight.
	 * That is, the rows and columns must represent the same vertices,
	 * but the values can be different.
	 * The implementation is currently unable to deal with edges being
	 * added, but that is a possible addition if a need arises.
	 * @param matrix This represents a changed version of the remembered graph
	 * @return A copy of the remembered graph, modified based on the given matrix
	 */
	public Graph reconstructMetadata(DoubleMatrix2D matrix) {
		//we're copying a Graph, so we're guaranteed to get a Graph back
		Graph graphCopy = (Graph) this.graph.copy();
		
		if(weighted) {
			for(int row = 0; row < matrix.rows(); row++) {
				//begin is a bit of a misnomer until this can handle directed graphs
				Vertex begin = ((Vertex) vertices.get(row));
				
				//we need to do this (and are guaranteed it will work) so we're working with edges in the graph copy,
				//but based on the order established when this memory was initialized
				begin = (Vertex) begin.getEqualVertex(graphCopy);
				
				for(int column = 0; column < matrix.columns(); column++) {
					//same comments as for begin
					Vertex end = (Vertex) vertices.get(column);
					end = (Vertex) end.getEqualVertex(graphCopy);
					//round or drop? for now, drop, on the assumption that these should still be ints anyways
					int weight = (int) matrix.getQuick(row, column);
					
					//where's the edge connecting these two? by requirement (check this for input data?) there must be only one
					Edge edge = begin.findEdge(end);
					if(edge != null) { //there's an edge in the original graph
						if(weight == 0) { //but not in our output
							graphCopy.removeEdge(edge); //so get rid of the edge
						} else { //there is in our output
							//we can share because its a String
							edge.setUserDatum("weight", new Integer(weight), UserData.SHARED); //update its weight
						}
					} else if(weight != 0) { //no edge in the original, but there is in the output
						//for now do nothing (since there should be no new edges with this algorithm)
						//later, if used elsehwere, create the appropriate directed or undirected edge and add it to the graph
					}
				}
			}
		} else { //not weighted
			for(int row = 0; row < matrix.rows(); row++) {
				Vertex begin = (Vertex) vertices.get(row);
				begin = (Vertex) begin.getEqualVertex(graphCopy);
				for(int column = 0; column < matrix.columns(); column++) {
					Vertex end = (Vertex) vertices.get(column);
					end = (Vertex) end.getEqualVertex(graphCopy);
					boolean connected = matrix.get(row, column) != 0;
					Edge edge = begin.findEdge(end);
					if(edge != null) { //edge in original graph
						if(!connected) { //but not in our output
							graphCopy.removeEdge(edge); //so get rid of the edge
						}
					} else if(connected){ //no edge in the original, but there is in the output
						//see above note about creating the appropriate edge
					}
				}
			}
		}
		
		return graphCopy;
	}

}
