package edu.iu.nwb.util.nwbfile.pipe;

import static org.easymock.EasyMock.*;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.pipe.utils.MockUtils;

public class OrphanEdgeRemoverTest {

	private static final ImmutableMap<String, Object> EMPTY_MAP = ImmutableMap.<String,Object>of();

	/**
	 * Test that, if we add one node, and an edge from that node to a missing second node,
	 * the OrphanEdgeRemover will drop that edge.
	 */
	@Test
	public void testRemoves() {
		NWBFileParserHandler mock = createNiceMock(NWBFileParserHandler.class);
		mock.addNode(1, "one", EMPTY_MAP);
		MockUtils.noMoreEdges(mock);
		MockUtils.noMoreNodes(mock);
		replay(mock);

		// create the remover, and set it to forward graph components to the mock.
		OrphanEdgeRemover o = new OrphanEdgeRemover();
		o.setNextStage(mock);

		o.addNode(1, "one", EMPTY_MAP);
		o.addDirectedEdge(1, 2, EMPTY_MAP);
		verify(mock);
	}
}
