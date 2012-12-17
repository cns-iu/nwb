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
	public static final String userColumnTitle = "Flickr UserID";
	public static final String imageUrlColumnTitle = "Image URL";
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

    public Data[] execute() throws AlgorithmExecutionException {
    	List<String> userIDs = extractUserIDsFromTable();
    	FlickrImageGainer imageGainer = new FlickrImageGainer(apiKey);
    	Map<String, List<String>> uidToUrlsMap = new HashMap<String, List<String>>();
    	
		try {
			for (String uID : userIDs) {
				 this.logger.log(LogService.LOG_INFO, 
			        		String.format("Downloading image URLs for user %s.", uID));
				 
				List<String> urlList = imageGainer.getImageURLs(tag, uID);

				if (!urlList.isEmpty()) {
					uidToUrlsMap.put(uID, urlList);
				}
			}
		} catch (FlickrRuntimeException e) {
			throw new AlgorithmExecutionException(
					"Flickr service or network is unavailable. Try again later", e);
		}
    	Table resultTable = covertResultIntoTable(uidToUrlsMap);
	    
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
    
    private Table covertResultIntoTable(Map<String, List<String>> uidToUrlsMap) {
    	Integer count = 0;
    	Table table = new Table();
    	table.addColumn(userColumnTitle, String.class);
        table.addColumn(imageUrlColumnTitle, String.class);
        for (Entry<String, List<String>> entry : uidToUrlsMap.entrySet()) {
        	String uid = entry.getKey();
        	List<String> urls = entry.getValue();
        	count += urls.size();
        	for (String url : urls) {
	        	int rowNumber = table.addRow();
	        	table.set(rowNumber, userColumnTitle, uid);
	            table.set(rowNumber, imageUrlColumnTitle, url);
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