package edu.iu.sci2.preprocessing.geocoder.coders;

import edu.iu.sci2.model.geocode.GeoDetail;

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
public interface DetailGeocoder {
	public GeoDetail geocodingLocation(String abbreviation) throws GeoCoderException;
}
