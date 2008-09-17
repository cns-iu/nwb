package edu.iu.nwb.tools.mergenodes;

import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeInformation;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;


public class MergeNodesFactory implements AlgorithmFactory, ParameterMutator {
	private MetaTypeInformation originalProvider;
    private String pid;
    
    public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition parameters) {
//  	Table t = (Table) data[0].getData();
		ObjectClassDefinition oldDefinition = originalProvider.getObjectClassDefinition(this.pid, null);

		BasicObjectClassDefinition definition;
		try {
			definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), oldDefinition.getIcon(16));
		} catch (IOException e) {
			definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), null);
		}

		AttributeDefinition[] definitions = oldDefinition.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for(int ii = 0; ii < definitions.length; ii++) {
			String id = definitions[ii].getID();
			if(id.equals("aff")) {
				definition.addAttributeDefinition(ObjectClassDefinition.OPTIONAL,definitions[ii]);
			}  else {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, definitions[ii]);
			}
		}

		return definition;	
    }
	
    protected void activate(ComponentContext ctxt) {
        MetaTypeService mts = (MetaTypeService)ctxt.locateService("MTS");
        originalProvider = mts.getMetaTypeInformation(ctxt.getBundleContext().getBundle());
        pid = (String) ctxt.getServiceReference().getProperty(org.osgi.framework.Constants.SERVICE_PID);      
    }

    protected void deactivate(ComponentContext ctxt) {
        originalProvider = null;
    }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new MergeNodes(data, parameters, context);
    }
  
}



	



    
    

    
   