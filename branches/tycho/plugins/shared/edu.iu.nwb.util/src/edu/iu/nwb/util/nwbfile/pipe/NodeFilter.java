package edu.iu.nwb.util.nwbfile.pipe;

import java.util.Map;

import com.google.common.base.Objects;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.model.AttributePredicate;

class NodeFilter extends ParserStage {
	private AttributePredicate filter;

	public NodeFilter(NWBFileParserHandler writer, AttributePredicate filter) {
		super(writer);
		this.filter = filter;
	}

	@Override
	public void setNodeCount(int numberOfNodes) {
		// Don't pass along the node count, since it will change
	}

	@Override
	public void addNode(int id, String label, Map<String, Object> attributes) {
		if (shouldAddNode(attributes)) {
			super.addNode(id, label, attributes);
		}
	}

	private boolean shouldAddNode(Map<String, Object> attributes) {
		return this.filter.apply(attributes);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("filter", filter)
				.add("next", nextToString())
				.toString();
	}
}
