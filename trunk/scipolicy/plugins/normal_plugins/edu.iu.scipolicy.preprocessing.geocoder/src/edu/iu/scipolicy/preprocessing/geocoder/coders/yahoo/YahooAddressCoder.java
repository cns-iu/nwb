package edu.iu.scipolicy.preprocessing.geocoder.coders.yahoo;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import edu.iu.scipolicy.preprocessing.geocoder.coders.yahoo.placefinder.PlaceFinderClient;
import edu.iu.scipolicy.preprocessing.geocoder.coders.yahoo.placefinder.beans.ResultSet;

public class YahooAddressCoder extends AbstractYahooCoder {
	
	public YahooAddressCoder(String applicationId) {
		super(applicationId);
	}

	@Override
	public CODER_TYPE getLocationType() {
		return CODER_TYPE.ADDRESS;
	}

	@Override
	public ResultSet requestYahooService(String location, String applicationId) 
												throws IOException, JAXBException {
		return PlaceFinderClient.requestAddress(location, applicationId);
	}
}
