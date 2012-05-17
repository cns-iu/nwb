package edu.iu.nwb.util.nwbfile.pipe.tests;

import static org.easymock.EasyMock.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;

import com.google.common.collect.ImmutableMap;

import junit.framework.TestCase;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.pipe.ParserPipe;

public class KeepNNodesTest extends TestCase {
	/**
	 * Test that we can keep the N nodes with the highest weight.
	 */
	public void testWeightHigh() throws Exception {
		ByteArrayOutputStream fromFilter = new ByteArrayOutputStream();
		NWBFileParserHandler filter = ParserPipe.create()
				.keepMinimumNodes(2, ParserPipe.getNaturalOrdering("weight").reverse())
				.outputToStream(fromFilter);

		byte[] filterInput =
				("*Nodes\n" +
				"id*int	label*string	weight*float\n" +
				"1	\"a\"	1.0\n" +
				"2	\"b\"	2.0\n" +
				"3	\"c\"	3.0\n" +
				"*DirectedEdges\n" +
				"source*int	target*int\n" +
				"1	2\n" +
				"2	3\n" +
				"3	1\n").getBytes();
		
		NWBFileParser parser = new NWBFileParser(new ByteArrayInputStream(filterInput));
		parser.parse(filter);
		
		InputStream toTest = new ByteArrayInputStream(fromFilter.toByteArray());
		parser = new NWBFileParser(toTest);
		
		NWBFileParserHandler mock = createNiceMock(NWBFileParserHandler.class);
		mock.addNode(2, "b", ImmutableMap.of("weight", (Object) 2.0f));
		mock.addNode(3, "c", ImmutableMap.of("weight", (Object) 3.0f));
		MockUtils.noMoreNodes(mock);
		
		mock.addDirectedEdge(2, 3, new LinkedHashMap<String, Object>());
		MockUtils.noMoreEdges(mock);
		replay(mock);
		parser.parse(mock);
		verify(mock);
	}
}
