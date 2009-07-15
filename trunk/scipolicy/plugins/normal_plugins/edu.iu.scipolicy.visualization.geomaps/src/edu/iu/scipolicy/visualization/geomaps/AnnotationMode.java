package edu.iu.scipolicy.visualization.geomaps;

import java.util.Dictionary;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

import prefuse.data.Table;

public interface AnnotationMode {
	@SuppressWarnings("unchecked") // TODO
	public void applyAnnotations(ShapefileToPostScriptWriter postScriptWriter, Table inTable, Dictionary parameters) throws AlgorithmExecutionException;
}
