package edu.iu.scipolicy.converter.nsf.csv_to_db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.DataSourceWithID;
import org.cishell.service.database.DatabaseCreationException;
import org.cishell.service.database.DatabaseService;
import org.osgi.service.log.LogService;

import au.com.bytecode.opencsv.CSVReader;

public class CSVtoDBAlgorithm implements Algorithm {
	private Data[] data;

	private LogService log;
	private DatabaseService dbService;

	public CSVtoDBAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;

		this.log = (LogService) context.getService(LogService.class.getName());
		this.dbService = (DatabaseService) context.getService(DatabaseService.class.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException {
		try {
		// unpack nsf-csv file

		File nsfCSV = (File) data[0].getData();

		// create nsf-db schema based on nsf-csv columns available

		Connection nsfDBConnection = null;
		try {
			// (create empty database)
			DataSourceWithID emptyDB = dbService.createDatabase();

			// (begin reading nsf-csv file header)
			CSVReader nsfCSVReader = new CSVReader(new FileReader(nsfCSV));

			String[] headerLine = nsfCSVReader.readNext();
			if (headerLine == null || headerLine.length == 0) {
				throw new AlgorithmExecutionException("Cannot read in an empty nsf file");
			}

			Map<String, Integer> columnNameToColumnIndex = new HashMap<String, Integer>();
			for (int columnIndex = 0; columnIndex < headerLine.length; columnIndex++) {
				columnNameToColumnIndex.put(headerLine[columnIndex], columnIndex);
			}
			
			Set<String> columnsPresentInCSV = new HashSet<String>(Arrays.asList(headerLine));

			DataSourceWithID nsfDBInProgress = emptyDB;
			
			nsfDBConnection = nsfDBInProgress.getConnection();

			addEmptyAwardTable(nsfDBConnection, columnsPresentInCSV, nsfDBInProgress.getID());

			// TODO: Add all tables to nsf DB
		
			// fill nsf-db with contents of nsf-csv
			
			fillNsfDbQuickly(nsfDBConnection, nsfCSVReader, columnNameToColumnIndex, columnsPresentInCSV, nsfDBInProgress.getID());
			
			// annotate nsf-db with appropriate metadata

			Data nsfDBData = annotateAndWrapAsData(nsfDBInProgress);
			
			// return nsf-db
			
			return new Data[]{nsfDBData};

		} catch (DatabaseCreationException e) {
			throw new AlgorithmExecutionException("Error occurred while creating database", e);
		} catch (FileNotFoundException e) {
			throw new AlgorithmExecutionException("Could not find nsf file to read in", e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException("Error occurred while reading nsf file", e);
		} catch (SQLException e) {
			throw new AlgorithmExecutionException("Could not establish connection to database", e);
		} finally {
			if (nsfDBConnection != null) {
					try {
						nsfDBConnection.close();
					} catch (SQLException e) {
						throw new AlgorithmExecutionException("Error occurred while closing database connection", e);
					}
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AlgorithmExecutionException(e);
		}
	}				

	private void addEmptyAwardTable(Connection dbConnection, Set columnsPresentInCSV, int dbID)
			throws AlgorithmExecutionException {
		// make the actual SQL to create the table

		// TODO: treat some columns as optional and others as mandatory (or is this too much of a hassle)
		// TODO: Tie this in with constants that control the names of NSF columns and their corresponding db column
		// names
		String createAwardTableSQL = "" + "CREATE TABLE AWARD" + dbID + "(" + "AWARD_NUMBER INTEGER," + "START_DATE DATE,"
				+ "EXPIRATION_DATE DATE," + "AWARDED_AMOUNT_TO_DATE INTEGER," + "PRIMARY KEY (AWARD_NUMBER)" + ")";

		// execute the award table creation SQL
		try {
			Statement createAwardTableStatement = dbConnection.createStatement();
			createAwardTableStatement.execute(createAwardTableSQL);
		} catch (SQLException e) {
			throw new AlgorithmExecutionException("Error occurred while interacting with database", e);
		}
	}
	
	//Contract specifies this must be FAST! (potential bottleneck)
	private void fillNsfDbQuickly(Connection nsfDbConnection, CSVReader nsfCSVReader, Map<String, Integer> columnNameToColumnIndex, Set<String> columnsPresentInCSV,
			int dbID)
		throws AlgorithmExecutionException {
		
		// TODO: Put data from every column (not just those currently needed) into nsf-db
		// TODO: Tie column names of csv AND db to constants file
		int awardNumberIndex = columnNameToColumnIndex.get("Award Number");
		int startDateIndex = columnNameToColumnIndex.get("Start Date");
		int expirationDateIndex = columnNameToColumnIndex.get("Expiration Date");
		int awardMoneyToDateIndex = columnNameToColumnIndex.get("Awarded Amount to Date");
		
		try {
			//TODO: Maybe a prepared statement here would improve performance?
			Statement fillDBStatement = nsfDbConnection.createStatement();
			String[] nextAwardLine = null;
			int currentRowNumber = 1; //We read the header row earlier (kind of a hack)
			while ((nextAwardLine = nsfCSVReader.readNext()) != null) {
				//TODO: Maybe in the future we can be more lenient with improper formatting, but for now it's either all perfect or we abort.
				String awardNumberString = nextAwardLine[awardNumberIndex];
				String startDateString = nextAwardLine[startDateIndex];
				String expirationDateString = nextAwardLine[expirationDateIndex];
				String awardMoneyToDateString = nextAwardLine[awardMoneyToDateIndex];
				
				//(This would be a huge no-no if we cared about security at all, but I'm assuming we don't have anything to secure)
				fillDBStatement.addBatch("INSERT INTO AWARD" + dbID + " (AWARD_NUMBER, START_DATE, EXPIRATION_DATE, AWARDED_AMOUNT_TO_DATE)" +
					"VALUES ("
						+ awardNumberString + "," 
						+ "'" + startDateString + "'," 
						+ "'" + expirationDateString + "'," 
						+ awardMoneyToDateString 
						+ ")");

				currentRowNumber++;
				System.out.println("Putting CSV row " + currentRowNumber + " into database");
			}
			
			//TODO: Check for errors 
			fillDBStatement.executeBatch();
			
		} catch (IOException e) {
			throw new AlgorithmExecutionException("An error occurred while loading nsf data into the database", e);
		} catch (SQLException e) {
			throw new AlgorithmExecutionException("An error occurred while loading nsf data into the database", e);
		}
	}
	
	private Data annotateAndWrapAsData(DataSourceWithID nsfDB) {
		//create data object
		
		//TODO: Will need to be more clear as to what metadata we put in this
		Data dbData = new BasicData(nsfDB, nsfDB.getClass().getName());
		
		//annotate data
		
		Dictionary<String, String> dbMetadata = dbData.getMetadata();
		
		//TODO: Get more information and use it to create better label
	  	dbMetadata.put(DataProperty.LABEL, "NSF award data");
	  	
	  	return dbData;
	}
	
	private void handleParsingException(int row, String columnName, String cellContents, String parseTypeName, Exception e) 
		throws AlgorithmExecutionException {
		String errorMessage = "" +
		"Encountered a formatting error in row " + row + ", in the '" + columnName + "' column. '" +
		cellContents + "' cannot be parsed as type '" + parseTypeName + "'. " + 
		"Aborting loading process.";
		throw new AlgorithmExecutionException(errorMessage, e);
	}
}