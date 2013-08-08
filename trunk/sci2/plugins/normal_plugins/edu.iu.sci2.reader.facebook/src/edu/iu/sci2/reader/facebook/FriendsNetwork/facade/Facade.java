package edu.iu.sci2.reader.facebook.FriendsNetwork.facade;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;

import org.cishell.framework.data.Data;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.osgi.service.log.LogService;

import edu.iu.sci2.reader.facebook.FriendsNetwork.api.FaceBookAPI;
import edu.iu.sci2.reader.facebook.FriendsNetwork.api.MyDetailsAPI;
import edu.iu.sci2.reader.facebook.FriendsNetwork.model.FriendsPair;
import edu.iu.sci2.reader.facebook.FriendsNetwork.utilities.CSVWriter;
import edu.iu.sci2.reader.facebook.FriendsNetwork.utilities.DownloadHandler;
import edu.iu.sci2.reader.facebook.FriendsNetwork.utilities.DownloadHandler.InvalidUrlException;
import edu.iu.sci2.reader.facebook.FriendsNetwork.utilities.DownloadHandler.NetworkConnectionException;

import prefuse.data.Table;

public class Facade {
	private CSVWriter csv;
	private LogService logger;
	private FileReader fr;
	String path, token;

	public Facade(LogService logger) {
		this.logger = logger;
	}

	/*public String getAccessToken() {
		try {
			URI url = new URI(
					"https://www.facebook.com/dialog/oauth?client_id=283202715139589"
							+ "&redirect_uri=https://morning-fjord-1741.herokuapp.com/token.php&scope=manage_friendlists"
							+ "&response_type=token" 
							+ "&scope=email,user_about_me,user_activities,user_birthday,user_education_history," +
							"user_events,user_hometown,user_interests,user_likes,user_groups,user_location,user_religion_politics,friends_events,read_friendlists");
			Desktop.getDesktop().browse(url);
		} catch (URISyntaxException e1) {
			logger.log(LogService.LOG_INFO, e1.getMessage());
		} catch (IOException e1) {
			logger.log(LogService.LOG_INFO, e1.getMessage());
		}

		String input = JOptionPane.showInputDialog("Enter Access Token:");
		return input;
	}*/
	
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr); 
		String s; 
		try {
			while((s = br.readLine()) != null) { 
			token = s;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return token;
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
	public void writeCSVFile(List<FriendsPair> list) throws IOException {

		final JFileChooser fc = new JFileChooser();
		int userSelection = fc.showSaveDialog(null);
		File fileToSave = null;
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			fileToSave = fc.getSelectedFile();
			System.out.println("Save as file: " + fileToSave.getAbsolutePath());
		}

		CSVWriter writer = new CSVWriter(fileToSave.getAbsolutePath());
		String[] entries = { "FacebookUser1", "FacebookUser2", "CommonEvent", "Number Of Mutual Friends" };
		writer.writeNext(entries);
		for (FriendsPair pair : list) {
			String[] nameList = { pair.getName1(), pair.getName2(),
					pair.getCommonEventList(), pair.getNumOfMutualFriends().toString() };
			writer.writeNext(nameList);
		}
		writer.close();
	}
	
	
	public String getAccessTokenFromTable(Data[] data){
		Table inputTable = (Table) data[0].getData();
		int count = inputTable.getTupleCount();
		if(count != 1 )return "";
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
	
	public Table generateTabularData(List<FriendsPair> list)
			throws IOException {
		
		Table table = new Table();
        table.addColumn("Facebook User 1", String.class);
        table.addColumn("Facebook User 2", String.class);
        table.addColumn("Number of Common Events Attended", Integer.class);
        table.addColumn("Number Of Mutual Friends", String.class);    
        //logger.log(LogService.LOG_INFO,"started writing");
    	for (FriendsPair pair : list) {
    	
    		int rowNumber = table.addRow();
            table.set(rowNumber, "Facebook User 1", pair.getName1());
            //logger.log(LogService.LOG_INFO,"event List"+pair.getName1());
            table.set(rowNumber, "Facebook User 2", pair.getName2());
            //logger.log(LogService.LOG_INFO,"event List"+pair.getName2());           
            table.set(rowNumber, "Number of Common Events Attended", pair.getNumberOfCommonEvents());
             //logger.log(LogService.LOG_INFO,"event List"+pair.getCommonEventList());                      
            table.set(rowNumber, "Number Of Mutual Friends", pair.getNumOfMutualFriends().toString());            //logger.log(LogService.LOG_INFO,"Num of Mutual Friends"+pair.getNumOfMutualFriends().toString());
           
            }	
    	return table;
	}

	public boolean isValidToken(String token) 
	{
		 token ="access_token="+token;
		FaceBookAPI mydetails = new MyDetailsAPI();
		String data = mydetails.callAPI(token, "");
		JSONObject obj;
		try {
			obj = new JSONObject(new JSONTokener(data));
		} catch (JSONException e) {
			return false;
		}
		return true;
	}

	public String getMyName(String token) throws JSONException {
		FaceBookAPI mydetails = new MyDetailsAPI();
		String data = mydetails.callAPI(token, "");
		JSONObject obj = new JSONObject(new JSONTokener(data));
		return obj.getString("name");
	}

	public String getMyId(String token) throws JSONException {
		FaceBookAPI mydetails = new MyDetailsAPI();
		String data = mydetails.callAPI(token, "");
		JSONObject obj = new JSONObject(new JSONTokener(data));
		return obj.getString("id");
	}

}
