package edu.iu.nwb.preprocessing.removeegraphattributes.nodes;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.cishell.framework.algorithm.AlgorithmExecutionException;

import edu.iu.nwb.preprocessing.removeegraphattributes.nwbIO.RemovableAttributeReader;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.NWBMetadataParsingException;

public class NWBRemovableNodeAttributeReader implements RemovableAttributeReader {
	private LinkedHashMap nodeSchema;

	public NWBRemovableNodeAttributeReader(File nwbFile)
			throws AlgorithmExecutionException {
		try {
			nodeSchema = NWBFileUtilities.getNodeSchema(nwbFile);
		} catch (NWBMetadataParsingException e) {
			throw new AlgorithmExecutionException(
				"Error: Couldn't get node schema: " + e.getMessage(), e);
		}
	}

	/* Keys on this node schema for attributes which are not necessary
	 * and so can be removed
	 */
	public Collection determineRemovableAttributeKeys() {
		return CollectionUtils.subtract(
				nodeSchema.keySet(),
				NWBFileProperty.NECESSARY_NODE_ATTRIBUTES.keySet());
	}
}
