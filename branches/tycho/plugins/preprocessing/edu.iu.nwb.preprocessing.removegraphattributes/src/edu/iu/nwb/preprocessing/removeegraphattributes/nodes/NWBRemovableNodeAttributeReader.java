package edu.iu.nwb.preprocessing.removeegraphattributes.nodes;

import java.io.File;
import java.util.LinkedHashMap;

import edu.iu.nwb.preprocessing.removeegraphattributes.nwbIO.NWBRemovableAttributeReader;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.NWBMetadataParsingException;

public class NWBRemovableNodeAttributeReader
		extends NWBRemovableAttributeReader {
	public NWBRemovableNodeAttributeReader(File nwbFile) {
		super(nwbFile);
	}
	

	protected LinkedHashMap<String, String> getNecessaryAttributes() {
		return NWBFileProperty.NECESSARY_NODE_ATTRIBUTES;
	}

	protected LinkedHashMap<String, String> getSchema(File nwbFile)
			throws NWBMetadataParsingException {
		return NWBFileUtilities.getNodeSchema(nwbFile);
	}
}
