package edu.iu.epic.visualization.linegraph.utilities;

import prefuse.data.Table;

public class TableStreamSource {
	private String title;
	private Table table;
	private String timeStepColumnName;
	private String lineColumnName;
	private String lineColumnDisplayName;
	private String stencilStreamName;
	private String stencilTimeStepID;
	private String stencilLineID;
	private String stencilValueID;

	public TableStreamSource(
			String title,
			Table table,
			String timeStepColumnName,
			String lineColumnName,
			String stencilStreamName,
			String stencilTimeStepID,
			String stencilLineID,
			String stencilValueID) {
		this.title = title;
		this.table = table;
		this.timeStepColumnName = timeStepColumnName;
		this.lineColumnName = lineColumnName;
		this.lineColumnDisplayName = String.format("%s - %s", this.lineColumnName, this.title);
		this.stencilStreamName = stencilStreamName;
		this.stencilTimeStepID = stencilTimeStepID;
		this.stencilLineID = stencilLineID;
		this.stencilValueID = stencilValueID;
	}
	
	public String getLineColumnDisplayName() {
		return this.lineColumnDisplayName;
	}

	public TupleStream getStream() {
		TupleStream stream = new TableTupleStream(
//			this.title,
    		this.table,
    		this.timeStepColumnName,
    		this.lineColumnName,
    		this.lineColumnDisplayName,
	    	this.stencilStreamName,
	    	this.stencilTimeStepID,
    		this.stencilLineID,
    		this.stencilValueID);
		
		return stream;
	}
}
