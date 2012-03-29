package edu.iu.sci2.preprocessing.geocoder.coders.yahoo;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import edu.iu.sci2.model.geocode.USZipCode;
import edu.iu.sci2.preprocessing.geocoder.coders.yahoo.placefinder.PlaceFinderClient;
import edu.iu.sci2.preprocessing.geocoder.coders.yahoo.placefinder.beans.ResultSet;

public class YahooZipCodeCoder extends AbstractYahooCoder {
	
	public YahooZipCodeCoder(String applicationId) {
		super(applicationId);
	}

	@Override
	public CODER_TYPE getLocationType() {
		return CODER_TYPE.US_ZIP_CODE;
	}

	@Override
	public ResultSet requestYahooService(String location, String applicationId)
												throws IOException, JAXBException {
		return PlaceFinderClient.requestZipCode(USZipCode.parse(location).getUzip(), 
												"United States", applicationId);
	}
}
