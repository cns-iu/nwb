package edu.iu.sci2.reader.facebook.FriendsNetwork;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.log.LogService;

import edu.iu.sci2.reader.facebook.utilities.CallAPI;
import edu.iu.sci2.reader.facebook.utilities.FacebookUtilities;
import edu.iu.sci2.reader.facebook.utilities.FriendPair;
import prefuse.data.Table;

public class FriendsNetwork implements Algorithm {
	private LogService logger;
	private List<FriendPair> pairList;
	private String token;
	private String data;

	public FriendsNetwork(CIShellContext ciShellContext, String token) {
		this.token = token;
		this.data = "access_token=" + token;
		this.logger = (LogService) ciShellContext.getService(LogService.class.getName());
		pairList = new ArrayList<FriendPair>();

	}

	public Data[] execute() throws AlgorithmExecutionException {
		Table table = null;
		if (FacebookUtilities.isValidToken(token, logger)) {
			table = getFriendsNetwork(token);
		} else {
			logger.log(
					LogService.LOG_ERROR,
					"You have failed to submit a valid access token. The time has either expired on your current access token, or you did not provide one. Please obtain a new access token by going to \"Facebook->Access Token\" on the File menu.");
			return null;
		}
		return generateOutData(table);
	}

	public Table getFriendsNetwork(String token)
			throws AlgorithmExecutionException {
		Table table = null;
		HashMap<Long, String> idToName = new HashMap<Long, String>();

		try {
			String myName = FacebookUtilities.getMyName(data, logger);
			String myId = FacebookUtilities.getMyId(data, logger);
			idToName.put(Long.parseLong(myId), myName);

			// gets all information about each of the authenticated user's
			// friends
			JSONObject friendsObj = new JSONObject(CallAPI.MyFriendsAPI(data,
					logger));
			JSONArray jsonFriendsArray = friendsObj.getJSONArray("data");
			for (int i = 0; i < jsonFriendsArray.length(); i++) {
				JSONObject currentResult = jsonFriendsArray.getJSONObject(i);
				String friendOneName = currentResult.getString("name");
				Long id = currentResult.getLong("id");
				idToName.put(id, friendOneName);
				pairList.add(new FriendPair(myName, friendOneName));
			}

			// retrieves an array of mutual friends
			JSONObject mutualFriendsObject = new JSONObject(
					CallAPI.MutualFriendsAPI(data, logger));
			JSONArray jsonMutualArray = mutualFriendsObject
					.getJSONArray("data");

			// gets a list of all events attended by the authenticated user's
			// friends
			JSONObject friendsEventObj = new JSONObject(
					CallAPI.FriendsEventAPI(data, logger));
			JSONArray jsonEventArr = friendsEventObj.getJSONArray("data");

			// converts each eventID into a name, and changes the separation
			// format
			HashMap<Long, String> eventIdToName = new HashMap<Long, String>();
			for (int i = 0; i < jsonEventArr.length(); i++) {
				JSONObject currentResult = jsonEventArr.getJSONObject(i);
				String eventName = currentResult.getString("name");

				Long id = currentResult.getLong("eid");
				eventIdToName.put(id, eventName.replace("|", ","));
			}

			// retrieves a list of events attended by you and your friends,
			// or your friends and their friends who are also your friends
			JSONObject friendsCommonEventObject = new JSONObject(
					CallAPI.FriendsCommonEventAPI(data, logger));
			JSONArray jsonCommonEventArr = friendsCommonEventObject
					.getJSONArray("data");

			Map<Long, ArrayList<String>> commonEvents = new HashMap<Long, ArrayList<String>>();
			for (int i = 0; i < jsonCommonEventArr.length(); i++) {
				JSONObject currentResult = jsonCommonEventArr.getJSONObject(i);
				Long uid = currentResult.getLong("uid");
				Long eid = currentResult.getLong("eid");
				if (commonEvents.get(eid) == null) {
					ArrayList<String> uidList = new ArrayList<String>();
					uidList.add(idToName.get(uid));
					commonEvents.put(eid, uidList);
				} else {
					ArrayList<String> uidList = commonEvents.get(eid);
					uidList.add(idToName.get(uid));
					commonEvents.put(eid, uidList);
				}
			}
			for (int i = 0; i < jsonMutualArray.length(); i++) {
				JSONObject currentResult = jsonMutualArray.getJSONObject(i);
				Long id1 = currentResult.getLong("uid1");
				Long id2 = currentResult.getLong("uid2");
				FriendPair fp = new FriendPair(idToName.get(id1),
						idToName.get(id2));
				pairList.add(fp);
			}

			// gets the number of shared friends between two users
			JSONObject MutualFriendsNumObject = new JSONObject(
					CallAPI.NumberOfMutualFriendsAPI(data, logger));
			JSONArray jsonMutualFriendsNum = MutualFriendsNumObject
					.getJSONArray("data");

			HashMap<String, Long> nameToNumMap = new HashMap<String, Long>();
			for (int i = 0; i < jsonMutualFriendsNum.length(); i++) {

				JSONObject currentResult = jsonMutualFriendsNum
						.getJSONObject(i);
				String name = currentResult.getString("name");
				Long numOfMutualFriends = currentResult
						.getLong("mutual_friend_count");
				nameToNumMap.put(name, numOfMutualFriends);
			}

			for (FriendPair fp : pairList) {
				for (Map.Entry<Long, ArrayList<String>> entry : commonEvents
						.entrySet()) {
					ArrayList<String> uid = (ArrayList<String>) entry
							.getValue();
					if (uid.contains(fp.getName1())
							&& uid.contains(fp.getName2())) {
						fp.setCommonEvent(eventIdToName.get(entry.getKey()));
					}
					if (fp.getName1() == myName
							&& nameToNumMap.containsKey(fp.getName2())) {
						fp.setNumOfMutualFriends(nameToNumMap.get(fp.getName2()));
					}
				}
			}
			table = generateTabularData(pairList);
		} catch (JSONException e) {
			throw new AlgorithmExecutionException(
					"An error has occurred while trying to reach the authentication URL. Please report to cns-sci2-help-l@iulist.indiana.edu",
					e);
		}
		return table;
	}

	public Table generateTabularData(List<FriendPair> list) {
		Table table = new Table();
		table.addColumn("Facebook User 1", String.class);
		table.addColumn("Facebook User 2", String.class);
		table.addColumn("Number of Common Events Attended", Integer.class);
		table.addColumn("Number Of Mutual Friends", String.class);
		for (FriendPair pair : list) {
			int rowNumber = table.addRow();
			table.set(rowNumber, "Facebook User 1", pair.getName1());
			table.set(rowNumber, "Facebook User 2", pair.getName2());
			table.set(rowNumber, "Number of Common Events Attended",
					pair.getNumberOfCommonEvents());
			table.set(rowNumber, "Number Of Mutual Friends", pair
					.getNumOfMutualFriends().toString());

		}
		return table;
	}

	private Data[] generateOutData(Table table) {
		Data output = new BasicData(table, Table.class.getName());
		Dictionary<String, Object> metadata = output.getMetadata();
		metadata.put(DataProperty.LABEL, "MutualFriendsNetwork");
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		return new Data[] { output };
	}

}