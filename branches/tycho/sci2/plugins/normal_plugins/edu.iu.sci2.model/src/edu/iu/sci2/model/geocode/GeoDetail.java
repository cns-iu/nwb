package edu.iu.sci2.model.geocode;

public class GeoDetail {
	private Geolocation geolocation;
	private USZipCode zipCode;
	private String street;
	private String city;
	private String state;
	private String country;
	private String county;
	
	public GeoDetail(Geolocation geolocation, USZipCode zipCode, String street, String city, 
			String state, String country, String county) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.country = country;
		this.county = county;
		
		if (geolocation == null) {
			this.geolocation = new Geolocation(null, null);
		} else {
			this.geolocation = geolocation;
		}
		
		if (zipCode == null) {
			this.zipCode = new USZipCode(null, null);
		} else {
			this.zipCode = zipCode;
		}
	}
	
	public Geolocation getGeolocation() {
		return geolocation;
	}
	
	public USZipCode getZipCode() {
		return this.zipCode;
	}
	
	public String getStreet() {
		return street;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getState() {
		return state;
	}
	
	public String getCountry() {
		return country;
	}
	
	public String getCounty() {
		return county;
	}
	
	@Override
	public String toString() {
		return "street: " + this.street
			 + "zip code: " + this.zipCode
			 + "city: " + this.city
			 + "state: " + this.state
			 + "country: " + this.country
			 + "county: " + this.county;
	}
}
