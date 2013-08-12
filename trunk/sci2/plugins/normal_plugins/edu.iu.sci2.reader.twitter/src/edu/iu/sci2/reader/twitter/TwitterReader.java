package edu.iu.sci2.reader.twitter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterReader implements Algorithm {
	public static final String USER_COLUMN_TITLE = "Twitter User";
	public static final String USER_NAME_COLUMN_TITLE = "Twitter User Name";
	public static final String CREATED_AT_COLUMN_TITLE = "Created At";
	public static final String MSG_COLUMN_TITLE = "Tweet";
	private static final String CONSUMER_KEY = "consumerKey";
	private static final String CONSUMER_SECRET = "consumerSecret";
	private static final String ACCESS_TOKEN = "accessToken";
	private static final String ACCESS_SECRET = "accessSecret";
	private LogService logger;
    private Data[] data;
	private String userIDColumn;
	private HashMap<String, String> authData;
	private String tag;
	private String propertyFile;
    
    public TwitterReader(Data[] data,
    				  Dictionary<String, Object> parameters, String propertyFile,
    				  CIShellContext ciShellContext) {
    	this.logger = (LogService) ciShellContext.getService(LogService.class.getName());
        this.data = data;
        this.userIDColumn = parameters.get("uid").toString();
        this.tag = parameters.get("tag").toString();
        this.propertyFile = propertyFile;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
			this.authData = getAuthData(propertyFile);
		} catch (IOException e1) {
			throw new AlgorithmExecutionException(
					"The given property file is invalid!");
		}
    	Set<String> userIDs = extractUniqueUserIDsFromTable();
    			
    	Table resultTable;
		try {
			resultTable = searchTweet(userIDs);
		} catch (TwitterException e) {
			throw new AlgorithmExecutionException(
					"Twitter service or network is unavailable. Try again later.", e);
		}
	    
        return generateOutputData(resultTable);
    }
    
    private HashMap<String, String> getAuthData(String filePath) throws IOException {
		HashMap<String, String> arr = new HashMap<String, String>();
		Properties properties = new Properties();
		properties.load(new FileInputStream(filePath));

		arr.put(CONSUMER_KEY, properties.getProperty(CONSUMER_KEY));
		arr.put(CONSUMER_SECRET, properties.getProperty(CONSUMER_SECRET));
		arr.put(ACCESS_TOKEN, properties.getProperty(ACCESS_TOKEN));
		arr.put(ACCESS_SECRET, properties.getProperty(ACCESS_SECRET));
	
		return arr;
	}
    
    private Set<String> extractUniqueUserIDsFromTable() {
    	Set<String> userIDs = new HashSet<String>();
    	Table inputTable = (Table) data[0].getData();
        int userIDColumnIndex = inputTable.getColumnNumber(userIDColumn);
         
        Iterator<?> rowsIterator = inputTable.iterator();
        while (rowsIterator.hasNext()) {
            int currentRowNumber = Integer.parseInt(rowsIterator.next().toString());
            Object userIDObject = inputTable.get(currentRowNumber, userIDColumnIndex);
            // Only accept String object
            if (userIDObject instanceof String) {
	            String userID = userIDObject.toString().trim();
	            /* UserID should not null or empty */
	            if (userID != null && !userID.isEmpty()) {
	            	userIDs.add(userID);
	            }
            }
        }   
        return userIDs;
    }
    
    private Table searchTweet(Set<String> userIDs) throws TwitterException {
    	List <Status> resultList = new ArrayList<Status>();
    	String queryString = "#" + tag;
    	
    	if (!userIDs.isEmpty()) {
    		queryString += " (";
    		
    		for(String id : userIDs) {
				String appendString = "from:\"" + id + "\" OR ";
				// Query Greater than 500 chars is not allowed. Download the result first and continue
				if((queryString.length() + appendString.length()) >= 500) {
					// Remove the last " OR " with -4
					queryString = queryString.substring(0, queryString.length() - 4) + ")";
	    			resultList.addAll(downloadAllTweets(queryString));
	    			queryString = "#" + tag + " (";
				}
				
				queryString += appendString;
    		}
    		
    		// Remove the last " OR " with -4
			queryString = queryString.substring(0, queryString.length() - 4) + ")";
    	}
    	
		resultList.addAll(downloadAllTweets(queryString));
    	
    	return covertResultIntoTable(resultList);
    }
    
    private Twitter createTwitterInstance() {
    	ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(authData.get(CONSUMER_KEY))
				.setOAuthConsumerSecret(authData.get(CONSUMER_SECRET))
				.setOAuthAccessToken(authData.get(ACCESS_TOKEN))
				.setOAuthAccessTokenSecret(authData.get(ACCESS_SECRET));

		return new TwitterFactory(cb.build()).getInstance();
    }
    
    /**
     * Download tweets for the given query. If the returned result contains multiple pages, 
     * download the tweets page by page. This method return a list of tweet.
     */
    private List<Status> downloadAllTweets(String querystring) throws TwitterException {
    	List <Status> resultList = new ArrayList<Status>();

		Twitter twitter = createTwitterInstance();
		
		Query query = new Query(querystring);
		try {
			while(query != null) {
				query.setCount(100);
				QueryResult queryResult = twitter.search(query);
				List<Status> tweets = queryResult.getTweets();
				resultList.addAll(tweets);
				query = queryResult.nextQuery();
			}
		} catch (Exception e) {
			// Currently, assume all exception is due to Twitter API limit. We might need to handle other issue in future.
			this.logger.log(LogService.LOG_WARNING, 
					"You have reach the Twitter query limit (180 queries per 15 minutes). Please try again in 15 minutes.");
		}
		
		// Return the queried results
		return resultList;
    }
    
    /**
     * Covert the result into Prefuse Table
     */
    private Table covertResultIntoTable(List<Status> statuses) {
    	Table table = new Table();
        table.addColumn(USER_COLUMN_TITLE, String.class);
        table.addColumn(USER_NAME_COLUMN_TITLE, String.class);
        table.addColumn(CREATED_AT_COLUMN_TITLE, Date.class);
        table.addColumn(MSG_COLUMN_TITLE, String.class);
        for (Status status : statuses) {
        	int rowNumber = table.addRow();
            table.set(rowNumber, USER_COLUMN_TITLE, String.valueOf(status.getUser().getId()));
            table.set(rowNumber, USER_NAME_COLUMN_TITLE, status.getUser().getName());
            table.set(rowNumber, CREATED_AT_COLUMN_TITLE, status.getCreatedAt());
            table.set(rowNumber, MSG_COLUMN_TITLE, status.getText());
 	    }
        this.logger.log(LogService.LOG_INFO, 
        		String.format("%d tweets were downloaded.", statuses.size()));
        return table;
    }
    
    private Data[] generateOutputData(Table table) {
        
        /*
         * After getting the output in table format make it available to the user.
         */
        Data output = new BasicData(table, Table.class.getName());
        Dictionary<String, Object> metadata = output.getMetadata();
        metadata.put(DataProperty.LABEL, "TwitterResult");
        metadata.put(DataProperty.PARENT, this.data[0]);
        metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
         
        return new Data[] { output };
    }
}