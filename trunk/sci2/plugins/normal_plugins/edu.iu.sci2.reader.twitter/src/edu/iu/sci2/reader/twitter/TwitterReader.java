package edu.iu.sci2.reader.twitter;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

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
	public static final String userColumnTitle = "Twitter User Name";
	public static final String msgColumnTitle = "Tweet";
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
    	List<String> userIDs = extractUserIDsFromTable();
    	Table resultTable;
		try {
			resultTable = searchTwit(userIDs);
		} catch (TwitterException e) {
			throw new AlgorithmExecutionException(
					"Twitter service or network is unavailable. Try again later", e);
		}
	    
        return generateOutputData(resultTable);
    }
    
    private List<String> extractUserIDsFromTable() {
    	List<String> userIDs = new ArrayList<String>();
    	Table inputTable = (Table) data[0].getData();
        int userIDColumnIndex = inputTable.getColumnNumber(userIDColumn);
         
        Iterator<?> rowsIterator = inputTable.iterator();
        while (rowsIterator.hasNext()) {
            int currentRowNumber = Integer.parseInt(rowsIterator.next().toString());
            String userID = inputTable.get(currentRowNumber, userIDColumnIndex).toString();
            userIDs.add(userID);
        }
        
        return userIDs;
    }
    
    private Table searchTwit(List<String> userIDs) throws TwitterException {
		//String uIDs = "katycns,xliu12,scott_bot";  
		//i.e. "(from:\"katycns\" OR from:\"xliu12\" OR from:\"scott_bot\") #ivmooc"

		String QueryString = "(";
		for (String uID: userIDs) {
			QueryString = QueryString + "from:\"" + uID + "\" OR ";
		}
		
		QueryString = QueryString.substring(0, QueryString.length() - 4) + ") #" + tag;
		System.out.println(QueryString);
		
	    Twitter twitter = new TwitterFactory().getInstance();
	    Query query = new Query(QueryString);
	    QueryResult result = twitter.search(query);
	    
	    return covertResultIntoTable(result.getTweets());
    }
    
    private Table covertResultIntoTable(List<Tweet> tweets) {
    	Table table = new Table();
        table.addColumn(userColumnTitle, String.class);
        table.addColumn(msgColumnTitle, String.class);
        for (Tweet tweet : tweets) {
        	int rowNumber = table.addRow();
            table.set(rowNumber, userColumnTitle, tweet.getFromUser());
            table.set(rowNumber, msgColumnTitle, tweet.getText());
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
        metadata.put(DataProperty.LABEL, "TwitResult");
        metadata.put(DataProperty.PARENT, this.data[0]);
        metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
         
        return new Data[] { output };
    }
}