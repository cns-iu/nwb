package edu.iu.nwb.util.nwbfile.pipe;

import static org.easymock.EasyMock.*;

import java.io.ByteArrayInputStream;
import java.util.LinkedHashMap;

import junit.framework.TestCase;

import com.google.common.collect.ImmutableMap;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.pipe.utils.MockUtils;

public class OrderedNodeCollectorTest extends TestCase {
	/**
	 * Test that we can keep the N nodes with the highest weight.
	 */
	public void testWeightHigh() throws Exception {

		byte[] filterInput =
				("*Nodes\n" +
				"id*int	label*string	weight*float\n" +
				"1	\"a\"	1.0\n" +
				"2	\"b\"	2.0\n" +
				"3	\"c\"	3.0\n" +
				"*DirectedEdges\n" +
				"source*int	target*int\n" +
				"2	3\n").getBytes();
		
		NWBFileParserHandler mock = createNiceMock(NWBFileParserHandler.class);
		mock.addNode(2, "b", ImmutableMap.of("weight", (Object) 2.0f));
		mock.addNode(3, "c", ImmutableMap.of("weight", (Object) 3.0f));
		MockUtils.noMoreNodes(mock);
		
		mock.addDirectedEdge(2, 3, new LinkedHashMap<String, Object>());
		MockUtils.noMoreEdges(mock);
		replay(mock);
		NWBFileParserHandler filter = new OrderedNodeCollector(mock, 2, ParserPipe.getNaturalOrdering("weight").reverse());
		NWBFileParser parser = new NWBFileParser(new ByteArrayInputStream(filterInput));
		parser.parse(filter);
		verify(mock);
	}
}
