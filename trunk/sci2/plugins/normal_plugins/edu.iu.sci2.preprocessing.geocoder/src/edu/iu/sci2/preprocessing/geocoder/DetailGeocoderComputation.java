package edu.iu.sci2.preprocessing.geocoder;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.cishell.utilities.FrequencyMap;
import org.cishell.utilities.TableUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.sci2.model.geocode.GeoDetail;
import edu.iu.sci2.preprocessing.geocoder.coders.DetailGeocoder;
import edu.iu.sci2.preprocessing.geocoder.coders.GeoCoderException;
import edu.iu.sci2.preprocessing.geocoder.coders.Geocoder;

/**
 * 
 * This is the result computation for detail information
 * @author kongch
 *
 */
public final class DetailGeocoderComputation implements Computation {
	
	private static DetailGeocoderComputation instance;
	public static final String[] LATITUDE_COLUMN_NAME_SUGGESTIONS = { "Latitude" };
	public static final String[] LONGITUDE_COLUMN_NAME_SUGGESTIONS = { "Longitude" };
	public static final String[] ZIP_CODE_COLUMN_NAME_SUGGESTIONS = { "ZIP code" };
	public static final String[] COUNTY_COLUMN_NAME_SUGGESTIONS = { "County" };
	public static final String[] CITY_COLUMN_NAME_SUGGESTIONS = { "City" };
	public static final String[] STATE_COLUMN_NAME_SUGGESTIONS = { "State" };
	public static final String[] COUNTRY_COLUMN_NAME_SUGGESTIONS = { "Country" };
	public static final GeoDetail DEFAULT_GEODETAIL_VALUE = 
		new GeoDetail(null, null, null, null, null, null, null);

	private DetailGeocoderComputation() {
	}
	
	public static DetailGeocoderComputation getInstance() {
		if (instance == null) {
			instance = new DetailGeocoderComputation(); 
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
		
		DetailGeocoder detailGeocoder;
		try {
			detailGeocoder = DetailGeocoder.class.cast(geocoder);
		} catch (ClassCastException  e) {
			logger.log(LogService.LOG_WARNING,
					String.format(
							"The %s class does not support detail geocoding",
							geocoder.getClass().getName()));
			throw e;
		}
		
		/*
		 * Create Blank new output table using the schema from the original table.
		 */
		Table outputTable = originalTable.getSchema().instantiate();
		String outputTableLatitudeColumnName = TableUtilities.formNonConflictingNewColumnName(
			originalTable.getSchema(), LATITUDE_COLUMN_NAME_SUGGESTIONS);
		String outputTableLongitudeColumnName = TableUtilities.formNonConflictingNewColumnName(
			originalTable.getSchema(), LONGITUDE_COLUMN_NAME_SUGGESTIONS);
		String outputTableZipCodeColumnName = TableUtilities.formNonConflictingNewColumnName(
			originalTable.getSchema(), ZIP_CODE_COLUMN_NAME_SUGGESTIONS);
		String outputTableCountyColumnName = TableUtilities.formNonConflictingNewColumnName(
			originalTable.getSchema(), COUNTY_COLUMN_NAME_SUGGESTIONS);
		String outputTableCityColumnName = TableUtilities.formNonConflictingNewColumnName(
			originalTable.getSchema(), CITY_COLUMN_NAME_SUGGESTIONS);
		String outputTableStateColumnName = TableUtilities.formNonConflictingNewColumnName(
			originalTable.getSchema(), STATE_COLUMN_NAME_SUGGESTIONS);
		String outputTableCountryColumnName = TableUtilities.formNonConflictingNewColumnName(
			originalTable.getSchema(), COUNTRY_COLUMN_NAME_SUGGESTIONS);

		outputTable.addColumn(outputTableLatitudeColumnName, Double.class);
		outputTable.addColumn(outputTableLongitudeColumnName, Double.class);
		outputTable.addColumn(outputTableZipCodeColumnName, String.class);
		outputTable.addColumn(outputTableCountyColumnName, String.class);
		outputTable.addColumn(outputTableCityColumnName, String.class);
		outputTable.addColumn(outputTableStateColumnName, String.class);
		outputTable.addColumn(outputTableCountryColumnName, String.class);

		logger.log(
			LogService.LOG_INFO,
			String.format(
				"Values are added to %s, %s, %s, %s, %s ,%s & %s, respectively.",
				outputTableLatitudeColumnName, 
				outputTableLongitudeColumnName,
				outputTableZipCodeColumnName,
				outputTableCountyColumnName,
				outputTableCityColumnName,
				outputTableStateColumnName,
				outputTableCountryColumnName));
		
		int locationColumnNumber = originalTable.getColumnNumber(locationColumnName);
		int latitudeColumnNumber = outputTable.getColumnNumber(outputTableLatitudeColumnName);
		int longitudeColumnNumber = outputTable.getColumnNumber(outputTableLongitudeColumnName);
		int zipCodeColumnNumber = outputTable.getColumnNumber(outputTableZipCodeColumnName);
		int streetColumnNumber = outputTable.getColumnNumber(outputTableCountyColumnName);
		int cityColumnNumber = outputTable.getColumnNumber(outputTableCityColumnName);
		int stateColumnNumber = outputTable.getColumnNumber(outputTableStateColumnName);
		int countryColumnNumber = outputTable.getColumnNumber(outputTableCountryColumnName);
		Map <String, GeoDetail> geocodedAddressToGeoDetail = new HashMap<String, GeoDetail>();
		FrequencyMap<String> failedFrequency = new FrequencyMap<String>(true);
		Iterator<?> locationColumnIterator = originalTable.iterator();
		while (locationColumnIterator.hasNext()) {
			int currentRowNumber = Integer.parseInt(locationColumnIterator.next().toString());
			
			/* Start geocoding */
			GeoDetail geodetail = DEFAULT_GEODETAIL_VALUE;
			String currentLocation = "";
			Object currentLocationObject = originalTable.get(currentRowNumber, 
																locationColumnNumber);
			if (currentLocationObject != null) {
				currentLocation = currentLocationObject.toString();
				String currentLocationUppercase = currentLocation.toUpperCase();
				
				/* Avoid re-geocoding the same place */
				if (geocodedAddressToGeoDetail.containsKey(currentLocationUppercase)) {
					geodetail = geocodedAddressToGeoDetail.get(currentLocationUppercase);
					if (geodetail == DEFAULT_GEODETAIL_VALUE) {
						failedFrequency.add(currentLocation);
					}
				} else {
					try {
						geodetail = detailGeocoder.geocodingLocation(currentLocationUppercase);
					} catch (GeoCoderException e) {
						failedFrequency.add(currentLocation);
					}
					
					/* Add to geocoded map */
					geocodedAddressToGeoDetail.put(currentLocationUppercase, geodetail);
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
			outputTable.set(currentRowNumber, 
							latitudeColumnNumber, 
							geodetail.getGeolocation().getLatitude());
			outputTable.set(currentRowNumber, 
							longitudeColumnNumber, 
							geodetail.getGeolocation().getLongitude());
			outputTable.set(currentRowNumber, 
							zipCodeColumnNumber, 
							geodetail.getZipCode().toString());
			outputTable.set(currentRowNumber, 
							streetColumnNumber, 
							geodetail.getCounty());
			outputTable.set(currentRowNumber, 
							cityColumnNumber, 
							geodetail.getCity());
			outputTable.set(currentRowNumber, 
							stateColumnNumber, 
							geodetail.getState());
			outputTable.set(currentRowNumber, 
							countryColumnNumber, 
							geodetail.getCountry());
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
