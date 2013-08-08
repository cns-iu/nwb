package edu.iu.sci2.reader.facebook.FacebookFriend;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;

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
import edu.iu.sci2.reader.facebook.FacebookFriend.api.FaceBookAPI;
import edu.iu.sci2.reader.facebook.FacebookFriend.api.FriendsCommonEventAPI;
import edu.iu.sci2.reader.facebook.FacebookFriend.api.FriendsEventAPI;
import edu.iu.sci2.reader.facebook.FacebookFriend.api.MyFriendsDataAPI;
import edu.iu.sci2.reader.facebook.FacebookFriend.facade.Facade;
import edu.iu.sci2.reader.facebook.FacebookFriend.model.FriendData;
import edu.iu.sci2.reader.facebook.FacebookFriend.utilities.Utils;
import org.osgi.service.log.LogService;

import prefuse.data.Table;

public class FacebookFriends implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext ciShellContext;
    private LogService logger;
    private HashMap<String, FriendData> idDataList;
    private Facade facade;
    
    
    public FacebookFriends(Data[] data,
    				  Dictionary parameters,
    				  CIShellContext ciShellContext) {
        this.data = data;
        this.parameters = parameters;
        this.ciShellContext = ciShellContext;
        this.logger = (LogService) ciShellContext.getService(LogService.class
				.getName());
		facade = new Facade(this.logger);
		idDataList = new HashMap<String, FriendData>();
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
		
		int confirmMsg = JOptionPane
				.showConfirmDialog(
						null,
						"Please login in your web browser and copy the access token returned to allow Sci2 to access your Friends infomation",
						"Are you ready to login in Web browser?",
						JOptionPane.YES_NO_OPTION);
		Table table = null;
		if (confirmMsg == JOptionPane.YES_OPTION) {
			this.logger.log(LogService.LOG_INFO, "Opening Facebook login page");
			String token = facade.getAccessTokenFromTable(data);
			//this.logger.log(LogService.LOG_INFO, token);
			if(token.isEmpty()){			
				this.logger
				.log(LogService.LOG_ERROR, "The token extracted from the table is empty, please select the correct table");
		        return null;
			}
		
			if(facade.isValidToken(token)){
				    table = getFriendsData(token);
			}
			else{
				this.logger
				.log(LogService.LOG_ERROR,"The Access token is expired, to get a new Access token select \"Facebook Reader->Access Token\" Menu.");							
				   return null;
		    }
		}
		
		
		
		return generateOutData(table);

    }
    
    private Table getFriendsData(String token) {
		
		this.logger.log(LogService.LOG_INFO, "Access Token: " + token);
		if (token == null)
			return null;
		String data = "access_token=" + token;
		String myName = "";
		String myId = "";
		Table table =null;
		try {
			myName = facade.getMyName(data);
			myId = facade.getMyId(data);
		} catch (JSONException e1) {
			logger.log(LogService.LOG_INFO, e1.getMessage());
		}

		JSONObject obj;
		try {
             //get my friends data API
			FaceBookAPI fb = new MyFriendsDataAPI(logger);
			String val = fb.callAPI(data, "");
			//logger.log(LogService.LOG_INFO, "value=" + val);
			JSONObject friendsObj = new JSONObject(val);
            //get the data from JSON  
			JSONArray friendsArray = friendsObj.getJSONArray("data");
			int len = friendsArray.length();
			for (int i = 0; i < len; i++) {
               // retrive values from each of the persons
				JSONObject currentResult = friendsArray.getJSONObject(i);
				Long id = currentResult.getLong("uid");
				FriendData fd = new FriendData(id);
				fd.setName(currentResult.getString("name"));
				fd.setId(id);
				fd.setGender(currentResult.getString("sex"));
				if (!currentResult.isNull("hometown_location")) {
					JSONObject htObj =currentResult
					.getJSONObject("hometown_location");
					String name = "";
					String state="";
					String country = "";
					String zip ="";
					if(!htObj.isNull("name")){
					    name = htObj.getString("name") +",";
					}
					if(!htObj.isNull("state")){
					    state = htObj.getString("state")+",";
					}					
					if(!htObj.isNull("country")){
					   country = htObj.getString("country")+",";
					}					
					if(!htObj.isNull("zip")){
						zip = htObj.getString("zip");	
					}
					String homeTown = name + state + country+zip;
					fd.setHomeTownLocation(Utils.removeDuplicates(homeTown));
					//logger.log(LogService.LOG_INFO,"Hometown="+homeTown );
				}
				if (!currentResult.isNull("current_location")) {
					JSONObject htObj =currentResult
					.getJSONObject("current_location");
					String state="";
					String name="";
					String city="";
					String country = "";
					String zip ="";
					if(!htObj.isNull("name")){
					    name = htObj.getString("name")+",";
					}
					if(!htObj.isNull("city")){
					    city = htObj.getString("city")+",";
					}
					if(!htObj.isNull("state")){
					    state = htObj.getString("state")+",";
					}					
					if(!htObj.isNull("country")){
					   country = htObj.getString("country")+",";
					}					
					if(!htObj.isNull("zip")){
						zip = htObj.getString("zip");	
					}
					
     				String currentLocation = name +city +state + country+zip;
					fd.setCurrentLocation(Utils.removeDuplicates(currentLocation));
					//logger.log(LogService.LOG_INFO,"Hometown="+homeTown );
				}
				
				
				if (!currentResult.isNull("status")) {
					JSONObject jsonObj = currentResult.getJSONObject("status");
					String message =jsonObj.getString("message");
					fd.setStatus(message);
				}
				if (!currentResult.isNull("interests")) {
					fd.setInterest(currentResult.getString("interests").replace(",", "|"));
				}
				if (!currentResult.isNull("birthday")) {
					 String birthday =currentResult.getString("birthday");
					fd.setBirthday(Utils.formatDate(birthday));
				}
				if (!currentResult.isNull("religion")) {
					fd.setReligion(currentResult.getString("religion"));
				}
				if (!currentResult.isNull("political")) {
					fd.setPoliticalView(currentResult.getString("political"));
				}
				if (!currentResult.isNull("relationship_status")) {
					fd.setRelationShipStatus(currentResult.getString("relationship_status"));
				}
				idDataList.put(id.toString(), fd);
			}
			fb = new FriendsEventAPI(logger);
			obj = new JSONObject(fb.callAPI(data, ""));
			JSONArray jsonArrayEvent = obj.getJSONArray("data");
			HashMap<Long, String> eventIdToName = new HashMap<Long, String>();
			for (int i = 0; i < jsonArrayEvent.length(); i++) {
				JSONObject currentResult = jsonArrayEvent.getJSONObject(i);
				String eventName = currentResult.getString("name");
				//logger.log(LogService.LOG_INFO,eventName);
				Long id = currentResult.getLong("eid");
				eventIdToName.put(id, eventName);
			}
			// call FriendsCommonEventAPI
			fb = new FriendsCommonEventAPI(logger);
			obj = new JSONObject(fb.callAPI(data, ""));
			JSONArray jsonArrayCommonEvent = obj.getJSONArray("data");

			for (int i = 0; i < jsonArrayCommonEvent.length(); i++) {
				JSONObject currentResult = jsonArrayCommonEvent
						.getJSONObject(i);
				Long uid = currentResult.getLong("uid");
				Long eid = currentResult.getLong("eid");

				FriendData fd = idDataList.get(uid.toString());
				if(fd == null || eid == null)continue;
				//logger.log(LogService.LOG_INFO,"Eventid ="+ eid.toString()+"eventName"+eventIdToName.get(eid));
				if(eventIdToName.get(eid) != null)
				   fd.addEvent(eventIdToName.get(eid).replace("|", ","));
			}             			
			table =facade.generateTabularData(idDataList) ;
		} catch (JSONException e) {
			logger.log(LogService.LOG_INFO, e.getMessage());
		} catch (IOException e) {
			logger.log(LogService.LOG_INFO, e.getMessage());
		}		
		return table;		
	}    
    
    private Data[] generateOutData(Table table) {
        /*
        * After getting the output in table format make it available to the user.
        */
       Data output = new BasicData(table, Table.class.getName());
       Dictionary<String, Object> metadata = output.getMetadata();
       metadata.put(DataProperty.LABEL, "FacebookFriendsData");
       metadata.put(DataProperty.PARENT, this.data[0]);
       metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);        
       return new Data[] { output };
   }
}