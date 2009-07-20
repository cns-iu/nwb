package edu.iu.nwb.preprocessing.removeedgeattributes;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.cishell.framework.algorithm.AlgorithmExecutionException;

import edu.iu.nwb.util.nwbfile.GetMetadataAndCounts;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.NWBMetadataParsingException;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class NWBEdgeAttributeReader {
	private GetMetadataAndCounts metadata;
	

	public NWBEdgeAttributeReader(File nwbFile)
			throws AlgorithmExecutionException {
		try {
			this.metadata =	NWBFileUtilities.parseMetadata(nwbFile);
		} catch (NWBMetadataParsingException e) {
			throw new AlgorithmExecutionException(e);
		}
	}

	
	protected Collection getRemovableAttributeKeys()
			throws IOException, ParsingException, AlgorithmExecutionException {
		LinkedHashMap edgeSchema =
			NWBFileUtilities.getEdgeSchemaFromMetadata(metadata);
		return getRemovableAttributeKeys(edgeSchema, NWBFileUtilities.NECESSARY_EDGE_ATTRIBUTES);
	}

	private Collection getRemovableAttributeKeys(Map schema,
										   Map necessaryAttributes) {
		return CollectionUtils.subtract(schema.keySet(), necessaryAttributes.keySet());
	}
}
