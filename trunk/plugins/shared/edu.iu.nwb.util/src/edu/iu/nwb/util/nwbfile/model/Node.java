package edu.iu.nwb.util.nwbfile.model;

import java.util.Map;

import com.google.common.base.Objects;

public final class Node extends NWBGraphPart {
	private final int id;
	private final String label;

	public Node(int id, String label, Map<String, ? extends Object> attributes) {
		super(attributes);
		this.id = id;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (! (obj instanceof Node)) {
			return false;
		} else {
			Node that = (Node) obj;
			return this.id == that.id
					&& this.label == that.label
					&& Objects.equal(this.getAttributes(), that.getAttributes());
		}
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(id, label, getAttributes());
	}


	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("id", id)
				.add("label", label)
				.add("attribs", getAttributes())
				.toString();
	}
}
