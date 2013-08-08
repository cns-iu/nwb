package edu.iu.sci2.reader.facebook.FriendsNetwork;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

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

import edu.iu.sci2.reader.facebook.FriendsNetwork.api.FaceBookAPI;
import edu.iu.sci2.reader.facebook.FriendsNetwork.api.FriendsCommonEventAPI;
import edu.iu.sci2.reader.facebook.FriendsNetwork.api.FriendsEventAPI;
import edu.iu.sci2.reader.facebook.FriendsNetwork.api.FriendsInAppAPI;
import edu.iu.sci2.reader.facebook.FriendsNetwork.api.FriendsWithFriendsAPI;
import edu.iu.sci2.reader.facebook.FriendsNetwork.api.MutualFriendsAPI;
import edu.iu.sci2.reader.facebook.FriendsNetwork.api.MyFriendsAPI;
import edu.iu.sci2.reader.facebook.FriendsNetwork.api.NumberOfMutualFriends;
import edu.iu.sci2.reader.facebook.FriendsNetwork.facade.Facade;
import edu.iu.sci2.reader.facebook.FriendsNetwork.model.FriendsPair;
import prefuse.data.Table;

public class FriendsNetwork implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext ciShellContext;
    private LogService logger;
	private List<FriendsPair> pairList;
	private Facade facade;
    
    public FriendsNetwork(Data[] data,
    				  Dictionary parameters,
    				  CIShellContext ciShellContext) {
        this.data = data;
        this.parameters = parameters;
        this.ciShellContext = ciShellContext;
        this.logger = (LogService) ciShellContext.getService(LogService.class
				.getName());

		pairList = new ArrayList<FriendsPair>();
		facade = new Facade(this.logger);

    }
    
    public Data[] execute() throws AlgorithmExecutionException {

    this.logger.log(LogService.LOG_INFO, "Call to Facebook API");
	this.logger
			.log(LogService.LOG_WARNING,
					"The use of the Facebook API is governed by following policies:");
	this.logger
			.log(LogService.LOG_WARNING,
					"This is a Facebook application that helps "
							+ "user export data out of Facebook for reuse in Visualization or any possible method of "
							+ "digital story telling. Data is exported in csv format. ");
	this.logger
			.log(LogService.LOG_WARNING,
					"According to Facebook's Statement of Rights and Responsibility. "
							+ "You own all of the content and information you post on Facebook, and you can control how it is shared through your privacy and application settings.");
	this.logger
			.log(LogService.LOG_INFO, "Please refer the following link:");
	this.logger.log(LogService.LOG_WARNING,
			"https://developers.facebook.com/policy");

	// String loginStatus = facade.checkLogin();
	//
	// if (loginStatus.equals("0")) {

	int confirmMsg = JOptionPane
			.showConfirmDialog(
					null,
					"Please login in your web browser and copy the access token returned to allow Sci2 to access your Friends infomation",
					"Are you ready to login in Web browser?",
					JOptionPane.YES_NO_OPTION);
	Table table = null;
	if (confirmMsg == JOptionPane.YES_OPTION) {
		this.logger.log(LogService.LOG_INFO, "Opening Facebook login page");
		String token =  facade.getAccessTokenFromTable(data);
		if(token.isEmpty()){			
			this.logger
			.log(LogService.LOG_ERROR, "The token extracted from the table is empty, please select the correct table");
	        return null;
		}
		if(facade.isValidToken(token)){
			    table = getFriendsNetwork(token);
			}
			else{
				this.logger
				.log(LogService.LOG_ERROR,"The Access token is expired, to get a new Access token select \"Facebook Reader->Access Token\" Menu.");							
			   return null;
	        }
	}
	//this.logger
	//.log(LogService.LOG_INFO, "generating output data");
	return generateOutData(table);
   }
   
public Table getFriendsNetwork(String token) {
		

		Table table = null;
		//this.logger.log(LogService.LOG_INFO, "Access Token: " + token);
		if (token != null) {
			String data = "access_token=" + token;
			String myName = "";
			String myId = "";
			try {
				myName = facade.getMyName(data);
				myId = facade.getMyId(data);
			} catch (JSONException e1) {
				logger.log(LogService.LOG_INFO, e1.getMessage());
			}

			// call friends API and store it in hash map
			HashMap<Long, String> idToName = new HashMap<Long, String>();
			idToName.put(Long.parseLong(myId), myName);
			JSONObject obj;
			try {

				FaceBookAPI fb = new MyFriendsAPI();
				JSONObject friendsObj = new JSONObject(fb.callAPI(data, ""));

				JSONArray friendsArray = friendsObj.getJSONArray("data");
				int len = friendsArray.length();
				for (int i = 0; i < len; i++) {
					JSONObject currentResult = friendsArray.getJSONObject(i);
					String friendOnename = currentResult.getString("name");
					Long id = currentResult.getLong("id");
					idToName.put(id, friendOnename);
					pairList.add(new FriendsPair(myName, friendOnename));

				}

				// call the MutualAPI
				fb = new MutualFriendsAPI();
				obj = new JSONObject(fb.callAPI(data, ""));
				JSONArray jsonArray = obj.getJSONArray("data");

				// call FriendsEventAPI
				fb = new FriendsEventAPI();
				obj = new JSONObject(fb.callAPI(data, ""));
				JSONArray jsonArrayEvent = obj.getJSONArray("data");
				HashMap<Long, String> eventIdToName = new HashMap<Long, String>();
				for (int i = 0; i < jsonArrayEvent.length(); i++) {
					JSONObject currentResult = jsonArrayEvent.getJSONObject(i);
					String eventName = currentResult.getString("name");
					Long id = currentResult.getLong("eid");
					eventIdToName.put(id, eventName.replace("|", ","));
					//this.logger.log(LogService.LOG_INFO, "eventName ="
					//		+ eventName + " id =" + id);
				}

				// call FriendsCommonEventAPI
				fb = new FriendsCommonEventAPI();
				obj = new JSONObject(fb.callAPI(data, ""));
				JSONArray jsonArrayCommonEvent = obj.getJSONArray("data");
				Map<Long, ArrayList<String>> commonEvents = new HashMap<Long, ArrayList<String>>();
				for (int i = 0; i < jsonArrayCommonEvent.length(); i++) {
					JSONObject currentResult = jsonArrayCommonEvent
							.getJSONObject(i);
					Long uid = currentResult.getLong("uid");
					Long eid = currentResult.getLong("eid");
					//this.logger.log(LogService.LOG_INFO, "uid =" + uid + "eid="
					//		+ eid);
					if (commonEvents.get(eid) == null) {
						ArrayList<String> uidList = new ArrayList<String>();
						uidList.add(idToName.get(uid));
						commonEvents.put(eid, uidList);
					} else {
						ArrayList<String> uidList = commonEvents.get(eid);
						uidList.add(idToName.get(uid));
						commonEvents.put(eid, uidList);
					}
					// this.logger.log(LogService.LOG_INFO,
					// "eid ="+eid+" uid ="+ commonEvents.get(eid));
				}

				//
				len = jsonArray.length();
				for (int i = 0; i < len; i++) {
					JSONObject currentResult = jsonArray.getJSONObject(i);
					Long id1 = currentResult.getLong("uid1");
					Long id2 = currentResult.getLong("uid2");
					FriendsPair fp = new FriendsPair(idToName.get(id1),
							idToName.get(id2));
					pairList.add(fp);
				}

				// get the number of the mutual friends
				fb = new NumberOfMutualFriends();
				obj = new JSONObject(fb.callAPI(data, ""));
				JSONArray jsonArrayMutualFriends = obj.getJSONArray("data");
				HashMap<String, Long> nameToNumMap = new HashMap<String, Long>();
				for (int i = 0; i < jsonArrayMutualFriends.length(); i++) {

					JSONObject currentResult = jsonArrayMutualFriends
							.getJSONObject(i);
					Long uid = currentResult.getLong("uid");
					String name = currentResult.getString("name");
					Long numOfMutualFriends = currentResult
							.getLong("mutual_friend_count");
					nameToNumMap.put(name, numOfMutualFriends);
				}

				for (FriendsPair fp : pairList) {
					for (Map.Entry<Long, ArrayList<String>> entry : commonEvents
							.entrySet()) {
						//this.logger.log(LogService.LOG_INFO,
						//"CommonEvent: "+entry.getValue());
						ArrayList<String> uid = (ArrayList<String>) entry
								.getValue();
						if (uid.contains(fp.getName1())
								&& uid.contains(fp.getName2())) {
							fp.setCommonEvent(eventIdToName.get(entry.getKey()));
							//this.logger.log(LogService.LOG_INFO,
							 //"CommonEvent: "+eventIdToName.get(entry.getKey()));
						}
						if (fp.getName1() == myName
								&& nameToNumMap.containsKey(fp.getName2())) {
							fp.setNumOfMutualFriends(nameToNumMap.get(fp
									.getName2()));
						}
					}
				}
				//this.logger.log(LogService.LOG_INFO,"generating tabular data");
				// facade.writeCSVFile(pairList);
				table = facade.generateTabularData(pairList);
			}

			catch (JSONException e) {
				logger.log(LogService.LOG_INFO, e.getMessage());
			}

			catch (IOException e) {
				logger.log(LogService.LOG_INFO, e.getMessage());
			}
		}
		return table;
	}

	void getFriendsOfFriendsNames() {
		String token = facade.getAccessToken();
		this.logger.log(LogService.LOG_INFO, "Access Token: " + token);
		String data = "access_token=" + token;
		String myName = "";
		try {
			myName = facade.getMyName(data);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONObject obj;
		try {
			FaceBookAPI fb = new FriendsInAppAPI(logger);
			obj = new JSONObject(fb.callAPI(data, ""));

			JSONArray jsonArray = obj.getJSONArray("data");
			int len = jsonArray.length();
			for (int i = 0; i < len; i++) {
				JSONObject currentResult = jsonArray.getJSONObject(i);
				String friendOnename = currentResult.getString("name");
				Long id = currentResult.getLong("uid");
				FriendsPair fp = new FriendsPair(myName, friendOnename);
				pairList.add(fp);

				this.logger.log(LogService.LOG_INFO, "Name = " + friendOnename);
				this.logger.log(LogService.LOG_INFO, "id = " + id);
				// code for friends of friends
				FaceBookAPI ff = new FriendsWithFriendsAPI();
				String string = ff.callAPI(data, id.toString());
				if (string.equals("No data") || string.isEmpty())
					continue;
				JSONObject ffobj = new JSONObject(string);
				JSONArray friensArray = ffobj.getJSONArray("data");

				for (int j = 0; j < friensArray.length(); j++) {
					JSONObject innerResult = friensArray.getJSONObject(j);
					String friendTwoName = innerResult.getString("name");
					// this.logger.log(LogService.LOG_INFO,
					// "friends friendName = "+friendOnename);
					this.logger.log(LogService.LOG_INFO,
							"friends friendName = " + friendTwoName);

					pairList.add(new FriendsPair(friendOnename, friendTwoName));
				}
			}
			// to get my friends
			FaceBookAPI myFriends = new MyFriendsAPI();
			obj = new JSONObject(myFriends.callAPI(data, ""));
			JSONArray friendsArray = obj.getJSONArray("data");
			len = friendsArray.length();
			for (int j = 0; j < len; j++) {
				JSONObject innerResult = friendsArray.getJSONObject(j);
				String friendTwoName = innerResult.getString("name");
				// this.logger.log(LogService.LOG_INFO,
				// "friends friendName = "+friendOnename);
				this.logger.log(LogService.LOG_INFO, "friends friendName = "
						+ friendTwoName);

				pairList.add(new FriendsPair(myName, friendTwoName));
			}
		} catch (JSONException e) {
			logger.log(LogService.LOG_INFO, e.getMessage());
		}
		try {
			facade.writeCSVFile(pairList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.log(LogService.LOG_INFO, e.getMessage());
		}

	}

	private Data[] generateOutData(Table table) {
		/*
		 * After getting the output in table format make it available to the
		 * user.
		 */
		Data output = new BasicData(table, Table.class.getName());
		Dictionary<String, Object> metadata = output.getMetadata();
		metadata.put(DataProperty.LABEL, "MutualFriendsNetwork");
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		return new Data[] { output };
	}

    
    
    
}