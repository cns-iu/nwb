package edu.iu.sci2.database.isi.extract.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseCreationException;
import org.cishell.utilities.AlgorithmUtilities;
import org.junit.BeforeClass;
import org.junit.Test;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;

import com.google.common.collect.ImmutableList;

public class ExtractCoCitationDocumentsAllTest {
	// Test the output of the following three algorithms, run in order...
	private static final String MERGE_PEOPLE_PID = "edu.iu.sci2.database.isi.merge.people.identical.MergeIdenticalPeople";
	private static final String MATCH_REFS_PID = "edu.iu.sci2.database.isi.match.reference_to_paper.MatchReferenceToPaperAlgorithm";
	private static final String EXTRACT_PID = "edu.iu.sci2.database.isi.extract.network.cocitation.document.all";
	
	private static Database database;
	private static Graph resultGraph;
	
	@BeforeClass
	public static void runAlgorithm() throws IOException, SQLException, DatabaseCreationException, AlgorithmExecutionException {
		database = FivePapersHelper.createDatabase();
		
		// Set to output of each step in the process
		Data dbData = FivePapersHelper.wrappedAsData(database);
		
		for (String pid : ImmutableList.of(MERGE_PEOPLE_PID, MATCH_REFS_PID, EXTRACT_PID)) {
			
			AlgorithmFactory fact = AlgorithmUtilities.getAlgorithmFactoryByPID(
					pid, Activator.getContext());
			
			Algorithm algo = fact.createAlgorithm(new Data[] {dbData}, 
					new Hashtable<String, Object>(), Activator.getCiContext());
			
			Data[] results = algo.execute();
			assertTrue(pid + " didn't generate any Data results", results.length > 0);
			dbData = results[0];
		}
		
		resultGraph = (Graph) dbData.getData();
	}

	@Test
	public void testCitedOnlyToCoreCocitation() {
		Graph g = resultGraph;
		
		Node x2000 = getNodeByLabel("Xxxx 2001");
		assertNotNull(x2000);
		
		Node avery1944 = getNodeByLabel("Avery OT, 1944, J EXP MED, V79, P137");
		assertNotNull(avery1944);
		
		Edge xToAvery = g.getEdge(x2000, avery1944);
		assertNotNull(
			"Should be an edge between Avery OT, 1944 (cited only) and Xxxx, 2001 (core), but there isn't",
			xToAvery);
		assertEquals("Avery OT, 1944 and Xxxx, 2001 should be co-cited exaclty once", 1, xToAvery.getInt("TIMES_COCITED"));
		assertEquals(2002, xToAvery.getInt("LATEST_COCITATION"));
		
	}
	
	@Test
	public void testNumberOfNodes() {
		assertEquals(19, resultGraph.getNodeCount());
	}
	
	@Test
	public void testNumberOfEdges() {
		assertEquals(29, resultGraph.getEdgeCount());
	}

	private Node getNodeByLabel(String label) {
		Graph g = resultGraph;

		@SuppressWarnings("unchecked")
		Iterator<Node> nodeIter = g.nodes();
		while (nodeIter.hasNext()) {
			Node n = nodeIter.next();
			if (label.equals(n.getString("LABEL"))) {
				return n;
			}
		}
		return null;
	}
	
//	@Test
//	public void testPrintStuff() {
//		Graph g = resultGraph;
//		
//		System.out.println("Number of nodes: " + g.getNodeCount());
//		System.out.println("Node schema: " + g.getNodeTable().getSchema());
//		
//		@SuppressWarnings("unchecked")
//		Iterator<Node> nodeIter = g.nodes();
//		while (nodeIter.hasNext()) {
//			Node n = nodeIter.next();
//			System.out.println(String.format(
//					"<<%s>>: %s", n.getString("LABEL"), n));
//		}
//		
//		System.out.println("Number of edges: " + g.getEdgeCount());
//		System.out.println("Edge schema: " + g.getEdgeTable().getSchema());
//		
//		@SuppressWarnings("unchecked")
//		Iterator<Edge> edgeIter = g.edges();
//		while (edgeIter.hasNext()) {
//			Edge e = edgeIter.next();
//			System.out.println(e);
//		}
//		
//	}
	
}
