package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import edu.uci.ics.jung.graph.Vertex;



public class PrefuseClusteringCoefficients {
	
    public static ClusteringCoefficients getNewInstance() {
        return new ClusteringCoefficients() ;
    }

    public double getClusteringCoefficientMN(final prefuse.data.Graph network) {
        double clusteringCoefficientMN = 0;
        int numTriangles = 0;
        int numConnectedTriples = 0;
        for (Iterator vertexIter = network.nodes(); vertexIter
                .hasNext();) {
            Node n = (Node) vertexIter.next();
            int numNeighbors = n.getDegree();
            numConnectedTriples += numNeighbors * (numNeighbors - 1) / 2;
           // ArrayList neighbors = new ArrayList(v.getNeighbors());
            for (Iterator neighborIter1 = n.neighbors(); neighborIter1.hasNext();) {
                Node n1 = (Node)neighborIter1.next();
                for (Iterator neighborIter2 = n.neighbors(); neighborIter2.hasNext();) {
                    Node n2 = (Node) neighborIter2.next();
                    //Edge e1 = (Edge)n1.edges().next();
                    for(Iterator edgeIter = n1.edges(); edgeIter.hasNext();){
                    	Edge edge = (Edge)edgeIter.next();
                    	if((n1 == edge.getSourceNode() && n2 == edge.getTargetNode())
                    			|| (n1 == edge.getTargetNode() && n2 == edge.getSourceNode())){
                    		++numTriangles;
                    	}
                    }
                }
            }
        }
    
        clusteringCoefficientMN = (double) numTriangles / numConnectedTriples;
        return clusteringCoefficientMN;
    }

   /* public double getClusteringCoefficientWS(Network network, int pendantClusteringCoefficient) {
        Graph graph = network.getGraph() ;
        double clusteringCoefficientWS = 0;
       // System.out.println(network.isDirected());
        if (!network.isDirected()) {
        	System.out.println("Calculating clustering coefficient");
            for (Iterator vertexIterator = graph.getVertices().iterator(); vertexIterator
                    .hasNext();) {
                Vertex v = (Vertex) vertexIterator.next();
                if (v.numNeighbors() == 1) {
                    clusteringCoefficientWS += pendantClusteringCoefficient;
                } else {
                    int numActualEdges = 0;
                    ArrayList neighbors = new ArrayList(v.getNeighbors());
                    int numNeighbors = neighbors.size();
                    for (int i = 0; i < numNeighbors; ++i) {
                        Vertex v1 = (Vertex) neighbors.get(i);
                        for (int j = i + 1; j < numNeighbors; ++j) {
                            Vertex v2 = (Vertex) neighbors.get(j);
                            if (v1.isNeighborOf(v2))
                                ++numActualEdges;
                        }
                    }
                   // System.out.println(clusteringCoefficientWS);
                    clusteringCoefficientWS += (double) numActualEdges
                            / (numNeighbors * (numNeighbors - 1) / 2);
                }
              //  System.out.println(clusteringCoefficientWS);
            }
            clusteringCoefficientWS /= graph.numVertices();
        } else
            clusteringCoefficientWS = Double.NaN;
        
       // System.out.println(clusteringCoefficientWS);
        return clusteringCoefficientWS;
    }

    public double getIncomingClusteringCoefficient(Network network) {
        Graph graph = network.getGraph() ;
        double incomingClusteringCoefficient = 0;
        if (!network.isDirected())
            incomingClusteringCoefficient = Double.NaN;
        else {
            incomingClusteringCoefficient = 0;
            int numValidNodes = 0;
            for (Iterator iterator = graph.getVertices().iterator(); iterator
                    .hasNext();) {
                Vertex v = (Vertex) iterator.next();
                if (v.inDegree() <= 1)
                    continue;
                ++numValidNodes;
                int denom = v.inDegree() * (v.inDegree() - 1);
                int num = 0;
                Iterator neighIter = v.getPredecessors().iterator();
                while (neighIter.hasNext()) {
                    Vertex vert = (Vertex) neighIter.next();
                    Iterator neighIter2 = v.getPredecessors().iterator();
                    while (neighIter2.hasNext())
                        if (((Vertex) neighIter2.next()).isNeighborOf(vert))
                            ++num;
                }
                incomingClusteringCoefficient += (double) num / denom;
            }
            incomingClusteringCoefficient /= numValidNodes;
        }
        return incomingClusteringCoefficient;
    }

    public double getMutualConnectionCoefficient(Network network) {
        Graph graph = network.getGraph() ;
        double mutualConnectionCoefficient = 0;
        if (mutualConnectionCoefficient == -1) {
            if (!network.isDirected())
                mutualConnectionCoefficient = 1.0;
            else if (!network.hasSelfLoops())
                mutualConnectionCoefficient = 0.0;
            else {
                mutualConnectionCoefficient = 0.0;
                Iterator iterator = graph.getVertices().iterator();
                while (iterator.hasNext()) {
                    int gDown_Intersection_gUp = 0;
                    Vertex v = (Vertex) iterator.next();
                    Set pred = v.getPredecessors();
                    Iterator sucIt = v.getSuccessors().iterator();
                    while (sucIt.hasNext()) {
                        Vertex vert = (Vertex) sucIt.next();
                        if (pred.contains(vert))
                            ++gDown_Intersection_gUp;
                    }
                    mutualConnectionCoefficient += (double) gDown_Intersection_gUp
                            / v.getSuccessors().size();
                }
                mutualConnectionCoefficient /= network.getNumVertices();
            }
        }
        return mutualConnectionCoefficient;
    }

    public double getOutgoingClusteringCoefficient(Network network) {
        Graph graph = network.getGraph();
        double outgoingClusteringCoefficient = 0;
        if (!network.isDirected())
            outgoingClusteringCoefficient = Double.NaN;
        else {
            outgoingClusteringCoefficient = 0;
            Iterator iterator = graph.getVertices().iterator();
            int numValidNodes = 0;
            while (iterator.hasNext()) {
                Vertex v = (Vertex) iterator.next();
                if (v.inDegree() <= 1)
                    continue;
                ++numValidNodes;
                int denom = v.outDegree() * (v.outDegree() - 1);
                int num = 0;
                Iterator neighIter = v.getSuccessors().iterator();
                while (neighIter.hasNext()) {
                    Vertex vert = (Vertex) neighIter.next();
                    Iterator neighIter2 = v.getSuccessors().iterator();
                    while (neighIter2.hasNext())
                        if (((Vertex) neighIter2.next()).isNeighborOf(vert))
                            ++num;
                }
                outgoingClusteringCoefficient += (double) num / denom;
            }
            outgoingClusteringCoefficient /= numValidNodes;
        }
        return outgoingClusteringCoefficient;
    }

    public double getTriangleCoefficient(Network network) {
        Graph graph = network.getGraph();
        double triangleCoefficient = 0;
        if (network.isDirected()) {
            triangleCoefficient = 0;
            Iterator iterator = graph.getVertices().iterator();
            while (iterator.hasNext()) {
                Vertex alpha = (Vertex) iterator.next();
                Iterator sucIt = alpha.getSuccessors().iterator();
                int num = 0;
                while (sucIt.hasNext()) {
                    Vertex beta = (Vertex) sucIt.next();
                    Iterator vSucIter = alpha.getSuccessors().iterator();
                    while (vSucIter.hasNext()) {
                        if (((Vertex) vSucIter.next()).isSuccessorOf(alpha))
                            ++num;
                    }
                    triangleCoefficient += (double) num / beta.outDegree();
                }
            }
            triangleCoefficient /= graph.numVertices();
        } else {
            triangleCoefficient = Double.NaN;
        }
        return triangleCoefficient;
    }
	
*/
}
