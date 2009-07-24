package edu.iu.nwb.visualization.drl;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class NWBToSimFileHandler extends NWBFileParserAdapter {
	private static final double DEFAULT_EDGE_WEIGHT = 1.0;
	
	private boolean useEdgeWeight;
	private String weightAttribute;
	private PrintWriter out;
	
	public NWBToSimFileHandler(boolean useEdgeWeight,
							   String weightAttribute,
							   OutputStream output) {
		this.useEdgeWeight = useEdgeWeight;
		this.weightAttribute = weightAttribute;
		this.out = new PrintWriter(output);
	}
	
	public void addUndirectedEdge(int source, int target, Map attributes) {
		if (useEdgeWeight) {
			if (attributes.containsKey(weightAttribute)) {
				out.println(createEdgeLine(source,
										   target,
										   attributes.get(weightAttribute)));
			}
		}
		else {
			out.println(createEdgeLine(source, target, ""+DEFAULT_EDGE_WEIGHT));
		}
	}
	
	private String createEdgeLine(int source, int target, Object weight) {
		return (source + "\t" + target + "\t" + weight + "\n");
	}
	
	public void finishedParsing() {
		out.close();
	}
}
