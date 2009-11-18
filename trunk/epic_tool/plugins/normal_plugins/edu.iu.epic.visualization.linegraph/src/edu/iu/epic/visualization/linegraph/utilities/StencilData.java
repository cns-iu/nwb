package edu.iu.epic.visualization.linegraph.utilities;

import java.util.ArrayList;
import java.util.List;

public class StencilData {
	private String stencilScript;
	private List<TableStreamSource> streamSources;
	
	public StencilData(String stencilScript,
					   List<TableStreamSource> streamSources) {
		this.stencilScript = stencilScript;
		this.streamSources = streamSources;
	}
	
	public String getStencilScript() {
		return this.stencilScript;
	}
	
	public List<TupleStream> createStreams() {
		List<TupleStream> streams = new ArrayList<TupleStream>();
		for (TableStreamSource source : streamSources) {
			TupleStream stream = source.getStream();
			streams.add(stream);
		}
		
		return streams;
	}
}
