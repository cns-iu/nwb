package edu.iu.scipolicy.preprocessing.geocoder.coders;

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

public class StateCoder {

	private static final int CACHE_ROW_ABBREVIATION_INDEX = 1;
	private static final int CACHE_ROW_FULLFORM_INDEX = 0;
	private static final int CACHE_ROW_LONGITUDE_INDEX = 3;
	private static final int CACHE_ROW_LATITUDE_INDEX = 2;
	
	private static URL stateFile = null;
	
	private static Map<String,List<Double>> stateFullformToLocation = null;
	
	private static Map<String,String> stateAbbreviationToFullform = null;
	

	public static void setStateFile(URL stateFile) {
		StateCoder.stateFile = stateFile;
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
}
