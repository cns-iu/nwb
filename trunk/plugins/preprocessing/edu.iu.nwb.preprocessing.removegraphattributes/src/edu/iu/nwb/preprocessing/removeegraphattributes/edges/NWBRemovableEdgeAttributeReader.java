package edu.iu.nwb.preprocessing.removeegraphattributes.edges;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.cishell.framework.algorithm.AlgorithmExecutionException;

import edu.iu.nwb.preprocessing.removeegraphattributes.nwbIO.RemovableAttributeReader;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.NWBMetadataParsingException;

public class NWBRemovableEdgeAttributeReader implements RemovableAttributeReader {
	private LinkedHashMap edgeSchema;

	public NWBRemovableEdgeAttributeReader(File nwbFile)
			throws AlgorithmExecutionException {
		try {
			edgeSchema = NWBFileUtilities.getEdgeMetadata(nwbFile);
		} catch (NWBMetadataParsingException e) {
			throw new AlgorithmExecutionException(
					"Error: Couldn't get edge schema: " + e.getMessage(), e);
		}
	}

	/* Keys on this edge schema for attributes which are not necessary
	 * and so can be removed
	 */
	public Collection determineRemovableAttributeKeys() {
		return CollectionUtils.subtract(
				edgeSchema.keySet(),
				NWBFileProperty.NECESSARY_EDGE_ATTRIBUTES.keySet());
	}
}
