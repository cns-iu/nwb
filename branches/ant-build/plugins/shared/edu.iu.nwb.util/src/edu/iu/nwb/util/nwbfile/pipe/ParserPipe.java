package edu.iu.nwb.util.nwbfile.pipe;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;

import edu.iu.nwb.util.nwbfile.ForwardingNWBHandler;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;
import edu.iu.nwb.util.nwbfile.model.AttributePredicate;
import edu.iu.nwb.util.nwbfile.model.Edge;
import edu.iu.nwb.util.nwbfile.model.NWBGraphPart;
import edu.iu.nwb.util.nwbfile.model.Node;

public class ParserPipe {
	private ParserStage pipeHead = new ParserStage();
	
	/**
	 * Filters nodes based on their attributes (but not label or id).  After removing
	 * nodes that fail the {@link AttributePredicate}, any edges that now have missing
	 * end-points are also removed.
	 * 
	 * @see AttributePredicate
	 * @param filter the condition to use to decide which nodes to keep
	 */
	public ParserPipe filterNodes(AttributePredicate filter) {
		extendParserPipe(new NodeFilter(GuardParserHandler.getInstance(), filter));
		return removeOrphanEdges();
	}
	
	/**
	 * Removes any edges that refer to source or target nodes that don't exist.
	 * <p>
	 * Automatically called after {@link #filterNodes(AttributePredicate)} and
	 * {@link #keepMinimumNodes(int, Ordering)}.
	 * @see AttributePredicate
	 */
	public ParserPipe removeOrphanEdges() {
		extendParserPipe(new OrphanEdgeRemover());
		return this;
	}
	
	/**
	 * Removes any edges that don't meet a specified condition.
	 * @param filter the condition to use to decide which edges to keep.
	 */
	public ParserPipe filterEdges(AttributePredicate filter) {
		extendParserPipe(new EdgeFilter(GuardParserHandler.getInstance(), filter));
		return this;
	}
	
	/**
	 * Adds an arbitrary {@link ParserStage} to the <b>end</b> of this pipe. 
	 */
	public ParserPipe addStage(ParserStage stage) {
		extendParserPipe(stage);
		return this;
	}
	
	/**
	 * Adds a filter to the end of this ParserPipe, which uses the provided
	 * {@link Ordering} to select the {@code nodeLimit} nodes which would be sorted first (i.e.
	 * lowest). The selected nodes are then passed through to a delegate
	 * {@code NWBFileParserHandler}, such as an {@link NWBFileWriter}. The
	 * non-selected nodes are dropped, and then any edges that are now missing
	 * end-points are also dropped.
	 * <p>
	 * It bears emphasizing that, when you provide an {@code Ordering} to this
	 * class, the nodes that are sorted LOWEST by the Ordering are preserved.
	 * This is equivalent to sorting the list of nodes, then choosing the
	 * "first" or "leftmost" {@code nodeLimit} nodes.
	 * <p>
	 * If you want to get the "last", "greatest", or "rightmost" nodes, you can
	 * call {@link Ordering#reverse() reverse()} on your {@code Ordering} before
	 * you pass it in.
	 * <p>
	 * When extracting {@code nodeLimit} nodes, this algorithm should use
	 * O({@code nodeLimit}) space. 
	 * 
	 * @see #getNaturalOrdering(String)
	 * @see Ordering#reverse()
	 * @param nodeLimit
	 *            the maximum number of nodes in the output graph
	 * @param ordering
	 *            the Ordering used to select the nodes
	 * @return this ParserPipe
	 */
	public ParserPipe keepMinimumNodes(int nodeLimit, Ordering<? super Node> ordering) {
		extendParserPipe(new OrderedNodeCollector(GuardParserHandler.getInstance(), nodeLimit, ordering));
		return removeOrphanEdges();
	}
	
	/**
	 * Adds a filter to the end of this ParserPipe, which uses the provided
	 * {@link Ordering} to select the {@code edgeLimit} nodes which would be
	 * sorted first (i.e. lowest).
	 * <p>
	 * This implementation works with directed, undirected, or hybrid graphs.
	 * For a hybrid graph, the attribute that determines the weight must be
	 * present in both the weighted and unweighted edges, but their schemas may
	 * otherwise be different. The top N edges, regardless of directedness, are
	 * returned.
	 * <p>
	 * When extracting N edges, this algorithm should operate in O(N) space.
	 * 
	 * @see #getNaturalOrdering(String)
	 * @see Ordering#reverse()
	 * @param edgeLimit
	 * @param ordering
	 * @return
	 */
	public ParserPipe keepMinimumEdges(int edgeLimit, Ordering<? super Edge> ordering) {
		extendParserPipe(new OrderedEdgeCollector(GuardParserHandler.getInstance(), edgeLimit, ordering));
		return this;
	}
	
	/**
	 * Returns an {@link Ordering} on {@link Node}s or {@link Edge}s, which retrieves a particular
	 * attribute and compares the resulting values according to their natural ordering.
	 * This is useful for the {@link #keepMinimumNodes(int, Ordering)} and 
	 * {@link #keepMinimumEdges(int, Ordering)} filters.
	 * <p>
	 * Doesn't handle {@code null} (or missing attributes) gracefully.  You should probably put a
	 * {@link #requireNodeAttribute(String)} or {@code requireEdgeAttribute} filter before
	 * using an attribute with this Ordering.
	 * <p>
	 * The source, target, and directedness of an Edge are not stored in the attributes dictionary,
	 * and neither are the id and label of a Node.  To use those, you will need to create your
	 * own {@code Ordering<Node>} (or Edge) that directly accesses those properties. 
	 * 
	 * @see Ordering
	 * @param attributeName the attribute to fetch on each Node or Edge
	 * @return
	 */
	public static <G extends NWBGraphPart> Ordering<G> getNaturalOrdering(final String attributeName) {
		// OK because we expect all values of the attribute to be of the same type,
		// one of the Numbers or a String.
		@SuppressWarnings("rawtypes")
		Function<G, Comparable> getter = attributeGetter(attributeName, Comparable.class);
		return Ordering.natural().onResultOf(getter);
	}

	/**
	 * Discards nodes that do not have a value for the given attribute.  In the input NWB file,
	 * this means they have a "*" instead of another value.
	 * 
	 * @param attribute the attribute to require
	 * @return
	 */
	public ParserPipe requireNodeAttribute(final String attribute) {
		return filterNodes(new AttributePredicate() {
			public boolean apply(Map<String, Object> input) {
				return (input.get(attribute) != null);
			}
			@Override public String toString() {
				return String.format("Node.%s not null", attribute);
			}
		});
	}
	
	/**
	 * Discards edges that do not have a value for the given attribute.
	 * 
	 * @param attribute the attribute to require
	 * @return
	 */
	public ParserPipe requireEdgeAttribute(final String attribute) {
		return filterEdges(new AttributePredicate() {
			public boolean apply(Map<String, Object> input) {
				return (input.get(attribute) != null);
			}
			@Override public String toString() {
				return String.format("Edge.%s not null", attribute);
			}
		});
	}
	
	/**
	 * Removes a particular attribute from all the Nodes.
	 * 
	 * @param attribute
	 * @return
	 */
	public ParserPipe removeNodeAttribute(final String attribute) {
		extendParserPipe(new NodeAttributeRemover(GuardParserHandler.getInstance(), attribute));
		return this;
	}
	
	/**
	 * Adds an attribute to the Node schema, but doesn't set it any of the nodes.
	 * <p>
	 * The {@code type} parameter must be one of the {@code NWBFileProperty.TYPE_*}
	 * constants.
	 * 
	 * @see NWBFileProperty#TYPE_FLOAT
	 * @see NWBFileProperty#TYPE_INT
	 * @see NWBFileProperty#TYPE_STRING
	 * @param name the attribute name to add
	 * @param type the attribute type
	 * @return
	 */
	public ParserPipe addNodeAttribute(final String name, final String type) {
		extendParserPipe(new NodeAttributeAdder(GuardParserHandler.getInstance(), name, type));
		return this;
	}
	
	/**
	 * Adds an attribute to the Node schema.  Then, sets the value of the attribute
	 * to a default value, unless it's already set somehow.  For instance, you could
	 * use {@link #injectNode(String, Map)} to insert a Node that already contains the
	 * attribute, and then set all the other Nodes to a different default value. 
	 * <p>
	 * The {@code type} parameter must be one of the {@code NWBFileProperty.TYPE_*}
	 * constants.
	 * 
	 * @see NWBFileProperty#TYPE_FLOAT
	 * @see NWBFileProperty#TYPE_INT
	 * @see NWBFileProperty#TYPE_STRING
	 * @param name the attribute name to add
	 * @param type the attribute type
	 * @param defaultValue the value to use on Nodes that don't have the attribute yet
	 * @return
	 */
	public ParserPipe addNodeAttribute(final String name, final String type, Object defaultValue) {
		extendParserPipe(new NodeAttributeDefaulter(GuardParserHandler.getInstance(), name, type, defaultValue));
		return this;
	}
	
	/**
	 * Adds an attribute to the Node schema.  Then, for each node, computes the value
	 * of the attribute using a routine that you supply.
	 * <p>
	 * The {@code type} parameter must be one of the {@code NWBFileProperty.TYPE_*}
	 * constants.
	 * 
	 * @see FieldMakerFunction
	 * @see NWBFileProperty#TYPE_FLOAT
	 * @see NWBFileProperty#TYPE_INT
	 * @see NWBFileProperty#TYPE_STRING
	 * @param name
	 * @param type
	 * @param computer
	 * @return
	 */
	public ParserPipe addComputedNodeAttribute(final String name, final String type, FieldMakerFunction computer) {
		extendParserPipe(new NodeAttributeComputer(GuardParserHandler.getInstance(), name, type, computer));
		return this;
	}
	
	/**
	 * Adds a node to the list of nodes in the file, <b>after</b> all the other nodes have been
	 * included.  The id of the node is automatically computed during the file parsing.
	 * <p>
	 * If you need to add a Node and get its ID, you may want to write your own ParserStage,
	 * or use a Prefuse Graph object instead.  Prefuse Graphs have an in-memory graph model,
	 * which makes them far more flexible, but also much less efficient.
	 * 
	 * @param label the new Node's label
	 * @param attributes the new Node's attributes
	 * @return
	 */
	public ParserPipe injectNode(final String label, final Map<String, ? extends Object> attributes) {
		extendParserPipe(NodeInjector.create(label, attributes));
		return this;
	}
	
	/**
	 * Changes the name of a Node attribute.  Renaming something to a name that already exists
	 * causes undefined behavior; use {@link #removeNodeAttribute(String)} first in that case.
	 * 
	 * @param oldName
	 * @param newName
	 * @return
	 */
	public ParserPipe renameNodeAttribute(final String oldName, final String newName) {
		extendParserPipe(NodeAttributeRenamer.create(GuardParserHandler.getInstance(), ImmutableMap.of(oldName, newName)));
		return this;
	}
	
	/**
	 * Sends output from the pipe to an {@link NWBFileParserHandler}, such as an
	 * {@link NWBFileWriter} (but see {@link #outputToFile(File)}).
	 * <p>
	 * Returns a {@code ParserStage} that refers to the head of this pipe.  This
	 * is suitable for using in {@link NWBFileParser#parse(NWBFileParserHandler)}.
	 * 
	 * @param end the sink for the data going down the pipe
	 * @return the input end of the pipe
	 */
	public ParserStage outputTo(final NWBFileParserHandler end) {
		// put in an immutable step -- so that later, extendParserPipe knows it's not changeable
		// want a better mechanism for this.  Maybe the head of the pipe should have a pointer
		// to the "extension point", the last ParserStageAdaptor.
		extendParserPipe(ForwardingNWBHandler.create(end));
		return pipeHead;
	}
	
	/**
	 * Sends output from this pipe to an NWB file.  Returns a {@code ParserStage} 
	 * that refers to the head of this pipe.  This
	 * is suitable for using in {@link NWBFileParser#parse(NWBFileParserHandler)}.
	 * 
	 * @see NWBFileUtilities#createTemporaryNWBFile()
	 * @param outFile the file to write
	 * @return the input end of the pipe
	 * @throws IOException if there's a problem opening the file
	 */
	public ParserStage outputToFile(final File outFile) throws IOException {
		extendParserPipe(ForwardingNWBHandler.create(new NWBFileWriter(outFile)));
		return pipeHead;
	}
	
	public static ParserPipe create() {
		return new ParserPipe();
	}
	private ParserPipe() {
		// nothing right now
	}
	
	private static <G extends NWBGraphPart, T> Function<G, T> attributeGetter(final String attribute, final Class<T> clazz) {
		return new Function<G, T>() {
			public T apply(G input) {
				return clazz.cast(input.getAttribute(attribute));
			}
		};
	}

	private void extendParserPipe(NWBFileParserHandler tail) {
		ParserStage current = pipeHead;
		while (current.hasValidDelegate()) {
			if (current.delegate instanceof ParserStage) {
				current = (ParserStage) current.delegate;
			} else {
				throw new IllegalStateException("Tried to extend a parsing pipeline with a closed end");
			}
		}
		// Set the new tail of the whole pipe
		current.setNextStage(tail);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("pipe", pipeHead)
				.toString();
	}
}
