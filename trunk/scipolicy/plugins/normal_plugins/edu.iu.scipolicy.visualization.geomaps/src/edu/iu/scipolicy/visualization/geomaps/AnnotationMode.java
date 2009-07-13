package edu.iu.scipolicy.visualization.geomaps;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.opengis.referencing.crs.ProjectedCRS;

import prefuse.data.Table;

public interface AnnotationMode {
	@SuppressWarnings("unchecked") // TODO
	public void printPS(Table inTable, Dictionary parameters, File temporaryPostScriptFile, ProjectedCRS projectedCRS) throws AlgorithmExecutionException, IOException;
}
