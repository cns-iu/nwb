package edu.iu.sci2.reader.facebook.utilities;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.utilities.network.DownloadHandler;
import org.cishell.utilities.network.DownloadHandler.InvalidUrlException;
import org.cishell.utilities.network.DownloadHandler.NetworkConnectionException;
import org.osgi.service.log.LogService;

@SuppressWarnings("deprecation")
public final class CallAPI {
	private CallAPI() {
	}

	private static String callAPI(String token, String strURL, LogService logger) throws AlgorithmExecutionException {
		try {
			URL url = new URL(strURL + token);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			String val = DownloadHandler.getResponse(connection);
			return val;
		} catch (InvalidUrlException e) {
			throw new AlgorithmExecutionException(
					"An error has occurred while trying to reach the authentication URL. Please report to cns-sci2-help-l@iulist.indiana.edu", e); 
		} catch (NetworkConnectionException e) {
			logger.log(
					LogService.LOG_ERROR,
					"Cannot connect to the authentication server. Please check your connection and try again.");
		} catch (IOException e) {
			logger.log(
					LogService.LOG_ERROR, "Failed to create connection.");
		}

		return "No data";
	}

	public static String FacebookAPI(String token, String strURL,
			LogService logger) throws AlgorithmExecutionException {
		return callAPI(token, strURL, logger);
	}

	public static String CheckLogin() throws AlgorithmExecutionException {
		// This originally returned "0". Look for changes in the output
		return callAPI("",
				"https://morning-fjord-1741.herokuapp.com/CheckLogin.php", null);
	}

	public static String FriendsCommonEventAPI(String token, LogService logger) throws AlgorithmExecutionException {
		return callAPI(
				token,
				"https://graph.facebook.com/fql?q=SELECT+eid,+uid+FROM+event_member+WHERE+uid+IN(SELECT+uid2+FROM+friend+WHERE+uid1=me())+OR+uid=me()&",
				logger);
	}

	public static String FriendsEventAPI(String token, LogService logger) throws AlgorithmExecutionException {
		return callAPI(
				token,
				"https://graph.facebook.com/fql?q=SELECT+name,+eid+FROM+event+WHERE+eid+IN(SELECT+eid+FROM+event_member+WHERE+rsvp_status='attending'+AND+uid+IN(SELECT+uid2+FROM+friend+WHERE+uid1=me())+OR+uid=me())&",
				logger);
	}

	public static String DetailsAPI(String token, LogService logger) throws AlgorithmExecutionException {
		return callAPI(token, "https://graph.facebook.com/me?", logger);
	}

	public static String FriendsDataAPI(String token, LogService logger) throws AlgorithmExecutionException {
		return callAPI(
				token,
				"https://graph.facebook.com/fql?q=select+uid,name,status,current_location,hometown_location,religion,political,birthday,relationship_status,sex,interests+from+user+where+uid+in+(select+uid2+from+friend+where+uid1=me())&",
				logger);
	}

	public static String MutualFriendsAPI(String token, LogService logger) throws AlgorithmExecutionException {
		return callAPI(
				token,
				"https://graph.facebook.com/fql?q=SELECT%20uid1,%20uid2%20FROM%20friend%20WHERE%20uid1%20IN%20(SELECT%20uid2%20FROM%20friend%20WHERE%20uid1%20=%20me()%20)%20AND%20uid2%20IN%20(SELECT%20uid1%20FROM%20friend%20WHERE%20uid2%20=%20me())&",
				logger);
	}

	public static String FriendsInAppAPI(String token, LogService logger) throws AlgorithmExecutionException {
		return callAPI(
				token,
				"https://graph.facebook.com/fql?q=SELECT%20uid,%20name%20FROM%20user%20WHERE%20is_app_user=1%20AND%20uid%20IN(SELECT%20uid2%20FROM%20friend%20WHERE%20uid1=me())&",
				logger);
	}

	public static String FriendsWithFriends(String token, String id,
			LogService logger) throws AlgorithmExecutionException {
		return callAPI(token,
				("https://graph.facebook.com/" + id + "/friends?"), logger);
	}

	public static String MyFriendsAPI(String token, LogService logger) throws AlgorithmExecutionException {
		return callAPI(token, "https://graph.facebook.com/me/friends?", logger);
	}

	public static String NumberOfMutualFriendsAPI(String token,
			LogService logger) throws AlgorithmExecutionException {
		return callAPI(
				token,
				"https://graph.facebook.com/fql?q=SELECT+uid,name,mutual_friend_count+FROM+user+WHERE+uid+IN(SELECT+uid1+FROM+friend+WHERE+uid2=me())&",
				logger);
	}
}
