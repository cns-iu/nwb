package tester.graphcomparison;

import java.io.File;
import java.io.FileInputStream;

import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.io.GraphMLReader;

public class GraphComparerTester {
	public static final String graphMLFileDirectory = "/home/mwlinnem/" +
		"workspace/NWB Converter Tester/test_files/GraphML Files/";
	public static void main(String[] args) {
		GraphComparer comparer = new DefaultGraphComparer();
		
		System.out.println("---Basic Tests Assuming Ids are not preserved-");
		runBasicTests(comparer, true);
		System.out.println("----------------------------------------------");
		System.out.println("---Basic Tests Assuming Ids are preserved-----");
		runBasicTests(comparer, false);
		System.out.println("----------------------------------------------");
		System.out.println("---Real Tests Assuming Ids are not preserved--");
		runRealTests(comparer, true);
		System.out.println("----------------------------------------------");
		System.out.println("---Real Tests Assuming Ids are preserved------");
	}
	
	private static void runRealTests(GraphComparer comparer,
			boolean idsPreserved) {
		
	}
	private static void runBasicTests(GraphComparer comparer,
			boolean idsPreserved) {
//		setup
		Schema edgeTableSchema = new Schema();
		edgeTableSchema.addColumn(Graph.DEFAULT_SOURCE_KEY, Integer.class);
		edgeTableSchema.addColumn(Graph.DEFAULT_TARGET_KEY, Integer.class);
		
		//test1
		Graph emptyGraph1 = new Graph();
		Graph emptyGraph2 = new Graph();
		
		ComparisonResult result1 = comparer.compare(emptyGraph1, 
				emptyGraph2, false);	
		System.out.println("Empty undirected graph test ... " + result1);
		
		//test2
		Graph directedGraph1 = new Graph(idsPreserved);
		Graph directedGraph2 = new Graph(idsPreserved);
		
		ComparisonResult result2 = comparer.compare(directedGraph1,
				directedGraph2, true);		
		System.out.println("Empty directed graph test ... " + result2);
		
		//test3
		Table nodeTable1 = new Table();
		nodeTable1.addRows(10);
		
		Table nodeTable2 = new Table();
		nodeTable2.addRows(10);
		
		Graph noEdgeGraph1 = new Graph(nodeTable1, idsPreserved);
		Graph noEdgeGraph2 = new Graph(nodeTable2, idsPreserved);
		
		ComparisonResult result3 = comparer.compare(noEdgeGraph1,
				noEdgeGraph2, false);
		System.out.println("No edge graph test ... " + result3);
		
		//test4 (should fail)
		Table nodeTable3 = new Table();
		nodeTable3.addRows(11);
		
		Graph noEdgeGraph3 = new Graph(nodeTable3, idsPreserved);
		
		ComparisonResult result4 = comparer.compare(noEdgeGraph1,
				noEdgeGraph3, false);
		System.out.println("No edge graph test 2 (should fail) ... " + result4);
	}
	

	private static Graph loadGraph(String fileName) {
		return loadGraph(graphMLFileDirectory, fileName);
	}
	
	private static Graph loadGraph(String directoryPath, String fileName) {
		File fileHandler = new File(directoryPath + fileName);
		try {
		Graph graph= (new GraphMLReader()).readGraph(new FileInputStream(fileHandler));
		return graph;
		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
			return null; //makes Eclipse happy
		}

	}
	
	
	
//	//test5
//	Table nodeTable4 = new Table();
//	nodeTable4.addRows(4);
//	
//	Table edgeTable4 = new Table();
//	edgeTable4.addColumns(edgeTableSchema);
//	edgeTable4.addRows(4);
//	
//	addEdge(edgeTable4, 0, 0, 1);
//	addEdge(edgeTable4, 1, 0, 3);
//	addEdge(edgeTable4, 2, 1, 0);
//	addEdge(edgeTable4, 3, 3, 2);
//	
//	Graph edgedGraph1 = new Graph(nodeTable4, edgeTable4, true);
//	Graph edgedGraph2 = new Graph(nodeTable4, edgeTable4, true);
//	
//	ComparisonResult result5 = comparer.compare(edgedGraph1, 
//					edgedGraph2, true);
//	System.out.println("Edged graph test ... " + result5);
	
//	private static void addEdge(Table edgeTable,int row, int sourceID, int targetID) {
//	edgeTable.setString(row, Graph.DEFAULT_SOURCE_KEY, String.valueOf(sourceID));
//	edgeTable.setString(row, Graph.DEFAULT_TARGET_KEY, String.valueOf(targetID));
//}
}
