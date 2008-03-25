package org.mediavirus.parvis;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.metatype.MetaTypeProvider;


public class ParallelCoordinatesAlgorithmFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new ParallelCoordinatesAlgorithm(data, parameters, context);
    }
    public MetaTypeProvider createParameters(Data[] data) {
    	return null;
    }
}