package edu.iu.nwb.core.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import edu.iu.iv.core.persistence.BasicFileResourceDescriptor;
import edu.iu.nwb.common.model.FileBackedNWBModel;
import edu.iu.nwb.core.model.NWBModel;

public class NWBFile {
	
	public NWBModel load(String filename) {
		return new FileBackedNWBModel(new BasicFileResourceDescriptor(new File(filename)));
	}
	
	public void save(NWBModel model, String filename) {
		try {
			PrintWriter out
			   = new PrintWriter(new BufferedWriter(new FileWriter(filename)));

			out.println("*Nodes");
			Iterator data = model.getNodes();
			while (data.hasNext()) {
				Object node = data.next();
				out.println(node.toString());
			}

			out.println("");
			out.println("*DirectedEdges");
			data = model.getDirectedEdges();
			while (data.hasNext()) {
				Object edge = data.next();
				out.println(edge.toString());
			}
			
			out.println("");
			out.println("*UndirectedEdges");
			data = model.getUndirectedEdges();
			while (data.hasNext()) {
				Object edge = data.next();
				out.println(edge.toString());
			}

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
