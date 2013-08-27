package edu.iu.sci2.reader.facebook.FacebookFriend;

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

import edu.iu.sci2.reader.facebook.utilities.CallAPI;
import edu.iu.sci2.reader.facebook.utilities.FacebookUtilities;
import edu.iu.sci2.reader.facebook.utilities.FriendObject;

import org.osgi.service.log.LogService;

import prefuse.data.Table;

public class FacebookFriends implements Algorithm {
	private LogService logger;
	private HashMap<String, FriendObject> idDataList;
	private String token;

	public FacebookFriends(CIShellContext ciShellContext, String token) {
		this.logger = (LogService) ciShellContext.getService(LogService.class
				.getName());
		this.token = token;
		idDataList = new HashMap<String, FriendObject>();
	}

	public Data[] execute() throws AlgorithmExecutionException {
		if (FacebookUtilities.isValidToken(token)) {
			return generateOutData(getFriendsData(token));
		} else {
			logger.log(
					LogService.LOG_ERROR,
					"The Access Token is not valid, to get a new Access token select \"Facebook->Access Token\" Menu.");
			return null;
		}
	}

	// TODO: This function should be modularized into FacebookUtilities
	private Table getFriendsData(String token) throws AlgorithmExecutionException {
		String data = "access_token=" + token;
		Table table = null;
		JSONObject obj;
		try {
			
			JSONObject friendsObj = new JSONObject(CallAPI.FriendsDataAPI(data, logger));
			JSONArray friendsArray = friendsObj.getJSONArray("data");
			
			for (int i = 0; i < friendsArray.length(); i++) {
				JSONObject currentResult = friendsArray.getJSONObject(i);
				Long id = currentResult.getLong("uid");
				FriendObject fd = new FriendObject(id);

				//get friend features
				fd.setName(currentResult.getString("name"));
				fd.setId(id);
				fd.setGender(currentResult.getString("sex"));
				fd.setHomeTownLocation(FacebookUtilities
						.getFriendsHometown(currentResult));
				fd.setCurrentLocation(FacebookUtilities
						.getFriendsCurrentLocation(currentResult));
				fd.setStatus(FacebookUtilities.getStatus(currentResult));
				fd.setInterest(FacebookUtilities.getInterests(currentResult));
				fd.setBirthday(FacebookUtilities.getBirthday(currentResult));
				fd.setReligion(FacebookUtilities.getReligion(currentResult));
				fd.setPoliticalView(FacebookUtilities
						.getPolitical(currentResult));
				fd.setRelationShipStatus(FacebookUtilities
						.getRelationship(currentResult));

				idDataList.put(id.toString(), fd);
			}

			obj = new JSONObject(CallAPI.FriendsEventAPI(data, logger));
			JSONArray jsonArrayEvent = obj.getJSONArray("data");

			HashMap<Long, String> eventIdToName = new HashMap<Long, String>();
			for (int i = 0; i < jsonArrayEvent.length(); i++) {
				JSONObject currentResult = jsonArrayEvent.getJSONObject(i);
				String eventName = currentResult.getString("name");
				Long id = currentResult.getLong("eid");
				eventIdToName.put(id, eventName);
			}

			obj = new JSONObject(CallAPI.FriendsCommonEventAPI(data, logger));
			JSONArray jsonArrayCommonEvent = obj.getJSONArray("data");

			for (int i = 0; i < jsonArrayCommonEvent.length(); i++) {
				JSONObject currentResult = jsonArrayCommonEvent
						.getJSONObject(i);
				Long uid = currentResult.getLong("uid");
				Long eid = currentResult.getLong("eid");

				FriendObject fd = idDataList.get(uid.toString());
				if (fd == null || eid == null)
					continue;
				if (eventIdToName.get(eid) != null)
					fd.addEvent(eventIdToName.get(eid).replace("|", ","));
			}
			table = generateTabularData(idDataList);
		} catch (JSONException e) {
			throw new AlgorithmExecutionException(
					"An error has occurred while trying to reach the authentication URL. Please report to cns-sci2-help-l@iulist.indiana.edu", e); 
		}
		return table;
	}

	public Table generateTabularData(HashMap<String, FriendObject> map) {
		// Create Output Table
		Table table = new Table();
		table.addColumn("Person Name", String.class);
		table.addColumn("Facebook Id", Long.class);
		table.addColumn("Status", String.class);
		table.addColumn("Gender", String.class);
		table.addColumn("Current Location", String.class);
		table.addColumn("Hometown", String.class);
		table.addColumn("Birthday", String.class);
		table.addColumn("Interests", String.class);
		table.addColumn("Religion", String.class);
		table.addColumn("Political View", String.class);
		table.addColumn("Relationship Status", String.class);
		table.addColumn("Events", String.class);

		// Insert data into output table
		for (Map.Entry<String, FriendObject> entry : map.entrySet()) {

			FriendObject data = entry.getValue();

			List<String> eventArray = data.getEventList();
			String eventStr = "";
			if (!eventArray.isEmpty()) {
				for (String event : eventArray) {
					eventStr = eventStr + event + "|";
				}
				eventStr = eventStr.substring(0, eventStr.length() - 1);
			}
			int rowNumber = table.addRow();
			table.set(rowNumber, "Person Name", data.getName());
			table.set(rowNumber, "Facebook Id", data.getId());
			table.set(rowNumber, "Status", data.getStatus());
			table.set(rowNumber, "Gender", data.getGender());
			table.set(rowNumber, "Current Location", data.getCurrentLocation());
			table.set(rowNumber, "Hometown", data.getHomeTownLocation());
			table.set(rowNumber, "Birthday", data.getBirthday());
			table.set(rowNumber, "Interests", data.getInterest());
			table.set(rowNumber, "Religion", data.getReligion());
			table.set(rowNumber, "Political View", data.getPoliticalView());
			table.set(rowNumber, "Relationship Status",
					data.getRelationShipStatus());
			table.set(rowNumber, "Events", eventStr);
		}
		logger.log(LogService.LOG_INFO,
				"Please check the Data Manager for generated data.");
		return table;
	}

	private Data[] generateOutData(Table table) {
		Data output = new BasicData(table, Table.class.getName());
		Dictionary<String, Object> metadata = output.getMetadata();
		metadata.put(DataProperty.LABEL, "FacebookFriendsData");
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		return new Data[] { output };
	}
}