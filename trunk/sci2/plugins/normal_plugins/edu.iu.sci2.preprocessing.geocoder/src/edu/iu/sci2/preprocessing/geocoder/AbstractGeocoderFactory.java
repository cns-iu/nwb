package edu.iu.sci2.preprocessing.geocoder;

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

import prefuse.data.Table;
import edu.iu.sci2.preprocessing.geocoder.coders.FamilyOfGeocoders;
import edu.iu.sci2.preprocessing.geocoder.coders.Geocoder;
import edu.iu.sci2.preprocessing.geocoder.coders.bing.BingFamilyOfGeocoder;
import edu.iu.sci2.preprocessing.geocoder.coders.yahoo.YahooFamilyOfGeocoder;

/**
 * AbstractGeocoderFactory is re-factor from GeocoderFactory. This allow 
 * new geocoder plugin to reuse the same factory code. The subclass need
 * only to override three methods (they are activate, getPlaceTypeOptionLabels 
 * and getPlaceTypeOptionValues)
 * 
 * @author Chintan Tank
 * @modified Chin Hua Kong
 */
public abstract class AbstractGeocoderFactory implements AlgorithmFactory, ParameterMutator {

	public static final String PLACE_NAME_COLUMN = "place_name_column";
	public static final String PLACE_TYPE = "place_type";
	public static final String YAHOO_APPLICATION_ID = "yahoo_application_id";
	public static final String BING_APPLICATION_ID = "bing_application_id";
	public static final String ENABLE_DETAIL = "details";
	
	public static final String ADDRESS = "Address";
	public static final String COUNTRY = "Country";
	public static final String US_STATE = "US State";
	public static final String US_ZIP_CODE = "US Zip Code";

	private FamilyOfGeocoders familyGeocoder;
	
	/*
	 * Initial activate for the subclass
	 */
	protected abstract void activate(ComponentContext componentContext);
	
	/*
	 * Supported labels for the subclass
	 */
	protected abstract String[] getPlaceTypeOptionLabels();
	
	/*
	 * Supported labels' value for the subclass
	 * Note: Must in the same order of the labels
	 */
	protected abstract String[] getPlaceTypeOptionValues();

	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		
		Computation computation = GeocoderComputation.getInstance();
		if (FamilyOfGeocoders.FAMILY_TYPE.Yahoo.equals(this.familyGeocoder.getFamilyType())) {
			YahooFamilyOfGeocoder.class.cast(familyGeocoder).
			setApplicationId((String) parameters.get(YAHOO_APPLICATION_ID));

			/* If the user request for detail from the geocoding result */
			if ((Boolean.class.cast(parameters.get(ENABLE_DETAIL)))) {
				computation = DetailGeocoderComputation.getInstance();
			}
		}
		if (FamilyOfGeocoders.FAMILY_TYPE.Bing.equals(this.familyGeocoder.getFamilyType())) {
			BingFamilyOfGeocoder.class.cast(familyGeocoder).
			setApplicationId((String) parameters.get(BING_APPLICATION_ID));

			/* If the user request for detail from the geocoding result */
			if ((Boolean.class.cast(parameters.get(ENABLE_DETAIL)))) {
				computation = DetailGeocoderComputation.getInstance();
			}
		}
		LogService logger = (LogService) ciShellContext.getService(LogService.class.getName());
		Table originalInputTable = (Table) data[0].getData();
		String locationColumnName = (String) parameters.get(PLACE_NAME_COLUMN);
		Geocoder geocoder = determineGeoCoder((String) parameters.get(PLACE_TYPE));

		return new GeocoderAlgorithm(
			data,
			logger,
			originalInputTable,
			locationColumnName,
			geocoder,
			computation);
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
		String[] placeTypeOptionLabels = this.getPlaceTypeOptionLabels();
		String[] placeTypeOptionValues = this.getPlaceTypeOptionValues();

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
			} else if (oldAttributeDefinitionID.equals(PLACE_TYPE)) {
				/*
				 * For place type.
				 */
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

	private Geocoder determineGeoCoder(String placeType) {
		if (placeType.equalsIgnoreCase(ADDRESS)) {
			return this.familyGeocoder.getAddressCoder();
		} else if (placeType.equalsIgnoreCase(COUNTRY)) {
			return this.familyGeocoder.getCountryCoder();
		} else if (placeType.equalsIgnoreCase(US_STATE)) {
			return this.familyGeocoder.getStateCoder();
		} else if (placeType.equalsIgnoreCase(US_ZIP_CODE)) {
			return this.familyGeocoder.getZipCodeCoder();
		} else {
			return null;
		}
	}

	protected void setFamilyGeocoder(FamilyOfGeocoders familyGeocoder) {
		this.familyGeocoder = familyGeocoder;
	}
}