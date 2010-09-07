package edu.iu.scipolicy.model.geocode;



/**
 * 
 * Data Model that represent US Zip Code.
 * @author kongch
 *
 */
public class USDistrict {
	public static final String ATTRIBUTE_SEPARATOR = "\t";
	private final String label;
	private final Geolocation geolocation;
	
	public USDistrict(String district, Geolocation geolocation) {
		this.label = district.trim();
		this.geolocation = geolocation;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public Geolocation getGeolocation() {
		return this.geolocation;
	}
	
	public static USDistrict valueOf(String districtString) {
		String[] tokens = districtString.split(ATTRIBUTE_SEPARATOR);
		if (tokens.length == 2) {
			Geolocation geolocation = Geolocation.valueOf(tokens[1]);
			return new USDistrict(tokens[0], geolocation);
		}
		throw new IllegalArgumentException(districtString);
	}
	
	@Override
	public String toString() {
		return this.label + ATTRIBUTE_SEPARATOR + this.geolocation.toString();
	}
}
