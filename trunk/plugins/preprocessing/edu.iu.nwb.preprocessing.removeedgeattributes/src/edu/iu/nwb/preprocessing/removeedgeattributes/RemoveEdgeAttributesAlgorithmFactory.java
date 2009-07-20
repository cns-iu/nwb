package edu.iu.nwb.preprocessing.removeedgeattributes;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.util.nwbfile.ParsingException;

public class RemoveEdgeAttributesAlgorithmFactory implements AlgorithmFactory,
															 ParameterMutator {
	/* This dummy attribute definition is included only so that mutateParameters
	 * does not receive a null OCD.  It will be removed in mutateParameters.
	 */
	public static final String DUMMY_AD_ID = "DUMMY_AD";
	
	public Algorithm createAlgorithm(Data[] data,
									 Dictionary parameters,
									 CIShellContext context) {
        return new RemoveEdgeAttributesAlgorithm(data, parameters, context);
    }
    
	// TODO: Motivation for mutating parameters?
    public ObjectClassDefinition mutateParameters(
    		Data[] data,
    		ObjectClassDefinition oldParameters) {
    	try {
    		ObjectClassDefinition newParameters =
    			removeDummyParameter(oldParameters);
    	
    		File nwbFile = (File) data[0].getData();
		
    		List removableAttributeKeys;
    		NWBEdgeAttributeReader reader = new NWBEdgeAttributeReader(nwbFile);
    		removableAttributeKeys = reader.getRemovableAttributeKeys();
    		
    		return addBooleanOptions(newParameters,
    								 removableAttributeKeys,
    								 new RemoveDescriptionCreator());
	    } catch(IOException e) {
			logWarning(e);			
			return oldParameters;
		} catch(ParsingException e) {
			logWarning(e);			
			return oldParameters;
		} catch(AlgorithmExecutionException e) {
			logWarning(e);			
			return oldParameters;
		}
	}

	private ObjectClassDefinition addBooleanOptions(
			ObjectClassDefinition oldOCD,
			List options,
			AttributeDescriptionCreator descriptionCreator) {
		BasicObjectClassDefinition ocd = cloneOCD(oldOCD);
		
		AttributeDefinition[] ads =
			oldOCD.getAttributeDefinitions(ObjectClassDefinition.ALL);
		for(int ii = 0; ii < ads.length; ii++) {
			ocd.addAttributeDefinition(ObjectClassDefinition.REQUIRED, ads[ii]);
		}
	
		for(Iterator optionIt = options.iterator(); optionIt.hasNext(); ) {
			String name = (String) optionIt.next();
			AttributeDefinition ad = new BasicAttributeDefinition(
												name,
												name,
												descriptionCreator.create(name),
												AttributeDefinition.BOOLEAN);
			ocd.addAttributeDefinition(ObjectClassDefinition.REQUIRED, ad);
		}
	
		return ocd;
	}
	
	private interface AttributeDescriptionCreator {
		String create(String name);
	}	
	private class RemoveDescriptionCreator implements AttributeDescriptionCreator {
		public String create(String name) {
			return "Remove attribute " + name + "?";
		}
	}

	private ObjectClassDefinition removeDummyParameter(
										ObjectClassDefinition parameters) {
		BasicObjectClassDefinition cleanedParameters = cloneOCD(parameters);
		
		AttributeDefinition[] ads = parameters.getAttributeDefinitions(
													ObjectClassDefinition.ALL);
		for ( int ii = 0; ii < ads.length; ii++ ) {
			AttributeDefinition ad = ads[ii];
			
			if ( !DUMMY_AD_ID.equals(ad.getName()) ) {
				cleanedParameters.addAttributeDefinition(
												ObjectClassDefinition.REQUIRED,
												ad);
			}
		}
		
		return cleanedParameters;
	}

	// TODO: Remove this and use the (org.cishell.utilities?)
	// MutateParameterUtilites version.
	private BasicObjectClassDefinition cloneOCD(ObjectClassDefinition ocd) {
		BasicObjectClassDefinition clonedOCD;
		try {
			clonedOCD = new BasicObjectClassDefinition(
													ocd.getID(),
													ocd.getName(),
													ocd.getDescription(),
													ocd.getIcon(16));
		}
		catch(IOException e) {
			clonedOCD = new BasicObjectClassDefinition(
													ocd.getID(),
													ocd.getName(),
													ocd.getDescription(),
													null);
		}
		
		return clonedOCD;
	}

	private void logWarning(Exception e) {
		RemoveEdgeAttributesAlgorithm.logger.log(
							LogService.LOG_WARNING,
							"Exception while reading edge attributes:" + e);
	}
}