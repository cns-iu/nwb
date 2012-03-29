package edu.iu.nwb.util.nwbfile.pipe;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.MinMaxPriorityQueue;
import com.google.common.collect.Ordering;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.model.Node;

/**
 * Normally, this class is instantiated with {@link ParserPipe#keepMinimumNodes(int, Ordering)}.
 * The documentation there will be useful to you.
 * 
 * @author thgsmith
 * 
 */
class OrderedNodeCollector extends ParserStage {
	private MinMaxPriorityQueue<Node> nodes;

	private boolean nodesWritten;

	private Ordering<? super Node> ordering;

	private int nodeLimit;

	public OrderedNodeCollector(NWBFileParserHandler writer,
			int nodeLimit, Ordering<? super Node> ordering) {
		super(writer);
		this.nodeLimit = nodeLimit;
		this.ordering = ordering;
		createQueue(nodeLimit, ordering);
	}

	private void createQueue(int edgeLimit, Ordering<? super Node> ordering) {
		nodes = MinMaxPriorityQueue.orderedBy(ordering)
				.maximumSize(edgeLimit).create();
	}


	/*
	 * Only keeps a node around if it's among the "first" N nodes.
	 */
	@Override
	public void addNode(int id, String label, Map<String, Object> attributes) {
		nodes.offer(new Node(id, label, attributes));
	}

	/*
	 * When we get the schemas for the edges, that means that we're done with
	 * the nodes. So then we write out the nodes to the file, and make a list of
	 * node IDs that have been kept.
	 */

	@Override
	public void setDirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		doneWithNodes();
		super.setDirectedEdgeSchema(schema);
	}

	@Override
	public void setUndirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		doneWithNodes();
		super.setUndirectedEdgeSchema(schema);
	}

	private void doneWithNodes() {
		this.writeNodesOnce();
	}

	private void writeNodesOnce() {
		if (!this.nodesWritten) {
			for (Node n : nodes) {
				super.addNode(n.getId(), n.getLabel(), n.getAttributes());
			}
			this.nodesWritten = true;
		}
	}
	
	@Override
	public void finishedParsing() {
		doneWithNodes();
		super.finishedParsing();
		nodes = null;
	}

	@Override
	public void setNodeCount(int numberOfNodes) {
		super.setNodeCount(Math.min(numberOfNodes, nodeLimit));
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("limit", nodeLimit)
				.add("ordering", ordering)
				.add("next", nextToString())
				.toString();
	}
}
