package edu.iu.scipolicy.converter.nsf.csv_to_db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

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
import edu.iu.scipolicy.utilities.nsf.NsfNames;

public class CSVtoDBAlgorithm implements Algorithm  {
	private Data[] data;

	private LogService log;
	private DatabaseService dbService;

	public CSVtoDBAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;

		this.log = (LogService) context.getService(LogService.class.getName());
		this.dbService = (DatabaseService) context.getService(DatabaseService.class.getName());
	}
	
	/*
	 * Insert NSF grant data, held in a comma-separated value file, into a relational database.
	 * (NSF awarded grant data comes from the National Science Foundation website at nsf.gov ).
	 */
	public Data[] execute() throws AlgorithmExecutionException {
		
		Connection nsfDBConnection = null;
		
		try {
	
		//prepare to create and fill the nsf database with nsf csv contents.
			
		File nsfCsv = (File) data[0].getData();

		CSVReader nsfCsvReader = createNsfCsvReader(nsfCsv);

		Map<String, Integer> columnNameToColumnIndex = 
			createMapFromNsfColumnNameToColumnIndex(nsfCsvReader);
			
		/*
		 * create an empty database,
		 * add nsf tables to it, and
		 * fill those tables with data from csv.
		 */
		
		DataSourceWithID emptyDb = dbService.createDatabase();
		DataSourceWithID nsfDBInProgress = emptyDb;	
		nsfDBConnection = nsfDBInProgress.getConnection();
		
		//(side-effects the nsf database to contain all the tables we have to fill.)
		createDbTables(nsfDBConnection, nsfDBInProgress.getID());

		//(side-effects the nsf database to fill the tables using the nsf csv contents.)
		fillNsfDbQuickly(nsfDBConnection, nsfDBInProgress.getID(), nsfCsvReader, columnNameToColumnIndex);

		//annotate and return the nsf database
		
		Data nsfDBData = annotateAndWrapAsData(nsfDBInProgress);
			
		return new Data[]{nsfDBData};

		/*
		 * Error handling: throw an exception, aborting the algorithm.
		 */
			
		} catch (DatabaseCreationException e) {
			String message = 
				"Error occurred while creating nsf database." +
				"Aborting nsf database creation.";
			throw new AlgorithmExecutionException(message, e);
		} catch (FileNotFoundException e) {
			String message = e.getMessage() + "." + 
				" Aborting nsf database creation.";
			throw new AlgorithmExecutionException(message, e);
		} catch (IOException e) {
			String message = 
				e.getMessage() + "." +
				"Aborting nsf database creation.";
			throw new AlgorithmExecutionException(message, e);
		} catch (SQLException e) {
			String message = 
				"Error occurred while creating nsf database." +
				"Aborting nsf database creation.";
			throw new AlgorithmExecutionException(message, e);
		} finally {
			if (nsfDBConnection != null) {
					try {
						nsfDBConnection.close();
					} catch (SQLException e) {
						String message = 
							"Error occurred while closing database connection";
						throw new AlgorithmExecutionException(message, e);
					}
			}
		}
	}		
	
	private void createDbTables(Connection dbConnection, int dbID)
			throws AlgorithmExecutionException {
		try {
		// TODO: treat some columns as optional and others as mandatory (or is this too much of a hassle)
		// TODO: Tie this in with constants that control the names of NSF columns and their corresponding db column
		// names
		String createAwardTableSQL = "" + "CREATE TABLE " + NsfNames.DB.AWARD_TABLE + dbID + 
		"(" + 
		NsfNames.DB.AWARD_NUMBER + " INTEGER," +
		NsfNames.DB.AWARD_TITLE + " VARCHAR(500)," +
		NsfNames.DB.AWARD_START_DATE + " DATE," + 
		NsfNames.DB.AWARD_EXPIRATION_DATE + " DATE," +
		NsfNames.DB.AWARDED_AMOUNT_TO_DATE + " INTEGER," + 
		"PRIMARY KEY (" + NsfNames.DB.AWARD_NUMBER + ")" + 
		")";

		// execute the award table creation SQL

			Statement createAwardTableStatement = dbConnection.createStatement();
			createAwardTableStatement.execute(createAwardTableSQL);
		} catch (SQLException e) {
			throw new AlgorithmExecutionException("Error occurred while interacting with database", e);
		}
	}
	
	//this must be FAST! (potential bottleneck)
	private void fillNsfDbQuickly(Connection nsfDbConnection, int dbID, CSVReader nsfCSVReader,
			Map<String, Integer> columnNameToColumnIndex)
		throws AlgorithmExecutionException {
		try {
			
		// TODO: Put data from every column (not just those currently needed) into nsf-db
		int awardNumberIndex = columnNameToColumnIndex.get(NsfNames.CSV.AWARD_NUMBER);
		int awardTitleIndex = columnNameToColumnIndex.get(NsfNames.CSV.AWARD_TITLE);
		int startDateIndex = columnNameToColumnIndex.get(NsfNames.CSV.AWARD_START_DATE);
		int expirationDateIndex = columnNameToColumnIndex.get(NsfNames.CSV.AWARD_EXPIRATION_DATE);
		int awardedAmountToDateIndex = columnNameToColumnIndex.get(NsfNames.CSV.AWARDED_AMOUNT_TO_DATE);
		
		String insertIntoAwardSql = "INSERT INTO " + NsfNames.DB.AWARD_TABLE + dbID + 
		" (" + 
		NsfNames.DB.AWARD_NUMBER + "," +
		NsfNames.DB.AWARD_TITLE + "," +
		NsfNames.DB.AWARD_START_DATE + "," + 
		NsfNames.DB.AWARD_EXPIRATION_DATE + "," +
		NsfNames.DB.AWARDED_AMOUNT_TO_DATE + 
		") " +
		"VALUES " 
		+ "("
		+ "?," //awardNumber
		+ "?," //awardTitle
		+ "?," //startDate
		+ "?," //expirationDate
		+ "?" //awardAmountToDate
		+ ")";
	
		PreparedStatement insertIntoAward = nsfDbConnection.prepareStatement(insertIntoAwardSql);
		String[] nextAwardLine = null;
		int currentRowNumber = 1;
		while ((nextAwardLine = nsfCSVReader.readNext()) != null) {
			
			//TODO: Maybe in the future we can be more lenient with improper formatting, but for now it's either all perfect or we abort.
			/*
			 * TODO: When we start working with multiple tables, 
			 * and splitting csv fields into multiple db fields..
			 * we will need to do something smarter here, but this
			 * should be good for now
			 */
			
			String awardNumberString = nextAwardLine[awardNumberIndex];
			int awardNumber = Integer.parseInt(awardNumberString);
			insertIntoAward.setInt(1, awardNumber);
				
			String awardTitleString = nextAwardLine[awardTitleIndex];
			String awardTitle = awardTitleString;
			insertIntoAward.setString(2, awardTitle);
				
			String startDateString = nextAwardLine[startDateIndex];
			java.sql.Date startDate = parseDate(startDateString);
			insertIntoAward.setDate(3, startDate);
				
			String expirationDateString = nextAwardLine[expirationDateIndex];
			java.sql.Date expirationDate = parseDate(expirationDateString);
			insertIntoAward.setDate(4, expirationDate);
				
				
			String awardAmountToDateString = nextAwardLine[awardedAmountToDateIndex];
			int awardAmountToDate = Integer.parseInt(awardAmountToDateString);
			insertIntoAward.setInt(5, awardAmountToDate);

			insertIntoAward.addBatch();
			//TODO: Check warnings
			insertIntoAward.executeBatch();
			
			currentRowNumber++;
		}
		
		int numRows = currentRowNumber;
		System.out.println("Successfully loaded " + numRows + " rows into the NSF database");
		
		} catch (IOException e) {
			throw new AlgorithmExecutionException("An error occurred while loading nsf data into the database", e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AlgorithmExecutionException("An error occurred while loading nsf data into the database", e);
		} catch (NumberFormatException e) {
			//TODO: Should say which number failed.
			throw new AlgorithmExecutionException("An error occurred while attempting to parse a number from the nsf csv file", e);
		}
	}
	
	private Data annotateAndWrapAsData(DataSourceWithID nsfDB) {
		//create data object
		
		//TODO: Will need to be more clear as to what metadata we put in this
		Data dbData = new BasicData(nsfDB, nsfDB.getClass().getName());
		
		//annotate data
		
		Dictionary dbMetadata = dbData.getMetadata();
		
		//TODO: Get more information and use it to create better label
	  	dbMetadata.put(DataProperty.LABEL, "NSF award data");
	  	dbMetadata.put(DataProperty.PARENT, data[0]);
	  	//TODO: Make a database icon?
	  	
	  	return dbData;
	}
	
	private CSVReader createNsfCsvReader(File nsfCsv) throws IOException {
		//TODO: Currently we only support "csv" nsf files, not "excel" nsf files.
		//TODO: Add flexibility to support tabbed separated and double-quote escape chars.
		final char fieldSeparator = ',';
		final char fieldQuoteCharacter = '"';
		final int lineToStartReadingFrom = 0;
		final char quoteEscapeCharacter = '\\';
		
		CSVReader nsfCsvReader = new CSVReader(new FileReader(nsfCsv),
				fieldSeparator, fieldQuoteCharacter, lineToStartReadingFrom, quoteEscapeCharacter);
		
		return nsfCsvReader;
	}
	
	private Map<String, Integer> createMapFromNsfColumnNameToColumnIndex(CSVReader nsfCsvReader) 
		throws AlgorithmExecutionException, IOException {
		String[] columnNames = nsfCsvReader.readNext();
		if (columnNames == null || columnNames.length == 0) {
			throw new AlgorithmExecutionException("Cannot read in an empty nsf file");
		}

		Map<String, Integer> columnNameToColumnIndex = new HashMap<String, Integer>();
		for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
			columnNameToColumnIndex.put(columnNames[columnIndex].trim(), columnIndex);
		}
		
		return columnNameToColumnIndex;
	}
	
//	private void handleParsingException(int row, String columnName, String cellContents, String parseTypeName, Exception e) 
//		throws AlgorithmExecutionException {
//		String errorMessage = "" +
//		"Encountered a formatting error in row " + row + ", in the '" + columnName + "' column. '" +
//		cellContents + "' cannot be parsed as type '" + parseTypeName + "'. " + 
//		"Aborting loading process.";
//		throw new AlgorithmExecutionException(errorMessage, e);
//	}
	
	private static final DateFormat[] ACCEPTED_DATE_FORMATS = { 
		DateFormat.getDateInstance(DateFormat.FULL),
		new SimpleDateFormat("dd/MM/yy"),
		new SimpleDateFormat("dd/MM/yyyy"),
		DateFormat.getDateInstance(DateFormat.SHORT),
		DateFormat.getDateInstance(DateFormat.MEDIUM),
		DateFormat.getDateInstance(DateFormat.LONG),
		};
	
	private java.sql.Date parseDate(String dateString) 
		throws AlgorithmExecutionException {
		for (DateFormat format : ACCEPTED_DATE_FORMATS) {
			try {
				format.setLenient(false);
				java.util.Date date = format.parse(dateString);
				//WE PARSED THE DATE SUCCESSFULLY (if we get to this point)!
				//Finish up our processing and return the date.
				
				//TODO: Methinks this is a hack we should eliminate
				if (date.getYear() < 1900)
					date.setYear(date.getYear() + 1900);
				java.sql.Date dateForSQL = new java.sql.Date(date.getTime());
				return dateForSQL;
			} catch (ParseException e) {
				continue;
			}
		}
		
		//we could not parse the date with any of the accepted formats.
		
		String exceptionMessage = 
			"Could not parse the field " + 
			"'" + dateString + "'" +
			" as a date. Aborting the algorithm.";
		throw new AlgorithmExecutionException(exceptionMessage);
	}
}