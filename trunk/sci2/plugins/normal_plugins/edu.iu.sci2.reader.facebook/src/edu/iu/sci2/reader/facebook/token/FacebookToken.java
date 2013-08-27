package edu.iu.sci2.reader.facebook.token;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

public class FacebookToken implements Algorithm {
	private LogService logger;

	public FacebookToken(Data[] data, CIShellContext ciShellContext) {
		this.logger = (LogService) ciShellContext.getService(LogService.class
				.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException {
		try {
			URI url = new URI(
					"https://www.facebook.com/dialog/oauth?client_id=283202715139589"
							+ "&redirect_uri=https://morning-fjord-1741.herokuapp.com/token.php&scope=manage_friendlists"
							+ "&response_type=token"
							+ "&scope=email,user_about_me,user_activities,user_birthday,user_education_history,"
							+ "user_events,user_hometown,user_interests,friends_interests,user_likes,user_groups,friends_hometown,friends_birthday,user_location,friends_location,user_religion_politics,friends_religion_politics,friends_events,read_friendlists,friends_relationships,friends_status,friends_about_me");
			Desktop.getDesktop().browse(url);
		} catch (URISyntaxException e1) {
			throw new AlgorithmExecutionException(
							"An error has occurred while trying to reach the authentication URL. Please report to cns-sci2-help-l@iulist.indiana.edu", e1); 
		} catch (IOException e1) {
			this.logger.log(LogService.LOG_ERROR, "Couldn't find or launch a default browser. Please set or install a default browser and try again.");
		}
		return null;
	}
}