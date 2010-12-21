package edu.iu.scipolicy.preprocessing.geocoder.coders.generic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import edu.iu.scipolicy.model.geocode.Geolocation;
import edu.iu.scipolicy.preprocessing.geocoder.coders.Geocoder;
import edu.iu.scipolicy.preprocessing.geocoder.coders.GeoCoderException;
import edu.iu.scipolicy.preprocessing.geocoder.coders.NoCacheFoundException;

public class CountryCoder implements Geocoder {

	public static final int CACHE_ROW_ABBREVIATION_INDEX = 1;
	public static final int CACHE_ROW_FULLFORM_INDEX = 0;
	public static final int CACHE_ROW_LONGITUDE_INDEX = 3;
	public static final int CACHE_ROW_LATITUDE_INDEX = 2;

	private Map<String, Geolocation> countryFullFormToLocation = null;
	private URL countryFile;
	
	/* 
	 * change Map<String, String> to Map<String, GeoLocation> which uses
	 * same memory size but increase lookup efficiency of abbreviation
	 */
	private Map<String, Geolocation> countryAbbreviationToLocation = null;
	
	public CountryCoder(URL countryFile) {
		this.countryFile = countryFile;
	}

	public CODER_TYPE getLocationType() {
		return CODER_TYPE.COUNTRY;
	}
	
	/*
	 * Initialize the Map for Countries from the text file.
	 * */
	private void initializeCountryLocationMappings(URL countryLocationFilePath) 
															throws GeoCoderException {
		this.countryFullFormToLocation = new HashMap<String, Geolocation>();
    	this.countryAbbreviationToLocation = new HashMap<String, Geolocation>();
    	
		InputStream inStream = null;
    	BufferedReader input = null;
    	String line;
    
    	try {
             URLConnection connection = countryLocationFilePath.openConnection();
             connection.setDoInput(true);
             inStream = connection.getInputStream();
             input = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
    		    		
    	    while (null != (line = input.readLine())) {
    	    	
    	    	String[] lineTokens = line.split(";");
    	    	
    	    	double latitude = Double.parseDouble(lineTokens[CACHE_ROW_LATITUDE_INDEX]);
    	    	double longitude = Double.parseDouble(lineTokens[ CACHE_ROW_LONGITUDE_INDEX]);
    	    	
    	    	Geolocation location = new Geolocation(latitude, longitude);
    	    	
    	    	/*
    	    	 * Map with Full Form as the Key and List of Latitude, Longitude as the Value.
    	    	 * */
    	    	this.countryFullFormToLocation.put(lineTokens[CACHE_ROW_FULLFORM_INDEX], location);
    	    	
    	    	/*
    	    	 * Map with Abbreviation as the Key and Full Form as the value.
    	    	 * */
    	    	this.countryAbbreviationToLocation.put(
    	    			lineTokens[CACHE_ROW_ABBREVIATION_INDEX],
    	    			location);
        	}
    	} catch (Exception e) {
    		String exceptionMessage = String.format(
				"Unable to access state database URL %s", countryLocationFilePath.toString());
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
					"Unable to close file for country database %s",
					countryLocationFilePath.toString());
				throw new GeoCoderException(exceptionMessage, e);
    	    }
    	}
	}

	public Geolocation geocodingFullForm(String fullForm) throws GeoCoderException {
		if (countryFullFormToLocation == null) {
			initializeCountryLocationMappings(countryFile);
		}
		return getGeolocationFromMap(countryFullFormToLocation, fullForm);
	}

	public Geolocation geocodingAbbreviation(String abbreviation) throws GeoCoderException {
		if (countryAbbreviationToLocation == null) {
			initializeCountryLocationMappings(countryFile);
		}
		return getGeolocationFromMap(countryAbbreviationToLocation, abbreviation);
	}
	
	/**
	 * Find the geolocation for the given location from the given maps.
	 * @param maps - a list of mapping from location to geolocation
	 * @param country - a country in string
	 * @return result if success
	 * @throws GeoCoderException - throw while there is error or no result is found
	 */
	private Geolocation getGeolocationFromMap(Map<String, Geolocation> maps, String country) 
																	throws GeoCoderException {
		
		Geolocation geolocation = maps.get(country);
		if (geolocation == null) {
			throw new GeoCoderException("Result not found");
		}
		
		return geolocation;
	}
}
