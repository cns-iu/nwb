package edu.iu.scipolicy.preprocessing.geocoder;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;

/**
 * This algorithm converts the place information provided into Latitude, Longitude co-ordinates. 
 * Currently it accepts States from United States of America and Countries (as published by the 
 * International Organization for Standardization). As per the current algorithm, mappings from 
 * a location to its co-ordinates are saved in 2 lists (modifiable) - one for States of USA & 
 * Other for Countries. A lookup is made from the value provided in the data provided by the user 
 * to this list. The data for states can either have 2 letter abbreviation or the complete name. 
 * The data for countries can either have 2 letter abbreviation as per ISO 3166-1 alpha-2 standard
 * found at <a href="http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">Link</a> or the complete name.
 * The output of this algorithm will be the original table with 2 columns added, one each for 
 * Latitude & Longitude.  
 * @author cdtank
 *
 */

public class GeocoderAlgorithm implements Algorithm {
	private Data[] data;
    private Dictionary<String, Object> parameters;
	private LogService logger;
	
	public static final String LOCATION_AS_STATE_IDENTIFIER = "STATE";
	public static final String LOCATION_AS_COUNTRY_IDENTIFIER = "COUNTRY";
	public static final String LOCATION_AS_ZIPCODE_IDENTIFIER = "ZIP CODE";

    public static final String PLACE_NAME_COLUMN = "place_name_column";
    public static final String PLACE_TYPE = "place_type";
    
    
    public GeocoderAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {    	
        this.data = data;
        this.parameters = parameters;
        
		this.logger = (LogService) context.getService(LogService.class.getName());
	}

    @SuppressWarnings("unchecked") // Raw Dictionary
	public Data[] execute() {
		String locationType = (String)parameters.get(PLACE_TYPE);
		String locationColumnName = (String)parameters.get(PLACE_NAME_COLUMN);
		
		Table originalInputTable = (Table) this.data[0].getData();
		
		/*
		 * After getting the Prefuse Table data pass it for making lookups & getting co-ordinates.
		 * */
		GeocoderComputation geoCoderComputation = new GeocoderComputation(
				locationType, locationColumnName,
				originalInputTable, logger);
		
		/*
		 * After getting the output in table format make it available to the user.
		 * */
		Data output = new BasicData(geoCoderComputation.getOutputTable(), Table.class.getName());
		Dictionary metadata = output.getMetadata();
		metadata.put(DataProperty.LABEL, "With Latitude & Longitude from '" 
			+ locationColumnName + "'");
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		
		return new Data[]{ output };
    }
}