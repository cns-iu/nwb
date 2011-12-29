package edu.iu.nwb.util.nwbfile.model;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class NWBGraphPart {
	private final ImmutableMap<String, Object> attributes;

	protected NWBGraphPart(Map<String, ? extends Object> attributes) {
		this.attributes = ImmutableMap.copyOf(attributes);
	}

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public ImmutableMap<String, Object> getAttributes() {
		return attributes;
	}

	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}
}