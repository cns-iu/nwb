package edu.iu.sci2.preprocessing.geocoder;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.cishell.utilities.FrequencyMap;
import org.cishell.utilities.TableUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.sci2.model.geocode.Geolocation;
import edu.iu.sci2.preprocessing.geocoder.coders.GeoCoderException;
import edu.iu.sci2.preprocessing.geocoder.coders.Geocoder;

/**
 * @author cdtank
 *
 */
public final class GeocoderComputation implements Computation {
	
	private static GeocoderComputation instance;
	public static final String[] LATITUDE_COLUMN_NAME_SUGGESTIONS = { "Latitude", "Lat" };
	public static final String[] LONGITUDE_COLUMN_NAME_SUGGESTIONS = { "Longitude", "Lon" };
	public static final Geolocation DEFAULT_NO_LOCATION_VALUE = new Geolocation(null, null);

	private GeocoderComputation() {
	}
	
	public static GeocoderComputation getInstance() {
		if (instance == null) {
			instance = new GeocoderComputation(); 
		}
		
		return instance;
	}
	
	/*
	 * Input data from the "Place_Column_Name" is obtained from the original table & Lookups 
	 * are made to appropriate maps. After processing all rows, the new output table is
	 * returned having original data and 2 new columns for Latitude & Longitude. 
	 */
	public Table compute(
			String locationColumnName,
			Table originalTable,
			LogService logger,
			Geocoder geocoder) {
		/*
		 * Create Blank new output table using the schema from the original table.
		 */
		Table outputTable = originalTable.getSchema().instantiate();
		String outputTableLatitudeColumnName = TableUtilities.formNonConflictingNewColumnName(
			originalTable.getSchema(), LATITUDE_COLUMN_NAME_SUGGESTIONS);
		String outputTableLongitudeColumnName = TableUtilities.formNonConflictingNewColumnName(
			originalTable.getSchema(), LONGITUDE_COLUMN_NAME_SUGGESTIONS);

		outputTable.addColumn(outputTableLatitudeColumnName, Double.class);
		outputTable.addColumn(outputTableLongitudeColumnName, Double.class);

		logger.log(
			LogService.LOG_INFO,
			String.format(
				"Latitude & Longitude values added to %s & %s, respectively.",
				outputTableLatitudeColumnName, 
				outputTableLongitudeColumnName));
		
		int locationColumnNumber = originalTable.getColumnNumber(locationColumnName);
		int latitudeColumnNumber = outputTable.getColumnNumber(outputTableLatitudeColumnName);
		int longitudeColumnNumber = outputTable.getColumnNumber(outputTableLongitudeColumnName);
		Map <String, Geolocation> geocodedAddressToGeoLocation = new HashMap<String, Geolocation>();
		FrequencyMap<String> failedFrequency = new FrequencyMap<String>(true);
		Iterator<?> locationColumnIterator = originalTable.iterator();
		while (locationColumnIterator.hasNext()) {
			int currentRowNumber = Integer.parseInt(locationColumnIterator.next().toString());
			
			/* Start geocoding */
			Geolocation geolocation = DEFAULT_NO_LOCATION_VALUE;
			String currentLocation = "";
			Object currentLocationObject = originalTable.get(currentRowNumber, 
																locationColumnNumber);
			if (currentLocationObject != null) {
				currentLocation = currentLocationObject.toString();
				String currentLocationUppercase = currentLocation.toUpperCase();
	
				/* Avoid re-geocoding the same place */
				if (geocodedAddressToGeoLocation.containsKey(currentLocationUppercase)) {
					geolocation = geocodedAddressToGeoLocation.get(currentLocationUppercase);
					if (geolocation == DEFAULT_NO_LOCATION_VALUE) {
						failedFrequency.add(currentLocation);
					}
				} else {
					try {
						geolocation = geocoder.geocodingFullForm(currentLocationUppercase);
					} catch (GeoCoderException e) {
						try {
							/* Try lookup in the abbreviation */
							geolocation = geocoder.geocodingAbbreviation(currentLocationUppercase);
						} catch (GeoCoderException e1) {
							/* No result is found */
							failedFrequency.add(currentLocation);
						}
					}
					
					/* Add to geocoded map */
					geocodedAddressToGeoLocation.put(currentLocationUppercase, geolocation);
				}
			} else {
				failedFrequency.add(currentLocation);
			}

			/*
			 * Add the new row to the new table
			 * by copying the original row & then adding 2 new columns to it.
			 */
			outputTable.addRow();
			TableUtilities.copyTableRow(
				currentRowNumber, currentRowNumber, outputTable, originalTable);
			outputTable.set(currentRowNumber, latitudeColumnNumber, geolocation.getLatitude());
			outputTable.set(
				currentRowNumber, longitudeColumnNumber, geolocation.getLongitude());
		}

		/* Warning user about failure */
		if (!failedFrequency.isEmpty()) {
			printWarningMessage(logger, locationColumnName, failedFrequency);
		}
		
		/* Show statistic information */
		int totalRow = originalTable.getRowCount();
		NumberFormat numberFormat = NumberFormat.getInstance();
		logger.log(LogService.LOG_INFO, String.format(
				"Successfully geocoded %s out of %s locations to geographic coordinates", 
				numberFormat.format(totalRow - failedFrequency.sum()),
				numberFormat.format(totalRow)));
		return outputTable;
	}
	
	private static void printWarningMessage(LogService logger, String locationColumnName, 
													FrequencyMap<String> failedFrequency) {

		for (String location : failedFrequency.keySet()) {
			String formatString =
			"No geographic coordinate found for location \"%s\" (from column  \"%s\")."
				+ " %d rows had this location.";
			
			logger.log(LogService.LOG_WARNING, String.format(formatString, 
											location, 
											locationColumnName,
											failedFrequency.getFrequency(location)
											));
		}
	}
}
