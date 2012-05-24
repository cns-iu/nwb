package edu.iu.nwb.util.nwbfile.model;

import java.util.Map;

import com.google.common.base.Objects;

import edu.iu.nwb.util.nwbfile.NWBFileProperty;

public class Edge extends NWBGraphPart {
	private final int target;
	private final int source;
	private final boolean isDirected;

	public Edge(int source, int target, Map<String, ? extends Object> attributes,
			boolean isDirected) {
		super(attributes);
		this.source = source;
		this.target = target;
		this.isDirected = isDirected;
	}


	public int getSource() {
		return source;
	}

	public int getTarget() {
		return target;
	}

	public boolean isDirected() {
		return isDirected;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (! (obj instanceof Edge)) {
			return false;
		} else {
			Edge that = (Edge) obj;
			return this.target == that.target
					&& this.source == that.source
					&& this.isDirected == that.isDirected
					&& Objects.equal(this.getAttributes(), that.getAttributes());
		}
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(target, source, isDirected, getAttributes());
	}


	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("source", source)
				.add("target", target)
				.add("directed", isDirected)
				.add("attribs", getAttributes())
				.toString();
	}


	@Override
	public boolean isAttributeReserved(String attributeName) {
		return NWBFileProperty.NECESSARY_EDGE_ATTRIBUTES.containsKey(attributeName);
	}
}
