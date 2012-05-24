package edu.iu.nwb.util.nwbfile.pipe.utils;

import static org.easymock.EasyMock.*;

import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.IAnswer;

import com.google.common.base.Joiner;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;

public class MockUtils {
	/**
	 * Assert that no more directed or undirected edges will be added to an
	 * NWBFileParserHandler mock. If any more edges are added, by calling
	 * {@link NWBFileParserHandler#addDirectedEdge(int, int, Map)} or
	 * {@link NWBFileParserHandler#addUndirectedEdge(int, int, Map)} on the mock
	 * object, an AssertionError will be thrown at the time of that method call.
	 * 
	 * @param mock
	 *            a mock object created using EasyMock
	 */
	public static void noMoreEdges(NWBFileParserHandler mock) {
		mock.addUndirectedEdge(anyInt(), anyInt(), EasyMock.<Map<String,Object>>anyObject());
		expectLastCall().andStubAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				throw new AssertionError("Got extra undirected edge: <" + Joiner.on("; ").skipNulls().join(getCurrentArguments()) + ">");
			}
		});
		mock.addDirectedEdge(anyInt(), anyInt(), EasyMock.<Map<String,Object>>anyObject());
		expectLastCall().andStubAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				throw new AssertionError("Got extra directed edge: <" + Joiner.on("; ").skipNulls().join(getCurrentArguments()) + ">");
			}
		});
	}

	/**
	 * Assert that no more nodes will be added to an
	 * NWBFileParserHandler mock. If any more nodes are added, by calling
	 * {@link NWBFileParserHandler#addNode(int, String, Map)} on the mock
	 * object, an AssertionError will be thrown at the time of that method call.
	 * 
	 * @param mock
	 *            a mock object created using EasyMock
	 */
	public static void noMoreNodes(NWBFileParserHandler mock) {
		mock.addNode(anyInt(), isA(String.class), EasyMock.<Map<String,Object>>anyObject());
		expectLastCall().andStubAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				throw new AssertionError("Got extra node: <" + Joiner.on("; ").skipNulls().join(getCurrentArguments()) + ">");
			}
		});
	}

}
