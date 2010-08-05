package edu.iu.scipolicy.preprocessing.geocoder.coders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class ZipCodeCoder implements GeoCoder {
	public static final String LOCATION_AS_ZIPCODE_IDENTIFIER = "ZIP CODE";

	public static final Map<String, String> EMPTY_FORMS_TO_LOCATIONS =
		Collections.unmodifiableMap(new HashMap<String, String>());

	private Map<String, GeoLocation> zipCodeToLocation;

	public ZipCodeCoder(URL zipCodeFile) {
		initializeZipCodeLocationMappings(zipCodeFile);
	}

	public String getLocationType() {
		return LOCATION_AS_ZIPCODE_IDENTIFIER;
	}

	public Map<String, GeoLocation> getFullFormsToLocations() {
		return this.zipCodeToLocation;
	}

	public Map<String, String> getAbbreviationsToFullForms() {
		return EMPTY_FORMS_TO_LOCATIONS;
	}

	private void initializeZipCodeLocationMappings(URL zipCodeFile) {
		// Open zip code file.
		
		InputStream inStream = null;
		BufferedReader input = null;
		
		try {
			URLConnection connection = zipCodeFile.openConnection();
			connection.setDoInput(true);
			inStream = connection.getInputStream();
			input = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
			
			CSVReader zipCsvReader = createZipcodeCsvReader(input);

			Map<String, GeoLocation> zipCodeToLocation = 
				createMapFromZipcodeToLocation(zipCsvReader);
			
			this.zipCodeToLocation = zipCodeToLocation;
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

	private static Map<String, GeoLocation> createMapFromZipcodeToLocation(
			CSVReader zipCsvReader) {
		try {
			zipCsvReader.readNext(); // (we ignore the first row, which contains column headers)

			Map<String, GeoLocation> zipCodeToLocation = new HashMap<String, GeoLocation>();
			String[] zipAndLocationLine;
			while ((zipAndLocationLine = zipCsvReader.readNext()) != null) {
				String zip = zipAndLocationLine[ZIPCODE_INDEX];
				
				String latitudeString = zipAndLocationLine[LATITUDE_INDEX];
				String longitudeString = zipAndLocationLine[LONGITUDE_INDEX];
			
				double latitude = Double.valueOf(latitudeString);
				double longitude = Double.valueOf(longitudeString);
					
				GeoLocation location = new GeoLocation(latitude, longitude); 
					
				zipCodeToLocation.put(zip, location);

			}

			return zipCodeToLocation;
		} catch (IOException e) {
			throw new GeoCoderException("IO error occurred while reading zipcodes from zipcode"
					+ " database file", e);
		}
	}
}
