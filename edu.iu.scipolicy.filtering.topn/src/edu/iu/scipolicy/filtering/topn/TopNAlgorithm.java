package edu.iu.scipolicy.filtering.topn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Dictionary;
import java.util.Enumeration;

import javax.sql.DataSource;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

public class TopNAlgorithm implements Algorithm {
    private Data[] data;
    private CIShellContext context;
    
    private int topN;
    private String columnToSortBy;
    private boolean isFromHighestToLowest;
    
    public TopNAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.context = context;
        
        //unpack parameters that user specified
        
		this.topN = ((Integer) parameters.get("topN")).intValue();
		this.columnToSortBy = ((String) parameters.get("columnToSortBy"));
		this.isFromHighestToLowest = ((Boolean) parameters.get("isFromHighestToLowest"));
    }

    public Data[] execute() throws AlgorithmExecutionException {
		
		//unpack table data
		
    	DataSource tableDB = (DataSource) data[0].getData();
		String tableName = (String) data[0].getMetadata().get("table_name"); //TODO: Ensure these always have this metadata
		
		//construct SQL query for retrieving top n rows
		
		String ascendingOrDescending = null;
		if (isFromHighestToLowest) {
			ascendingOrDescending = "ASC";
		} else {
			ascendingOrDescending = "DESC";
		}
		
		String getTopNSQLQuery = 
			"SELECT *" +
			"FROM " + tableName +
			"ORDER BY " + columnToSortBy + " " + ascendingOrDescending + "" +
		    "LIMIT " + topN; //TODO: How to do this with standard SQL?
		
		//execute SQL query to retrieve top n rows
		
		ResultSet topNRowsResult;	
		try {
			Connection tableDBConnection = tableDB.getConnection();
			Statement sqlStatement = tableDBConnection.createStatement();
			topNRowsResult = sqlStatement.executeQuery(getTopNSQLQuery);
		} catch (SQLException e) {
			throw new AlgorithmExecutionException(e);
		}
		
		//turn query results in new database
		
		DataSource topRowsResult = convertQueryResultsToDatabase(topNRowsResult);
		
		//prepare the result's metadata
	
		Data topRowsResultData = prepareMetadata(topRowsResult, this.data[0]);
		
		//return result
		
		return new Data[]{ topRowsResultData };
    }
    
    private DataSource convertQueryResultsToDatabase(ResultSet queryResults) {
    	//TODO: Make this work foo (make it part of DBService?)!
    	return null;
    }
    
    private Data prepareMetadata(DataSource resultDB, Data originalData) {
    	Data resultData = new BasicData(resultDB, resultDB.getClass().getName());
    	copyOverMetaData(originalData, resultData);
    	
    	Dictionary resultMetadata = resultData.getMetadata();
    	resultMetadata.put(DataProperty.PARENT, data[0]); //TODO: Algorithm writers shouldn't have to do this
    	resultMetadata.put(DataProperty.LABEL, "top " + topN + " rows by " + columnToSortBy);
    	
    	return resultData;
    }
    
    private void copyOverMetaData(Data sourceData, Data targetData) {
    	Dictionary sourceMetaData = sourceData.getMetadata();
    	Dictionary targetMetaData = targetData.getMetadata();
    	
    	Enumeration keyEnum = sourceMetaData.keys();
    	while (keyEnum.hasMoreElements()) {
    		Object key = keyEnum.nextElement();
    		
    		targetMetaData.put(key, sourceMetaData.get(key));
    	}
    }
}