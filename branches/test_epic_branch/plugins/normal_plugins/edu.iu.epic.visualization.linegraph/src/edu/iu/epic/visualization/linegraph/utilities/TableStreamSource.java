package edu.iu.epic.visualization.linegraph.utilities;

import prefuse.data.Table;

public class TableStreamSource {
	private Table table;
	private String timeStepColumnName;
	private String lineColumnName;
	private String stencilStreamName;
	private String stencilTimeStepID;
	private String stencilLineID;
	private String stencilValueID;

	public TableStreamSource(
			Table table,
			String timeStepColumnName,
			String lineColumnName,
			String stencilStreamName,
			String stencilTimeStepID,
			String stencilLineID,
			String stencilValueID) {
		this.table = table;
		this.timeStepColumnName = timeStepColumnName;
		this.lineColumnName = lineColumnName;
		this.stencilStreamName = stencilStreamName;
		this.stencilTimeStepID = stencilTimeStepID;
		this.stencilLineID = stencilLineID;
		this.stencilValueID = stencilValueID;
	}
	
	public String getLineColumnName() {
		return this.lineColumnName;
	}

	public TupleStream getStream() {
		TupleStream stream = new TableTupleStream(
    		this.table,
    		this.timeStepColumnName,
    		this.lineColumnName,
	    	this.stencilStreamName,
	    	this.stencilTimeStepID,
    		this.stencilLineID,
    		this.stencilValueID);
		
		return stream;
	}
}
