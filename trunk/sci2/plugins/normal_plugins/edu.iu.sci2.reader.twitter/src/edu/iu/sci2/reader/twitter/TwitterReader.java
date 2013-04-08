package edu.iu.sci2.reader.twitter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterReader implements Algorithm {
	private static final String REACH_LIMIT_ERROR = "403";
	public static final String USER_COLUMN_TITLE = "Twitter User";
	public static final String USER_NAME_COLUMN_TITLE = "Twitter User Name";
	public static final String CREATED_AT_COLUMN_TITLE = "Created At";
	public static final String MSG_COLUMN_TITLE = "Tweet";
	private LogService logger;
    private Data[] data;
	private String userIDColumn;
	private String tag;
    
    public TwitterReader(Data[] data,
    				  Dictionary<String, Object> parameters,
    				  CIShellContext ciShellContext) {
    	this.logger = (LogService) ciShellContext.getService(LogService.class.getName());
        this.data = data;
        this.userIDColumn = parameters.get("uid").toString();
        this.tag = parameters.get("tag").toString();
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	Set<String> userIDs = extractUniqueUserIDsFromTable();
    			
    	Table resultTable;
		try {
			resultTable = searchTweet(userIDs);
		} catch (TwitterException e) {
			throw new AlgorithmExecutionException(
					"Twitter service or network is unavailable. Try again later", e);
		}
	    
        return generateOutputData(resultTable);
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

		
    	List <Tweet> resultList = new ArrayList<Tweet>();
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
    
    /**
     * Download tweets for the given query. If the returned result contains multiple pages, 
     * download the tweets page by page. This method return a list of tweet.
     */
    private List<Tweet> downloadAllTweets(String querystring) throws TwitterException {
    	List <Tweet> resultList = new ArrayList<Tweet>();
		Twitter twitter = new TwitterFactory().getInstance();
	    int k = 0;
	    boolean changed = true;
	    try {
			while (changed) {
			    Query query = new Query(querystring); 
			    query.setRpp(100);
			    query.setPage(++k); // next page
			    QueryResult result = twitter.search(query);
			    changed = resultList.addAll(result.getTweets());
			}
		} catch (Exception e) {
			/* If it terminate with none Tweeter limit issue. We might need to handle other issue in future. */
			if (!e.getMessage().startsWith(REACH_LIMIT_ERROR)) {
				throw new TwitterException(e);
			}
		}
	    return resultList;
    }
    
    /**
     * Covert the result into Prefuse Table
     */
    private Table covertResultIntoTable(List<Tweet> tweets) {
    	Table table = new Table();
        table.addColumn(USER_COLUMN_TITLE, String.class);
        table.addColumn(USER_NAME_COLUMN_TITLE, String.class);
        table.addColumn(CREATED_AT_COLUMN_TITLE, Date.class);
        table.addColumn(MSG_COLUMN_TITLE, String.class);
        for (Tweet tweet : tweets) {
        	int rowNumber = table.addRow();
            table.set(rowNumber, USER_COLUMN_TITLE, tweet.getFromUser());
            table.set(rowNumber, USER_NAME_COLUMN_TITLE, tweet.getFromUserName());
            table.set(rowNumber, CREATED_AT_COLUMN_TITLE, tweet.getCreatedAt());
            table.set(rowNumber, MSG_COLUMN_TITLE, tweet.getText());
 	    }
        this.logger.log(LogService.LOG_INFO, 
        		String.format("There are %d tweets downloaded.", tweets.size()));
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