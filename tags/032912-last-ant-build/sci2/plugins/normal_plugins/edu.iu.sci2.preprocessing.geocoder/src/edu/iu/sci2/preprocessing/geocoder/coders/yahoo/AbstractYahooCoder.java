package edu.iu.sci2.preprocessing.geocoder.coders.yahoo;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import edu.iu.sci2.model.geocode.GeoDetail;
import edu.iu.sci2.model.geocode.Geolocation;
import edu.iu.sci2.model.geocode.USZipCode;
import edu.iu.sci2.preprocessing.geocoder.coders.DetailGeocoder;
import edu.iu.sci2.preprocessing.geocoder.coders.GeoCoderException;
import edu.iu.sci2.preprocessing.geocoder.coders.Geocoder;
import edu.iu.sci2.preprocessing.geocoder.coders.yahoo.placefinder.beans.ResultSet;
import edu.iu.sci2.preprocessing.geocoder.coders.yahoo.placefinder.beans.YahooServiceResult;

/**
 * 
 * Abstract class for Yahoo coders that share the same behavior.
 * @author kongch
 *
 */
public abstract class AbstractYahooCoder implements Geocoder, DetailGeocoder {
	private final String applicationId;
	
	public AbstractYahooCoder(String applicationId) {
		this.applicationId = applicationId;
	}
	
	public abstract CODER_TYPE getLocationType();
	public abstract ResultSet requestYahooService(String location, String applicationId) 
																throws IOException, JAXBException;
	
	public Geolocation geocodingFullForm(String fullForm) throws GeoCoderException {
		try {
			return extractGeoLocation(requestYahooService(fullForm, applicationId));
		} catch (IOException e) {
			throw new GeoCoderException("Yahoo service error", e);
		} catch (JAXBException e) {
			throw new GeoCoderException("Yahoo service error", e);
		}
	}

	public Geolocation geocodingAbbreviation(String abbreviation) throws GeoCoderException {
		return geocodingFullForm(abbreviation);
	}
	
	public GeoDetail geocodingLocation(String locationString) throws GeoCoderException {
		try {
			return extractGeoDetail(requestYahooService(locationString, applicationId));
		} catch (IOException e) {
			throw new GeoCoderException("Yahoo service error", e);
		} catch (JAXBException e) {
			throw new GeoCoderException("Yahoo service error", e);
		}
	}
	
	private GeoDetail extractGeoDetail(ResultSet resultSet) throws GeoCoderException {
		
		/*
		 * Convert result to geoDetail and return the result
		 */
		Geolocation geolocation = extractGeoLocation(resultSet);
		if (geolocation != null) {
			YahooServiceResult result = resultSet.getResult().get(0);
			return new GeoDetail(
					geolocation, 
					USZipCode.parse(result.getUzip()), 
					result.getStreet(),
					result.getCity(),
					result.getState(),
					result.getCountry(),
					result.getCounty());
		}
		
		throw new GeoCoderException("No result found");
	}
	
	private Geolocation extractGeoLocation(ResultSet resultSet) throws GeoCoderException {
		/*
		 * Convert result to geolocation and return the result
		 */
		if (resultSet.getResult().size() != 0) {
			YahooServiceResult result = resultSet.getResult().get(0);
			/* getLatitude() and getLongitude() return some odd result sometime */
			return new Geolocation(new Double(result.getOffsetlat()), 
											new Double(result.getOffsetlon()));
		}
		
		throw new GeoCoderException("No result found");
	}
}
