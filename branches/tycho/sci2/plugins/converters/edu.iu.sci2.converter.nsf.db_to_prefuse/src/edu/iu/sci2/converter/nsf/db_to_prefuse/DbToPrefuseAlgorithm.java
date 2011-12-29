package edu.iu.sci2.converter.nsf.db_to_prefuse;

import java.sql.SQLException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.DataSourceWithID;
import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.io.DataIOException;
import prefuse.data.io.sql.ConnectionFactory;
import prefuse.data.io.sql.DatabaseDataSource;
import edu.iu.sci2.utilities.nsf.NsfNames;

public class DbToPrefuseAlgorithm implements Algorithm {
	
	private static String[] BASIC_AWARD_TABLE_FIELDS = {
		NsfNames.DB.AWARD_NUMBER,
		NsfNames.DB.AWARD_TITLE,
		NsfNames.DB.AWARD_START_DATE,
		NsfNames.DB.AWARD_EXPIRATION_DATE,
		NsfNames.DB.AWARDED_AMOUNT_TO_DATE
	};

	private Data[] data;

	private LogService log;

	public DbToPrefuseAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;

		this.log = (LogService) context.getService(LogService.class.getName());
	}

	/*
	 * extract basic information about each awarded grant in the nsf db, 
	 * and put it into a prefuse table to return.
	 */
	public Data[] execute() throws AlgorithmExecutionException {
		
		DataSourceWithID nsfDb = (DataSourceWithID) data[0].getData();

		Table awardTable = extractTable(nsfDb);	 

	    Data awardTableData = wrapAndAnnotateAwardTable(awardTable);
		
	    return new Data[]{ awardTableData };
		
	}
	
	private Table extractTable(DataSourceWithID nsfDb) 
		throws AlgorithmExecutionException {
		try {
			DatabaseDataSource prefuseDataExtractor = 
				ConnectionFactory.getDatabaseConnection(nsfDb.getConnection());
			
		    String extractBasicAwardTableSql = "SELECT " +
	    		StringUtilities.implodeStringArray(BASIC_AWARD_TABLE_FIELDS, ",") + 
	    		" FROM " +
	    		NsfNames.DB.AWARD_TABLE + nsfDb.getID();
		    
		    Table awardTable = 
		    	prefuseDataExtractor.getData(extractBasicAwardTableSql);
		
		    return awardTable;
		
		} catch (SQLException e) {
			String message =
				"An unexpected error occurred while extracting data from the nsf database." + 
				"Aborting extraction algorithm.";
			throw new AlgorithmExecutionException(message, e);
		}
		 catch (DataIOException e) {
			 String message =
				"An unexpected error occurred while extracting data from the nsf database." + 
				"Aborting extraction algorithm.";
				throw new AlgorithmExecutionException(message, e);
		 } 
	}

	private Data wrapAndAnnotateAwardTable(Table awardTable) {
		Data awardTableData = new BasicData(awardTable, Table.class.getName());
			
		Dictionary awardTableMetadata = awardTableData.getMetadata();
		awardTableMetadata.put(DataProperty.LABEL, "Basic NSF Awards Information");
		awardTableMetadata.put(DataProperty.PARENT, data[0]);
		awardTableMetadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
			
		return awardTableData;
	}
}