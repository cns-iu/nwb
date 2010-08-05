package edu.iu.scipolicy.preprocessing.geocoder.coders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class StateCoder implements GeoCoder {
	public static final String LOCATION_AS_STATE_IDENTIFIER = "STATE";

	public static final int CACHE_ROW_ABBREVIATION_INDEX = 1;
	public static final int CACHE_ROW_FULLFORM_INDEX = 0;
	public static final int CACHE_ROW_LONGITUDE_INDEX = 3;
	public static final int CACHE_ROW_LATITUDE_INDEX = 2;

	private Map<String, GeoLocation> stateFullFormToLocation = null;
	private Map<String, String> stateAbbreviationToFullForm = null;
	
	public StateCoder(URL stateFile) {
		initializeStateLocationMappings(stateFile);
	}

	public String getLocationType() {
		return LOCATION_AS_STATE_IDENTIFIER;
	}

	public Map<String, GeoLocation> getFullFormsToLocations() {
		return this.stateFullFormToLocation;
	}

	public Map<String, String> getAbbreviationsToFullForms() {
		return this.stateAbbreviationToFullForm;
	}
	
	/*
	 * Initialize the Map for State from the text file.
	 * */
	private void initializeStateLocationMappings(URL stateLocationFilePath) {
		this.stateFullFormToLocation = new HashMap<String, GeoLocation>();
		this.stateAbbreviationToFullForm = new HashMap<String, String>();
		
    	InputStream inStream = null;
    	BufferedReader input = null;
    	String line;

    	try {
             URLConnection connection = stateLocationFilePath.openConnection();
             connection.setDoInput(true);
             inStream = connection.getInputStream();
             input = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
    		    		
    	    while (null != (line = input.readLine())) {
    	    	String[] lineTokens = line.split(";");
    	    	
    	    	double latitude = Double.parseDouble(lineTokens[CACHE_ROW_LATITUDE_INDEX]);
    	    	double longitude = Double.parseDouble(lineTokens[CACHE_ROW_LONGITUDE_INDEX]);
    	    	
    	    	GeoLocation location = new GeoLocation(latitude, longitude);
    	    	
    	    	/*
    	    	 * Map with Full Form as the Key and List of Latitude, Longitude as the Value.
    	    	 * */
    	    	this.stateFullFormToLocation.put(lineTokens[CACHE_ROW_FULLFORM_INDEX], location);
    	    	
       	    	/*
    	    	 * Map with Abbreviation as the Key and Full Form as the value.
    	    	 * */
    	    	this.stateAbbreviationToFullForm.put(
	    			lineTokens[CACHE_ROW_ABBREVIATION_INDEX],
	    			lineTokens[CACHE_ROW_FULLFORM_INDEX]);
        	}
    	} catch (IOException e) {
    		String exceptionMessage = String.format(
				"Unable to access state database URL %s", stateLocationFilePath.toString());
			throw new NoCacheFoundException(exceptionMessage);
    	} finally {
    		try {
    			if (input != null) {
    				input.close();
    			}
    	        if (inStream != null) {
    	        	inStream.close();
    	        }
    	    } catch (IOException e) {
    	    	String exceptionMessage = String.format(
					"Unable to close file for state database %s",
					stateLocationFilePath.toString());
				throw new GeoCoderException(exceptionMessage, e);
    	    }
    	}
	}
}
