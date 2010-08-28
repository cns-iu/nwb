package edu.iu.scipolicy.preprocessing.geocoder.coders;

public class Geolocation {
	
	private Double latitude;
	private Double longitude;
	
	public Geolocation(Double latitude, Double longitude) {
		this.setLatitude(latitude);
		this.setLongitude(longitude);
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLatitude() {
		return latitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
}
