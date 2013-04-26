package edu.iu.sci2.preprocessing.geocoder.coders.bing;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.iu.sci2.preprocessing.geocoder.coders.FamilyOfGeocoders;
import edu.iu.sci2.preprocessing.geocoder.coders.Geocoder;
import edu.iu.sci2.preprocessing.geocoder.coders.Geocoder.CODER_TYPE;

/**
 * 
 * BingFamilyGeoCoder provides coders based on Bing PlaceFinder
 * geocoding web service. Since Bing limited 50k query per user
 * per 24 hours, User have to registered an account from Bing
 * at http://developer.yahoo.com/geo/placefinder/ and uses the 
 * given application id to query Bing service
 * @author kongch
 *
 */

public class BingFamilyOfGeocoder implements FamilyOfGeocoders {

	private String applicationId;
	private static final CODER_TYPE[] SUPPORTED_CODERS = {	CODER_TYPE.COUNTRY, 
															CODER_TYPE.US_STATE,
															CODER_TYPE.US_ZIP_CODE,
															CODER_TYPE.ADDRESS	};
	
	public BingFamilyOfGeocoder() {
	}
	
	public BingFamilyOfGeocoder(String applicationId) {
		this.setApplicationId(applicationId);
	}

	public FAMILY_TYPE getFamilyType() {
		return FAMILY_TYPE.Bing;
	}

	public Geocoder getCountryCoder() {
		return new BingCountryCoder(this.applicationId);
	}

	public Geocoder getStateCoder() {
		return new BingStateCoder(this.applicationId);
	}

	public Geocoder getZipCodeCoder() {
		return new BingZipCodeCoder(this.applicationId);
	}

	public Geocoder getAddressCoder() {
		return new BingAddressCoder(this.applicationId);
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
