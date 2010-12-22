package edu.iu.sci2.model.geocode;

public class Geolocation {
	public static final String ATTRIBUTES_SEPARATOR = ",";
	public static final String COORDINATE_OPEN = "(";
	public static final String COORDINATE_CLOSE = ")";
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
	
	/**
	 * Parses a given geographical coordinate string to Geolocation. If invalid data 
	 * format is given, IllegalArgumentException will be thrown. Thrown 
	 * NullPointerException if null input is given
	 * @param coordinateString - geographical coordinate in (latitude, longitude) form.
	 * @return Returns Geolocation object that contains values  extracted from the given 
	 * input string
	 */
	public static Geolocation valueOf(String coordinateString) {
		if (coordinateString == null) {
			throw new NullPointerException();
		}
		
		coordinateString = coordinateString.replace(COORDINATE_OPEN, "");
		coordinateString = coordinateString.replace(COORDINATE_CLOSE, "");
		String[] pointsString = coordinateString.split(ATTRIBUTES_SEPARATOR);
		
		if (pointsString.length == 2) {
			try {
				return new Geolocation(Double.valueOf(pointsString[0].trim()), 
										Double.valueOf(pointsString[1].trim()));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		}
		
		throw new IllegalArgumentException(coordinateString);
	}
	
	public String toString() {
		return COORDINATE_OPEN + this.latitude
				+ ATTRIBUTES_SEPARATOR + this.longitude
				+ COORDINATE_CLOSE;
	}
}
