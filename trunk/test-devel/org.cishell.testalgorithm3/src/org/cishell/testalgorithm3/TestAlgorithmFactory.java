package org.cishell.testalgorithm3;

import java.util.Dictionary;
import java.util.Enumeration;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;


public class TestAlgorithmFactory implements AlgorithmFactory, ManagedService {
    private MetaTypeProvider provider;

    protected void activate(ComponentContext ctxt) {
        //You may delete all references to metatype service if 
        //your algorithm does not require parameters and return
        //null in the createParameters() method
        MetaTypeService mts = (MetaTypeService)ctxt.locateService("MTS");
        provider = mts.getMetaTypeInformation(ctxt.getBundleContext().getBundle());       
       
        System.out.println("TestAlgorithm 3 properties in activate!_!_!_!_!_!_");
  Dictionary properties = ctxt.getProperties();
        
        Enumeration propertiesKeys = properties.keys();
		
		while (propertiesKeys.hasMoreElements()) {
			String propertiesKey = (String) propertiesKeys.nextElement();
			
			Object propertiesValue = properties.get(propertiesKey);
			
			System.out.println("    " + propertiesKey + ": " + propertiesValue.toString());
		}
    }
    protected void deactivate(ComponentContext ctxt) {
        provider = null;
    }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new TestAlgorithm(data, parameters, context);
    }
    public MetaTypeProvider createParameters(Data[] data) {
        return provider;
    }
	public void updated(Dictionary properties) throws ConfigurationException {
		// TODO Auto-generated method stub
		
	}
}