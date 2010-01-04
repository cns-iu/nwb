package edu.iu.cns.database.connector;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseCreationException;
import org.cishell.service.database.DatabaseService;
import org.osgi.service.log.LogService;

public class DatabaseConnectorAlgorithm implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext ciShellContext;
    
    private LogService logger;
    private DatabaseService databaseService;
    
    public DatabaseConnectorAlgorithm(Data[] data,
    				  Dictionary parameters,
    				  CIShellContext ciShellContext) {
        this.data = data;
        this.parameters = parameters;
        this.ciShellContext = ciShellContext;
        
        logger = (LogService) ciShellContext.getService(LogService.class.getName());
        databaseService = (DatabaseService) ciShellContext.getService(DatabaseService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	String driver = (String) parameters.get("driver");
    	String url = (String) parameters.get("url");
    	String username = (String) parameters.get("username");
    	String password = (String) parameters.get("password");

      	try {
			Database db = 
				databaseService.connectToExistingDatabase(driver, url, username, password);
			//TODO: put db:any as a constant in some central CIShell place?
	    	Data dbData = new BasicData(db, Database.GENERIC_DB_MIME_TYPE);
	 
	    	Dictionary metadata = dbData.getMetadata();
	        metadata.put(DataProperty.LABEL, "Database from " + url);
	        metadata.put(DataProperty.TYPE,DataProperty.DATABASE_TYPE); 
	        
	        return new Data[]{ dbData };
		} catch (DatabaseCreationException e) {
			throw new AlgorithmExecutionException(e);
		}
		
	    	
	 
    }
}