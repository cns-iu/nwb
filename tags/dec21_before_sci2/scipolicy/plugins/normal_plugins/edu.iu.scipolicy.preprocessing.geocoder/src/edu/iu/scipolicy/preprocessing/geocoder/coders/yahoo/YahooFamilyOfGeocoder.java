package edu.iu.scipolicy.preprocessing.geocoder.coders.yahoo;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.iu.scipolicy.preprocessing.geocoder.coders.FamilyOfGeocoders;
import edu.iu.scipolicy.preprocessing.geocoder.coders.Geocoder;
import edu.iu.scipolicy.preprocessing.geocoder.coders.Geocoder.CODER_TYPE;

/**
 * 
 * YahooFamilyGeoCoder provides coders based on Yahoo PlaceFinder
 * geocoding web service. Since Yahoo limited 50k query per user
 * per 24 hours, User have to registered an account from Yahoo
 * at http://developer.yahoo.com/geo/placefinder/ and uses the 
 * given application id to query Yahoo service
 * @author kongch
 *
 */

public class YahooFamilyOfGeocoder implements FamilyOfGeocoders {

	private String applicationId;
	private static final CODER_TYPE[] SUPPORTED_CODERS = {	CODER_TYPE.COUNTRY, 
															CODER_TYPE.US_STATE,
															CODER_TYPE.US_ZIP_CODE,
															CODER_TYPE.ADDRESS	};
	
	public YahooFamilyOfGeocoder() {
	}
	
	public YahooFamilyOfGeocoder(String applicationId) {
		this.setApplicationId(applicationId);
	}

	public FAMILY_TYPE getFamilyType() {
		return FAMILY_TYPE.Yahoo;
	}

	public Geocoder getCountryCoder() {
		return new YahooCountryCoder(this.applicationId);
	}

	public Geocoder getStateCoder() {
		return new YahooStateCoder(this.applicationId);
	}

	public Geocoder getZipCodeCoder() {
		return new YahooZipCodeCoder(this.applicationId);
	}

	public Geocoder getAddressCoder() {
		return new YahooAddressCoder(this.applicationId);
	}

	public Set<CODER_TYPE> getSupportedCoders() {
		Set<CODER_TYPE> set = new HashSet<CODER_TYPE>();
		set.addAll(Arrays.asList(SUPPORTED_CODERS));
		return Collections.unmodifiableSet(set);
	}
	
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId.trim();
	}
}
