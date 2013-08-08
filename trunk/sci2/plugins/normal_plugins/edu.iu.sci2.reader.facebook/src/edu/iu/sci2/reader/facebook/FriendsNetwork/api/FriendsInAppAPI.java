package edu.iu.sci2.reader.facebook.FriendsNetwork.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.osgi.service.log.LogService;
import edu.iu.sci2.reader.facebook.FriendsNetwork.utilities.DownloadHandler;
import edu.iu.sci2.reader.facebook.FriendsNetwork.utilities.DownloadHandler.InvalidUrlException;
import edu.iu.sci2.reader.facebook.FriendsNetwork.utilities.DownloadHandler.NetworkConnectionException;

public class FriendsInAppAPI implements FaceBookAPI {
    private LogService logger;
//    private String title = "Facebook API Information";
//    private String message = " Hey hi!!";
//    private String detail = "Hey het hey!!";
    //private guibuilder abc;
	public FriendsInAppAPI(LogService logger)
	{
		this.logger = logger;
	}
	@Override
	public String callAPI(String token, String id) {
		try{
			//logger.log(LogService.LOG_INFO,"Inside call");
			URL url = new URL("https://graph.facebook.com/fql?q=SELECT%20uid,%20name%20FROM%20user%20WHERE%20is_app_user=1%20AND%20uid%20IN(SELECT%20uid2%20FROM%20friend%20WHERE%20uid1=me())&"+token);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();		
			connection.setRequestMethod("GET");	
			//showInformation(title, message, detail);
			String val = DownloadHandler.getResponse(connection);
			logger.log(LogService.LOG_INFO,"value ="+val);
			return val;
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
