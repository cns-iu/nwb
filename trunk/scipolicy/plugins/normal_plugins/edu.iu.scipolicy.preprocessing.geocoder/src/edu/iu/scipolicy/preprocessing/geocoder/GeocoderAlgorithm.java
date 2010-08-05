package edu.iu.scipolicy.preprocessing.geocoder;

import java.util.Dictionary;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.scipolicy.preprocessing.geocoder.coders.GeoCoder;

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
	private GeoCoder geoCoder;
    
    public GeocoderAlgorithm(
    		Data[] data,
    		LogService logger,
    		Table originalInputTable,
    		String locationColumnName,
    		GeoCoder geoCoder) {
        this.data = data;
		this.logger = logger;
		this.originalInputTable = originalInputTable;
		this.locationColumnName = locationColumnName;
		this.geoCoder = geoCoder;
	}

	public Data[] execute() {
		Table outputTable = GeocoderComputation.compute(
			this.locationColumnName, this.originalInputTable, this.logger, this.geoCoder);
		
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
