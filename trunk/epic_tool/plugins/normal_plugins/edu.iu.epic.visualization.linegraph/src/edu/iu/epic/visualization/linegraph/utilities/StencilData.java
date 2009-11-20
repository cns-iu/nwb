package edu.iu.epic.visualization.linegraph.utilities;

import java.util.ArrayList;
import java.util.List;

/* I don't like StencilData.  Stencil is both a script and the platform for
 *  executing that script.  The (Stencil) data to be fed into it is something
 *  different.
 * Certainly there's a term for a program and its data being coupled?
 *  Maybe execution unit?
 */
public class StencilData {
	private String stencilScript;
	private List<TableStreamSource> streamSources;
	
	public StencilData(
			String stencilScript, List<TableStreamSource> streamSources) {
		this.stencilScript = stencilScript;
		this.streamSources = streamSources;
	}
	
	public String getStencilScript() {
		return this.stencilScript;
	}
	
	public List<String> getLineColumnNames() {
		List<String> lineColumnNames = new ArrayList<String>();
		
		for (TableStreamSource streamSource : this.streamSources) {
			lineColumnNames.add(streamSource.getLineColumnName());
		}
		
		return lineColumnNames;
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
