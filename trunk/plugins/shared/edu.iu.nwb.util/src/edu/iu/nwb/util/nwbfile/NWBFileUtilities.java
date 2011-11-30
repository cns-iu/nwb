package edu.iu.nwb.util.nwbfile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.cishell.utilities.FileUtilities;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class NWBFileUtilities {
	public static final Collection<String> DEFAULT_NUMBER_KEY_TYPES =
		Collections.unmodifiableList(Arrays.asList(
			NWBFileProperty.TYPE_INT, NWBFileProperty.TYPE_FLOAT));
	public static final Collection<String> DEFAULT_NUMBER_KEYS_TO_SKIP =
		Collections.unmodifiableList(Arrays.asList(
			NWBFileProperty.ATTRIBUTE_SOURCE,
			NWBFileProperty.ATTRIBUTE_TARGET));
	
	/*
	 * This creates an NWBFileParser with nwbFile and passes in handler when
	 * parsing.  handler is thus side-effected.
	 */
	public static void parseNWBFileWithHandler(File nwbFile, NWBFileParserHandler handler)
			throws ParsingException {
		NWBFileParser nwbParser;
    	
    	try {
    		nwbParser = new NWBFileParser(nwbFile);
    		nwbParser.parse(handler);
    	}
    	catch (IOException e) {
    		throw new ParsingException(e);
    	}
	}
	
	/*
	 * This parses out the NWB metadata from the passed-in NWB file and
	 * returns it.
	 */
	// TODO: Create NWBMetadata class?
	public static GetMetadataAndCounts parseMetadata(File nwbFile)
			throws NWBMetadataParsingException {
		GetMetadataAndCounts nwbFileMetaDataGetter = new GetMetadataAndCounts();
    	
    	try {
    		parseNWBFileWithHandler(nwbFile, nwbFileMetaDataGetter);
    	}
    	catch (ParsingException parsingException) {
    		throw new NWBMetadataParsingException(parsingException);
    	}
    	
    	return nwbFileMetaDataGetter;
	}

	/**
	 * If a graph has only directed or undirected edges, this will return the
	 * schema for that set of edges. This is a LinkedHashMap, with its elements
	 * in order of the columns of data.
	 * 
	 * <p>
	 * If the graph is hybrid, with both directed and undirected edges, it's not
	 * clear what to do because this method is expected to return exactly one
	 * schema. So an IllegalArgumentException is thrown.
	 * 
	 */
	public static LinkedHashMap<String, String> getEdgeSchemaFromMetadata(
			GetNWBFileMetadata nwbFileMetaDataGetter) {
		LinkedHashMap<String, String> directedEdgeSchema = nwbFileMetaDataGetter
				.getDirectedEdgeSchema();
		LinkedHashMap<String, String> undirectedEdgeSchema = nwbFileMetaDataGetter
				.getUndirectedEdgeSchema();

		// Handle missing schemas
		if (directedEdgeSchema == null) {
			if (undirectedEdgeSchema == null) {
				return new LinkedHashMap<String, String>();
			}
			return undirectedEdgeSchema;
		} else if (undirectedEdgeSchema == null) {
			return directedEdgeSchema;
		}

		/*
		 * BOTH are non-null! It's not clear what to do, still need to examine
		 * the callers to determine whether they can handle having two kinds of
		 * schemas smooshed together.
		 * 
		 * Try using getConsistentEdgeAttributes below?
		 */

		throw new IllegalArgumentException(
				"Hybrid un/directed graph, don't know what to do");

	}

	/**
	 * Removes the required attributes of edges
	 * 
	 * @see NWBFileProperty.NECESSARY_EDGE_ATTRIBUTES
	 */
	public static Map<String, String> removeRequiredEdgeProps(
			Map<String, String> properties) {
		return Maps.difference(properties,
				NWBFileProperty.NECESSARY_EDGE_ATTRIBUTES).entriesOnlyOnLeft();
	}
	
	/**
	 * Removes the required attributes of edges
	 * 
	 * @see NWBFileProperty.NECESSARY_EDGE_ATTRIBUTES
	 */
	public static Map<String, String> removeRequiredNodeProps(
			Map<String, String> properties) {
		return Maps.difference(properties,
				NWBFileProperty.NECESSARY_NODE_ATTRIBUTES).entriesOnlyOnLeft();
	}
	
	

	/**
	 * Finds all the edge attributes that are present in all edges. That is, if
	 * there are both directed and undirected edges, this method returns only
	 * the attributes that have the same names and data types in the directed
	 * and undirected portions.
	 * 
	 * <p>
	 * The Map of attributes that's returned is not in any particular order, in
	 * contrast to {@link NWBFileUtilities.getEdgeSchemaFromMetadata}.
	 * 
	 * @param handler
	 * @return
	 */

	// common hybrid consistent candidate
	public static Map<String, String> getConsistentEdgeAttributes(
			GetNWBFileMetadata handler) {
		Map<String, String> directedEdgeSchema, undirectedEdgeSchema;
		directedEdgeSchema = handler.getDirectedEdgeSchema();
		undirectedEdgeSchema = handler.getUndirectedEdgeSchema();

		// Handle missing schemas
		if (directedEdgeSchema == null) {
			if (undirectedEdgeSchema == null) {
				return new TreeMap<String, String>();
			}
			return undirectedEdgeSchema;
		} else if (undirectedEdgeSchema == null) {
			return directedEdgeSchema;
		}

		TreeSet<String> columnsInBoth = new TreeSet<String>(Sets.intersection(
				directedEdgeSchema.keySet(), undirectedEdgeSchema.keySet()));
		// Q: What if the same column has a different data type in the
		// directed and undirected parts of the graph?
		// A: That would suck. So let's prevent it.
		TreeMap<String, String> schemaOut = Maps.newTreeMap();
		for (String column : columnsInBoth) {
			if (directedEdgeSchema.get(column).equals(
					undirectedEdgeSchema.get(column))) {
				schemaOut.put(column, directedEdgeSchema.get(column));
			}
		}
		return schemaOut;
	}

	/**
	 * Given a Map of column names to data types (as returned by
	 * {@link NWBFileUtilities.getAllEdgeAttributes} or
	 * {@link NWBFileUtilities.getEdgeSchemaFromMetadata}, find the column names
	 * that are numeric.
	 * 
	 * <p>
	 * This could include the standard "source", "target", and "id" columns,
	 * you'd need to remove those later.
	 * 
	 * @see NWBFileProperty.NUMERIC_ATTRIBUTE_TYPES
	 * @see NWBFileProperty.NECESSARY_NODE_ATTRIBUTES
	 * @see NWBFileProperty.NECESSARY_EDGE_ATTRIBUTES
	 */
	public static List<String> findNumericAttributes(Map<String, String> schema) {
		List<String> numerics = new ArrayList<String>();
		if (schema == null) {
			return numerics;
		}

		ImmutableSet<String> numericTypes = ImmutableSet
				.copyOf(NWBFileProperty.NUMERIC_ATTRIBUTE_TYPES);
		for (Map.Entry<String, String> column : schema.entrySet()) {
			if (numericTypes.contains(column.getValue())) {
				numerics.add(column.getKey());
			}
		}

		return numerics;
	}

	public static LinkedHashMap<String, String> getEdgeMetadata(File nwbFile)
			throws NWBMetadataParsingException {
		GetNWBFileMetadata nwbFileMetaDataGetter = parseMetadata(nwbFile);

		return getEdgeSchemaFromMetadata(nwbFileMetaDataGetter);
	}
	
	public static LinkedHashMap<String, String> getNodeSchema(File nwbFile)
			throws NWBMetadataParsingException {
		GetNWBFileMetadata nwbFileMetaDataGetter = parseMetadata(nwbFile);
		
		return nwbFileMetaDataGetter.getNodeSchema();
	}
	
	public static File createTemporaryNWBFile() throws IOException {
		File temporaryNWBFile =
			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory
				("NWB-", "nwb");
		
		return temporaryNWBFile;
	}
}