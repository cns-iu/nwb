package edu.iu.sci2.reader.facebook.FacebookFriend.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.osgi.service.log.LogService;

import edu.iu.sci2.reader.facebook.FacebookFriend.utilities.DownloadHandler;
import edu.iu.sci2.reader.facebook.FacebookFriend.utilities.DownloadHandler.InvalidUrlException;
import edu.iu.sci2.reader.facebook.FacebookFriend.utilities.DownloadHandler.NetworkConnectionException;

public class MyFriendsDataAPI implements FaceBookAPI {
    private LogService logger;
	public MyFriendsDataAPI(LogService logger)
	{
		this.logger =logger;
	}
	@Override
	public String callAPI(String token, String id) {
		try{
			URL url = new URL("https://graph.facebook.com/fql?q=select+uid,name,status,current_location,hometown_location,religion,political,birthday,relationship_status,sex,interests+from+user+where+uid+in+(select+uid2+from+friend+where+uid1=me())&"+token);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();		
			connection.setRequestMethod("GET");	
			return DownloadHandler.getResponse(connection);
		} catch (IOException e1) {
			logger.log(LogService.LOG_INFO, e1.getMessage());
		} catch (InvalidUrlException e1) {
			logger.log(LogService.LOG_INFO, e1.getMessage());
		} catch (NetworkConnectionException e1) {
			logger.log(LogService.LOG_INFO, e1.getMessage());
		} 
		return "No data";	
	}

}
