package edu.iu.nwb.util.nwbfile.pipe;

import static org.easymock.EasyMock.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import junit.framework.TestCase;

import com.google.common.collect.ImmutableMap;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.nwb.util.nwbfile.pipe.ParserPipe;
import edu.iu.nwb.util.nwbfile.pipe.utils.MockUtils;

public class OrderedEdgeCollectorTest extends TestCase {
	private static final String WEIGHT_ATTR = "weight";

	/**
	 * Test that we can keep the N edges with the highest weight.
	 */
	public void testWeightHigh() throws Exception {
		NWBFileParserHandler mock = createNiceMock(NWBFileParserHandler.class);
		mock.addDirectedEdge(1, 5, ImmutableMap.of("weight", (Object) 5.0f));
		mock.addDirectedEdge(1, 4, ImmutableMap.of("weight", (Object) 4.0f));
		mock.addDirectedEdge(1, 3, ImmutableMap.of("weight", (Object) 3.0f));
		MockUtils.noMoreEdges(mock);

		replay(mock);

		NWBFileParserHandler filter = new OrderedEdgeCollector(mock, 3,
				ParserPipe.getNaturalOrdering(WEIGHT_ATTR).reverse());
		
		NWBFileParser p = new NWBFileParser(new ByteArrayInputStream(
				getTestNWB()));
		p.parse(filter);
		verify(mock);
	}

	/**
	 * Test that we can keep the N edges with the lowest weight.
	 */
	public void testWeightLow() throws Exception {
		NWBFileParserHandler mock = createNiceMock(NWBFileParserHandler.class);
		mock.addDirectedEdge(1, 3, ImmutableMap.of("weight", (Object) 3.0f));
		mock.addDirectedEdge(1, 2, ImmutableMap.of("weight", (Object) 2.0f));
		mock.addDirectedEdge(1, 1, ImmutableMap.of("weight", (Object) 1.0f));
		MockUtils.noMoreEdges(mock);

		replay(mock);
		NWBFileParserHandler filter = new OrderedEdgeCollector(mock, 3,
				ParserPipe.getNaturalOrdering(WEIGHT_ATTR));
		
		NWBFileParser p = new NWBFileParser(new ByteArrayInputStream(
				getTestNWB()));
		p.parse(filter);
		verify(mock);
	}

	private static byte[] getTestNWB() {
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
	 * Test that we can keep the N edges with the highest weight, in a hybrid
	 * graph (with both directed and undirected edges).
	 */
	public void testHybridGraph() throws IOException, ParsingException {
		StringBuilder s = new StringBuilder();
		s.append("*Nodes\n");
		s.append("id*int	label*string\n");
		s.append("1	\"a\"\n");
		s.append("2 \"b\"\n");
		s.append("*DirectedEdges 1\n");
		s.append("source*int	target*int	weight*float\n");
		s.append("1	1	1.0\n");
		s.append("*UndirectedEdges 1\n");
		s.append("source*int	target*int	weight*float	anotherparam*int\n");
		s.append("1	2	1.0	4\n");
		byte[] inBytes = s.toString().getBytes();

		NWBFileParserHandler mock = createNiceMock(NWBFileParserHandler.class);
		mock.addDirectedEdge(1, 1, ImmutableMap.of("weight", (Object) 1.0f));
		mock.addUndirectedEdge(1, 2,
				ImmutableMap.of("weight", (Object) 1.0f, "anotherparam", 4));
		MockUtils.noMoreEdges(mock);

		replay(mock);
		NWBFileParserHandler filter = new OrderedEdgeCollector(mock, 3,
				ParserPipe.getNaturalOrdering(WEIGHT_ATTR).reverse());
		
		NWBFileParser p = new NWBFileParser(new ByteArrayInputStream(inBytes));
		p.parse(filter);
		verify(mock);
	}

}
