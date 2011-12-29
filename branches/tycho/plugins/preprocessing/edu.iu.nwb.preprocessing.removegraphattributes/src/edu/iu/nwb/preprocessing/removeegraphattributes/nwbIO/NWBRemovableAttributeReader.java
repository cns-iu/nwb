package edu.iu.nwb.preprocessing.removeegraphattributes.nwbIO;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.apache.commons.collections.CollectionUtils;

import edu.iu.nwb.util.nwbfile.NWBMetadataParsingException;

public abstract class NWBRemovableAttributeReader {	
	private File nwbFile;

	public NWBRemovableAttributeReader(File nwbFile) {
		this.nwbFile = nwbFile;
	}

	protected abstract LinkedHashMap<String, String> getSchema(File nwbFile)
			throws NWBMetadataParsingException;
	protected abstract LinkedHashMap<String, String> getNecessaryAttributes();

	/* Keys on this schema for attributes which are not necessary
	 * and so can be removed
	 */
	@SuppressWarnings("unchecked")	// Apache Collections
	public Collection<String> determineRemovableAttributeKeys()
			throws NWBMetadataParsingException {
		return CollectionUtils.subtract(
				getSchema(nwbFile).keySet(),
				getNecessaryAttributes().keySet());
	}
}
