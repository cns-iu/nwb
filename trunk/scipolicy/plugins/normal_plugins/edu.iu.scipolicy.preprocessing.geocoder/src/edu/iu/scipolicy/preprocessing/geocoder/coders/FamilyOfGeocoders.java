package edu.iu.scipolicy.preprocessing.geocoder.coders;

import java.util.Set;

import edu.iu.scipolicy.preprocessing.geocoder.coders.Geocoder.CODER_TYPE;

/**
 * 
 * Interface for Geocoder family. The family could be categorized
 * as Generic family, Yahoo family, etc. Each family provides the
 * supported coders by implementing this interface
 * @author kongch
 *
 */
public interface FamilyOfGeocoders {

	/* Existing families */
	public static enum FAMILY_TYPE { Generic, Yahoo };
	
	/* Family type */
	public FAMILY_TYPE getFamilyType();
	
	/* Supported coders */
	public Geocoder getCountryCoder();
	public Geocoder getStateCoder();
	public Geocoder getZipCodeCoder();
	public Geocoder getAddressCoder();
	
	/* List of supported coders */
	public Set<CODER_TYPE> getSupportedCoders();

}
