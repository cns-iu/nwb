package edu.iu.nwb.util.nwbfile.pipe;

import java.util.Map;

import com.google.common.base.Objects;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.model.AttributePredicate;
import edu.iu.nwb.util.nwbfile.model.AttributePredicates;

/**
 * An {@link NWBFileParserHandler} that removes some edges, then passes through to another ParserHandler,
 * 
 * @author thgsmith
 * 
 */

class EdgeFilter extends ParserStage {
	private AttributePredicate filter;

	/**
	 * Try {@link AttributePredicates#keepAbove(String, double)} and friends for this Predicate.
	 * <p>
	 * If the {@code AttributePredicate} returns true, the edge will be kept.
	 */
	public EdgeFilter(NWBFileParserHandler writer, AttributePredicate filter) {
		super(writer);
		this.filter = filter;
	}
	
	@Override
	public void addDirectedEdge(int sourceNode, int targetNode,
			Map<String, Object> attributes) {
		if (shouldAddEdge(attributes)) {
			super.addDirectedEdge(sourceNode, targetNode, attributes);
		}
	}

	@Override
	public void addUndirectedEdge(int node1, int node2,
			Map<String, Object> attributes) {
		if (shouldAddEdge(attributes)) {
			super.addUndirectedEdge(node1, node2, attributes);
		}
	}

	private boolean shouldAddEdge(Map<String, Object> attributes) {
		return filter.apply(attributes);
	}

	@Override
	public void setDirectedEdgeCount(int numberOfEdges) {
		// do nothing: the count will change because of filtering
		// This leaves the count out of the output file (which is OK)
	}
	
	@Override
	public void setUndirectedEdgeCount(int numberOfEdges) {
		// do nothing: the count will change because of filtering
		// This leaves the count out of the output file (which is OK)
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("filter", filter)
				.add("next", nextToString())
				.toString();
	}
}
