package edu.iu.cns.algorithm.databasecomparer;

import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.service.database.Database;
import org.osgi.service.log.LogService;

import edu.iu.cns.algorithm.databasecomparer.core.ComparisonResult;
import edu.iu.cns.algorithm.databasecomparer.core.DatabaseComparer;
import edu.iu.cns.algorithm.databasecomparer.core.DifferenceLog;

public class DatabaseComparerAlgorithm implements Algorithm {
	
	private Database db1;
	private Database db2;
	private boolean useFullErrorReporting;
	private LogService logger;
    
    public DatabaseComparerAlgorithm(Data[] data,
    				  Dictionary parameters,
    				  CIShellContext ciShellContext) {
    	this.db1 = (Database) data[0].getData();
    	this.db2 = (Database) data[1].getData();
    	
    	this.useFullErrorReporting = ((Boolean) parameters.get("quickReporting")).booleanValue();
    	
    	this.logger = (LogService) ciShellContext.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		ComparisonResult result = DatabaseComparer.compare(db1, db2);
    	if (result.databasesWereEqual()) {
    		System.out.println("Databases were equal!");
    	} else {
    		DifferenceLog differenceLog = result.getDifferenceLog();
    		List differences = differenceLog.getDifferencesFound();
    		System.out.println("Databases were different. Differences follow:");
    		for (int ii = 0; ii < differences.size() ; ii++) {
    			String difference = (String) differences.get(ii);
    			System.out.println("Difference " + ii + ":" + difference);
    		}
    	}
    		return null;
    	} catch (Exception e) {
    		throw new AlgorithmExecutionException(e);
    		//TODO: Make better
    	}
    }
}