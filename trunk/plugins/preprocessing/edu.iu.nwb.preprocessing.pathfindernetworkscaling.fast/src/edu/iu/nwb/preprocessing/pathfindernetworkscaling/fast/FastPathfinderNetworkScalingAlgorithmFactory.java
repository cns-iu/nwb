package edu.iu.nwb.preprocessing.pathfindernetworkscaling.fast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.util.nwbfile.GetNWBFileMetadata;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.ParsingException;

/**
 * @author cdtank
 *
 */

public class FastPathfinderNetworkScalingAlgorithmFactory 
	implements AlgorithmFactory, ParameterMutator  {
	
	private static final String UNWEIGHTED_WARNING = 
		"Unweighted (Cannot be Scaled using Fast Pathfinder.)";
	private static final String UNWEIGHTED_USE_MST_WARNING = 
		"Unweighted (Cannot be Scaled using Fast Pathfinder, use MST Pathfinder instead.)";
	private LogService log;
	private boolean isUnweighted;
	
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		return new FastPathfinderNetworkScalingAlgorithm(data, parameters, context, isUnweighted);
	}
	
	protected void activate(ComponentContext ctxt) {
		ServiceReference logReference = 
			ctxt.getBundleContext().getServiceReference(LogService.class.getName());
    	this.log = (LogService) ctxt.getBundleContext().getService(logReference);
    }
	
	/*
	 * To set the value of the drop-down box for edge weight column names. 
	 * Only numeric columns are considered.
	 * */
	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {
		File nwbFile = (File) data[0].getData();
		GetNWBFileMetadata metaDataHandler = new GetNWBFileMetadata();

		try {
			NWBFileParser parser = new NWBFileParser(nwbFile);
			parser.parse(metaDataHandler);
		} catch (IOException e1) {
			this.log.log(LogService.LOG_WARNING, "Error occurred while trying to open NWB file " 
					+ "'" + nwbFile.getAbsolutePath() + "'.");
			return parameters;
		} catch (ParsingException e) {
			this.log.log(LogService.LOG_WARNING, "Error occurred while trying to parse NWB file."
					, e); 
			return parameters;
		}
		
		
		BasicObjectClassDefinition definition;
		try {
			definition = new BasicObjectClassDefinition(parameters.getID(), 
														parameters.getName(), 
														parameters.getDescription(), 
														parameters.getIcon(16));
		} catch (IOException e) {
			definition = new BasicObjectClassDefinition(parameters.getID(), 
														parameters.getName(), 
														parameters.getDescription(), 
														null);
		}
		
		String[] edgeAttributesArray;
		LinkedHashMap edgeSchema;
		boolean isUndirected;
		
		
		if (metaDataHandler.getDirectedEdgeSchema() != null) {
			isUndirected = false;
			edgeSchema = metaDataHandler.getDirectedEdgeSchema();
			edgeAttributesArray = getNumericEdgeAttributes(edgeSchema, isUndirected);	
		} else {
			isUndirected = true;
			edgeSchema = metaDataHandler.getUndirectedEdgeSchema();
			edgeAttributesArray = getNumericEdgeAttributes(edgeSchema, isUndirected);
		}

		isUnweighted = isNetworkWeighted(edgeAttributesArray);
		
		AttributeDefinition[] definitions = 
			parameters.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for (int ii = 0; ii < definitions.length; ii++) {
			String id = definitions[ii].getID();
			
			if (id.equals("weightcolumn")) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(definitions[ii].getID(), 
								definitions[ii].getName(), 
								definitions[ii].getDescription(), 
								definitions[ii].getType(), 
								edgeAttributesArray, 
								edgeAttributesArray
								));
			} else {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, definitions[ii]);
			}
		}

		return definition;
	}
	
	
	/**
	 * Used to decide if the network is weighted or not.
	 * @param edgeAttributesArray
	 * @return
	 */
	private boolean isNetworkWeighted(String[] edgeAttributesArray) {
		if (edgeAttributesArray.length == 1) {
			if (edgeAttributesArray[0].equalsIgnoreCase(UNWEIGHTED_USE_MST_WARNING) 
					|| edgeAttributesArray[0].equalsIgnoreCase(UNWEIGHTED_WARNING)) {
				return true;
			}
		}
		return false;
	}

	private String[] getNumericEdgeAttributes(Map edgeSchema, boolean isUndirected) {
		List numericKeys = new ArrayList();
		
		/*
		 * In order to skip "source" & "target" options for the edge weight column.
		 * */
		Iterator keysSkip = edgeSchema.keySet().iterator();
		keysSkip.next();
		keysSkip.next();
		
		for (Iterator keys = keysSkip; keys.hasNext();) {
			String key = (String) keys.next();
			if (!edgeSchema.get(key).equals(NWBFileProperty.TYPE_STRING)) {
				numericKeys.add(key);
			}
		}
		
		if (numericKeys.size() < 1) {
			if (isUndirected) {
				numericKeys.add(UNWEIGHTED_USE_MST_WARNING);
			} else {
				numericKeys.add(UNWEIGHTED_WARNING);
			}
		}
		
		return (String[]) numericKeys.toArray(new String[]{});
	}
	
}