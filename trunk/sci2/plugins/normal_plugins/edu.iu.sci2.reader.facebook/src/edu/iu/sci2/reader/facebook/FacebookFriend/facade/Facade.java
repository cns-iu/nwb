package edu.iu.sci2.reader.facebook.FacebookFriend.facade;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

import org.cishell.framework.data.Data;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.osgi.service.log.LogService;

import edu.iu.sci2.reader.facebook.FacebookFriend.api.FaceBookAPI;
import edu.iu.sci2.reader.facebook.FacebookFriend.api.MyDetailsAPI;
import edu.iu.sci2.reader.facebook.FacebookFriend.model.FriendData;
import edu.iu.sci2.reader.facebook.FacebookFriend.utilities.CSVWriter;
import edu.iu.sci2.reader.facebook.FacebookFriend.utilities.DownloadHandler;
import edu.iu.sci2.reader.facebook.FacebookFriend.utilities.DownloadHandler.InvalidUrlException;
import edu.iu.sci2.reader.facebook.FacebookFriend.utilities.DownloadHandler.NetworkConnectionException;

import prefuse.data.Table;

public class Facade {
	private CSVWriter csv;
	private LogService logger;
	private FileReader fr;
	private String path, token;

	public Facade(LogService logger) {
		this.logger = logger;
	}
	
	public String getAccessToken(){
		final JFileChooser fc = new JFileChooser();
		int userSelection = fc.showOpenDialog(null);
		File fileToLoad = null;
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		fileToLoad = fc.getSelectedFile();
		path = fileToLoad.getAbsolutePath();
		}
		try {
			fr = new FileReader(path);
		} catch (FileNotFoundException e) {
			logger.log(LogService.LOG_INFO, e.getMessage());
		}
		BufferedReader br = new BufferedReader(fr); 
		String s; 
		try {
			while((s = br.readLine()) != null) { 
			token = s;
			}
		} catch (IOException e) {
			logger.log(LogService.LOG_INFO, e.getMessage());
		}
		return token;
	}	
	
	public String getAccessTokenFromTable(Data[] data){
		
		Table inputTable = (Table) data[0].getData();
		//if(!value.contains("FacebookAccessToken")) return "";
		//logger.log(LogService.LOG_INFO,"Tuple Count"+inputTable.getTupleCount());
		if(inputTable.getTupleCount() != 1) return "";
		Iterator<?> rowsIterator = inputTable.iterator();
		int tokenIndex = inputTable.getColumnNumber("Token");
		int currentRowNumber = Integer.parseInt(rowsIterator.next()
				.toString());
		String tokenVal="";
		
		if(inputTable.isValidRow(currentRowNumber))
		{
			tokenVal = inputTable.get(currentRowNumber, tokenIndex)
			.toString();		
		}
		return tokenVal;
		
}
	
	
	public boolean isValidToken(String token) 
	{
    	//logger.log(LogService.LOG_INFO,"Token Valid ="+token);
		token = "access_token=" +token;
		FaceBookAPI mydetails = new MyDetailsAPI(logger);
		String data = mydetails.callAPI(token, "");
		JSONObject obj;
		try {
			obj = new JSONObject(new JSONTokener(data));
		} catch (JSONException e) {
			return false;
		}
		return true;
	}

	// check login
	public String checkLogin() {
		try {
			URL url = new URL(
					"https://morning-fjord-1741.herokuapp.com/CheckLogin.php");
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			return DownloadHandler.getResponse(connection);
		} catch (IOException e1) {
			logger.log(LogService.LOG_INFO, e1.getMessage());
		} catch (InvalidUrlException e1) {
			logger.log(LogService.LOG_INFO, e1.getMessage());
		} catch (NetworkConnectionException e1) {
			logger.log(LogService.LOG_INFO, e1.getMessage());
		}

		return "0";
	}
	

	// writes the CSV file
	
	public Table generateTabularData(HashMap<String, FriendData> map)
			throws IOException {
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
        
    	for (Map.Entry<String, FriendData> entry : map.entrySet()) {
    		
    		FriendData data = entry.getValue();
			////logger.log(LogService.LOG_INFO,"In Data");
			// convert event array into string
    		
			List<String> eventArray =  data.getEventList();
			String eventStr = "";
			//logger.log(LogService.LOG_INFO,"Event Array:");
			if (!eventArray.isEmpty()) {				
				for (String event : eventArray) {
					eventStr = eventStr+event + "|";
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
            table.set(rowNumber, "Relationship Status",data.getRelationShipStatus());
            table.set(rowNumber, "Events",eventStr);
    	}	
    	logger.log(LogService.LOG_INFO,"Please check the Data Manager for generated data");
    	return table;
	}
	
	
	public String getMyName(String token) throws JSONException {
		FaceBookAPI mydetails = new MyDetailsAPI(logger);
		String data = mydetails.callAPI(token, "");
		JSONObject obj = new JSONObject(new JSONTokener(data));
		return obj.getString("name");
	}

	public String getMyId(String token) throws JSONException {
		FaceBookAPI mydetails = new MyDetailsAPI(logger);
		String data = mydetails.callAPI(token, "");
		JSONObject obj = new JSONObject(new JSONTokener(data));
		return obj.getString("id");
	}
}
