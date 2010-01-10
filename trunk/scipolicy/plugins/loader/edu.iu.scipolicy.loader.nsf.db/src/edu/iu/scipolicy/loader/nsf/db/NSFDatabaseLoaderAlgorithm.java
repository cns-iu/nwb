package edu.iu.scipolicy.loader.nsf.db;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

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

import au.com.bytecode.opencsv.CSVReader;
import edu.iu.cns.database.loader.framework.RowItemContainer;
import edu.iu.cns.database.loader.framework.utilities.DatabaseModel;
import edu.iu.cns.database.loader.framework.utilities.DerbyDatabaseCreator;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Award;
import edu.iu.scipolicy.loader.nsf.db.utilities.NSFTableModelParser;
import edu.iu.scipolicy.utilities.nsf.NsfNames;

/**
 * @author cdtank
 *
 */

public class NSFDatabaseLoaderAlgorithm implements Algorithm {
	
	private Data[] data;
    private Dictionary parameters;
	private LogService logger;
	private DatabaseService databaseProvider;
	
    public static final String AGGREGATE_ON_COLUMN = "aggregateoncolumn";
    
    public NSFDatabaseLoaderAlgorithm(Data[] data, 
    								  Dictionary parameters,
    								  CIShellContext context) {
    	
        this.data = data;
        this.parameters = parameters;
		this.logger = (LogService) context.getService(LogService.class.getName());
        this.databaseProvider =
        	(DatabaseService) context.getService(DatabaseService.class.getName());
	}

    public Data[] execute() throws AlgorithmExecutionException {
    

		File nsfCsv = (File) data[0].getData();
    	
//    	File nsfCsv = new File("C:\\Documents and Settings\\cdtank\\workspace\\edu.iu.scipolicy.loader.nsf.db\\sample_nsf.csv");

    	Data output = null;
		
		CSVReader nsfCsvReader;
		try {
			
			nsfCsvReader = createNsfCsvReader(nsfCsv);
			
			Map<String, Integer> nSFFieldsColumnNameToColumnIndex = 
				createMapFromNsfColumnNameToColumnIndex(nsfCsvReader);
			
			DatabaseModel inMemoryModel = new NSFTableModelParser()
											.createInMemoryModel(nSFFieldsColumnNameToColumnIndex, 
																 nsfCsvReader,
																 nsfCsv);
			
			RowItemContainer<Award> rw = (RowItemContainer<Award>) inMemoryModel.getRowItemListByDatabaseTableName("AWARD");
			
			/*System.out.println(rw.getSchema());
			
			for (RowItem<Award> aw : rw.getItems()) {
				
				System.out.println(aw.getAttributes());
				
				System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||");
				System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||");
				
			}*/
			
			Database database = DerbyDatabaseCreator.createFromModel(databaseProvider, inMemoryModel, "NSF");
			
			/*
			 * After getting the output in table format make it available to the user.
			 * */
			//TODO: replcae null with the actual db object  data
			output = new BasicData(database, NSFDatabase.NSF_DATABASE_MIME_TYPE);
	    	Dictionary<String, Object> parentMetadata = data[0].getMetadata();
	    	Dictionary<String, Object> metadata = output.getMetadata();
	    	metadata.put(
	    		DataProperty.LABEL, "NSF Database From " + parentMetadata.get(DataProperty.LABEL));
	    	metadata.put(DataProperty.TYPE, DataProperty.DATABASE_TYPE);
	    	metadata.put(DataProperty.PARENT, data[0]);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		return new Data[]{output};
    }

	private static CSVReader createNsfCsvReader(File nsfCsv) throws IOException {
		//TODO: Currently we only support "csv" nsf files, not "excel" nsf files.
		//TODO: Add flexibility to support tabbed separated and double-quote escape chars.
		final char defaultFieldSeparator = ',';
		final char secondaryFieldSeparator = '\t';
		final char fieldQuoteCharacter = '"';
		final int lineToStartReadingFrom = 0;
		final char quoteEscapeCharacter = '\\';
		
		CSVReader nsfCsvReader = new CSVReader(new FileReader(nsfCsv),
				defaultFieldSeparator, fieldQuoteCharacter, lineToStartReadingFrom, quoteEscapeCharacter);
		
		/*
		 * Test if "," as a separator failed to create appropriate csv handler. If so create a
		 * new csv handler by using "\t" as the field separator.
		 * TODO: If this approach seems inefficient refactor to use better approach or library
		 * since the current library is not flexible enough. 
		 * */
		if (nsfCsvReader.readNext().length < 26) {
			return new CSVReader(new FileReader(nsfCsv),
					secondaryFieldSeparator, fieldQuoteCharacter, lineToStartReadingFrom, quoteEscapeCharacter);
		} else {
			return new CSVReader(new FileReader(nsfCsv),
					defaultFieldSeparator, fieldQuoteCharacter, lineToStartReadingFrom, quoteEscapeCharacter);
		}
		
		
	}

	private static Map<String, Integer> createMapFromNsfColumnNameToColumnIndex(CSVReader nsfCsvReader) 
		throws AlgorithmExecutionException, IOException {
		String[] columnNames = nsfCsvReader.readNext();
		if (columnNames == null || columnNames.length == 0) {
			throw new AlgorithmExecutionException("Cannot read in an empty nsf file");
		}
	
		Map<String, Integer> columnNameToColumnIndex = new HashMap<String, Integer>();
		for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
			columnNameToColumnIndex.put(columnNames[columnIndex].trim(), columnIndex);
		}
		
		/*
		 * Hard coding the column index for "Award Number". Since there is a duplicate
		 * column for the same with Same Name. Already emailed NSF about this.
		 * Analyzed good sample set of NSF Fields to summize that "Award Number" in the
		 * 0th position is more stable than the 24th Position.  
		 * */
		columnNameToColumnIndex.put(NsfNames.CSV.AWARD_NUMBER, 0);
		
		return columnNameToColumnIndex;
	}

}