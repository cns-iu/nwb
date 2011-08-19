package edu.iu.epic.visualization.linegraph.model.table;

import java.util.ArrayList;
import java.util.List;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;
import edu.iu.epic.visualization.linegraph.model.StencilStreamMetadata;
import edu.iu.epic.visualization.linegraph.model.TimestepBounds;
import edu.iu.epic.visualization.linegraph.model.tuple.TableSubsetTupleStream;
import edu.iu.epic.visualization.linegraph.model.tuple.TupleStream;
import edu.iu.epic.visualization.linegraph.zoom.ZoomAlgorithm;

public class TableStreamSource {
	private String title;
	private Table table;
	private String timeStepColumnName;
	private String lineColumnName;
	private String lineColumnDisplayName;
	private StencilStreamMetadata stencilStreamMetadata;

	public TableStreamSource(
			String title,
			Table table,
			String timeStepColumnName,
			String lineColumnName,
			StencilStreamMetadata stencilStreamMetadata
		) {
		this.title = title;
		this.table = table;
		this.timeStepColumnName = timeStepColumnName;
		this.lineColumnName = lineColumnName;
		this.lineColumnDisplayName = String.format("%s - %s", this.lineColumnName, this.title);
		
		this.stencilStreamMetadata = stencilStreamMetadata;
	}
	
	public String getLineColumnDisplayName() {
		return this.lineColumnDisplayName;
	}
	
	/**
	 * Goes through the table's timestep column & extracts all the timesteps.
	 * @return
	 */
	public List<Integer> getTimeSteps() {
		
		List<Integer> timeSteps = new ArrayList<Integer>();
		
		IntIterator rowIterator = this.table.rows(); 
		
		while (rowIterator.hasNext()) {
			int currentRow = rowIterator.nextInt();
			Tuple currentTuple = this.table.getTuple(currentRow);
			timeSteps.add(currentTuple.getInt(this.timeStepColumnName));
		}
		
		return timeSteps;
	}
	
	public TimestepBounds getSubsetBounds(ZoomAlgorithm zoomAlgorithm) {
		return zoomAlgorithm.getSubsetBounds(
				this.table, 
				this.lineColumnName, 
				this.timeStepColumnName);
	}

	public TupleStream getStream(TimestepBounds timestepBounds) {
		
		TupleStream stream = new TableSubsetTupleStream(
	    		this.table,
	    		this.timeStepColumnName,
	    		this.lineColumnName,
	    		this.lineColumnDisplayName,
		    	this.stencilStreamMetadata,
	    		timestepBounds);
		
		return stream;
	}
}
