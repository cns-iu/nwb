package edu.iu.scipolicy.tool.db_to_prefuse;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.io.DataIOException;
import prefuse.data.io.sql.ConnectionFactory;
import prefuse.data.io.sql.DatabaseDataSource;

public class DbToPrefuseAlgorithm implements Algorithm {
	
	private Data inputData;

	private LogService log;

	public DbToPrefuseAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		this.inputData =  data[0];

		this.log = (LogService) context.getService(LogService.class.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException {
		try {
		Database db = (Database) inputData.getData();
		Connection dbConnection = db.getConnection();
		
		List<Data> outputTables = new ArrayList<Data>();
		List<String> tableNames = getTableNames(dbConnection);
		for (String tableName : tableNames) {
			Table prefuseTable = extractTable(tableName, dbConnection);	 
			
			Data prefuseTableData = wrapAndAnnotateTable(tableName, prefuseTable);
			outputTables.add(prefuseTableData);
		}

	   
		
	    return (Data[]) outputTables.toArray(new Data[outputTables.size()]);
		} catch (Exception e) {
			throw new AlgorithmExecutionException(e);
		}
	}
	
	private Table extractTable(String tableName, Connection dbConnection) 
		throws AlgorithmExecutionException {
		try {
			DatabaseDataSource prefuseDataExtractor = 
				ConnectionFactory.getDatabaseConnection(dbConnection);
			
		    String extractBasicAwardTableSql = "SELECT * FROM " + tableName;
		    
		    Table table = 
		    	prefuseDataExtractor.getData(extractBasicAwardTableSql);
		
		    return table;
		
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

	private Data wrapAndAnnotateTable(String name, Table table) {
		Data tableData = new BasicData(table, Table.class.getName());
			
		Dictionary awardTableMetadata = tableData.getMetadata();
		awardTableMetadata.put(DataProperty.LABEL, "Database Table: " + name);
		awardTableMetadata.put(DataProperty.PARENT, this.inputData);
		awardTableMetadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
			
		return tableData;
	}
	
	
	private static final int SCHEMA_NAME_INDEX = 2;
	private static final int TABLE_NAME_INDEX = 3;

	 private static final String NONSYSTEM_SCHEMA_NAME = "APP";
	 
	 private static List<String> getTableNames(Connection dbConnection) throws SQLException {
		   DatabaseMetaData dbMetadata = dbConnection.getMetaData();
		   ResultSet allTableNames = dbMetadata.getTables(null, "APP", null, null);
		   
		   List<String> nonSystemTableNames = new ArrayList<String>();
		   while (allTableNames.next()) {
			   String schemaName = allTableNames.getString(SCHEMA_NAME_INDEX);
			   if (isNonSystemSchemaName(schemaName)) {
				   String tableName = allTableNames.getString(TABLE_NAME_INDEX);
				   nonSystemTableNames.add(tableName);
			   }
		   }
		   
		   return nonSystemTableNames;
	}
  
  

	
	private static boolean isNonSystemSchemaName(String tableName) {
		return tableName.indexOf(NONSYSTEM_SCHEMA_NAME) != -1;
	}
}