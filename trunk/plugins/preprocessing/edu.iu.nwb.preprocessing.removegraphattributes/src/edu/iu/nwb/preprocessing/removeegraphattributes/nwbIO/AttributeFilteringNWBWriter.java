package edu.iu.nwb.preprocessing.removeegraphattributes.nwbIO;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public abstract class AttributeFilteringNWBWriter extends NWBFileWriter {
	protected Collection<String> keysToRemove;
	
	public AttributeFilteringNWBWriter(File file, Collection<String> keysToRemove)
			throws IOException {
		super(file);		
		this.keysToRemove = keysToRemove;
	}
}
