package edu.iu.nwb.analysis.extractnetfromtable.algorithms;

import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicMetaTypeProvider;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeInformation;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Schema;
import prefuse.data.Table;


public class ExtractNetFromTableAlgorithmFactory implements AlgorithmFactory {
    private MetaTypeInformation originalProvider;
    private String pid;

    protected void activate(ComponentContext ctxt) {
        //You may delete all references to metatype service if 
        //your algorithm does not require parameters and return
        //null in the createParameters() method
        MetaTypeService mts = (MetaTypeService)ctxt.locateService("MTS");
        originalProvider = mts.getMetaTypeInformation(ctxt.getBundleContext().getBundle());
        pid = (String) ctxt.getServiceReference().getProperty(org.osgi.framework.Constants.SERVICE_PID);
        
    }
    protected void deactivate(ComponentContext ctxt) {
        originalProvider = null;
    }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new ExtractNetFromTableAlgorithm(data, parameters, context);
    }
    
    private String[] createKeyArray(Schema schema) {
		String[] keys = new String[schema.getColumnCount() + 1];
		keys[0] = "";

		for(int ii = 1; ii <= schema.getColumnCount(); ii++) {
			keys[ii] = schema.getColumnName(ii - 1);
		}

		return keys;
	}
    
    public MetaTypeProvider createParameters(Data[] data) {
    	Table t = (Table) data[0].getData();

			ObjectClassDefinition oldDefinition = originalProvider.getObjectClassDefinition(this.pid, null);

			BasicObjectClassDefinition definition;
			try {
				definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), oldDefinition.getIcon(16));
			} catch (IOException e) {
				definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), null);
			}

			String[] columnNames = createKeyArray(t.getSchema());
			

			AttributeDefinition[] definitions = oldDefinition.getAttributeDefinitions(ObjectClassDefinition.ALL);

			for(int ii = 0; ii < definitions.length; ii++) {
				String id = definitions[ii].getID();
				//System.err.println(id);
				if(id.equals("colName")) {
					definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
							new BasicAttributeDefinition("colName", "Column Name", "The label for the node size property", AttributeDefinition.STRING, columnNames, columnNames));
				} else if(id.equals("aff")) {
					definition.addAttributeDefinition(ObjectClassDefinition.OPTIONAL,definitions[ii]);
				}  else {
					definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, definitions[ii]);
				}
			}

			MetaTypeProvider provider = new BasicMetaTypeProvider(definition);
			return provider;
		
    }
}