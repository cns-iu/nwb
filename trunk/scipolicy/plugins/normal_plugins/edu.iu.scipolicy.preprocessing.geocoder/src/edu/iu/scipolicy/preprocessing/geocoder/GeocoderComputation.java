package edu.iu.scipolicy.preprocessing.geocoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cishell.utilities.TableUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.scipolicy.preprocessing.geocoder.coders.CountryCoder;
import edu.iu.scipolicy.preprocessing.geocoder.coders.GeoLocation;
import edu.iu.scipolicy.preprocessing.geocoder.coders.StateCoder;
import edu.iu.scipolicy.preprocessing.geocoder.coders.ZipCoder;

/**
 * @author cdtank
 *
 */
public class GeocoderComputation {
	
	private LogService logger;

	private static final String[] LATITUDE_COLUMN_NAME_SUGGESTIONS = {"Latitude", "Lat"};
	private static final String[] LONGITUDE_COLUMN_NAME_SUGGESTIONS = {"Longitude", "Lon"};
	
	private static GeoLocation DEFAULT_NO_LOCATION_VALUE = new GeoLocation(null, null);
	
	private String locationType;
	private String locationColumnName;
	private Table originalTable, outputTable;

	private String outputTableLatitudeColumnName;

	private String outputTableLongitudeColumnName;

	public GeocoderComputation(String locationType, String locationColumnName,
			Table table, LogService logger) {
		
		this.locationType = locationType;
		this.locationColumnName = locationColumnName;
		this.originalTable = table;
		this.logger = logger;
		
		processTable();
	
	}

	/*
	 * Input data from the "Place_Column_Name" is obtained from the original table & Lookups 
	 * are made to appropriate maps. After processing all rows, the new output table is
	 * returned having original data and 2 new columns for Latitude & Longitude. 
	 * */
	private void processTable() {
		
		/*
		 * Create Blank new output table using the schema from the original table.
		 * */
		// TODO Replace with schema.instantiate() and test.
		outputTable = TableUtilities.createTableUsingSchema(originalTable.getSchema());
		outputTableLatitudeColumnName = TableUtilities.formNonConflictingNewColumnName(
			originalTable.getSchema(), LATITUDE_COLUMN_NAME_SUGGESTIONS);
		
		outputTableLongitudeColumnName = TableUtilities.formNonConflictingNewColumnName(
			originalTable.getSchema(), LONGITUDE_COLUMN_NAME_SUGGESTIONS);
		
		outputTable.addColumn(outputTableLatitudeColumnName, Double.class);
		outputTable.addColumn(outputTableLongitudeColumnName, Double.class);
		
		logger.log(LogService.LOG_INFO, "Latitude & Longitude values added to "
				+ outputTableLatitudeColumnName + " & " 
				+ outputTableLongitudeColumnName + " respectively.");
		
		Iterator locationColumnIterator = originalTable.iterator();
		
		Map<String, GeoLocation> fullFormToLocation = new HashMap<String, GeoLocation>();
		Map<String, String> abbreviationToFullForm = new HashMap<String, String>();
		
		/*
		 * Used to make selection of appropriate set of Maps for lookup - based on the 
		 * Place_Type input by the user.
		 * */
		if (locationType.equalsIgnoreCase(GeocoderAlgorithm.LOCATION_AS_STATE_IDENTIFIER)) {
			fullFormToLocation = StateCoder.getStateFullformToLocation();
			abbreviationToFullForm = StateCoder.getStateAbbreviationToFullform();
		} else if (locationType.equalsIgnoreCase(
				GeocoderAlgorithm.LOCATION_AS_COUNTRY_IDENTIFIER)) {
			fullFormToLocation = CountryCoder.getCountryFullformToLocation();
			abbreviationToFullForm = CountryCoder.getCountryAbbreviationToFullform();
		} else if (locationType.equalsIgnoreCase(
				GeocoderAlgorithm.LOCATION_AS_ZIPCODE_IDENTIFIER)) {
			fullFormToLocation = ZipCoder.getZipCodeToLocation();
		}
		
		
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
			
			GeoLocation cachedLocationValue = null;
			
			/*
			 * First make the lookup from the Full-form or complete name Map.
			 * */
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
					logger.log(LogService.LOG_WARNING, "No \"" 
							+ locationType + "\" location co-ordinates found. " 
							+ "The row with \"" + currentLocationKey 
							+ "\" location has not been given a latitude " 
							+ "or longitude.");
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
			outputTable.set(currentRowNumber, longitudeColumnNumber, cachedLocationValue.longitude);
		}
		
		
	}

	/**
	 * @return the outputTable
	 */
	public Table getOutputTable() {
		return outputTable;
	}
	
}
