package edu.iu.nwb.preprocessing.removeedgeattributes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

import edu.iu.nwb.util.nwbfile.GetNWBFileMetadata;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class NWBEdgeAttributeReader {
	// TODO: Should this be in edu.iu.nwb.util.nwbfile.NWBFileProperty?
	// TODO: Maybe put it in NWBFileUtiliteis in above package once Patrick
	// commits?
	public static Map NECESSARY_EDGE_ATTRIBUTES;
	static {
		/* It's very important that the implementation is LinkedHashMap,
		 * as this preserves key order according to insertion order.
		 * An unordered map could violate the schema specification that
		 * ATTRIBUTE_SOURCE must come before ATTRIBUTE_TARGET.
		 * By the same reasoning, you must not re-order the insertions below.
		 */
		Map m = new LinkedHashMap();
		m.put(NWBFileProperty.ATTRIBUTE_SOURCE, NWBFileProperty.TYPE_INT);
		m.put(NWBFileProperty.ATTRIBUTE_TARGET, NWBFileProperty.TYPE_INT);
		NECESSARY_EDGE_ATTRIBUTES = Collections.unmodifiableMap(m);
	}
	
	private GetNWBFileMetadata metadataHandler;
	private NWBFileParser parser;
	

	public NWBEdgeAttributeReader(File nwbFile)
			throws IOException, ParsingException {
		// TODO: Use NWBFileUtilities to get the metadataHandler once Patrick
		// commits.
		metadataHandler = new GetNWBFileMetadata();		
		parser = new NWBFileParser(nwbFile);			
		parser.parse(metadataHandler);
	}

	
	protected List getRemovableAttributeKeys()
			throws IOException, ParsingException, AlgorithmExecutionException {
		// TODO: Use NWBFileUtilities to get the correct edge schema once
		// Patrick commits.
		Map directedEdgeSchema = metadataHandler.getDirectedEdgeSchema();
		Map undirectedEdgeSchema = metadataHandler.getUndirectedEdgeSchema();
		
		if( directedEdgeSchema != null) {
			return getRemovableAttributeKeys(directedEdgeSchema,
												NECESSARY_EDGE_ATTRIBUTES);
		}
		else if( undirectedEdgeSchema != null) {
			return getRemovableAttributeKeys(undirectedEdgeSchema,
												NECESSARY_EDGE_ATTRIBUTES);
		}
		else {
			throw new NoEdgeSchemaException();
		}
	}

	private List getRemovableAttributeKeys(Map schema, Map necessaryAttributes) {
		List attributeKeys = new ArrayList();
		
		for (Iterator attributeKeyIt = schema.keySet().iterator();
				attributeKeyIt.hasNext(); ) {
			String attributeKey = (String) attributeKeyIt.next();
			
			if (!necessaryAttributes.containsKey(attributeKey)) {
				attributeKeys.add(attributeKey);
			}
		}
		
		return attributeKeys;
	}
}
