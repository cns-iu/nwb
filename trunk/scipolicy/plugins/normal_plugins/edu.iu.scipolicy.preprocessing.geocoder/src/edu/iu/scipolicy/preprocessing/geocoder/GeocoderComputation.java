package edu.iu.scipolicy.preprocessing.geocoder;

import java.util.Iterator;
import java.util.Map;

import org.cishell.utilities.TableUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.scipolicy.preprocessing.geocoder.coders.GeoCoder;
import edu.iu.scipolicy.preprocessing.geocoder.coders.GeoLocation;

/**
 * @author cdtank
 *
 */
public class GeocoderComputation {
	public static final String[] LATITUDE_COLUMN_NAME_SUGGESTIONS = { "Latitude", "Lat" };
	public static final String[] LONGITUDE_COLUMN_NAME_SUGGESTIONS = { "Longitude", "Lon" };

	public static GeoLocation DEFAULT_NO_LOCATION_VALUE = new GeoLocation(null, null);

	/*
	 * Input data from the "Place_Column_Name" is obtained from the original table & Lookups 
	 * are made to appropriate maps. After processing all rows, the new output table is
	 * returned having original data and 2 new columns for Latitude & Longitude. 
	 * */
	public static Table compute(
			String locationColumnName,
			Table originalTable,
			LogService logger,
			GeoCoder geoCoder) {
		/*
		 * Create Blank new output table using the schema from the original table.
		 * */
		// TODO Replace with schema.instantiate() and test.
		Table outputTable = TableUtilities.createTableUsingSchema(originalTable.getSchema());
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
		
		Iterator locationColumnIterator = originalTable.iterator();
		
		Map<String, GeoLocation> fullFormToLocation = geoCoder.getFullFormsToLocations();
		Map<String, String> abbreviationToFullForm = geoCoder.getAbbreviationsToFullForms();

		while (locationColumnIterator.hasNext()) {
			int currentRowNumber = Integer.parseInt(locationColumnIterator.next().toString());
			int locationColumnNumber = 
				originalTable.getColumnNumber(locationColumnName);
			int latitudeColumnNumber =
				outputTable.getColumnNumber(outputTableLatitudeColumnName);
			int longitudeColumnNumber = 
				outputTable.getColumnNumber(outputTableLongitudeColumnName);
			String currentLocationKey = 
				originalTable.get(currentRowNumber, locationColumnNumber).toString(); 

			// TODO: Refactor this further.
			GeoLocation cachedLocationValue = null;

			String currentLocationKeyUppercase = currentLocationKey.toUpperCase();
			cachedLocationValue = fullFormToLocation.get(currentLocationKeyUppercase);
			
			/*
			 * If the lookup was unsuccessful due to no match try lookup in the abbreviation map. 
			 * */
			if (cachedLocationValue == null) {
				String fullformForAbbreviation = 
					abbreviationToFullForm.get(currentLocationKey.toUpperCase());

				/*
				 * If the lookup was successful,
				 * then the input string was an abbreviation of a state or country.
				 * Populate the co-ordinates by now making lookup from the recent lookup 
				 * i.e. use fullformForAbbreviation. 
				 * */
				if (fullformForAbbreviation != null) {
					cachedLocationValue = 
						fullFormToLocation.get(fullformForAbbreviation.toUpperCase());
				} else {
					/*
					 * The input string is not found either in full-form or abbreviations list.
					 * Assume default value for the co-ordinates. Warn user about it. 
					 * */
					cachedLocationValue = DEFAULT_NO_LOCATION_VALUE;
					String formatString =
						"No \"%s\" location coordinates found. The row with \"%s\" location has" +
						" not been given a latitude or longitude.";
					String logMessage = String.format(
						formatString, geoCoder.getLocationType(), currentLocationKey);
					logger.log(LogService.LOG_WARNING, logMessage);
//					logger.log(LogService.LOG_WARNING, "No \"" 
//							+ geoCoder.getLocationType() + "\" location co-ordinates found. " 
//							+ "The row with \"" + currentLocationKey 
//							+ "\" location has not been given a latitude " 
//							+ "or longitude.");
				}
			}

			/*
			 * Add the new row to the new table
			 * by copying the original row & then adding 2 new columns to it.
			 * */
			outputTable.addRow();
			TableUtilities.copyTableRow(
				currentRowNumber, currentRowNumber, outputTable, originalTable);
			outputTable.set(currentRowNumber, latitudeColumnNumber, cachedLocationValue.latitude);
			outputTable.set(
				currentRowNumber, longitudeColumnNumber, cachedLocationValue.longitude);
		}

		return outputTable;
	}
}
