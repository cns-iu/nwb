package prefuseGraphComparison;

import java.util.Iterator;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Queue;

public class PrefuseGraphComparison {
	
	
	public static boolean compare(Graph a, Graph b){
		
		boolean compare = true;
		
		if((isEqualNodeCount(a,b)) && (isEqualEdgeCount(a,b))&& (isCheckforDirectedness(a,b))){
			
			
			Boolean[] visitedA = new Boolean[a.getNodeCount()];
			Boolean[] visitedB = new Boolean[b.getNodeCount()];
			
			
				
			for(Iterator nodeA = a.nodeRows(); nodeA.hasNext();){
				visitedA[Integer.parseInt((nodeA.next().toString()))] = false;
			}
			
			for(Iterator nodeB = b.nodeRows(); nodeB.hasNext();){
				visitedB[Integer.parseInt((nodeB.next().toString()))] = false;
			}
			
			
			
			Queue graphQueueA = new Queue();
			Queue graphQueueB = new Queue();
			
			graphQueueA.enQueue("0");
			graphQueueB.enQueue("0");
			
			while(!graphQueueA.isEmpty() && !graphQueueB.isEmpty()){
				
				
				int graphNodeIdA = Integer.parseInt(graphQueueA.deQueue().toString());
				int graphNodeIdB = Integer.parseInt(graphQueueB.deQueue().toString());
				
				if((visitedA[graphNodeIdA]== false) && (visitedB[graphNodeIdB]==false)){
					
					if(graphNodeIdA == graphNodeIdB){
						visitedA[graphNodeIdA]=true;
						visitedA[graphNodeIdB]=true;
						
						int[] edgeIdArrayA = new int[a.getNode(graphNodeIdA).getDegree()];
						int[] edgeIdArrayB = new int[b.getNode(graphNodeIdB).getDegree()];
						
						
						
						int i = 0;
						for(Iterator edgesA = a.edgeRows(graphNodeIdA); edgesA.hasNext();){
							
							edgeIdArrayA[i++] = Integer.parseInt((edgesA.next().toString()));
						}
						
						int j = 0;
						for(Iterator edgesB = b.edgeRows(graphNodeIdB); edgesB.hasNext();){
							
							edgeIdArrayB[j++] = Integer.parseInt(edgesB.next().toString());
						}
						if(edgeIdArrayA.length == edgeIdArrayB.length){
							for(int k = 0; k < edgeIdArrayA.length; k++){
								int index = find(edgeIdArrayA[k],edgeIdArrayB);
								
								if(index==0){
									compare = false;
								}
								else {
									graphQueueA.enQueue(a.getEdge(k).getTargetNode().getRow());
									graphQueueB.enQueue(b.getEdge(index).getTargetNode().getRow());
								}
							}
							
						}else compare= false;
						
					}else compare = false;	
					
				}	
				
			}//end while loop
			
		}else compare = false;
		
		return compare;
	}
				
				
	private static boolean isCheckforDirectedness(Graph a, Graph b) {
		if(a.isDirected()==b.isDirected())
			return true;
		else
			return false;
		
	}


	private static int find(int i, int[] edgeIdArrayB) {
		int j = 0;
		while(j<=edgeIdArrayB.length){
			if(edgeIdArrayB[j] == i)
				return j;
			else j++;
		}
		return 0;
	}
	

	private static boolean isEqualEdgeCount(Graph a, Graph b) {

		if(a.getEdgeCount() == b.getEdgeCount())
			return true;
		else
			return false;
	}

	private static boolean isEqualNodeCount(Graph a, Graph b) {

		if(a.getNodeCount() == b.getNodeCount())
			return true;
		else 
			return false;
	}

	public static void main(String[] args){
		Graph g = new Graph();
		
        for ( int i=0; i<4; ++i ) {
            Node n1 = g.addNode();
            Node n2 = g.addNode();
            Node n3 = g.addNode();
            Node n4 = g.addNode();
            g.addEdge(n1, n2);
//            g.addEdge(n1, n4);
//            g.addEdge(n2, n3);
//            g.addEdge(n3, n4);
        }
//        g.addEdge(3, 7);
//        g.addEdge(5, 9);
//        g.addEdge(11, 15);
//        g.addEdge(12, 0);
	
	
		Graph h = new Graph();
	    for ( int i=0; i<4; ++i ) {
	        Node n1 = h.addNode();
	        Node n2 = h.addNode();
	        Node n3 = h.addNode();
	        Node n4 = h.addNode();
	        h.addEdge(n1, n2);
//	        h.addEdge(n1, n4);
//	        h.addEdge(n2, n3);
//	        h.addEdge(n3, n4);
	    }
//	    h.addEdge(3, 7);
//	    h.addEdge(5, 9);
//	    h.addEdge(11, 15);
//	    h.addEdge(15, 0);
	    
	    
	    if(compare(g,h)){
	    	System.out.println("Success");
	    }else {System.out.println("Fail");}
	}
	
}

	
	


