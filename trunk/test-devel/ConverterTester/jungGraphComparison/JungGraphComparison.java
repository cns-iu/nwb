package jungGraphComparison;

import java.util.Iterator;
import java.util.SortedSet;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import edu.uci.ics.jung.algorithms.GraphMatrixOperations;
import edu.uci.ics.jung.graph.ArchetypeGraph;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.AbstractArchetypeEdge;
import edu.uci.ics.jung.graph.impl.AbstractArchetypeVertex;
import edu.uci.ics.jung.graph.impl.AbstractSparseEdge;
import edu.uci.ics.jung.graph.impl.AbstractSparseGraph;
import edu.uci.ics.jung.utils.TestGraphs;


public class JungGraphComparison {
		
	public static boolean compare(Graph testGraph1, Graph testGraph2){
		
		Boolean compare = true;
		
		if(isStructurallyEquivalent(testGraph1, testGraph2)){
			
			if(checkForEdgeCount(testGraph1,testGraph2)){
				
				SortedSet vertexSet = (SortedSet) testGraph1.getVertices();
				for(Iterator vertexIter = vertexSet.iterator(); vertexIter.hasNext();){
					
					AbstractArchetypeVertex v1 = (AbstractArchetypeVertex)vertexIter.next();
					AbstractArchetypeVertex v2 = (AbstractArchetypeVertex) v1.getEqualVertex(testGraph2);
					
					if(v2!=null){
						
						for(Iterator edgeIter = v1.getIncidentEdges().iterator(); edgeIter.hasNext();){
							
							AbstractArchetypeEdge e1 = (AbstractArchetypeEdge)edgeIter.next();
							AbstractArchetypeEdge e2 = (AbstractArchetypeEdge)e1.getEqualEdge(testGraph2);
							
							if(e2!=null){
								
								AbstractSparseEdge es1 = (AbstractSparseEdge) e1;
								Vertex v1Target = es1.getOpposite((Vertex) v1);
								Vertex v2Target = (Vertex) v1Target.getEqualVertex(testGraph2);
								
								if(v2Target==null){
									
									System.out.println("Vertex id not matching");
									compare = false;
								}
								
							}
							else{
								System.out.println("Edge id not matching");
								compare = false;
							}
						}
					}
					else{
						System.out.println("Vertex id not matching");
						compare = false;
					}
				}
				
			}
			else{
				compare= false;
			}				
		}
		else{
			compare = false;
		}		
		return compare;
	}
	
	

	private static boolean checkForEdgeCount(Graph testGraph1, Graph testGraph2) {
		if(testGraph1.numEdges()==testGraph2.numEdges()){
			return true;
		}
		else { 
			System.out.println("edge count not equal");
			return false;
		}
	}



	private static boolean isStructurallyEquivalent(Graph testGraph1, Graph testGraph2) {
		
		GraphMatrixOperations gmo = new GraphMatrixOperations();
		
		DoubleMatrix2D dmTest1 = gmo.graphToSparseMatrix(testGraph1);
		
		DoubleMatrix2D dmTest2 = gmo.graphToSparseMatrix(testGraph2);
		
		double[][] test1Array = dmTest1.toArray();
		
		double[][] test2Array = dmTest2.toArray();
		
		Boolean isEqual = true;
		if(test1Array.length == test1Array.length){
			for(int i = 0; i < test1Array.length; i++){
				for(int j = 0; j < test1Array[i].length; j++){
					if (test1Array[i][j] != test2Array[i][j]){
						isEqual = false;
						System.out.println("edge weights not equivalent");
						break;
					}
				}
			}
		}
		else {
			System.out.println("not having equal number of vertices");
			isEqual = false;
		}
		
		if(isEqual){
			return true;
		}
		else
			return false;
	}

	public static void main(String[] args){
		TestGraphs t = new TestGraphs();
		Graph a = t.createTestGraph(true);
		Graph b = (Graph) a.copy();
		
		if(compare(a,b)){
			System.out.println("Success");
		}
		
		else System.out.println("Fail");
		
	}

}
