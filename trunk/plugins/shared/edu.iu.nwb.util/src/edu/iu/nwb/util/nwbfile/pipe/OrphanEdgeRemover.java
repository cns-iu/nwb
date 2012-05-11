package edu.iu.nwb.util.nwbfile.pipe;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

class OrphanEdgeRemover extends ParserStage {
	private Set<Integer> nodes = Sets.newHashSet();

	@Override
	public void addNode(int id, String label, Map<String, Object> attributes) {
		nodes.add(id);
		super.addNode(id, label, attributes);
	}

	@Override
	public void setDirectedEdgeCount(int numberOfEdges) {
		// we will drop some edges, so don't pass this call through.
	}

	@Override
	public void addDirectedEdge(int sourceNode, int targetNode,
			Map<String, Object> attributes) {
		if (nodes.contains(sourceNode) && nodes.contains(targetNode)) {
			super.addDirectedEdge(sourceNode, targetNode, attributes);
		}
	}

	@Override
	public void setUndirectedEdgeCount(int numberOfEdges) {
		// we will drop some edges, so don't pass this call through.
	}

	@Override
	public void addUndirectedEdge(int sourceNode, int targetNode,
			Map<String, Object> attributes) {
		if (nodes.contains(sourceNode) && nodes.contains(targetNode)) {
			super.addUndirectedEdge(sourceNode, targetNode, attributes);
		}
	}

	// inherited toString from ParserStage is fine.
}
