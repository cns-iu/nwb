package edu.iu.scipolicy.visualization.geomaps;

import java.util.Dictionary;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

import prefuse.data.Table;

public interface AnnotationMode {
	@SuppressWarnings("unchecked") // TODO
	public void applyAnnotations(Table inTable, Dictionary parameters, ShapefileToPostScript shapefileToPostScript) throws AlgorithmExecutionException;
}
