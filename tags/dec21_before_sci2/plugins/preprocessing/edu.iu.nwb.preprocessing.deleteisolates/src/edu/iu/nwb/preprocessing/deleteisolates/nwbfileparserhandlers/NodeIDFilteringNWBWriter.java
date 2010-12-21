package edu.iu.nwb.preprocessing.deleteisolates.nwbfileparserhandlers;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class NodeIDFilteringNWBWriter extends NWBFileWriter {
	private Set goodIDs;
	private int numberFiltered;
	
	public NodeIDFilteringNWBWriter(Set goodIDs, File outputNWBFile)
			throws IOException {
		super(outputNWBFile);
		
		this.goodIDs = goodIDs;
		
		this.numberFiltered = 0;
	}
	
	public int getNumberFiltered() {
		return numberFiltered;
	}
	
	public void addNode(int id, String label, Map attributes) {
		if (goodIDs.contains(new Integer(id))) {
			super.addNode(id, label, attributes);
		} else {
			this.numberFiltered++;
		}
	}
}