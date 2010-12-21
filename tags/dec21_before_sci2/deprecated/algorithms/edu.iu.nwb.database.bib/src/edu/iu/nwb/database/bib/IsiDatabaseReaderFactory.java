package edu.iu.nwb.database.bib;

import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;


public class IsiDatabaseReaderFactory implements AlgorithmFactory {

    private URL script;
    
	protected void activate(ComponentContext ctxt) {
		script = ctxt.getBundleContext().getBundle().getResource("/edu/iu/nwb/database/bib/python/databaser.py");
    }
    protected void deactivate(ComponentContext ctxt) {
    }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new IsiDatabaseReader(data, parameters, context, script);
    }
    public MetaTypeProvider createParameters(Data[] data) {
        return null;
    }
}