package edu.iu.sci2.preprocessing.geocoder.coders.yahoo.placefinder;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.JAXBException;
import org.cishell.utilities.network.DownloadHandler;
import org.cishell.utilities.network.DownloadHandler.InvalidUrlException;
import org.cishell.utilities.network.DownloadHandler.NetworkConnectionException;

import edu.iu.sci2.preprocessing.geocoder.coders.yahoo.placefinder.beans.ResultSet;

/**
 * 
 * PlaceFinderClient provide geocoding request through Yahoo
 * PlaceFinder RESTful web service. It use JAXB technology to 
 * unmarshall the XML response from Yahoo service to ResultSet
 * (Java Object that contains the geocoding result). The schema
 * - placeFinder.xsd is written based on W3C XML schema 1.0 at 
 * http://www.w3.org/2001/XMLSchema
 * @author kongch
 *
 */
public final class PlaceFinderClient {
	public static final int UTF8_ERROR_CODE = 102; /* UTF8 error code from Yahoo! */
	public static final int BUFFER_SIZE = 4096;
	public static final String GET_METHOD = "GET";
	public static final String SERVICE_EPR = "http://where.yahooapis.com/geocode?";
	public static final String LOCATION_PARAM = "location";
	public static final String COUNTRY_PARAM = "country";
	public static final String STATE_PARAM = "state";
	public static final String POSTAL_PARAM = "postal";
	public static final String APPID_PARAM = "appid";
	public static final String VALUE_SEPARATOR = "=";
	public static final String PARAM_SEPARATOR = "&";
	public static final char SPACE_CHAR = ' ';
	public static final char SPACE_REPLACEMENT = '+';
	public static final String SCHEMA_FILENAME = "placeFinder.xsd";

	private PlaceFinderClient() {	
	}
	
	/**
	 * Prepare params and request Yahoo! PlaceFinderService
	 * @param address - full address
	 * @param applicationId - Yahoo application ID. Please obtains an ID from 
	 * 					http://developer.yahoo.com/geo/placefinder/
	 * @return ResultSet - Please refer to the placeFinder.xsd 
	 * 						(XML schema for Yahoo! PlaceFinder response)
	 * @throws IOException - Thrown while fail to generate URL or connection error
	 * @throws JAXBException - Yahoo! schema changed
	 * @throws NetworkConnectionException 
	 * @throws InvalidUrlException 
	 */
	public static ResultSet requestAddress(String address, String applicationId)
			throws IOException, JAXBException, InvalidUrlException,
			NetworkConnectionException {
		Map<String, String> paramToValue = new HashMap<String, String>();
		paramToValue.put(LOCATION_PARAM, address);
		paramToValue.put(APPID_PARAM, applicationId);
		return request(paramToValue);
	}
	
	/**
	 * Prepare params and request Yahoo! PlaceFinderService
	 * @param country - country name
	 * @param applicationId - Yahoo application ID. Please obtains an ID from 
	 * 					http://developer.yahoo.com/geo/placefinder/
	 * @return ResultSet - Please refer to the placeFinder.xsd 
	 * 						(XML schema for Yahoo! PlaceFinder response)
	 * @throws IOException - Thrown while fail to generate URL or connection error
	 * @throws JAXBException - Yahoo! schema changed
	 * @throws NetworkConnectionException 
	 * @throws InvalidUrlException 
	 */
	public static ResultSet requestCountry(String country, String applicationId)
			throws IOException, JAXBException, InvalidUrlException,
			NetworkConnectionException {
		Map<String, String> paramToValue = new HashMap<String, String>();
		paramToValue.put(COUNTRY_PARAM, country);
		paramToValue.put(APPID_PARAM, applicationId);
		return request(paramToValue);
	}
	
	/**
	 * Prepare params and request Yahoo! PlaceFinderService
	 * @param state - state name
	 * @param country - country name of the state
	 * @param applicationId - Yahoo application ID. Please obtains an ID from 
	 * 					http://developer.yahoo.com/geo/placefinder/
	 * @return ResultSet - Please refer to the placeFinder.xsd 
	 * 						(XML schema for Yahoo! PlaceFinder response)
	 * @throws IOException - Thrown while fail to generate URL or connection error
	 * @throws JAXBException - Yahoo! schema changed
	 * @throws NetworkConnectionException 
	 * @throws InvalidUrlException 
	 */
	public static ResultSet requestState(String state, String country,
			String applicationId) throws IOException, JAXBException,
			InvalidUrlException, NetworkConnectionException {
		Map<String, String> paramToValue = new HashMap<String, String>();
		paramToValue.put(STATE_PARAM, state);
		paramToValue.put(COUNTRY_PARAM, country);
		paramToValue.put(APPID_PARAM, applicationId);
		return request(paramToValue);
	}
	
	/**
	 * Prepare params and request Yahoo! PlaceFinderService
	 * @param zipCode - ZIP code
	 * @param country - country name of the ZIP code
	 * @param applicationId - Yahoo application ID. Please obtains an ID from 
	 * 					http://developer.yahoo.com/geo/placefinder/
	 * @return ResultSet - Please refer to the placeFinder.xsd 
	 * 						(XML schema for Yahoo! PlaceFinder response)
	 * @throws IOException - Thrown while fail to generate URL or connection error
	 * @throws JAXBException - Yahoo! schema changed
	 * @throws NetworkConnectionException 
	 * @throws InvalidUrlException 
	 */
	public static ResultSet requestZipCode(String zipCode, String country, String applicationId) 
			throws IOException, JAXBException,
			InvalidUrlException, NetworkConnectionException {
		Map<String, String> paramToValue = new HashMap<String, String>();
		paramToValue.put(POSTAL_PARAM, zipCode);
		paramToValue.put(COUNTRY_PARAM, country);
		paramToValue.put(APPID_PARAM, applicationId);
		return request(paramToValue);
	}
	
	/**
	 * Return ResultSet that represent the output.
	 * @param paramToValue - param and value pair mappings
	 * @return ResultSet - Please refer to the placeFinder.xsd 
	 * 						(XML schema for Yahoo PlaceFinder response)
	 * @throws IOException - Thrown while fail to generate URL or connection error
	 * @throws JAXBException - Yahoo! schema changed
	 * @throws NetworkConnectionException 
	 * @throws InvalidUrlException 
	 */
	private static ResultSet request(Map<String, String> paramToValue) 
			throws IOException, JAXBException, InvalidUrlException,
			NetworkConnectionException {

		/* create URL */
		URL url = createURL(paramToValue);
		
		/* Start HTTP request */
		String responseString = query(GET_METHOD, url);
		
		/* Parse responseString to ResultSet */
		ResultSet resultSet = generateResultSet(responseString);
		
		/* Fix UTF8 error and re-geocoding */
		if (resultSet.getError().intValue() == UTF8_ERROR_CODE) {
			URL urlUTF8 = new URL(new String(url.toString().getBytes(), "UTF-8"));
			
			responseString = query(GET_METHOD, urlUTF8);
			resultSet = generateResultSet(responseString);
		}
		
		return resultSet;
	}
    
	/*
	 *  Unmarshall XML response to ResultSet by using JAXB 
	 */
	private static ResultSet generateResultSet(String inputString) throws JAXBException {
	
		/* Get Schema URL */
		URL schemaURL = PlaceFinderClient.class.getResource(SCHEMA_FILENAME);
		
		UnmarshallerJAXB unmarshallerJAXB = UnmarshallerJAXB.newInstance(schemaURL);
		if (unmarshallerJAXB != null) {
			StringReader reader = new StringReader(inputString);
			try {
				return unmarshallerJAXB.unmarshal(reader);
			} catch (JAXBException e) {
				reader = removeWoeIdAndWoeType(inputString);
				return unmarshallerJAXB.unmarshal(reader);
			}
		}
		
		/* Generate error code due to Yahoo schema changed */
		ResultSet resultSet = new ResultSet();
		resultSet.setFound(BigInteger.ZERO);
		resultSet.setErrorMessage("Failed to unmarshal due to XML schema changed");
		return resultSet;
	}

	/* Try to fixed unmarshaller problem with empty WOE entities */
	private static StringReader removeWoeIdAndWoeType(String inputString) {
		return new StringReader(
				inputString.replace("<woeid></woeid>", "").replace("<woetype></woetype>", ""));
	}
    
	private static String query(String method, URL url) 
			throws IOException, InvalidUrlException, NetworkConnectionException {
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(method);
		
		/* Start connection and read from socket */
		return DownloadHandler.getResponse(connection);
	}
    
	private static URL createURL(Map<String, String> paramToValue) 
															throws MalformedURLException {
		StringBuilder builder = new StringBuilder();
		
		/* set Yahoo! PlaceFinder service EPR */
		builder.append(SERVICE_EPR);
		
		/* set first param */
		Iterator<String> paramIterator = paramToValue.keySet().iterator();
		String param = paramIterator.next();
		builder.append(param);
		builder.append(VALUE_SEPARATOR);
		builder.append(paramToValue.get(param).replace(SPACE_CHAR, SPACE_REPLACEMENT));
		
		/* set remaing param */
		while (paramIterator.hasNext()) {
			param = paramIterator.next();
			builder.append(PARAM_SEPARATOR);
			builder.append(param);
			builder.append(VALUE_SEPARATOR);
			builder.append(paramToValue.get(param).replace(SPACE_CHAR, SPACE_REPLACEMENT));
		}
		
		return new URL(builder.toString());
	}
}
