/*
 * Created on Sep 8, 2004
 */
package edu.iu.iv.toolkits.networkanalysistoolkit.analysis;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;

/**
 * @author Team IVC
 */
// Shashikant Penumarthy
public class GraphThresholder {

    private GraphThresholder() {
        // do nothing
    }

    public static GraphThresholder getNewInstance() {
        return new GraphThresholder() ;
    }

    /**
     * <b>Not implemented yet </b>
     * //FIXME: 
     */
    public Graph getEdgeWeightThresholdedGraph() {
        throw new UnsupportedOperationException("This method has not been implemented yet") ;
    }

    public Graph getVertexThresholdedGraph(Graph graph, int idmin, int idmax,
            int odmin, int odmax) {
        Graph vertexThresholdedDirectedGraph;
        ThresholdVertexFilter tvf = new ThresholdVertexFilter(idmin, idmax,
                odmin, odmax);
        vertexThresholdedDirectedGraph = tvf.filter(graph).assemble();
        return vertexThresholdedDirectedGraph;
    }

    public Graph getVertexThresholdedGraph(UndirectedGraph graph,
            int mindegree, int maxdegree) {
        Graph vertexThresholdedUndirectedGraph;
        ThresholdVertexFilter tvf = new ThresholdVertexFilter(mindegree,
                maxdegree);
        vertexThresholdedUndirectedGraph = tvf.filter(graph).assemble();
        return vertexThresholdedUndirectedGraph;
    }
}