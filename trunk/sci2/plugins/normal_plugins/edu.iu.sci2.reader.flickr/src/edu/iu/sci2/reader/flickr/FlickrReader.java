package edu.iu.sci2.reader.flickr;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;

public class FlickrReader implements Algorithm {
	public static final String USER_COLUMN_TITLE = "Flickr UserID";
	public static final String IMAGE_URL_COLUMN_TITLE = "Image URL";
	public static final String USERNAME_COLUMN_TITLE = "User Name";
	public static final String TITLE_COLUMN_TITLE = "Title";
	private LogService logger;
    private Data[] data;
	private String userIDColumn;
	private String tag;
	private String apiKey;
    
    public FlickrReader(Data[] data,
    				  Dictionary<String, Object> parameters,
    				  CIShellContext ciShellContext) {
    	this.logger = (LogService) ciShellContext.getService(LogService.class.getName());
        this.data = data;
        this.userIDColumn = parameters.get("uid").toString();
        this.apiKey = parameters.get("apikey").toString();
        this.tag = parameters.get("tag").toString();
    }
    
    public static void main(String[] args) {
    	FlickrImageGainer imageGainer = new FlickrImageGainer("4f6ed3c0e47e7c633016933f0b0eb884");
    	imageGainer.getImageResults("NBA", "81872511@N08");
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	List<String> userIDs = extractUserIDsFromTable();
    	FlickrImageGainer imageGainer = new FlickrImageGainer(apiKey);
    	Map<String, List<FlickrResult>> uidToResultsMap = new HashMap<String, List<FlickrResult>>();
    	
		try {
			for (String uID : userIDs) {
				 this.logger.log(LogService.LOG_INFO, 
			        		String.format("Downloading image URLs for user %s.", uID));
				 
				List<FlickrResult> resultList = imageGainer.getImageResults(tag, uID);

				if (!resultList.isEmpty()) {
					uidToResultsMap.put(uID, resultList);
				}
			}
		} catch (FlickrRuntimeException e) {
			throw new AlgorithmExecutionException(
					"Flickr service or network is unavailable. Try again later", e);
		}
    	Table resultTable = covertResultIntoTable(uidToResultsMap);
	    
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
    
    private Table covertResultIntoTable(Map<String, List<FlickrResult>> uidToResultsMap) {
    	Integer count = 0;
    	Table table = new Table();
    	table.addColumn(USER_COLUMN_TITLE, String.class);
        table.addColumn(USERNAME_COLUMN_TITLE, String.class);
        table.addColumn(TITLE_COLUMN_TITLE, String.class);
        table.addColumn(IMAGE_URL_COLUMN_TITLE, String.class);
        for (Entry<String, List<FlickrResult>> entry : uidToResultsMap.entrySet()) {
        	String uid = entry.getKey();
        	List<FlickrResult> results = entry.getValue();
        	count += results.size();
        	for (FlickrResult ret : results) {
	        	int rowNumber = table.addRow();
	        	table.set(rowNumber, USER_COLUMN_TITLE, uid);
	        	table.set(rowNumber, USERNAME_COLUMN_TITLE, ret.getUsername());
	        	table.set(rowNumber, TITLE_COLUMN_TITLE, ret.getTitle());
	            table.set(rowNumber, IMAGE_URL_COLUMN_TITLE, ret.getUrl());
        	}
 	    }
        this.logger.log(LogService.LOG_INFO, 
        		String.format("There are %d image URLs downloaded.", count));
        
        return table;
    }
    
    private Data[] generateOutputData(Table table) {
        
        /*
         * After getting the output in table format make it available to the user.
         */
        Data output = new BasicData(table, Table.class.getName());
        Dictionary<String, Object> metadata = output.getMetadata();
        metadata.put(DataProperty.LABEL, "FlickrResult");
        metadata.put(DataProperty.PARENT, this.data[0]);
        metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
         
        return new Data[] { output };
    }
}