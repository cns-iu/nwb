package edu.iu.nwb.util.nwbfile.pipe.tests;

import static org.easymock.EasyMock.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.ImmutableMap;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;

public class MockUtilsTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private static final Object something = new Object();
	
	/**
	 * Test that we can assert that no more (directed) edges will be added.
	 */
	@Test
	public void testNoMoreDirectedEdges() {
		NWBFileParserHandler mock = createNiceMock(NWBFileParserHandler.class);
		mock.addDirectedEdge(1, 2, ImmutableMap.of("hi", something));
		MockUtils.noMoreEdges(mock);
		
		replay(mock);
		// adding the first edge should succeed.
		mock.addDirectedEdge(1, 2, ImmutableMap.of("hi", something));

		exception.expect(AssertionError.class);
		mock.addDirectedEdge(3, 4, ImmutableMap.of("hi", something));
		verify(mock);
	}

	/**
	 * Test that we can assert that no more (undirected) edges will be added.
	 */
	@Test
	public void testNoMoreUndirectedEdges() {
		NWBFileParserHandler mock = createNiceMock(NWBFileParserHandler.class);
		mock.addUndirectedEdge(1, 2, ImmutableMap.of("hi", something));
		MockUtils.noMoreEdges(mock);

		replay(mock);
		// adding the first edge should succeed.
		mock.addUndirectedEdge(1, 2, ImmutableMap.of("hi", something));
		
		exception.expect(AssertionError.class);
		mock.addUndirectedEdge(1, 2, ImmutableMap.of("hi", something));
		verify(mock);
	}

	/**
	 * Test that we can assert that no more nodes will be added.
	 */
	@Test
	public void testNoMoreNodes() {
		NWBFileParserHandler mock = createNiceMock(NWBFileParserHandler.class);
		mock.addNode(1, "hi", ImmutableMap.of("hi", something));
		MockUtils.noMoreNodes(mock);
		
		replay(mock);
		// adding the first node should succeed.
		mock.addNode(1, "hi", ImmutableMap.of("hi", something));
		
		exception.expect(AssertionError.class);
		mock.addNode(2, "bye", ImmutableMap.of("hi", something));
		verify(mock);
	}

}
