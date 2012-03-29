package edu.iu.sci2.preprocessing.geocoder;

import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.sci2.preprocessing.geocoder.coders.Geocoder;

public interface Computation {
	/**
	 * Compute is a method that create new columns on existing table,
	 * compute the results and add the results to the related columns.
	 * @param locationColumnName - Title of the location column
	 * @param originalTable - The original input table from CSV
	 * @param logger - Logging object
	 * @param geocoder - Geocoder object to be used, it could be 
	 * 					 YahooGeocoder or GenericGecoder
	 * @return Return the new table that contains the results
	 */
	public Table compute(
			String locationColumnName,
			Table originalTable,
			LogService logger,
			Geocoder geocoder);
}
