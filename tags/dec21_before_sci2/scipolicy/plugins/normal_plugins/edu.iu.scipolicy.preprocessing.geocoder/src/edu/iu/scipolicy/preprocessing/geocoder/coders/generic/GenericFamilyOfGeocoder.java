package edu.iu.scipolicy.preprocessing.geocoder.coders.generic;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleContext;

import edu.iu.scipolicy.preprocessing.geocoder.coders.FamilyOfGeocoders;
import edu.iu.scipolicy.preprocessing.geocoder.coders.Geocoder;
import edu.iu.scipolicy.preprocessing.geocoder.coders.Geocoder.CODER_TYPE;

/**
 * 
 * GenericFamilyGeoCoder provides coders based on generic solution
 * BundleContext is used to pre-loaded geocoded data to each coder.
 * @author kongch
 *
 */
public class GenericFamilyOfGeocoder implements FamilyOfGeocoders {
	private static final String COUNTRY_GEO_CODE_FILE_PATH = "countries_geo_code.txt";
	private static final String US_STATE_GEO_CODE_FILE_PATH = "us_states_geo_code.txt";
	private static final String US_ZIP_CODE_GEO_CODE_FILE_PATH = "us_zipcode_geo_code.csv";

	private Map<CODER_TYPE, URL> coderTypeToUrl;
	
	public GenericFamilyOfGeocoder(BundleContext bundleContext) {
		coderTypeToUrl = new HashMap<CODER_TYPE, URL>();
		coderTypeToUrl.put(CODER_TYPE.US_STATE, 
							bundleContext.getBundle().getResource(US_STATE_GEO_CODE_FILE_PATH));
		coderTypeToUrl.put(CODER_TYPE.COUNTRY, 
							bundleContext.getBundle().getResource(COUNTRY_GEO_CODE_FILE_PATH));
		coderTypeToUrl.put(CODER_TYPE.US_ZIP_CODE,	
							bundleContext.getBundle().getResource(US_ZIP_CODE_GEO_CODE_FILE_PATH));
	}
	
	public FAMILY_TYPE getFamilyType() {
		return FAMILY_TYPE.Generic;
	}

	public Geocoder getCountryCoder() {
		return new CountryCoder(coderTypeToUrl.get(CODER_TYPE.COUNTRY));
	}

	public Geocoder getStateCoder() {
		return new StateCoder(coderTypeToUrl.get(CODER_TYPE.US_STATE));
	}

	public Geocoder getZipCodeCoder() {
		return new ZipCodeCoder(coderTypeToUrl.get(CODER_TYPE.US_ZIP_CODE));
	}

	/* Not supported for generic */
	public Geocoder getAddressCoder() {
		return null;
	}

	public Set<CODER_TYPE> getSupportedCoders() {
		return Collections.unmodifiableSet(coderTypeToUrl.keySet());
	}
}
