package edu.iu.nwb.util.nwbfile.pipe;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;

class NodeInjector extends ParserStage {

	private final ImmutableList<AnonymousNode> nodesToAdd;
	private boolean haveInjectedNodes = false;
	private int maxNodeId = 0;
	
	static NodeInjector create(String label, Map<String, ? extends Object> attributes) {
		return new NodeInjector(GuardParserHandler.getInstance(),
				ImmutableList.of(new AnonymousNode(label, attributes)));
	}

	NodeInjector(NWBFileParserHandler handler,
			List<AnonymousNode> nodes) {
		super(handler);
		this.nodesToAdd = ImmutableList.copyOf(nodes);
	}

	@Override
	public void setNodeCount(int numberOfNodes) {
		super.setNodeCount(numberOfNodes + nodesToAdd.size());
	}
	
	

	@Override
	public void finishedParsing() {
		this.inputNodesHaveEnded();
		
		super.finishedParsing();
	}
	@Override
	public void setDirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		this.inputNodesHaveEnded();
		super.setDirectedEdgeSchema(schema);
	}
	@Override
	public void setUndirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		this.inputNodesHaveEnded();
		super.setUndirectedEdgeSchema(schema);
	}

	private void inputNodesHaveEnded() {
		if (! haveInjectedNodes) {
			doNodeInjection();
			haveInjectedNodes = true;
		}
	}

	private void doNodeInjection() {
		for (AnonymousNode node : nodesToAdd) {
			super.addNode(++this.maxNodeId, node.getLabel(), node.getAttributes());
		}
	}


	@Override
	public void addNode(int id, String label, Map<String, Object> attributes) {
		super.addNode(id, label, attributes);
		if (id > this.maxNodeId) {
			this.maxNodeId = id;
		}
	}


	public static class Builder {
		private List<AnonymousNode> nodes = Lists.newArrayList();
		private NWBFileParserHandler handler;

		public Builder(NWBFileParserHandler handler) {
			this.handler = handler;
		}

		public Builder withNode(String label) {
			nodes.add(new AnonymousNode(label, ImmutableMap.<String, Object>of()));
			return this;
		}

		public Builder withNode(String label, String k1, Object v1) {
			nodes.add(new AnonymousNode(label, ImmutableMap.of(k1, v1)));
			return this;
		}

		public Builder withNode(String label, String k1, Object v1, String k2, Object v2) {
			nodes.add(new AnonymousNode(label, ImmutableMap.of(k1, v1, k2, v2)));
			return this;
		}

		public Builder withNode(String label, String k1, Object v1, String k2, Object v2,
				String k3, Object v3) {
			nodes.add(new AnonymousNode(label, ImmutableMap.of(k1, v1, k2, v2, k3, v3)));
			return this;
		}

		public Builder withNode(String label, String k1, Object v1, String k2, Object v2,
				String k3, Object v3, String k4, Object v4) {
			nodes.add(new AnonymousNode(label, ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4)));
			return this;
		}

		public Builder withNode(String label, String k1, Object v1, String k2, Object v2,
				String k3, Object v3, String k4, Object v4, String k5, Object v5) {
			nodes.add(new AnonymousNode(label, ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5)));
			return this;
		}

		public Builder withNode(String label, Map<String, ? extends Object> attributes) {
			nodes.add(new AnonymousNode(label, ImmutableMap.copyOf(attributes)));
			return this;
		}
		
		public ParserStage build() {
			return new NodeInjector(handler, nodes);
		}
	}
	
	/** AnonymousNode is just a Node without an ID. */
	private static class AnonymousNode {
		private final String label;
		private final ImmutableMap<String, Object> attributes;
		
		public AnonymousNode(String label, Map<String, ? extends Object> attributes) {
			this.label = label;
			this.attributes = ImmutableMap.copyOf(attributes);
		}

		public String getLabel() {
			return label;
		}

		public ImmutableMap<String, Object> getAttributes() {
			return attributes;
		}
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("to inject", nodesToAdd.size())
				.add("have injected", haveInjectedNodes)
				.add("next", nextToString())
				.toString();
	}
}
