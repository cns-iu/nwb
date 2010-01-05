package edu.iu.scipolicy.loader.nsf.db;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import au.com.bytecode.opencsv.CSVReader;
import edu.iu.scipolicy.loader.nsf.db.model.NSFModel;
import edu.iu.scipolicy.loader.nsf.db.utilities.NSFTableModelParser;
import edu.iu.scipolicy.utilities.nsf.NsfNames;

/**
 * This algorithm aggregates/collapses the input table based off on values in a 
 * "Aggregated On" column provided by the user. The types of aggregation performed
 * on each column can be selected by the user from a drop-down box. 
 * Currently "Sum", "Difference", "Average", "Min", "Max" aggregations are available 
 * for numerical column types.
 * Non-numerical column types are treated as String and hence a user can select 
 * appropriate text delimiters for each. 
 * 
 * @author cdtank
 *
 */

public class NSFDatabaseLoaderAlgorithm/* implements Algorithm*/ {
	
	private Data[] data;
    private Dictionary parameters;
	private LogService logger;
	private List<Integer> tableColumnNumericalParameterIDs, tableColumnStringParameterIDs;
	
    public static final String AGGREGATE_ON_COLUMN = "aggregateoncolumn";
    
/*    public NSFDatabaseLoaderAlgorithm(Data[] data, Dictionary parameters,
								  CIShellContext context, 
								  List<Integer> inputNumericalParameterIDs, 
								  List<Integer> inputStringParameterIDs) {
    	
        this.data = data;
        this.parameters = parameters;
        this.tableColumnNumericalParameterIDs = inputNumericalParameterIDs;
        this.tableColumnStringParameterIDs = inputStringParameterIDs;
		this.logger = (LogService) context.getService(LogService.class.getName());
	}*/

//    public Data[] execute() throws AlgorithmExecutionException {
    
    public static void main(String[] args) throws AlgorithmExecutionException {

//		File nsfCsv = (File) data[0].getData();
    	
    	File nsfCsv = new File("C:\\Documents and Settings\\Administrator\\workspace-slis\\workspace\\edu.iu.scipolicy.loader.nsf.db\\sample_nsf.csv");

		
		CSVReader nsfCsvReader;
		try {
			
			nsfCsvReader = createNsfCsvReader(nsfCsv);
			
			Map<String, Integer> nSFFieldsColumnNameToColumnIndex = 
				createMapFromNsfColumnNameToColumnIndex(nsfCsvReader);
			
			NSFModel inMemoryModel = new NSFTableModelParser()
											.createInMemoryModel(nSFFieldsColumnNameToColumnIndex, 
																 nsfCsvReader,
																 nsfCsv);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * After getting the Prefuse Table data pass it for aggregation.
		 * */
		
		/*
		 * After getting the output in table format make it available to the user.
		 * */
		//TODO: replcae null with the actual db object  data
/*		Data output = new BasicData(null, Table.class.getName());
		Dictionary metadata = output.getMetadata();
		metadata.put(DataProperty.LABEL, "Aggregation performed using unique values in \"" 
										 + "\" column.");
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);*/
		
//		return new Data[]{output};
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