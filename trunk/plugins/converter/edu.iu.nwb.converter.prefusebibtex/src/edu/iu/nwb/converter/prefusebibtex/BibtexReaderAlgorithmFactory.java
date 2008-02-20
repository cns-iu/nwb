package edu.iu.nwb.converter.prefusebibtex;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;


public class BibtexReaderAlgorithmFactory implements AlgorithmFactory {
	
    protected void activate(ComponentContext ctxt) { 
    }
    
    protected void deactivate(ComponentContext ctxt) {
    }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new BibtexReaderAlgorithm(data, parameters, context);
    }
    
    public MetaTypeProvider createParameters(Data[] data) {
        return null;
    }
}