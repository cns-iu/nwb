package edu.iu.sci2.preprocessing.geocoder.coders.bing;

import org.osgi.service.component.ComponentContext;

import edu.iu.sci2.preprocessing.geocoder.AbstractGeocoderFactory;

/**
 * 
 * Geocoder factory for Bing Geocoding.
 * @author kongch
 *
 */
public class BingGeocoderFactory extends AbstractGeocoderFactory {
	public static final String[] SUPPORTED_PLACE_TYPE = {   ADDRESS, 
															COUNTRY, 
															US_STATE, 
															US_ZIP_CODE};
	
	@Override
	protected void activate(ComponentContext componentContext) {
		this.setFamilyGeocoder(new BingFamilyOfGeocoder());
	}

	@Override
	protected String[] getPlaceTypeOptionLabels() {
		return SUPPORTED_PLACE_TYPE;
	}

	@Override
	protected String[] getPlaceTypeOptionValues() {
		return SUPPORTED_PLACE_TYPE;
	}
}
