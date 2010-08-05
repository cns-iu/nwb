package edu.iu.scipolicy.preprocessing.geocoder.coders;

import java.util.Map;

public interface GeoCoder {
	public String getLocationType();
	public Map<String, GeoLocation> getFullFormsToLocations();
	public Map<String, String> getAbbreviationsToFullForms();
}
