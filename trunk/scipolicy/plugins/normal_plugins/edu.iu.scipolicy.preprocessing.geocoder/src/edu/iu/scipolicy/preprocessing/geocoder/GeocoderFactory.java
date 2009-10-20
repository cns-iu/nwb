package edu.iu.scipolicy.preprocessing.geocoder;

import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.TableUtilities;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import edu.iu.scipolicy.preprocessing.geocoder.coders.CountryCoder;
import edu.iu.scipolicy.preprocessing.geocoder.coders.StateCoder;
import edu.iu.scipolicy.preprocessing.geocoder.coders.ZipCoder;

public class GeocoderFactory implements AlgorithmFactory, ParameterMutator {
	
	URL country_GeoCodePath, state_GeoCodePath, zipCode_GeoCodePath;
	
	private static final String country_GeoCodeFilePath = "countries_geo_code.txt";
	private static final String state_GeoCodeFilePath = "us_states_geo_code.txt";
	private static final String zipCode_GeoCodeFilePath = "us_zipcode_geo_code.csv";

	/*
	 * Fetch the Latitude & Longitude values from the text files in to appropriate Maps.
	 * This will be done only once.
	 * */
	protected void activate(ComponentContext ctxt) {
    	BundleContext bContext = ctxt.getBundleContext();
    	
    	this.state_GeoCodePath = bContext.getBundle().getResource(state_GeoCodeFilePath);
    	StateCoder.setStateFile(state_GeoCodePath);
    	
    	this.country_GeoCodePath = bContext.getBundle().getResource(country_GeoCodeFilePath);
    	CountryCoder.setCountryFile(country_GeoCodePath);
    	
    	this.zipCode_GeoCodePath = bContext.getBundle().getResource(zipCode_GeoCodeFilePath);
    	ZipCoder.setZipCodeFile(zipCode_GeoCodePath);
    }
	
	@SuppressWarnings("unchecked") // Raw Dictionary
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {

		return new GeocoderAlgorithm(data, parameters, context);
	}

	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		/*
		 * 
		 * User needs to select the place type as Country, State, or Zip Code..
		 *  
		 * Fill the 'Place Name Column' parameter drop-down box with column names 
		 * from the input table, so the user can choose which column of place names
		 * they want to create latitudes and longitudes for.
		 *  
		 *  Only columns of type 'String' are considered.
		 */
		
		Data inData = data[0];
		Table table = (Table) inData.getData();
		
		BasicObjectClassDefinition newParameters = 
			new BasicObjectClassDefinition(oldParameters.getID(), oldParameters
					.getName(), oldParameters.getDescription(), null);


		AttributeDefinition[] oldAttributeDefinitions = oldParameters
				.getAttributeDefinitions(ObjectClassDefinition.ALL);
		
		String[] probablePlaceIdentifyingColumnList = 
			TableUtilities.getValidStringColumnNamesInTable(table);
		String[] placeTypeOptionLabels = {"State", "Country", "Zip Code"};
		String[] placeTypeOptionValues = {GeocoderAlgorithm.LOCATION_AS_STATE_IDENTIFIER,
				GeocoderAlgorithm.LOCATION_AS_COUNTRY_IDENTIFIER,
				GeocoderAlgorithm.LOCATION_AS_ZIPCODE_IDENTIFIER};

		/*
		 * Make the drop down boxes appear for Place Type & Column Name.
		 * */
		for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
			
			/*
			 * For place column name.
			 * */
			if (oldAttributeDefinitionID.equals(GeocoderAlgorithm.PLACE_NAME_COLUMN)) {
				newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(oldAttributeDefinition.getID(), 
								oldAttributeDefinition.getName(), 
								oldAttributeDefinition.getDescription(), 
								oldAttributeDefinition.getType(), 
								probablePlaceIdentifyingColumnList, 
								probablePlaceIdentifyingColumnList));
			}
			/*
			 * For place type.
			 * */
			else if (oldAttributeDefinitionID.equals(GeocoderAlgorithm.PLACE_TYPE)) {
				newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(oldAttributeDefinition.getID(), 
								oldAttributeDefinition.getName(), 
								oldAttributeDefinition.getDescription(), 
								oldAttributeDefinition.getType(), 
								placeTypeOptionLabels, 
								placeTypeOptionValues));
			} else {
				newParameters.addAttributeDefinition(
						ObjectClassDefinition.REQUIRED, oldAttributeDefinition);
			}
		}
		return newParameters;
	}
	
}