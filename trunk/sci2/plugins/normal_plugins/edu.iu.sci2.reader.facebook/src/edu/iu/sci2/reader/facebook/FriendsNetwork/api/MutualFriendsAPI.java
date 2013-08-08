package edu.iu.sci2.reader.facebook.FriendsNetwork.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import edu.iu.sci2.reader.facebook.FriendsNetwork.utilities.DownloadHandler;
import edu.iu.sci2.reader.facebook.FriendsNetwork.utilities.DownloadHandler.InvalidUrlException;
import edu.iu.sci2.reader.facebook.FriendsNetwork.utilities.DownloadHandler.NetworkConnectionException;

public class MutualFriendsAPI implements FaceBookAPI {

	@Override
	public String callAPI(String token, String id) {
		try{
			   //logger.log(LogService.LOG_INFO,"Inside call");
				URL url = new URL("https://graph.facebook.com/fql?q=SELECT%20uid1,%20uid2%20FROM%20friend%20WHERE%20uid1%20IN%20(SELECT%20uid2%20FROM%20friend%20WHERE%20uid1%20=%20me()%20)%20AND%20uid2%20IN%20(SELECT%20uid1%20FROM%20friend%20WHERE%20uid2%20=%20me())&"+token);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");	
				String val = DownloadHandler.getResponse(connection);
				//logger.log(LogService.LOG_INFO,"value ="+val);
				return val;
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
