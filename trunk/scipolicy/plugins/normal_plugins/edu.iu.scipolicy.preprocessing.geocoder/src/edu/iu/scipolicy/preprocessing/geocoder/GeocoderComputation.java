package edu.iu.scipolicy.preprocessing.geocoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.scipolicy.utilities.TableUtilities;

/**
 * @author cdtank
 *
 */
public class GeocoderComputation {
	
	private LogService logger;

	private static final String LONGITUDE_COLUMN_NAME = "Longitude";
	private static final String LATITUDE_COLUMN_NAME = "Latitude";
	
	private static List<Double> DEFAULT_NO_LOCATION_VALUE = Arrays.asList(0.0, 0.0);
	
	private String locationType;
	private String locationColumnName;
	private Table originalTable, outputTable;

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
		outputTable = TableUtilities.createTableUsingSchema(originalTable.getSchema());
		
		outputTable.addColumn(LATITUDE_COLUMN_NAME, Double.class);
		outputTable.addColumn(LONGITUDE_COLUMN_NAME, Double.class);
		
		Iterator locationColumnIterator = originalTable.iterator();
		
		Map<String,List<Double>> fullFormToLocationDictionary = new HashMap<String, List<Double>>();
		Map<String,String> abbreviationToFullFormDictionary = new HashMap<String, String>();
		
		/*
		 * Used to make selection of appropriate set of Maps for lookup - based on the 
		 * Place_Type input by the user.
		 * */
		if(locationType.equalsIgnoreCase(GeocoderAlgorithm.LOCATION_AS_STATE_IDENTIFIER)) {
			fullFormToLocationDictionary = GeocoderDictionaries.getStateFullformToLocation();
			abbreviationToFullFormDictionary = GeocoderDictionaries.getStateAbbreviationToFullform();
		}
		else {
			fullFormToLocationDictionary = GeocoderDictionaries.getCountryFullformToLocation();
			abbreviationToFullFormDictionary = GeocoderDictionaries.getCountryAbbreviationToFullform();
		}
		
		
		while(locationColumnIterator.hasNext()){
			
			int currentRowNumber = Integer.parseInt(locationColumnIterator.next().toString());
			int locationColumnNumber = originalTable.getColumnNumber(locationColumnName);
			int latitudeColumnNumber = outputTable.getColumnNumber(LATITUDE_COLUMN_NAME);
			int longitudeColumnNumber = outputTable.getColumnNumber(LONGITUDE_COLUMN_NAME);
			String currentLocationKey = originalTable.get(currentRowNumber, locationColumnNumber).toString(); 
			
			List<Double> cachedLocationValue = new ArrayList<Double>();
			
			/*
			 * First make the lookup from the Full-form or complete name Map.
			 * */
			cachedLocationValue = fullFormToLocationDictionary.get(currentLocationKey.toUpperCase());
			
			/*
			 * If the lookup was unsuccessful due to no match try lookup in the abbreviation map. 
			 * */
			if(cachedLocationValue == null) {
				
				String fullformForAbbreviation = abbreviationToFullFormDictionary.get(currentLocationKey.toUpperCase());

				/*
				 * If the lookup was successful then the input string was an abbreviation of a state or country.
				 * Populate the co-ordinates by now making lookup from the recent lookup i.e. use fullformForAbbreviation. 
				 * */
				if(fullformForAbbreviation != null) {
					cachedLocationValue = fullFormToLocationDictionary.get(fullformForAbbreviation.toUpperCase());
				}
				
				/*
				 * The input string is not found either in full-form or abbreviations list. Assume default value
				 * for the co-ordinates. Warn user about it. 
				 * */
				else {
					cachedLocationValue = DEFAULT_NO_LOCATION_VALUE;
					logger.log(LogService.LOG_WARNING, "No \"" + locationType + "\" location co-ordinates found for \"" + currentLocationKey 
							+ "\". Default value assumed for (Latitide, Longitude) as (" + DEFAULT_NO_LOCATION_VALUE.get(0) 
							+ ", " + DEFAULT_NO_LOCATION_VALUE.get(1) + ")");
				}
				
			}
			
			/*
			 * Add the new row to the new table by copying the original row & then adding 2 new columns to it.
			 * */
			outputTable.addRow();
			TableUtilities.copyTableRow(currentRowNumber, currentRowNumber, outputTable, originalTable);
			outputTable.set(currentRowNumber, latitudeColumnNumber, cachedLocationValue.get(0));
			outputTable.set(currentRowNumber, longitudeColumnNumber, cachedLocationValue.get(1));
		}
		
		
	}

	/**
	 * @return the outputTable
	 */
	public Table getOutputTable() {
		return outputTable;
	}
	
	
	
	

}
