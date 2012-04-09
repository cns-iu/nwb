package edu.iu.sci2.database.scopus.load;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class ScopusDatabaseLoaderAlgorithmFactory implements AlgorithmFactory {
    @Override
	public Algorithm createAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext ciShellContext) {
        return new ScopusDatabaseLoaderAlgorithm(data, parameters, ciShellContext);
    }
}