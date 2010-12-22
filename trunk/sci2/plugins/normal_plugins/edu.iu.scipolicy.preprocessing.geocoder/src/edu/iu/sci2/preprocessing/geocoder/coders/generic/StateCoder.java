package edu.iu.sci2.preprocessing.geocoder.coders.generic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import edu.iu.sci2.model.geocode.Geolocation;
import edu.iu.sci2.preprocessing.geocoder.coders.GeoCoderException;
import edu.iu.sci2.preprocessing.geocoder.coders.Geocoder;
import edu.iu.sci2.preprocessing.geocoder.coders.NoCacheFoundException;

public class StateCoder implements Geocoder {

	public static final int CACHE_ROW_ABBREVIATION_INDEX = 1;
	public static final int CACHE_ROW_FULLFORM_INDEX = 0;
	public static final int CACHE_ROW_LONGITUDE_INDEX = 3;
	public static final int CACHE_ROW_LATITUDE_INDEX = 2;

	private Map<String, Geolocation> stateFullFormToLocation = null;
	private URL stateFile;
	
	/* 
	 * change Map<String, String> to Map<String, GeoLocation> which uses
	 * same memory size but increase lookup efficiency of abbreviation
	 */
	private Map<String, Geolocation> stateAbbreviationToLocation = null;
	
	public StateCoder(URL stateFile) {
		this.stateFile = stateFile;
	}

	public CODER_TYPE getLocationType() {
		return CODER_TYPE.US_STATE;
	}
	
	/*
	 * Initialize the Map for State from the text file.
	 * */
	private void initializeStateLocationMappings(URL stateLocationFilePath) 
														throws GeoCoderException {
		this.stateFullFormToLocation = new HashMap<String, Geolocation>();
		this.stateAbbreviationToLocation = new HashMap<String, Geolocation>();
		
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
    	    	
    	    	Geolocation location = new Geolocation(latitude, longitude);
    	    	
    	    	/*
    	    	 * Map with Full Form as the Key and List of Latitude, Longitude as the Value.
    	    	 * */
    	    	this.stateFullFormToLocation.put(lineTokens[CACHE_ROW_FULLFORM_INDEX], location);
    	    	
       	    	/*
    	    	 * Map with Abbreviation as the Key and Full Form as the value.
    	    	 * */
    	    	this.stateAbbreviationToLocation.put(
	    			lineTokens[CACHE_ROW_ABBREVIATION_INDEX],
	    			location);
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

	public Geolocation geocodingFullForm(String fullForm) throws GeoCoderException {
		if (stateFullFormToLocation == null) {
			initializeStateLocationMappings(stateFile);
		}
		return getGeolocationFromMap(stateFullFormToLocation, fullForm);
	}

	public Geolocation geocodingAbbreviation(String abbreviation) throws GeoCoderException {
		if (stateAbbreviationToLocation == null) {
			initializeStateLocationMappings(stateFile);
		}
		return getGeolocationFromMap(stateAbbreviationToLocation, abbreviation);
	}
	
	/**
	 * Find the geolocation for the given location from the given maps.
	 * @param maps - a list of mapping from location to geolocation
	 * @param state - a state in string
	 * @return result if success
	 * @throws GeoCoderException - throw while there is error or no result is found
	 */
	private Geolocation getGeolocationFromMap(Map<String, Geolocation> maps, String state) 
																	throws GeoCoderException {
		
		Geolocation geolocation = maps.get(state);
		if (geolocation == null) {
			throw new GeoCoderException("Result not found");
		}
		
		return geolocation;
	}
}
