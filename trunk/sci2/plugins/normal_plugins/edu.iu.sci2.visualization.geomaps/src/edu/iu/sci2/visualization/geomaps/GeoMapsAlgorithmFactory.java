package edu.iu.sci2.visualization.geomaps;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.osgi.service.metatype.ObjectClassDefinition;

/*
 * TODO:
 *
 * File extension is "ps" even though the metadata says "eps".  We'd have to write
 * some (trivial) new code that takes the eps MIME type to file-ext eps.  Worth it?
 * Note: Joseph has written a prototype for this; ask him to commit.
 * 
 * The legend components currently will include data in its extrema even when that
 * piece of data isn't visible on the map.  For example, if the map is of the United
 * States, but the input data included figures for Egypt, then if the Egypt data
 * represents extremes in the data, then that will be reflected in the legend even
 * though you can't see Egypt on the output map.  Like if Egypt has a circle size
 * of 10000, and all the United States figures are less than 100, then the circle size
 * legend component will show the maximum as 10000 and show a circle of that size even
 * though no circle of that size is visible.  But is this generally wrong?  Perhaps
 * the user wants the Egyptian extreme to skew the US visualizations.
 * Need an executive call on this one.
 */

public abstract class GeoMapsAlgorithmFactory
		implements AlgorithmFactory, ParameterMutator {
	@Override
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
        return new GeoMapsAlgorithm(data, parameters, context, getAnnotationMode(), getOutputAlgorithmName());
    }
    
    protected abstract AnnotationMode getAnnotationMode();
    
    protected abstract String getOutputAlgorithmName();
    
    @Override
	public abstract ObjectClassDefinition mutateParameters(
    		Data[] data, ObjectClassDefinition oldParameters);
}