package edu.iu.scipolicy.preprocessing.geocoder.coders.yahoo;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import edu.iu.scipolicy.model.geocode.Geolocation;
import edu.iu.scipolicy.preprocessing.geocoder.coders.Geocoder;
import edu.iu.scipolicy.preprocessing.geocoder.coders.GeoCoderException;
import edu.iu.scipolicy.preprocessing.geocoder.coders.yahoo.placefinder.beans.ResultSet;
import edu.iu.scipolicy.preprocessing.geocoder.coders.yahoo.placefinder.beans.YahooServiceResult;

/**
 * 
 * Abstract class for Yahoo coders that share the same behavior.
 * @author kongch
 *
 */
public abstract class AbstractYahooCoder implements Geocoder {
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
	
	private Geolocation extractGeoLocation(ResultSet resultSet) throws GeoCoderException {
		/*
		 * Convert result to geolocation and return the result
		 */
		if (resultSet.getResult().size() != 0) {
			YahooServiceResult result = resultSet.getResult().get(0);
			return new Geolocation(new Double(result.getLatitude()), 
											new Double(result.getLongitude()));
		}
		
		throw new GeoCoderException("No result found");
	}
}
