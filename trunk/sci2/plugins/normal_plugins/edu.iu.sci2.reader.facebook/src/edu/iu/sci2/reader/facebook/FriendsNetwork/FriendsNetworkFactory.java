package edu.iu.sci2.reader.facebook.FriendsNetwork;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

//TODO: Fix all warning
public class FriendsNetworkFactory implements AlgorithmFactory {
	public Algorithm createAlgorithm(Data[] data, Dictionary<String, Object> parameters,
			CIShellContext ciShellContext) {
		return new FriendsNetwork(ciShellContext, parameters.get("accessToken").toString());
	}
}