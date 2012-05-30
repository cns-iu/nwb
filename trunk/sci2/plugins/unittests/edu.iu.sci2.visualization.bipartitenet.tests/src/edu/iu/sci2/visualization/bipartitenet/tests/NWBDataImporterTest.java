package edu.iu.sci2.visualization.bipartitenet.tests;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;

import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.sci2.visualization.bipartitenet.model.BipartiteGraphDataModel;
import edu.iu.sci2.visualization.bipartitenet.model.Edge;
import edu.iu.sci2.visualization.bipartitenet.model.NWBDataImporter;
import edu.iu.sci2.visualization.bipartitenet.model.Node;
import edu.iu.sci2.visualization.bipartitenet.model.NodeDestination;

@RunWith(JUnit4.class)
public class NWBDataImporterTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testGetsAResult() throws IOException, ParsingException {
		NWBDataImporter importer = NWBDataImporter.create("bipartitetype", "Who", "Desirability", null, Node.LABEL_ORDERING,
		Node.LABEL_ORDERING);
		BipartiteGraphDataModel model;
		model = importer.constructModelFromFile(getTestNetwork());
		assertNotNull(model);
	}

	private InputStream getTestNetwork() {
		return this.getClass().getResourceAsStream("test-network.nwb");
	}
	
	@Test
	public void testBadTypeColumn() throws IOException, ParsingException {
		NWBDataImporter importer = NWBDataImporter.create("wrongname", "Who", "Desirability", null, Node.LABEL_ORDERING,
		Node.LABEL_ORDERING);
		exception.expect(ParsingException.class);
		importer.constructModelFromFile(getTestNetwork());
	}
	
	@Test
	public void testBadSizeColumn() throws IOException, ParsingException {
		NWBDataImporter importer = NWBDataImporter.create("bipartitetype", "Who", "wrongname", null, Node.LABEL_ORDERING,
		Node.LABEL_ORDERING);
		exception.expect(ParsingException.class);
		importer.constructModelFromFile(getTestNetwork());
	}
	
	@Test
	public void testCorrectModel() throws IOException, ParsingException {
		NWBDataImporter importer = NWBDataImporter.create("bipartitetype", "left", null, "weight", Node.LABEL_ORDERING,
				Node.LABEL_ORDERING);
		BipartiteGraphDataModel model;
		model = importer.constructModelFromFile(new ByteArrayInputStream(getShortTestNetwork()));
		assertNotNull(model);
		
		ImmutableList<Node> leftNodes = model.getLeftNodes();
		assertEquals(ImmutableList.of(new Node("a", 1, NodeDestination.LEFT)), leftNodes);

		ImmutableList<Node> rightNodes = model.getRightNodes();
		assertEquals(ImmutableList.of(new Node("b", 1, NodeDestination.RIGHT)), rightNodes);
		
		ImmutableList<Edge> edges = model.getEdges();
		assertEquals(ImmutableList.of(new Edge(leftNodes.get(0), rightNodes.get(0), 2.0)),
				edges);
	}

	@Test
	public void testCantRunTwice() throws Exception {
		NWBDataImporter importer = NWBDataImporter.create("bipartitetype", "left", null, "weight", Node.LABEL_ORDERING,
				Node.LABEL_ORDERING);
		importer.constructModelFromFile(new ByteArrayInputStream(getShortTestNetwork()));
		exception.expect(IllegalStateException.class);
		importer.constructModelFromFile(new ByteArrayInputStream(getShortTestNetwork()));
	}
	
	// TODO test sorting!
	
	private byte[] getShortTestNetwork() {
		StringBuilder s = new StringBuilder();

		s.append("*Nodes 2\n");
		s.append("id*int	label*string	bipartitetype*string\n");

		s.append("1		\"a\"	\"left\"\n");
		s.append("2		\"b\"	\"right\"\n");

		s.append("*DirectedEdges 1\n");
		s.append("source*int	target*int	weight*float\n");

		s.append("1		2	2.0\n");

		return s.toString().getBytes();
	}
}
