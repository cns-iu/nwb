package edu.iu.sci2.preprocessing.geocoder.coders.bing.placefinder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.cishell.utilities.network.DownloadHandler;
import org.cishell.utilities.network.DownloadHandler.InvalidUrlException;
import org.cishell.utilities.network.DownloadHandler.NetworkConnectionException;
import org.osgi.service.log.LogService;
import edu.iu.sci2.preprocessing.geocoder.GeocoderAlgorithm;
import edu.iu.sci2.preprocessing.geocoder.coders.bing.placefinder.beans.Response;

/**
 * 
 * PlaceFinderClient provide geocoding request through Bing PlaceFinder RESTful
 * web service. It use JAXB technology to unmarshall the XML response from Bing
 * service to Response (Java Object that contains the geocoding result). The
 * schema - placeFinder.xsd is written based on W3C XML schema 1.0 at
 * http://www.w3.org/2001/XMLSchema
 * 
 * @author kongch
 * 
 */
public final class PlaceFinderClient {
	public static final int BUFFER_SIZE = 4096;
	public static final int RETRIES = 3; //retry 3 times just in case bing doesn't return locations temporarily
	public static final String GET_METHOD = "GET";
	public static final String SERVICE_EPR = "http://dev.virtualearth.net/REST/v1/Locations?";
	public static final String LOCATION_PARAM = "q";
	public static final String APPID_PARAM = "key";
	public static final String OUTPUT_PARAM = "o";
	public static final String OUTPUT_TYPE = "xml";
	public static final String VALUE_SEPARATOR = "=";
	public static final String PARAM_SEPARATOR = "&";
	public static final String SPACE_CHAR = " ";
	public static final String SPACE_REPLACEMENT = "%20";
	public static final String SCHEMA_FILENAME = "placeFinder.xsd";

	private PlaceFinderClient() {
	}

	/**
	 * Prepare params and request Bing PlaceFinderService
	 * 
	 * @param address
	 *            - full address
	 * @param applicationId
	 *            - Bing application ID. Please obtains an ID from
	 *            http://www.microsoft.com/maps
	 * @return Response - Please refer to the placeFinder.xsd (XML schema for
	 *         Bing PlaceFinder response)
	 * @throws IOException
	 *             - Thrown while fail to generate URL or connection error
	 * @throws JAXBException
	 *             - Bing schema changed
	 * @throws NetworkConnectionException
	 * @throws InvalidUrlException
	 */
	public static Response requestAddress(String address, String applicationId)
			throws IOException, JAXBException, InvalidUrlException,
			NetworkConnectionException {
		Map<String, String> paramToValue = new HashMap<String, String>();
		paramToValue.put(LOCATION_PARAM, address);
		paramToValue.put(APPID_PARAM, applicationId);
		paramToValue.put(OUTPUT_PARAM, OUTPUT_TYPE);
		return request(paramToValue);
	}

	/**
	 * Prepare params and request Bing PlaceFinderService
	 * 
	 * @param country
	 *            - country name
	 * @param applicationId
	 *            - Bing application ID. Please obtains an ID from
	 *            http://www.microsoft.com/maps
	 * @return Response - Please refer to the placeFinder.xsd (XML schema for
	 *         Bing PlaceFinder response)
	 * @throws IOException
	 *             - Thrown while fail to generate URL or connection error
	 * @throws JAXBException
	 *             - Bing schema changed
	 * @throws NetworkConnectionException
	 * @throws InvalidUrlException
	 */
	public static Response requestCountry(String country, String applicationId)
			throws IOException, JAXBException, InvalidUrlException,
			NetworkConnectionException {
		Map<String, String> paramToValue = new HashMap<String, String>();
		paramToValue.put(LOCATION_PARAM, "&countryRegion=" + country);
		paramToValue.put(OUTPUT_PARAM, OUTPUT_TYPE);
		paramToValue.put(APPID_PARAM, applicationId);
		
		return request(paramToValue);
	}
 
	/**
	 * Prepare params and request Bing PlaceFinderService
	 * 
	 * @param state
	 *            - state name
	 * @param country
	 *            - country name of the state
	 * @param applicationId
	 *            - Bing application ID. Please obtains an ID from
	 *            http://www.microsoft.com/maps
	 * @return Response - Please refer to the placeFinder.xsd (XML schema for
	 *         Bing PlaceFinder response)
	 * @throws IOException
	 *             - Thrown while fail to generate URL or connection error
	 * @throws JAXBException
	 *             - Bing schema changed
	 * @throws NetworkConnectionException
	 * @throws InvalidUrlException
	 */
	public static Response requestState(String state, String country,
			String applicationId) throws IOException, JAXBException,
			InvalidUrlException, NetworkConnectionException {
		Map<String, String> paramToValue = new HashMap<String, String>();
		paramToValue.put(LOCATION_PARAM, "&adminDistrict=" + state);
		paramToValue.put(OUTPUT_PARAM, OUTPUT_TYPE);
		paramToValue.put(APPID_PARAM, applicationId);
		return request(paramToValue);
	}

	/**
	 * Prepare params and request Bing PlaceFinderService
	 * 
	 * @param zipCode
	 *            - ZIP code
	 * @param country
	 *            - country name of the ZIP code
	 * @param applicationId
	 *            - Bing application ID. Please obtains an ID from
	 *            http://www.microsoft.com/maps
	 * @return Response - Please refer to the placeFinder.xsd (XML schema for
	 *         Bing PlaceFinder response)
	 * @throws IOException
	 *             - Thrown while fail to generate URL or connection error
	 * @throws JAXBException
	 *             - Bing schema changed
	 * @throws NetworkConnectionException
	 * @throws InvalidUrlException
	 */
	public static Response requestZipCode(String zipCode, String country,
			String applicationId) throws IOException, JAXBException,
			InvalidUrlException, NetworkConnectionException {
		Map<String, String> paramToValue = new HashMap<String, String>();
		paramToValue.put(LOCATION_PARAM, "&postalCode=" + zipCode + "&countryRegion=" + country);
		paramToValue.put(OUTPUT_PARAM, OUTPUT_TYPE);
		paramToValue.put(APPID_PARAM, applicationId);
		return request(paramToValue);
	}

	/**
	 * Return Response that represent the output.
	 * 
	 * @param paramToValue
	 *            - param and value pair mappings
	 * @return Response - Please refer to the placeFinder.xsd (XML schema for
	 *         Bing PlaceFinder response)
	 * @throws IOException
	 *             - Thrown while fail to generate URL or connection error
	 * @throws JAXBException
	 *             - Bing schema changed
	 * @throws NetworkConnectionException
	 * @throws InvalidUrlException
	 */
	private static Response request(Map<String, String> paramToValue)
			throws IOException, JAXBException, InvalidUrlException,
			NetworkConnectionException {

		/* create URL */

		URL url = createURL(paramToValue);

		/* Start HTTP request */
		String responseString = query(GET_METHOD, url);
		/* Parse responseString to Response */
		return generateResponse(responseString);
	}

	/*
	 * Unmarshall XML response to Response by using JAXB
	 */
	private static Response generateResponse(String inputString)
			throws JAXBException {

		/* Get Schema URL */
		URL schemaURL = PlaceFinderClient.class.getResource(SCHEMA_FILENAME);

		UnmarshallerJAXB unmarshallerJAXB = UnmarshallerJAXB
				.newInstance(schemaURL);
		if (unmarshallerJAXB != null) {

			int responsePosition = inputString.indexOf("Response");
			String ResponseTag = inputString.substring(responsePosition
					+ "Response".length(), responsePosition
					+ inputString.substring(responsePosition).indexOf('>'));
			
			inputString = inputString.substring(inputString.indexOf('<')); // Removing unknown characters from start
			inputString = inputString.replace(ResponseTag, ""); // Removing unnecessary attributes in Response. Unmarshall failed if tag contains attribute
			StringReader reader = new StringReader(inputString);
			Response response = unmarshallerJAXB.unmarshal(reader);
			return response;

		}

		/* Generate error code due to Bing schema changed */
		Response response = new Response();

		return response;
	}

	private static String query(String method, URL url) throws IOException,
			InvalidUrlException, NetworkConnectionException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(method);

		/* Start connection and read from socket */
		return DownloadHandler.getResponse(connection);
	}

	private static URL createURL(Map<String, String> paramToValue)
			throws MalformedURLException {
		StringBuilder builder = new StringBuilder();

		/* set Bing PlaceFinder service EPR */
		builder.append(SERVICE_EPR);

		/* set first param */
		Iterator<String> paramIterator = paramToValue.keySet().iterator();
		String param = paramIterator.next();
		builder.append(param);
		builder.append(VALUE_SEPARATOR);
		builder.append(paramToValue.get(param).replaceAll(SPACE_CHAR,
				SPACE_REPLACEMENT));
		/* set remaing param */
		while (paramIterator.hasNext()) {
			param = paramIterator.next();
			builder.append(PARAM_SEPARATOR);
			builder.append(param);
			builder.append(VALUE_SEPARATOR);
			if (paramToValue.get(param) != null)
				builder.append(paramToValue.get(param).replaceAll(SPACE_CHAR,
						SPACE_REPLACEMENT));
		}
		return new URL(builder.toString());
	}
}
