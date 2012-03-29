/*
 * Created on Aug 23, 2004
 */
package edu.iu.iv.toolkits.networkanalysistoolkit.analysis;

import java.util.Iterator;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.utils.GraphProperties;
import edu.uci.ics.jung.utils.Pair;
import edu.uci.ics.jung.utils.PredicateUtils;

/**
 * @author Ketan Mane
 * @author Shashikant Penumarthy
 */
public class Network {

    public Network(Graph graph) {
        this.graph = graph ;
    }
    
    private Graph graph;

    public Graph getGraph() {
        return this.graph;
    }

    public int getNumVertices() {
        return this.graph.numVertices();
    }

    public boolean isConnected() {
        return GraphProperties.isConnected(this.graph);
    }

    public boolean isDirected() {
        return PredicateUtils.enforcesDirected(this.graph);
    }

    public boolean hasSelfLoops() {
        return GraphProperties.containsSelfLoops(this.graph);
    }

    public boolean hasParallelEdges() {
        return GraphProperties.containsParallelEdges(this.graph);
    }

    public int getNumEdges() {
        return this.graph.numEdges();
    }

    public int getNumLoops() {
        int numLoops = 0;
        for (Iterator iterator = this.graph.getEdges().iterator(); iterator
                .hasNext();) {
            Edge e = (Edge) iterator.next();
            Pair pair = e.getEndpoints();
            if (((Vertex) pair.getFirst()).equals((Vertex) pair.getSecond()))
                ++numLoops;
        }
        return numLoops;
    }
}