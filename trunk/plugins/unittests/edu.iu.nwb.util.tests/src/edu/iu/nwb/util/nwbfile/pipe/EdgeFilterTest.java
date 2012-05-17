package edu.iu.nwb.util.nwbfile.pipe;

import static org.easymock.EasyMock.*;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import com.google.common.collect.ImmutableMap;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.model.AttributePredicates;
import edu.iu.nwb.util.nwbfile.pipe.utils.MockUtils;

public class EdgeFilterTest extends TestCase {

	/**
	 * Test keeping edges with weight above a threshold.
	 */
	public void testAbove() throws Exception {
		// Make a mock that has the edges we expect.
		NWBFileParserHandler mock = createNiceMock(NWBFileParserHandler.class);
		mock.addDirectedEdge(1, 4, ImmutableMap.of("weight", (Object) 4.0f));
		mock.addDirectedEdge(1, 5, ImmutableMap.of("weight", (Object) 5.0f));
		MockUtils.noMoreEdges(mock);
		replay(mock);

		// make a filter that will pipe to the mock
		NWBFileParserHandler filter = new EdgeFilter(mock, AttributePredicates.keepAbove("weight", 3.0)); 
		
		// parse an nwb file and verify that we got the right edges.
		byte[] toFilter = getTestNWB();
		NWBFileParser parser = new NWBFileParser(new ByteArrayInputStream(
				toFilter));
		parser.parse(filter);
		verify(mock);

	}

	/**
	 * Test keeping edges with weight below a threshold.
	 */
	public void testBelow() throws Exception {
		NWBFileParserHandler mock = createMock(NWBFileParserHandler.class);
		resetToNice(mock);

		mock.addDirectedEdge(1, 1, ImmutableMap.of("weight", (Object) 1.0f));
		mock.addDirectedEdge(1, 2, ImmutableMap.of("weight", (Object) 2.0f));
		MockUtils.noMoreEdges(mock);
		replay(mock);

		NWBFileParserHandler filter = new EdgeFilter(mock, AttributePredicates.keepBelow("weight", 3.0));

		byte[] toFilter = getTestNWB();
		NWBFileParser parser = new NWBFileParser(new ByteArrayInputStream(
				toFilter));
		parser.parse(filter);
		verify(mock);
	}

	private byte[] getTestNWB() {
		StringBuilder s = new StringBuilder();

		s.append("*Nodes 5\n");
		s.append("id*int	label*string\n");

		s.append("1		\"a\"\n");
		s.append("2		\"b\"\n");
		s.append("3		\"c\"\n");
		s.append("4		\"d\"\n");
		s.append("5		\"e\"\n");

		s.append("*DirectedEdges 5\n");
		s.append("source*int	target*int	weight*float\n");

		s.append("1		1	1.0\n");
		s.append("1		2	2.0\n");
		s.append("1		3	3.0\n");
		s.append("1		4	4.0\n");
		s.append("1		5	5.0\n");

		return s.toString().getBytes();
	}

	/**
	 * Test keeping edges with weight above a threshold, in a hybrid graph (with both directed
	 * and undirected edges).
	 */
	public void testHybrid() throws Exception {
		StringBuilder s = new StringBuilder();
		s.append("*Nodes\n");
		s.append("id*int	label*string\n");
		s.append("1	\"a\"\n");
		s.append("2 \"b\"\n");
		s.append("*DirectedEdges 1\n");
		s.append("source*int	target*int	weight*float\n");
		s.append("1	1	5.0\n");
		s.append("*UndirectedEdges 1\n");
		s.append("source*int	target*int	weight*float	anotherparam*int\n");
		s.append("1	2	6.0	4\n");

		NWBFileParserHandler mock = createNiceMock(NWBFileParserHandler.class);
		mock.addUndirectedEdge(1, 2,
				ImmutableMap.of("anotherparam", 4, "weight", (Object) 6.0f));
		MockUtils.noMoreEdges(mock);
		replay(mock);

		NWBFileParserHandler filter = new EdgeFilter(mock, AttributePredicates.keepAbove("weight", 5.5));
		
		byte[] toFilter = s.toString().getBytes();
		
		NWBFileParser parser = new NWBFileParser(new ByteArrayInputStream(
				toFilter));
		parser.parse(filter);
		verify(mock);
	}
}
