package edu.iu.scipolicy.preprocessing.extractzipcode;

import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.TableUtilities;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

public class ExtractZipcodeFactory implements AlgorithmFactory, ParameterMutator {
	
	@SuppressWarnings("unchecked")
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {

		return new ExtractZipcodeAlgorithm(data, parameters, context);
	}

	public ObjectClassDefinition mutateParameters(Data[] data, 
												  ObjectClassDefinition oldParameters) {
		/*
		 * 
		 * User needs to decide whether the output values will contain truncated
		 * ZIP codes i.e. 5 digit form or long form i.e. 9 digits.   
		 *  
		 * Fill the 'Address' parameter drop-down box with column names 
		 * from the input table, so the user can choose which column of address
		 * they want to be considered for extraction of ZIP code.
		 *  
		 * Only columns of type 'String' are considered.
		 *  
		 *  
		 */
		
		Data inData = data[0];
		Table table = (Table) inData.getData();
		
		BasicObjectClassDefinition newParameters;

		try {
			newParameters = new BasicObjectClassDefinition(oldParameters.getID(), oldParameters
					.getName(), oldParameters.getDescription(), oldParameters.getIcon(16));
		} catch (IOException e) {
			newParameters = new BasicObjectClassDefinition(oldParameters.getID(), oldParameters
					.getName(), oldParameters.getDescription(), null);
		}

		AttributeDefinition[] oldAttributeDefinitions = oldParameters
				.getAttributeDefinitions(ObjectClassDefinition.ALL);
		
		String[] probableZipCodeColumns = 
			TableUtilities.getValidStringColumnNamesInTable(table);

		/*
		 * Make the drop down boxes appear for Address.
		 * */
		for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
			
			/*
			 * For place column name.
			 * */
			if (oldAttributeDefinitionID.equals(ExtractZipcodeAlgorithm.PLACE_NAME_COLUMN)) {
				newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(oldAttributeDefinition.getID(), 
								oldAttributeDefinition.getName(), 
								oldAttributeDefinition.getDescription(), 
								oldAttributeDefinition.getType(), 
								probableZipCodeColumns, 
								probableZipCodeColumns));
			} else {
				newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED, 
													 oldAttributeDefinition);
			}
		}
		return newParameters;
	}
	
}