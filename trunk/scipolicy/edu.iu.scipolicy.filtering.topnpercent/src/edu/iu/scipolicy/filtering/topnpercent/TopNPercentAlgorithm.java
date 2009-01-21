package edu.iu.scipolicy.filtering.topnpercent;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.DataSourceWithID;
import org.cishell.templates.database.SQLExecutionAlgorithm;
import org.cishell.templates.database.SQLFormationException;

public class TopNPercentAlgorithm extends SQLExecutionAlgorithm {
    private float topNPercent;
    private String columnToSortBy;
    private boolean isFromHighestToLowest;
    
    public TopNPercentAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
    	super(data, parameters, context);
        
        // Unpack parameters that user specified.
		this.topNPercent = ((Float)parameters.get("topNPercent")).floatValue();
		this.columnToSortBy = ((String)parameters.get("columnToSortBy"));
		this.isFromHighestToLowest =
			((Boolean)parameters.get("isFromHighestToLowest")).booleanValue();
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
    		this.topNPercent + " * COUNT(tempTable)";
    }
    
    public Data createOutDataFromDataSource(DataSourceWithID dataSource) {
    	// Use super to create the base data.
    	Data outData = super.createOutDataFromDataSource(dataSource);
    	// Get the metadata.
    	Dictionary outMetadata = outData.getMetadata();
    	
    	// TODO: Algorithm writers shouldn't have to do this.
    	outMetadata.put(DataProperty.PARENT, data[0]);
    	outMetadata.put(DataProperty.LABEL,
    					"top " + this.topNPercent + "% rows by " +
    					this.columnToSortBy);
    	
    	return outData;
    }
}