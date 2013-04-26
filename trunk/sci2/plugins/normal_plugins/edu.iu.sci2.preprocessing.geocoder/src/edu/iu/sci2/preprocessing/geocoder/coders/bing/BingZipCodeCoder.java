package edu.iu.sci2.preprocessing.geocoder.coders.bing;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.cishell.utilities.network.DownloadHandler.InvalidUrlException;
import org.cishell.utilities.network.DownloadHandler.NetworkConnectionException;

import edu.iu.sci2.model.geocode.USZipCode;
import edu.iu.sci2.preprocessing.geocoder.coders.bing.placefinder.PlaceFinderClient;
import edu.iu.sci2.preprocessing.geocoder.coders.bing.placefinder.beans.Response;

public class BingZipCodeCoder extends AbstractBingCoder {
	
	public BingZipCodeCoder(String applicationId) {
		super(applicationId);
	}

	@Override
	public CODER_TYPE getLocationType() {
		return CODER_TYPE.US_ZIP_CODE;
	}

	@Override
	public Response requestBingService(String location, String applicationId)
			throws IOException, JAXBException, InvalidUrlException,
			NetworkConnectionException {
		return PlaceFinderClient.requestZipCode(
				USZipCode.parse(location).getUzip(), 						
				"United States", applicationId);
	}
}
