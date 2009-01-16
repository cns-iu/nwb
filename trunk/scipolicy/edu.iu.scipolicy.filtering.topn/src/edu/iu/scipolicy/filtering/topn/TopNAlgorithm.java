package edu.iu.scipolicy.filtering.topn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Dictionary;
import java.util.Enumeration;

import javax.sql.DataSource;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.templates.database.SQLExecutionAlgorithm;
import org.cishell.templates.database.SQLFormationException;

public class TopNAlgorithm extends SQLExecutionAlgorithm {
    private int topN;
    private String columnToSortBy;
    private boolean isFromHighestToLowest;
    
    public TopNAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
    	super(data, parameters, context);
        
        //unpack parameters that user specified
        
		this.topN = ((Integer) parameters.get("topN")).intValue();
		this.columnToSortBy = ((String) parameters.get("columnToSortBy"));
		this.isFromHighestToLowest = ((Boolean) parameters.get("isFromHighestToLowest")).booleanValue();
    }
    
    public String formSQL() throws SQLFormationException {
    	// TODO: Ensure these always have this metadata.
    	String tableName = (String)data[0].getMetadata().get("table_name");
    	String ascendingOrDescending = null;
    	
		if (isFromHighestToLowest)
			ascendingOrDescending = "ASC";
		else
			ascendingOrDescending = "DESC";
    	
    	return "SELECT * FROM (SELECT ROW_NUMBER() OVER(ORDER BY " +
    		this.columnToSortBy + ascendingOrDescending + ") AS rowNumber, " +
    		"COLUMNS FROM " + tableName + ") AS tempTable WHERE rowNumber <= " +
    		this.topN;
    }
    
    public Data createOutDataFromDataSource(DataSource dataSource) {
    	// Use super to create the base data.
    	Data outData = super.createOutDataFromDataSource(dataSource);
    	// Get the metadata.
    	Dictionary outMetadata = outData.getMetadata();
    	
    	// TODO: Algorithm writers shouldn't have to do this.
    	outMetadata.put(DataProperty.PARENT, data[0]);
    	outMetadata.put(DataProperty.LABEL,
    					"top " + this.topN + " rows by " + this.columnToSortBy);
    	
    	return outData;
    }
}