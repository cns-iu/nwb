package edu.iu.scipolicy.preprocessing.geocoder;

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
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import edu.iu.scipolicy.preprocessing.geocoder.coders.CountryCoder;
import edu.iu.scipolicy.preprocessing.geocoder.coders.GeoCoder;
import edu.iu.scipolicy.preprocessing.geocoder.coders.StateCoder;
import edu.iu.scipolicy.preprocessing.geocoder.coders.ZipCodeCoder;

public class GeocoderFactory implements AlgorithmFactory, ParameterMutator {
	private static final String COUNTRY_GEO_CODE_FILE_PATH = "countries_geo_code.txt";
	private static final String STATE_GEO_CODE_FILE_PATH = "us_states_geo_code.txt";
	private static final String ZIP_CODE_GEO_CODE_FILE_PATH = "us_zipcode_geo_code.csv";

	public static final String PLACE_NAME_COLUMN = "place_name_column";
	public static final String PLACE_TYPE = "place_type";

	private StateCoder stateCoder;
	private CountryCoder countryCoder;
	private ZipCodeCoder zipCodeCoder;

	/*
	 * Fetch the Latitude & Longitude values from the text files in to appropriate Maps.
	 * This will be done only once.
	 * */
	protected void activate(ComponentContext componentContext) {
    	BundleContext bundleContext = componentContext.getBundleContext();

    	this.stateCoder =
    		new StateCoder(bundleContext.getBundle().getResource(STATE_GEO_CODE_FILE_PATH));
    	this.countryCoder = new CountryCoder(
    		bundleContext.getBundle().getResource(COUNTRY_GEO_CODE_FILE_PATH));
    	this.zipCodeCoder = new ZipCodeCoder(
    		bundleContext.getBundle().getResource(ZIP_CODE_GEO_CODE_FILE_PATH));
    }

	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		LogService logger = (LogService) ciShellContext.getService(LogService.class.getName());
		Table originalInputTable = (Table) data[0].getData();
		String locationColumnName = (String) parameters.get(PLACE_NAME_COLUMN);
		GeoCoder geoCoder = determineGeoCoder((String) parameters.get(PLACE_TYPE));

		return new GeocoderAlgorithm(
			data,
			logger,
			originalInputTable,
			locationColumnName,
			geoCoder);
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
		
		BasicObjectClassDefinition newParameters = new BasicObjectClassDefinition(
			oldParameters.getID(), oldParameters.getName(), oldParameters.getDescription(), null);
		AttributeDefinition[] oldAttributeDefinitions = oldParameters.getAttributeDefinitions(
			ObjectClassDefinition.ALL);

		String[] probablePlaceIdentifyingColumnList = 
			TableUtilities.getAllColumnNames(table.getSchema()).toArray(new String[0]);
		String[] placeTypeOptionLabels = { "State", "Country", "Zip Code" };
		String[] placeTypeOptionValues = {
			StateCoder.LOCATION_AS_STATE_IDENTIFIER,
			CountryCoder.LOCATION_AS_COUNTRY_IDENTIFIER,
			ZipCodeCoder.LOCATION_AS_ZIPCODE_IDENTIFIER
		};

		/*
		 * Make the drop down boxes appear for Place Type & Column Name.
		 * */
		for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
			
			/*
			 * For place column name.
			 * */
			if (oldAttributeDefinitionID.equals(PLACE_NAME_COLUMN)) {
				newParameters.addAttributeDefinition(
					ObjectClassDefinition.REQUIRED,
					new BasicAttributeDefinition(
						oldAttributeDefinition.getID(), 
						oldAttributeDefinition.getName(), 
						oldAttributeDefinition.getDescription(), 
						oldAttributeDefinition.getType(), 
						probablePlaceIdentifyingColumnList, 
						probablePlaceIdentifyingColumnList));
			}
			/*
			 * For place type.
			 * */
			else if (oldAttributeDefinitionID.equals(PLACE_TYPE)) {
				newParameters.addAttributeDefinition(
					ObjectClassDefinition.REQUIRED,
					new BasicAttributeDefinition(
						oldAttributeDefinition.getID(), 
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

	private GeoCoder determineGeoCoder(String placeType) {
		if (placeType.equalsIgnoreCase(StateCoder.LOCATION_AS_STATE_IDENTIFIER)) {
			return this.stateCoder;
		} else if (placeType.equalsIgnoreCase(CountryCoder.LOCATION_AS_COUNTRY_IDENTIFIER)) {
			return this.countryCoder;
		} else if (placeType.equalsIgnoreCase(ZipCodeCoder.LOCATION_AS_ZIPCODE_IDENTIFIER)) {
			return this.zipCodeCoder;
		} else {
			return null;
		}
	}
}