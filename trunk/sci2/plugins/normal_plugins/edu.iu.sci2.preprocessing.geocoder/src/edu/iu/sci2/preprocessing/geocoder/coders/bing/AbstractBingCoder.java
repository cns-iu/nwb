package edu.iu.sci2.preprocessing.geocoder.coders.bing;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.cishell.utilities.network.DownloadHandler.InvalidUrlException;
import org.cishell.utilities.network.DownloadHandler.NetworkConnectionException;

import edu.iu.sci2.model.geocode.GeoDetail;
import edu.iu.sci2.model.geocode.Geolocation;
import edu.iu.sci2.model.geocode.USZipCode;
import edu.iu.sci2.preprocessing.geocoder.coders.DetailGeocoder;
import edu.iu.sci2.preprocessing.geocoder.coders.GeoCoderException;
import edu.iu.sci2.preprocessing.geocoder.coders.Geocoder;
import edu.iu.sci2.preprocessing.geocoder.coders.bing.placefinder.beans.Response;
import edu.iu.sci2.preprocessing.geocoder.coders.bing.placefinder.beans.Response.ResourceSets.ResourceSet.Resources.Location;
import edu.iu.sci2.preprocessing.geocoder.coders.bing.placefinder.beans.Response.ResourceSets.ResourceSet.Resources.Location.Address;



/**
 * 
 * Abstract class for Bing coders that share the same behavior.
 * @author kongch
 *
 */
public abstract class AbstractBingCoder implements Geocoder, DetailGeocoder {
	private final String applicationId;
	
	public AbstractBingCoder(String applicationId) {
		this.applicationId = applicationId;
	}
	
	public abstract CODER_TYPE getLocationType();

	public abstract Response requestBingService(String location,
			String applicationId) throws IOException, JAXBException,
			InvalidUrlException, NetworkConnectionException;

	public Geolocation geocodingFullForm(String fullForm) throws GeoCoderException {		
		try {
			Response resp = requestBingService(fullForm, applicationId);
			return extractGeoLocation(resp);
		} catch (IOException e) {
			throw new GeoCoderException("Bing service error", e);
		} catch (JAXBException e) {
			throw new GeoCoderException("Bing service error", e);
		} catch (InvalidUrlException e) {
			throw new GeoCoderException("Bing service error", e);
		} catch (NetworkConnectionException e) {
			throw new GeoCoderException("Bing service error", e);
		}
	}

	public Geolocation geocodingAbbreviation(String abbreviation) throws GeoCoderException {
		return geocodingFullForm(abbreviation);
	}
	
	public GeoDetail geocodingLocation(String locationString) throws GeoCoderException {
		try {
			return extractGeoDetail(requestBingService(locationString, applicationId));
		} catch (IOException e) {
			throw new GeoCoderException("Bing service error", e);
		} catch (JAXBException e) {
			throw new GeoCoderException("Bing service error", e);
		} catch (InvalidUrlException e) {
			throw new GeoCoderException("Bing service error", e);
		} catch (NetworkConnectionException e) {
			throw new GeoCoderException("Bing service error", e);
		}
	}
	
	private GeoDetail extractGeoDetail(Response response) throws GeoCoderException {
		
		/*
		 * Convert result to geoDetail and return the result
		 */
		Geolocation geolocation = extractGeoLocation(response);
		if (geolocation != null) {
			List<Location> locations = response.getResourceSets().getResourceSet().getResources().getLocation();
			Location location = locations.get(0);
			Address address = location.getAddress();
			return new GeoDetail(
					
					geolocation, 
					USZipCode.parse(address.getPostalCode()), 
					address.getAddressLine(), //street
					address.getLocality(), //city
					address.getAdminDistrict(), //state
					address.getCountryRegion(), //country
					address.getAdminDistrict2()); //county
		}
		
		throw new GeoCoderException("No result found");
	}
	
	private Geolocation extractGeoLocation(Response response) throws GeoCoderException {
		/*
		 * Convert result to geolocation and return the result
		 */
		List<Location> locations = null;
		try {
		 locations = response.getResourceSets().getResourceSet().getResources().getLocation();
		}
		catch(NullPointerException e){
			throw new GeoCoderException("No result found");
		}
		if (locations.size()>0) {
			
			Location location = locations.get(0);			
			/* getLatitude() and getLongitude() return some odd result sometime */
			return new Geolocation(new Double(location.getPoint().getLatitude()), 
											new Double(location.getPoint().getLongitude()));
		}
		
		throw new GeoCoderException("No result found");
	}
}
