/**
 * 
 */
package edu.iu.scipolicy.preprocessing.geocoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cdtank
 */
public class GeocoderDictionaries {
	
	private static final int CACHE_ROW_ABBREVIATION_INDEX = 1;
	private static final int CACHE_ROW_FULLFORM_INDEX = 0;
	private static final int CACHE_ROW_LONGITUDE_INDEX = 3;
	private static final int CACHE_ROW_LATITUDE_INDEX = 2;
	private static URL stateFile = null;
	private static URL countryFile = null;
	
	private static Map<String,List<Double>> countryFullformToLocation = null;

	private static Map<String,String> countryAbbreviationToFullform = null;
	
	private static Map<String,List<Double>> stateFullformToLocation = null;
	
	private static Map<String,String> stateAbbreviationToFullform = null;
	
	public static void setStateFile(URL stateFile) {
		GeocoderDictionaries.stateFile = stateFile;
	}
	
	public static void setCountryFile(URL countryFile) {
		GeocoderDictionaries.countryFile = countryFile;
	}
	


	/**
	 * @return the countryFullformToLocation
	 */
	public static Map<String, List<Double>> getCountryFullformToLocation() {
		if (countryFullformToLocation == null)  {
			initializeCountryLocationMappings(countryFile);
		}

		return countryFullformToLocation;
	}

	/**
	 * @return the countryAbbreviationToFullform
	 */
	public static Map<String, String> getCountryAbbreviationToFullform() {
		if (countryAbbreviationToFullform == null) {
			initializeCountryLocationMappings(countryFile);
		}
		
		return countryAbbreviationToFullform;
	}

	/**
	 * @return the stateFullformToLocation
	 */
	public static Map<String, List<Double>> getStateFullformToLocation() {
		if (stateFullformToLocation == null) {
			initializeStateLocationMappings(stateFile);
		}
		
		return stateFullformToLocation;
	}

	/**
	 * @return the stateAbbreviationToFullform
	 */
	public static Map<String, String> getStateAbbreviationToFullform() {
		if (stateAbbreviationToFullform == null) {
			initializeStateLocationMappings(stateFile);
		}
		
		return stateAbbreviationToFullform;
	}

	/*
	 * Initialize the Map for Countries from the text file.
	 * */
	private static void initializeCountryLocationMappings(
			URL countryLocationFilePath) {
		if (countryFile == null) {
			throw new NoCacheFoundException("You must call setCountryFile before calling this method!");
		}
		
		countryFullformToLocation = new HashMap<String,List<Double>>();
    	countryAbbreviationToFullform = new HashMap<String,String>();
    	
		InputStream inStream = null;
    	BufferedReader input = null;
    	String line;
    
    	try {
             URLConnection connection = countryLocationFilePath.openConnection();
             connection.setDoInput(true);
             inStream = connection.getInputStream();
             input = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
    		    		
    	    while (null != (line = input.readLine())) {
    	    	
    	    	List<Double> location = new ArrayList<Double>();
    	    	String[] lineTokens = line.split(";");
    	    	
    	    	location.add(Double.parseDouble(lineTokens[2]));
    	    	location.add(Double.parseDouble(lineTokens[3]));
    	    	
    	    	/*
    	    	 * Map with Full Form as the Key and List of Latitude, Longitude as the Value.
    	    	 * */
    	    	countryFullformToLocation.put(lineTokens[0], location);
    	    	
    	    	/*
    	    	 * Map with Abbreviation as the Key and Full Form as the value.
    	    	 * */
    	    	countryAbbreviationToFullform.put(lineTokens[CACHE_ROW_ABBREVIATION_INDEX], lineTokens[0]);
        	}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	finally {
    		try {
    			if (input != null) input.close();
    	        if (inStream != null) inStream.close();
    	    }
    	    catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	}
	
		
	}

	/*
	 * Initialize the Map for State from the text file.
	 * */
	private static void initializeStateLocationMappings(
			URL stateLocationFilePath) {
		if (stateFile == null) {
			throw new NoCacheFoundException("You must call setStateFile before calling this method!");
		}
		
		stateFullformToLocation = new HashMap<String,List<Double>>();
		stateAbbreviationToFullform = new HashMap<String,String>();
		
    	InputStream inStream = null;
    	BufferedReader input = null;
    	String line;

    	try {
             URLConnection connection = stateLocationFilePath.openConnection();
             connection.setDoInput(true);
             inStream = connection.getInputStream();
             input = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
    		    		
    	    while (null != (line = input.readLine())) {
    	    	
    	    	List<Double> location = new ArrayList<Double>();
    	    	String[] lineTokens = line.split(";");
    	    	
    	    	location.add(Double.parseDouble(lineTokens[CACHE_ROW_LATITUDE_INDEX]));
    	    	location.add(Double.parseDouble(lineTokens[CACHE_ROW_LONGITUDE_INDEX]));
    	    	
    	    	/*
    	    	 * Map with Full Form as the Key and List of Latitude, Longitude as the Value.
    	    	 * */
    	    	stateFullformToLocation.put(lineTokens[CACHE_ROW_FULLFORM_INDEX], location);
    	    	
       	    	/*
    	    	 * Map with Abbreviation as the Key and Full Form as the value.
    	    	 * */
    	    	stateAbbreviationToFullform.put(lineTokens[CACHE_ROW_ABBREVIATION_INDEX], lineTokens[CACHE_ROW_FULLFORM_INDEX]);
        	}
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    	finally {
    		try {
    			if (input != null) input.close();
    	        if (inStream != null) inStream.close();
    	    }
    	    catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	}
	}
	
	public static class NoCacheFoundException extends RuntimeException {
		NoCacheFoundException(String message) {
			super(message);
		}
	}
}
