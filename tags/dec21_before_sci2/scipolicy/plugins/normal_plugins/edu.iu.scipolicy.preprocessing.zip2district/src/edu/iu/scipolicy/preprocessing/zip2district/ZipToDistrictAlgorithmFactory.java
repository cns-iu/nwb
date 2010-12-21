package edu.iu.scipolicy.preprocessing.zip2district;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.TableUtilities;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.scipolicy.preprocessing.zip2district.mapper.Mapper;
import edu.iu.scipolicy.preprocessing.zip2district.mapper.ZipCodeToCongressionalDistrict;

import prefuse.data.Table;

/**
 * ZipToDistrictAlgorithmFactory provide geocoding from sip to
 * congressional district.
 * @author Chin Hua Kong
 */
public class ZipToDistrictAlgorithmFactory implements AlgorithmFactory, ParameterMutator {

	public static final String ZIP_CODE_COLUMN_NAME = "zip_code_column";
	public static final String US_ZIP_CODE = "US Zip Code";
	private Mapper convertor;
	
	
	/*
	 * Initial activate for the subclass
	 */
	protected void activate(ComponentContext componentContext) {
		convertor = new ZipCodeToCongressionalDistrict(componentContext.getBundleContext());
	}

	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		
		LogService logger = (LogService) ciShellContext.getService(LogService.class.getName());
		Table originalInputTable = (Table) data[0].getData();
		String locationColumnName = (String) parameters.get(ZIP_CODE_COLUMN_NAME);

		return new ZipToDistrictAlgorithm(
			data,
			logger,
			originalInputTable,
			locationColumnName,
			convertor);
	}

	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		/*
		 * 
		 * User needs to select Zip Code Column to be converted.
		 *  
		 * Fill the 'Place Name Column' parameter drop-down box with column names 
		 * from the input table, so the user can choose which column of place names
		 * they want to create latitudes and longitudes for.
		 *  
		 *  Only columns of type 'String' are considered.
		 */
		
		Data inData = data[0];
		Table table = (Table) inData.getData();
		
		BasicObjectClassDefinition newParameters = new BasicObjectClassDefinition(
			oldParameters.getID(), oldParameters.getName(), oldParameters.getDescription(), null);
		AttributeDefinition[] oldAttributeDefinitions = oldParameters.getAttributeDefinitions(
			ObjectClassDefinition.ALL);

		String[] probablePlaceIdentifyingColumnList = 
			TableUtilities.getAllColumnNames(table.getSchema()).toArray(new String[0]);

		/*
		 * Make the drop down boxes appear for Place Type & Column Name.
		 * */
		for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
			
			/*
			 * For zip code column name.
			 * */
			if (oldAttributeDefinitionID.equals(ZIP_CODE_COLUMN_NAME)) {
				newParameters.addAttributeDefinition(
					ObjectClassDefinition.REQUIRED,
					new BasicAttributeDefinition(
						oldAttributeDefinition.getID(), 
						oldAttributeDefinition.getName(), 
						oldAttributeDefinition.getDescription(), 
						oldAttributeDefinition.getType(), 
						probablePlaceIdentifyingColumnList, 
						probablePlaceIdentifyingColumnList));
			} else {
				newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED, 
														oldAttributeDefinition);
			}
		}

		return newParameters;
	}
}