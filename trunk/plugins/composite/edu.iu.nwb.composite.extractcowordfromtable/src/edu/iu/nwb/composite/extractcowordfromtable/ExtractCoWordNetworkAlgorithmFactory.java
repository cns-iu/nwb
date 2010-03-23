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




public class ExtractCoWordNetworkAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	protected static final String PREFIX = "column_";
	
	protected static final String UNFILTERED_COLUMNS[] =
	{
		"abstract", "keyword", "title", "field of application", "chemicals/cas",
	};
	/* {
		"", "City of Publisher", "DOI", "Document Type", "E-mail Address", "Ending Page",
		"File Type", "ISI Document Delivery Number", "ISSN", "New Article Number",
		"PA", "Part Number", "Publication Date", "Publication Type", "Publication Year",
		"Publisher Web Address", "Reprint Address", "Research Addresses", "SC", "Special Issue",
		"Times Cited", "Unique ID", "Version Number", "Volume"
	}; */
	
	private AlgorithmFactory extractDirectedNetwork;
	private AlgorithmFactory bibliographicCoupling;
	private LogService log;
	private BundleContext bContext;
	
	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {
		Table dataTable = (Table) data[0].getData();

		ObjectClassDefinition oldObjectClassDefinition = parameters;
		BasicObjectClassDefinition newObjectClassDefinition;
		
		try
		{
			newObjectClassDefinition = new BasicObjectClassDefinition(oldObjectClassDefinition.getID(),
				oldObjectClassDefinition.getName(), oldObjectClassDefinition.getDescription(),
				oldObjectClassDefinition.getIcon(16));
		}
		catch (IOException e)
		{
			newObjectClassDefinition = new BasicObjectClassDefinition(oldObjectClassDefinition.getID(),
				oldObjectClassDefinition.getName(), oldObjectClassDefinition.getDescription(), null);
		}

		String[] columnNames = createKeyArray(dataTable.getSchema());
		Arrays.sort(columnNames);

		AttributeDefinition[] attributeDefinitions =
			oldObjectClassDefinition.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for(int ii = 0; ii < attributeDefinitions.length; ii++)
		{
			String id = attributeDefinitions[ii].getID();
			
			if(id.equals("sourceColumn"))
			{
				newObjectClassDefinition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
					new BasicAttributeDefinition(id, attributeDefinitions[ii].getName(),
						attributeDefinitions[ii].getDescription(), attributeDefinitions[ii].getType(),
						columnNames, columnNames));
			}
			else if(id.equals("delimiter"))
			{
				newObjectClassDefinition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
					attributeDefinitions[ii]);
			}
			else if(id.equals("targetColumn"))
			{
				// Skip target column.
			}
			else
			{
				newObjectClassDefinition.addAttributeDefinition(ObjectClassDefinition.OPTIONAL,
					attributeDefinitions[ii]);
			}
		}

		
		// return newObjectClassDefinition;
		return addBooleanOptions (newObjectClassDefinition, columnNames, PREFIX);
	}

	private ObjectClassDefinition addBooleanOptions(ObjectClassDefinition oldObjectClassDefinition,
													String[] columnNames, String prefix)
	{
		BasicObjectClassDefinition newObjectClassDefinition;
		
		try
		{
			newObjectClassDefinition = new BasicObjectClassDefinition(oldObjectClassDefinition.getID(),
				oldObjectClassDefinition.getName(), oldObjectClassDefinition.getDescription(),
				oldObjectClassDefinition.getIcon(16));
		}
		catch (IOException e)
		{
			newObjectClassDefinition = new BasicObjectClassDefinition(oldObjectClassDefinition.getID(),
				oldObjectClassDefinition.getName(), oldObjectClassDefinition.getDescription(), null);
		}
		
		AttributeDefinition[] attributeDefinitions =
			oldObjectClassDefinition.getAttributeDefinitions(ObjectClassDefinition.REQUIRED);
		
		for(int ii = 0; ii < attributeDefinitions.length; ii++)
		{
			newObjectClassDefinition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
				attributeDefinitions[ii]);
		}
		
		attributeDefinitions =
			oldObjectClassDefinition.getAttributeDefinitions(ObjectClassDefinition.OPTIONAL);
		
		for(int ii = 0; ii < attributeDefinitions.length; ii++)
		{
			newObjectClassDefinition.addAttributeDefinition(ObjectClassDefinition.OPTIONAL,
				attributeDefinitions[ii]);
		}
		
		attributeDefinitions =
			oldObjectClassDefinition.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for(int ii = 0; ii < columnNames.length; ii++)
		{
			String name = columnNames[ii];
			
			// Ugh, there has to be a library for this.
			final int numFilteredColumns = UNFILTERED_COLUMNS.length;
			
			// We're white listing, so we're filtering by default.
			boolean filtered = true;
			
			for (int jj = 0; jj < numFilteredColumns; jj++)
			{
				// If the column contains one of our white listed items, let it through.
				if (name.toLowerCase().indexOf(UNFILTERED_COLUMNS[jj]) != -1)
				{
					filtered = false;
					
					break;
				}
			}
			
			// If the column was filtered (determined in the prior loop), continue on with the next
			// loop iteration.
			if (filtered)
				continue;
			 
			newObjectClassDefinition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
				new BasicAttributeDefinition(prefix + name, name, "Normalize column " + name + "?",
											 AttributeDefinition.BOOLEAN));
		}

		
		return newObjectClassDefinition;
	}
	
	protected void activate(ComponentContext ctxt) {
        this.log = (LogService) ctxt.locateService("LOG");
        bContext = ctxt.getBundleContext();
    }
    protected void deactivate(ComponentContext ctxt) {}

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
    	String filter = "";
        try {
        	filter = "(service.pid=edu.iu.nwb.analysis.extractdirectednetfromtable.ExtractDirectedNetwork)";
        	extractDirectedNetwork = getAlgorithmFactory(filter);
        	
        	//WARNING: This will break if the PID changes, and given that its current pid is misleading, this seems likely
        	filter = "(service.pid=edu.iu.nwb.preprocessing.cocitationsimilarity.CoCitationSimilarityAlgorithm)";
        	bibliographicCoupling = getAlgorithmFactory(filter);
			
        	DataConversionService converter = (DataConversionService)
            	context.getService(DataConversionService.class.getName());
        	
        	 return new ExtractCoWordNetworkAlgorithm(data, parameters, context,
             		extractDirectedNetwork, converter, bibliographicCoupling);
             
		} catch (InvalidSyntaxException e) {
			log.log(LogService.LOG_ERROR, "Invalid syntax in filter " + filter, e);
			return null;
		}
    }
    
    private AlgorithmFactory getAlgorithmFactory (String filter) 
    throws InvalidSyntaxException {
    	ServiceReference[] algFactoryRefs =
    		bContext.getServiceReferences(AlgorithmFactory.class.getName(), filter);
    	
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