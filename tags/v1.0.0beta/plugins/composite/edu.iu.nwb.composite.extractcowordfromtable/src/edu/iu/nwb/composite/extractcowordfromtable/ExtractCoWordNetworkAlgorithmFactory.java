package edu.iu.nwb.composite.extractcowordfromtable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.service.conversion.DataConversionService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Schema;
import prefuse.data.Table;




public class ExtractCoWordNetworkAlgorithmFactory implements AlgorithmFactory,ParameterMutator {
	private AlgorithmFactory extractDirectedNetwork;
	private AlgorithmFactory bibliographicCoupling;
	private LogService log;
	private BundleContext bContext;
	
	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {
		Table t = (Table) data[0].getData();

		ObjectClassDefinition oldDefinition = parameters;

		BasicObjectClassDefinition definition;
		try {
			definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), oldDefinition.getIcon(16));
		} catch (IOException e) {
			definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), null);
		}

		String[] columnNames = createKeyArray(t.getSchema());
		Arrays.sort(columnNames);

		AttributeDefinition[] definitions = oldDefinition.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for(int ii = 0; ii < definitions.length; ii++) {
			String id = definitions[ii].getID();
			if((id.equals("sourceColumn") || id.equals("targetColumn"))){
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(id, definitions[ii].getName(), definitions[ii].getDescription(), definitions[ii].getType(), columnNames, columnNames));
			}
			else if(id.equals("delimiter")){
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, definitions[ii]);
			}
			else{
				definition.addAttributeDefinition(ObjectClassDefinition.OPTIONAL, definitions[ii]);
			}
		}

		
		return definition;	
	}


    protected void activate(ComponentContext ctxt) {
        this.log = (LogService) ctxt.locateService("LOG");
        bContext = ctxt.getBundleContext();
    }
    protected void deactivate(ComponentContext ctxt) {
    }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
    	String filter = "";
        try {
        	filter = "(service.pid=edu.iu.nwb.analysis.extractdirectednetfromtable.algorithms.ExtractDirectedNetworkAlgorithm)";
        	extractDirectedNetwork = getAlgorithmFactory(filter);
        	
        	//WARNING: This will break if the PID changes, and given that its current pid is misleading, this seems likely
        	filter = "(service.pid=edu.iu.nwb.preprocessing.cocitationsimilarity.CoCitationSimilarityAlgorithm)";
        	bibliographicCoupling = getAlgorithmFactory(filter);
			
        	DataConversionService converter = (DataConversionService)
            context.getService(DataConversionService.class.getName());
        	
        	 return new ExtractCoWordNetworkAlgorithm(data, parameters, context,
             		extractDirectedNetwork, converter, bibliographicCoupling);
             
		} catch (InvalidSyntaxException e) {
			log.log(LogService.LOG_ERROR, "Invalid syntax in filter " + filter);
			return null;
		}
    }
    
    private AlgorithmFactory getAlgorithmFactory (String filter) 
    throws InvalidSyntaxException {
    	ServiceReference[] algFactoryRefs = bContext
    	.getServiceReferences(AlgorithmFactory.class.getName(), filter);
    	
    	if (algFactoryRefs != null && algFactoryRefs.length != 0) {
    		ServiceReference algFactoryRef = algFactoryRefs[0];
    		
    		AlgorithmFactory algFactory = 
    			(AlgorithmFactory) bContext.getService(algFactoryRef);
    		
    		return algFactory;
    	} else {
    		this.log.log(LogService.LOG_ERROR, "ISI Load and Clean Algorithm" +
    				" was unable to find an algorithm that satisfied the " +
    				"following filter: " + filter);
    		return null;
    	}
    	
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