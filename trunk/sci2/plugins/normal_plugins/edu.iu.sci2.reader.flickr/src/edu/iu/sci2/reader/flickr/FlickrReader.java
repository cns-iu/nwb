package edu.iu.sci2.reader.flickr;

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

public class FlickrReader implements Algorithm {
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
    	List<String> urls;
		try {
			urls = imageGainer.getImageURL(tag, userIDs);
		} catch (FlickrRuntimeException e) {
			throw new AlgorithmExecutionException(
					"Flickr service or network is unavailable. Try again later", e);
		}
    	Table resultTable = covertResultIntoTable(urls);
	    
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
    
    private Table covertResultIntoTable(List<String> imageUrls) {
    	Table table = new Table();
        table.addColumn("image URL", String.class);
        for (String url : imageUrls) {
        	int rowNumber = table.addRow();
            table.set(rowNumber, "image URL", url);
 	    }
        this.logger.log(LogService.LOG_INFO, 
        		String.format("There are %d image URLs downloaded.", imageUrls.size()));
        
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