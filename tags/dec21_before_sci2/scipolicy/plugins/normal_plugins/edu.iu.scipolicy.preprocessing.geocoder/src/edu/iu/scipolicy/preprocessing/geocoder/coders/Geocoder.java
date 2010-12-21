package edu.iu.scipolicy.preprocessing.geocoder.coders;

import edu.iu.scipolicy.model.geocode.Geolocation;

/**
 * 
 * New Geocoder interface favor by OO concept. Avoid returning Map 
 * structure which abandon the encapsulation concept. Third party 
 * should not need to understand the underlying implemented structure.
 * 
 * It also speed up the generic geocoder abbreviation geocoder look up
 * by eliminated the additional lookup of abbreviation to fullForm.
 * @author kongch
 *
 */
public interface Geocoder {
	public static enum CODER_TYPE { ADDRESS, COUNTRY, US_STATE, US_ZIP_CODE };
	public CODER_TYPE getLocationType();
	public Geolocation geocodingFullForm(String fullForm) throws GeoCoderException;
	public Geolocation geocodingAbbreviation(String abbreviation) throws GeoCoderException;
}