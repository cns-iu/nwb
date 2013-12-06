package edu.iu.nwb.analysis.communitydetection.slm.convertor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public final class NWBToEdgeListConverter {

	private NWBToEdgeListConverter() {
	}

	public static void convert(File outputFile, NetworkInfo networkInfo) 
			throws FileNotFoundException, UnsupportedEncodingException {
		List<Edge> edges = networkInfo.getEdges();
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(outputFile, "UTF-8");
			for (Edge e : edges) {
				writer.println(e.toVosString());
			}
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
