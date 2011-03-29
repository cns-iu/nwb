package edu.iu.sci2.preprocessing.geocoder;

import java.util.Dictionary;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.sci2.preprocessing.geocoder.coders.Geocoder;

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
	private LogService logger;
	private Table originalInputTable;
	private String locationColumnName;
	private Geocoder geocoder;
	private Computation computation;
    
    public GeocoderAlgorithm(
    		Data[] data,
    		LogService logger,
    		Table originalInputTable,
    		String locationColumnName,
    		Geocoder geocoder,
    		Computation computation) {
        this.data = data;
		this.logger = logger;
		this.originalInputTable = originalInputTable;
		this.locationColumnName = locationColumnName;
		this.geocoder = geocoder;
		this.computation = computation;
	}

	public Data[] execute() {
		Table outputTable = computation.compute(
			this.locationColumnName, this.originalInputTable, this.logger, this.geocoder);
		
		/*
		 * After getting the output in table format make it available to the user.
		 * */
		Data output = new BasicData(outputTable, Table.class.getName());
		Dictionary<String, Object> metadata = output.getMetadata();
		metadata.put(
			DataProperty.LABEL,
			String.format("With Latitude & Longitude from '%s'", this.locationColumnName));
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		
		return new Data[] { output };
    }
}
