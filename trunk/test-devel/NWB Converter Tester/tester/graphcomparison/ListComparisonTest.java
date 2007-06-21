package tester.graphcomparison;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ListComparisonTest {

	public static void main(String[] args) {
		Map map1 = makeTheMap1();
		Map map2 = makeTheMap2();
		
		System.out.println(map1.equals(map2));
	}
	
	private static Map<Integer, Set<Integer>> makeTheMap1() {
		Integer nodeA = 1;
		Integer nodeB = 2;
	    Integer nodeC = 5;
	    Integer nodeD = 97;
	    
	    Set<Integer> nodeAEdges = new HashSet<Integer>();
	    nodeAEdges.add(nodeB);
	    nodeAEdges.add(nodeD);
	    
	    Set<Integer> nodeBEdges = new HashSet<Integer>();
	    nodeBEdges.add(nodeA);
	    
	    Set<Integer> nodeCEdges = new HashSet<Integer>();
	    
	    Set<Integer> nodeDEdges = new HashSet<Integer>();
	    nodeDEdges.add(nodeC);
	    
	    Map<Integer, Set<Integer>> map1 = new HashMap<Integer, Set<Integer>>();
	    map1.put(nodeA, nodeAEdges);
	    map1.put(nodeB, nodeBEdges);
	    map1.put(nodeC, nodeCEdges);
	    map1.put(nodeD, nodeDEdges);
	    
	    return map1;
	}
	
	private static Map<Integer, Set<Integer>> makeTheMap2() {
		Integer nodeA = 1;
		Integer nodeB = 2;
	    Integer nodeC = 5;
	    Integer nodeD = 97;
	    
	    Set<Integer> nodeAEdges = new HashSet<Integer>();
	    nodeAEdges.add(nodeB);
	    nodeAEdges.add(nodeD);
	    
	    Set<Integer> nodeBEdges = new HashSet<Integer>();
	    nodeBEdges.add(nodeA);
	    
	    Set<Integer> nodeCEdges = new HashSet<Integer>();
	    
	    Set<Integer> nodeDEdges = new HashSet<Integer>();
	    nodeDEdges.add(nodeC);
	    
	    Map<Integer, Set<Integer>> map1 = new HashMap<Integer, Set<Integer>>();
	    map1.put(nodeA, nodeAEdges);
	    map1.put(nodeB, nodeBEdges);
	    map1.put(nodeC, nodeCEdges);
	    map1.put(nodeD, nodeDEdges);
	    
	    return map1;
	}
	
	
	
	
}
