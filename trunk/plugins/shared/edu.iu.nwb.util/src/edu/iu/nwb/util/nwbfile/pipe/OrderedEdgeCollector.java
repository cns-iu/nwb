package edu.iu.nwb.util.nwbfile.pipe;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.MinMaxPriorityQueue;
import com.google.common.collect.Ordering;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.model.Edge;

/**
 * @see ParserPipe#keepMinimumEdges(int, Ordering)
 * @author thgsmith
 * 
 */

class OrderedEdgeCollector extends ParserStage {
	private MinMaxPriorityQueue<Edge> edges;

	private LinkedHashMap<String, String> directedEdgeSchema;
	private LinkedHashMap<String, String> undirectedEdgeSchema;

	private boolean hasDirectedEdges = false;
	private boolean hasUndirectedEdges = false;

	private Ordering<? super Edge> ordering;

	private int edgeLimit;

	public OrderedEdgeCollector(NWBFileParserHandler writer,
			int edgeLimit, Ordering<? super Edge> ordering) {
		super(writer);
		this.edgeLimit = edgeLimit;
		this.ordering = ordering;
		this.edges = createQueue(edgeLimit, ordering);
	}

	@Override
	public void addDirectedEdge(int source, int target,	Map<String, Object> attributes) {
		this.hasDirectedEdges = true;
		edges.offer(new Edge(source, target, attributes, true));
	}

	@Override
	public void addUndirectedEdge(int source, int target, Map<String, Object> attributes) {
		this.hasUndirectedEdges = true;
		edges.offer(new Edge(source, target, attributes, false));
	}


	private static MinMaxPriorityQueue<Edge> createQueue(int edgeLimit, Ordering<? super Edge> ordering) {
		return MinMaxPriorityQueue.orderedBy(ordering).maximumSize(edgeLimit).create();
	}

	@Override
	public void finishedParsing() {
		if (this.hasDirectedEdges) {
			super.setDirectedEdgeSchema(this.directedEdgeSchema);
			for (Edge e : edges) {
				if (e.isDirected()) {
					super.addDirectedEdge(e.getSource(), e.getTarget(), e.getAttributes());
				}
			}
		}
		if (this.hasUndirectedEdges) {
			super.setUndirectedEdgeSchema(this.undirectedEdgeSchema);
			for (Edge e : edges) {
				if (!e.isDirected()) {
					super.addUndirectedEdge(e.getSource(), e.getTarget(), e.getAttributes());
				}
			}
		}
		super.finishedParsing();
		edges = null;
	}

	@Override
	public void setDirectedEdgeCount(int numberOfEdges) {
		// We don't know the number of edges ahead of time, so don't pass this on
	}

	@Override
	public void setUndirectedEdgeCount(int numberOfEdges) {
		// We don't know the number of edges ahead of time, so don't pass this on
	}

	@Override
	public void setDirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		this.directedEdgeSchema = schema;
	}

	@Override
	public void setUndirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		this.undirectedEdgeSchema = schema;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("limit", edgeLimit)
				.add("ordering", ordering)
				.add("next", nextToString())
				.toString();
	}
}
