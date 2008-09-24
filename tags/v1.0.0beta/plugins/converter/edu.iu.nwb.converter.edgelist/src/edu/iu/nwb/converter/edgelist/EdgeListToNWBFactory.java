package edu.iu.nwb.converter.edgelist;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;

public class EdgeListToNWBFactory implements AlgorithmFactory {

    /**
     * Create an converter
     * 
     * @param data The data to convert
     * @param parameters Parameters passed to the algorithm
     * @param context Access to the CIShell environment
     */
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
    	return new EdgeListToNWB(data, parameters, context);
		
        
    }
     
}