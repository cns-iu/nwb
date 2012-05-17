package edu.iu.nwb.util.nwbfile.pipe.tests;

import static org.easymock.EasyMock.*;

import java.io.ByteArrayInputStream;

import com.google.common.collect.ImmutableMap;

import junit.framework.TestCase;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.model.AttributePredicates;
import edu.iu.nwb.util.nwbfile.pipe.ParserPipe;

public class KeepNodesAboveBelowTest extends TestCase {

	/**
	 * Test that we can keep nodes with weight above a threshold.
	 */
	public void testAbove() throws Exception {
		NWBFileParserHandler mock = createNiceMock(NWBFileParserHandler.class);
		mock.addNode(4, "d", ImmutableMap.of("weight", (Object) 4.0f));
		mock.addNode(5, "e", ImmutableMap.of("weight", (Object) 5.0f));
		MockUtils.noMoreNodes(mock);
		// all edges go back to node 1, which was omitted.
		MockUtils.noMoreEdges(mock);
		replay(mock);

		NWBFileParserHandler filter = ParserPipe.create()
				.filterNodes(AttributePredicates.keepAbove("weight", 3.0))
				.outputTo(mock);
		
		byte[] toFilter = getTestNWB();
		NWBFileParser parser = new NWBFileParser(new ByteArrayInputStream(
				toFilter));
		parser.parse(filter);

		verify(mock);
	}
	
	/**
	 * Test that we can keep nodes with weight below a threshold.
	 */
	public void testBelow() throws Exception {
		NWBFileParserHandler mock = createNiceMock(NWBFileParserHandler.class);
		mock.addNode(1, "a", ImmutableMap.of("weight", (Object) 1.0f));
		mock.addNode(2, "b", ImmutableMap.of("weight", (Object) 2.0f));
		MockUtils.noMoreNodes(mock);
		mock.addDirectedEdge(1, 1, ImmutableMap.of("weight", (Object) 1.0f));
		mock.addDirectedEdge(1, 2, ImmutableMap.of("weight", (Object) 2.0f));
		MockUtils.noMoreEdges(mock);
		replay(mock);

		NWBFileParserHandler filter = ParserPipe.create()
				.filterNodes(AttributePredicates.keepBelow("weight", 3.0))
				.outputTo(mock);

		byte[] toFilter = getTestNWB();
		NWBFileParser parser = new NWBFileParser(new ByteArrayInputStream(
				toFilter));
		parser.parse(filter);

		verify(mock);
	}
	
	private byte[] getTestNWB() {
		StringBuilder s = new StringBuilder();

		s.append("*Nodes 5\n");
		s.append("id*int	label*string	weight*float\n");

		s.append("1		\"a\"	1.0\n");
		s.append("2		\"b\"	2.0\n");
		s.append("3		\"c\"	3.0\n");
		s.append("4		\"d\"	4.0\n");
		s.append("5		\"e\"	5.0\n");

		s.append("*DirectedEdges 5\n");
		s.append("source*int	target*int	weight*float\n");

		s.append("1		1	1.0\n");
		s.append("1		2	2.0\n");
		s.append("1		3	3.0\n");
		s.append("1		4	4.0\n");
		s.append("1		5	5.0\n");

		return s.toString().getBytes();
	}

}
