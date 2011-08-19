package edu.iu.epic.visualization.linegraph.model;

public class StencilStreamMetadata {

	public String stencilStreamName;
	public String stencilTimeStepID;
	public String stencilLineID;
	public String stencilValueID;
	
	public StencilStreamMetadata(String stencilStreamName,
								 String stencilTimeStepID, 
								 String stencilLineID,
								 String stencilValueID) {
		
		this.stencilStreamName = stencilStreamName;
		this.stencilTimeStepID = stencilTimeStepID;
		this.stencilLineID = stencilLineID;
		this.stencilValueID = stencilValueID;
	}
	
}
