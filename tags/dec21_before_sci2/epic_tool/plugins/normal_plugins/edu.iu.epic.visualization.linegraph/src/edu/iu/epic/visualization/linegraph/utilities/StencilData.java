package edu.iu.epic.visualization.linegraph.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StencilData {
	private Collection<TableStreamSource> streamSources;
	private Collection<String> lineColumnNames = new ArrayList<String>();
	
	public StencilData(Collection<TableStreamSource> streamSources) {
		this.streamSources = streamSources;

		for (TableStreamSource streamSource : this.streamSources) {
			this.lineColumnNames.add(streamSource.getLineColumnDisplayName());
		}
	}
	
	public Collection<String> getLineColumnNames() {
		return this.lineColumnNames;
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
