/*
 * Created on Sep 28, 2004
 */
package edu.iu.iv.modeling.barabasialbertmodel;

import java.util.Iterator;
import java.util.Random;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.UndirectedSparseVertex;

/**
 * This class has been modeled after JUNG's BarabasiAlbertGenerator. Only it
 * does not add self-loops. Also see
 * edu.uci.ics.jung.random.generators.BarabasiAlbertGenerator.
 * 
 * @author Shashikant
 */
public class BarabasiAlbertModelGenerator {

    private Random randomGen;

    int numEdgesToAttachPerTimeStep;

    int numInitialNodes;

    public BarabasiAlbertModelGenerator(int numInitialNodes,
            int numEdgesToAttach, int seed) {
        this.numInitialNodes = numInitialNodes;
        this.randomGen = new Random(seed);
        this.numEdgesToAttachPerTimeStep = numEdgesToAttach;
        initialize();
    }

    public BarabasiAlbertModelGenerator(int numInitialNodes,
            int numEdgesToAttach) {
        // if no seed provided use the current time as random number seed.
        this(numInitialNodes, numEdgesToAttach, (int) System
                .currentTimeMillis());
    }

    int elapsedTimeSteps;

    public void evolveGraph(int numTimeSteps) {
        for (int i = 0; i < numTimeSteps; i++) {
            Vertex newVertex = new UndirectedSparseVertex();
            this.graph.addVertex(newVertex);
            double totalDegree = 2 * this.graph.numEdges();
            for (int numEdgesAdded = 0; numEdgesAdded < numEdgesToAttachPerTimeStep;) {
                Edge e = null;
                for (Iterator iter = this.graph.getVertices().iterator(); iter
                        .hasNext();) {
                    Vertex existingVertex = (Vertex) iter.next();
                    if (existingVertex == newVertex
                            || existingVertex.isNeighborOf(newVertex))
                        continue;
                    // find probability that it can be attached. 
                    // preferential attachment:
                    // the higher the probability of an existing vertex, 
                    // the more likely it is that the new vertex will prefer
                    // to attach itself to that vertex.
                    double probability = (double) existingVertex.degree()
                            / totalDegree;
                    double random = this.randomGen.nextDouble();
                    if (probability > 0.0 && probability > random) {
                        e = new UndirectedSparseEdge(newVertex, existingVertex);
                        this.graph.addEdge(e);
                        ++numEdgesAdded;
                        totalDegree += 2;
                        break;
                    }
                }
            }
            ++elapsedTimeSteps;
        }
    }

    Graph graph;

    public Graph generateGraph() {
        return this.graph;
    }

    public int getNumElapsedTimeSteps() {
        return elapsedTimeSteps;
    }

    private void initialize() {
        elapsedTimeSteps = 0;
        graph = new UndirectedSparseGraph();
        for (int i = 0; i < numInitialNodes; ++i)
            graph.addVertex(new UndirectedSparseVertex());
        connectInitialVertices();
    }

    private void connectInitialVertices() {
        Vertex[] vertexArray = (Vertex[])this.graph.getVertices().toArray(new UndirectedSparseVertex[0]) ;
        for (int i = 0 ; i < vertexArray.length ; ++i)
            for (int j = 0 ; j < i ; ++j)
                this.graph.addEdge(new UndirectedSparseEdge(vertexArray[i], vertexArray[j])) ;
    }

    public void reset() {
        initialize();
    }

}