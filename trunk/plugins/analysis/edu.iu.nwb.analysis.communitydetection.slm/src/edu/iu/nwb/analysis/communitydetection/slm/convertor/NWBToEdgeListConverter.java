package edu.iu.nwb.analysis.communitydetection.slm.convertor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class NWBToEdgeListConverter {

	private NetworkInfo networkInfo;
	private String weightAttribute;
	private boolean isWeighted;

	public NWBToEdgeListConverter(NetworkInfo networkInfo, File outputFile,
			String weightAttribute, boolean isWeighted) {
		this.networkInfo = networkInfo;
		this.weightAttribute = weightAttribute;
		this.isWeighted = isWeighted;

		try {
			this.createOutputFile(outputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void createOutputFile(File outputFile) 
			throws FileNotFoundException, UnsupportedEncodingException {
		List<Edge> edges = this.networkInfo.getEdges();
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(outputFile, "UTF-8");
			for (Edge e : edges) {
				writer.println(e.getSource().getNewID() + "  " + e.getTarget().getNewID());
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

}
