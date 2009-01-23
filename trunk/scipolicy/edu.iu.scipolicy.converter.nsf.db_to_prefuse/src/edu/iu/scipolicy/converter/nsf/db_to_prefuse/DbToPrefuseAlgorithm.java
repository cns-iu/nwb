package edu.iu.scipolicy.converter.nsf.db_to_prefuse;

import java.sql.SQLException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.DataSourceWithID;
import org.cishell.service.database.DatabaseService;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.io.DataIOException;
import prefuse.data.io.sql.ConnectionFactory;
import prefuse.data.io.sql.DatabaseDataSource;

public class DbToPrefuseAlgorithm implements Algorithm {
	
	private static String[] awardExtractionFieldsInAwardTable = {
		"AWARD_NUMBER",
		"TITLE",
		"START_DATE",
		"EXPIRATION_DATE",
		"AWARDED_AMOUNT_TO_DATE"
	};

	private Data[] data;

	private LogService log;
	private DatabaseService dbService;

	public DbToPrefuseAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;

		this.log = (LogService) context.getService(LogService.class.getName());
		this.dbService = (DatabaseService) context.getService(DatabaseService.class.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException {
		//unpack nsf db
		
		DataSourceWithID nsfDb = (DataSourceWithID) data[0].getData();
		
		//extract prefuse table from nsf db
		
			Table awardTable;
			DatabaseDataSource wf;
			try {
				wf = ConnectionFactory.getDatabaseConnection(nsfDb.getConnection());
			    awardTable = wf.getData(formSQL(nsfDb.getID()));
			    //TODO: Rename award table column names to be the same as original csv
		 
			} catch (SQLException e) {
				throw new AlgorithmExecutionException(e);
			}
			 catch (DataIOException e) {
					throw new AlgorithmExecutionException(e);
			
			 }
			 
		//wrap and annote prefuse table with metadata
	    
	    Data awardTableData = wrapAndAnnotateAwardTable(awardTable);
		
		//return prefuse table data
	    
	    return new Data[]{ awardTableData };
		
	}
		
		   
	   private static String implodeStringArray(String[] stringArray,
	    										String separator) {
	    	final int stringArrayLength = stringArray.length;
	    	StringBuilder workingResultString = new StringBuilder();
	    	
	    	for (int ii = 0; ii < stringArrayLength; ii++) {
	    		workingResultString.append(stringArray[ii]);
	    		if (ii != stringArrayLength - 1) {
	    			workingResultString.append(separator);
	    		}
	    	}
	    	
	    	return workingResultString.toString();
	    }
	    
		private String formSQL(int dbID) {
	    	String sql = "SELECT " +
	    		implodeStringArray(awardExtractionFieldsInAwardTable, ",") + " FROM " +
	    		NSFConstants.AWARD_TABLE + dbID;
	    	System.out.println(sql);
	    	return sql;
	    }
		
		private Data wrapAndAnnotateAwardTable(Table awardTable) {
			Data awardTableData = new BasicData(awardTable, Table.class.getName());
			
			Dictionary awardTableMetadata = awardTableData.getMetadata();
			awardTableMetadata.put(DataProperty.LABEL, "NSF Awards Information");
			awardTableMetadata.put(DataProperty.PARENT, data[0]);
			
			return awardTableData;
		}
}