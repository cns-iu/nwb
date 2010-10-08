package edu.iu.scipolicy.preprocessing.geocoder.coders.generic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.iu.scipolicy.model.geocode.Geolocation;
import edu.iu.scipolicy.model.geocode.USZipCode;
import edu.iu.scipolicy.preprocessing.geocoder.coders.Geocoder;
import edu.iu.scipolicy.preprocessing.geocoder.coders.GeoCoderException;
import edu.iu.scipolicy.preprocessing.geocoder.coders.NoCacheFoundException;

import au.com.bytecode.opencsv.CSVReader;

public class ZipCodeCoder implements Geocoder {
	public static final String LOCATION_AS_ZIPCODE_IDENTIFIER = "ZIP CODE";

	public static final Map<String, String> EMPTY_FORMS_TO_LOCATIONS =
		Collections.unmodifiableMap(new HashMap<String, String>());

	private Map<String, Geolocation> zipCodeToLocation;
	private URL zipCodeFile;

	public ZipCodeCoder(URL zipCodeFile) {
		this.zipCodeFile = zipCodeFile;
	}

	public CODER_TYPE getLocationType() {
		return CODER_TYPE.US_ZIP_CODE;
	}

	private void initializeZipCodeLocationMappings(URL zipCodeFile) throws GeoCoderException {
		// Open zip code file.
		
		InputStream inStream = null;
		BufferedReader input = null;
		
		try {
			URLConnection connection = zipCodeFile.openConnection();
			connection.setDoInput(true);
			inStream = connection.getInputStream();
			input = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
			
			CSVReader zipCsvReader = createZipcodeCsvReader(input);

			this.zipCodeToLocation = createMapFromZipcodeToLocation(zipCsvReader);
		} catch (IOException e) {
			String exceptionMessage = String.format(
				"Unable to access zipcode database URL %s", zipCodeFile.toString());
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
					"Unable to close file for zipcode database %s", zipCodeFile.toString());
				throw new GeoCoderException(exceptionMessage, e);
			}
		}
	}

	private static CSVReader createZipcodeCsvReader(BufferedReader input) {
		final char fieldSeparator = ',';
		final char fieldQuoteCharacter = '"';
		final int lineToStartReadingFrom = 0;
		final char quoteEscapeCharacter = '\\';

			CSVReader zipCsvReader = new CSVReader(input, fieldSeparator, fieldQuoteCharacter,
					lineToStartReadingFrom, quoteEscapeCharacter);

			return zipCsvReader;


	}

	// us_zipcode_geo_code format is "zip, latitude, longitude"
	private static final int ZIPCODE_INDEX = 0;
	private static final int LATITUDE_INDEX = 1;
	private static final int LONGITUDE_INDEX = 2;

	private static Map<String, Geolocation> createMapFromZipcodeToLocation(
			CSVReader zipCsvReader) throws GeoCoderException {
		try {
			zipCsvReader.readNext(); // (we ignore the first row, which contains column headers)

			Map<String, Geolocation> zipCodeToLocation = new HashMap<String, Geolocation>();
			String[] zipAndLocationLine;
			while ((zipAndLocationLine = zipCsvReader.readNext()) != null) {
				String zip = zipAndLocationLine[ZIPCODE_INDEX];
				
				String latitudeString = zipAndLocationLine[LATITUDE_INDEX];
				String longitudeString = zipAndLocationLine[LONGITUDE_INDEX];
			
				double latitude = Double.valueOf(latitudeString);
				double longitude = Double.valueOf(longitudeString);
					
				Geolocation location = new Geolocation(latitude, longitude); 
					
				zipCodeToLocation.put(USZipCode.parse(zip).getUzip(), location);

			}

			return zipCodeToLocation;
		} catch (IOException e) {
			throw new GeoCoderException("IO error occurred while reading zipcodes from zipcode"
					+ " database file", e);
		}
	}

	/**
	 * Find the geolocation for the given location from the given maps.
	 * @param fullForm - a ZIP code in string
	 * @return result if success
	 * @throws GeoCoderException - throw while there is error or no result is found
	 */
	public Geolocation geocodingFullForm(String fullForm) throws GeoCoderException {
		if (zipCodeToLocation == null) {
			initializeZipCodeLocationMappings(zipCodeFile);
		}
		
		Geolocation geolocation = zipCodeToLocation.get(USZipCode.parse(fullForm).getUzip());
		if (geolocation == null) {
			throw new GeoCoderException("Result not found");
		}
		
		return geolocation;
	}

	/*
	 * Zip code don't have short term
	 */
	public Geolocation geocodingAbbreviation(String abbreviation) throws GeoCoderException {
		throw new GeoCoderException("No result is found");
	}
}
