package edu.iu.epic.visualization.linegraph.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.iu.epic.visualization.linegraph.model.table.TableStreamSource;
import edu.iu.epic.visualization.linegraph.model.tuple.TupleStream;
import edu.iu.epic.visualization.linegraph.zoom.ZoomAlgorithm;

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
	
	public Integer getTimestepUpperBoundFromAllStreamSources(ZoomAlgorithm zoomAlgorithm) {
		
		List<Integer> timesteps = new ArrayList<Integer>();
		
		for (TableStreamSource streamSource : this.streamSources) {
			timesteps.add(streamSource.getSubsetBounds(zoomAlgorithm).getUpperbound());
		}
		
		return Collections.max(timesteps);
	}
	
	
	/**
	 * This method is used to capture all the distinct timesteps among all the 
	 * streams participating in A StencilData. This will be used further by the
	 * StencilController to get all the timesteps from all the streams from all
	 * the StencilDatums currently in existence. 
	 * @return
	 */
	public Set<Integer> getTimestepsFromAllSources() {
		Set<Integer> timesteps = new HashSet<Integer>();
		
		for (TableStreamSource streamSource : this.streamSources) {
			timesteps.addAll(streamSource.getTimeSteps());
		}
		
		return timesteps;
	}
	
	/**
	 * This method is used to get the timesteps from all the sources but the output
	 * will be a sorted list. One use case is to feed timesteps to the slider that 
	 * is used to control the data points shown on the line graph. 
	 * @return
	 */
	public List<Integer> getSortedTimestepsFromAllSources() {
		
		List<Integer> unifiedTimestepList = 
			new ArrayList<Integer>(getTimestepsFromAllSources());
		
		Collections.sort(unifiedTimestepList);
		
		return unifiedTimestepList;
	}
	
	public List<TupleStream> createStreams(TimestepBounds timestepBounds) {
		List<TupleStream> streams = new ArrayList<TupleStream>();
		
		for (TableStreamSource source : this.streamSources) {
			TupleStream stream = source.getStream(timestepBounds);
			streams.add(stream);
		}
		
		return streams;
	}
}
