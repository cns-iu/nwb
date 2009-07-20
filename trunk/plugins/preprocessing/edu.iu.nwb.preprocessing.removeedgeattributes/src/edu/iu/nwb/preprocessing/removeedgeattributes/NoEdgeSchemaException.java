package edu.iu.nwb.preprocessing.removeedgeattributes;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

public class NoEdgeSchemaException extends AlgorithmExecutionException {
	private static final long serialVersionUID = 1L;
	public static final String MESSAGE = "No edge schema found for NWB file.";

	
	public NoEdgeSchemaException() {
		super(MESSAGE);
	}	
}
