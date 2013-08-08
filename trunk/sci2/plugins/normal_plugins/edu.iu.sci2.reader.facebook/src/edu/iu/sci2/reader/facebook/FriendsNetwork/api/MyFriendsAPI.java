package edu.iu.sci2.reader.facebook.FriendsNetwork.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.iu.sci2.reader.facebook.FriendsNetwork.utilities.DownloadHandler;
import edu.iu.sci2.reader.facebook.FriendsNetwork.utilities.DownloadHandler.InvalidUrlException;
import edu.iu.sci2.reader.facebook.FriendsNetwork.utilities.DownloadHandler.NetworkConnectionException;

public class MyFriendsAPI implements FaceBookAPI {

	@Override
	public String callAPI(String token, String id) {
		try{
			URL url = new URL("https://graph.facebook.com/me/friends?"+token);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();		
			connection.setRequestMethod("GET");	
			
			return DownloadHandler.getResponse(connection);
		} catch (IOException e1) {
			//logger.log(LogService.LOG_INFO, e1.getMessage());
		} catch (InvalidUrlException e1) {
			//logger.log(LogService.LOG_INFO, e1.getMessage());
		} catch (NetworkConnectionException e1) {
			//logger.log(LogService.LOG_INFO, e1.getMessage());
		} 
		return "No data";	
	}

}
