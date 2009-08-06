package edu.iu.nwb.preprocessing.removeegraphattributes.nwbIO;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class AttributeFilteringNWBWriter extends NWBFileWriter {
	protected Collection keysToRemove;
	
	public AttributeFilteringNWBWriter(File file, Collection keysToRemove)
			throws IOException {
		super(file);		
		this.keysToRemove = keysToRemove;
	}
}
