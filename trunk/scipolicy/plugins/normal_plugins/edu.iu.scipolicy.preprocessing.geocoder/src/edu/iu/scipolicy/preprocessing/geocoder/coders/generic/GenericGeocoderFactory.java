package edu.iu.scipolicy.preprocessing.geocoder.coders.generic;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import edu.iu.scipolicy.preprocessing.geocoder.AbstractGeocoderFactory;

/**
 * 
 * This is create after re-factored. It obtains the 
 * functionality of GeocoderFactory.
 * @author kongch
 *
 */
public class GenericGeocoderFactory extends AbstractGeocoderFactory {

	private static final String[] SUPPORTED_PLACE_TYPE = { COUNTRY, US_STATE, US_ZIP_CODE };
	
	/*
	 * Fetch the Latitude & Longitude values from the text files in to appropriate Maps.
	 * This will be done only once.
	 */
	@Override
	protected void activate(ComponentContext componentContext) {
		BundleContext bundleContext = componentContext.getBundleContext();
		this.setFamilyGeocoder(new GenericFamilyOfGeocoder(bundleContext));
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
