package edu.iu.nwb.preprocessing.removeedgeattributes;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.cishell.framework.algorithm.AlgorithmExecutionException;

import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.NWBMetadataParsingException;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class NWBEdgeAttributeReader {
	private LinkedHashMap edgeSchema;

	public NWBEdgeAttributeReader(File nwbFile)
			throws AlgorithmExecutionException {
		try {
			edgeSchema = NWBFileUtilities.getEdgeMetadata(nwbFile);
		} catch (NWBMetadataParsingException e) {
			throw new AlgorithmExecutionException(e);
		}
	}

	// Keys on this edge schema which are not necessary and so can be removed.
	protected Collection getRemovableAttributeKeys()
			throws IOException, ParsingException, AlgorithmExecutionException {
		return CollectionUtils.subtract(edgeSchema.keySet(), NWBFileProperty.NECESSARY_EDGE_ATTRIBUTES.keySet());
	}
}
