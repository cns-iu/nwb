package edu.iu.epic.visualization.linegraph.utilities;

import java.util.ArrayList;
import java.util.List;

public class StencilData {
	private String stencilScript;
	private List<StreamSource> streamSources;
	
	public StencilData(String stencilScript,
					   List<StreamSource> streamSources) {
		this.stencilScript = stencilScript;
		this.streamSources = streamSources;
	}
	
	public String getStencilScript() {
		return this.stencilScript;
	}
	
	public List<TupleStream> createStreams() {
		List<TupleStream> streams = new ArrayList<TupleStream>();
		for (StreamSource source : streamSources) {
			TupleStream stream = source.getStream();
			streams.add(stream);
		}
		
		return streams;
	}
}
