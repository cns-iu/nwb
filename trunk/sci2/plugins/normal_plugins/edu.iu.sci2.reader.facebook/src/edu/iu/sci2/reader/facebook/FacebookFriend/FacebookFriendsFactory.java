package edu.iu.sci2.reader.facebook.FacebookFriend;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class FacebookFriendsFactory implements AlgorithmFactory {
	public Algorithm createAlgorithm(Data[] data,
			Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		return new FacebookFriends(ciShellContext, parameters.get("accessToken").toString());
	}
}