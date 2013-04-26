package edu.iu.sci2.preprocessing.geocoder.coders.bing;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.cishell.utilities.network.DownloadHandler.InvalidUrlException;
import org.cishell.utilities.network.DownloadHandler.NetworkConnectionException;

import edu.iu.sci2.preprocessing.geocoder.coders.bing.placefinder.PlaceFinderClient;
import edu.iu.sci2.preprocessing.geocoder.coders.bing.placefinder.beans.Response;

public class BingCountryCoder extends AbstractBingCoder {
	
	public BingCountryCoder(String applicationId) {
		super(applicationId);
	}

	@Override
	public CODER_TYPE getLocationType() {
		return CODER_TYPE.COUNTRY;
	}

	@Override
	public Response requestBingService(String location, String applicationId)
			throws IOException, JAXBException, InvalidUrlException,
			NetworkConnectionException {
		return PlaceFinderClient.requestCountry(location, applicationId);
	}
}
