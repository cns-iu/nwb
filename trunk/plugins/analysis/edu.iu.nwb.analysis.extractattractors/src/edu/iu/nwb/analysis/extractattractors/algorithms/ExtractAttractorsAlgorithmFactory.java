package edu.iu.nwb.analysis.extractattractors.algorithms;

import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeInformation;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Schema;
import prefuse.data.Table;


public class ExtractAttractorsAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	private MetaTypeInformation originalProvider;
	private String pid;


	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition parameters) {
		Table t = (Table) data[1].getData();

		ObjectClassDefinition oldDefinition = parameters;

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
			if(id.equals("labelColumn")) {
				definition.addAttributeDefinition(ObjectClassDefinition.OPTIONAL,
						new BasicAttributeDefinition("labelColumn", "Label Column", "Optional column to use when labeling the attractors.", AttributeDefinition.STRING, columnNames, columnNames));
			}  
			else{
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
		return new ExtractAttractorsAlgorithm(data, parameters, context);
	}

	private String[] createKeyArray(Schema schema) {
		String[] keys = new String[schema.getColumnCount() + 1];
		keys[0] = "";

		for(int ii = 1; ii <= schema.getColumnCount(); ii++) {
			keys[ii] = schema.getColumnName(ii - 1);
		}

		return keys;
	}


}