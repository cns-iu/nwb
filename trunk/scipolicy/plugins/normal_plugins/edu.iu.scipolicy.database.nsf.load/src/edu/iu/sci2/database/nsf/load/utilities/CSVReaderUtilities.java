package edu.iu.sci2.database.nsf.load.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.cishell.framework.data.Data;
import org.cishell.utilities.StringUtilities;
import org.cishell.utilities.UnicodeReader;

import edu.iu.scipolicy.utilities.nsf.NSF_CSV_FieldNames;

import au.com.bytecode.opencsv.CSVReader;

public class CSVReaderUtilities {
	public final static char TAB_SEPARATOR = '\t';
	
	public static String[] getHeader(Data data) throws IOException {
		return getHeader((File)data.getData());
	}
	
	/**
	 * 
	 * @param file - The target CSV file
	 * @return return array of column names if success else return null
	 * @throws IOException
	 */
	public static String[] getHeader(File file) throws IOException {
		CSVReader reader = createCSVReaderWithRightSeparator(file, false);
		String[] header = StringUtilities.simpleCleanStrings(reader.readNext());
		reader.close();

		if (header != null) {
			return StringUtilities.simpleCleanStrings(header);
		} else {
			return null;
		}
	}

	/**
	 * Verify the given CSV file separator and create the reader with 
	 * the right separator. Currently, it only supports comma and tab 
	 * separators
	 * @param file - the target CSV file
	 * @param skipHeader - true if want to skip the header
	 * @return The reader with the right separator
	 * @throws IOException - Thrown while failed to open and read from file
	 */
	public static CSVReader createCSVReaderWithRightSeparator(File file, boolean skipHeader) 
			throws IOException {
		
		CSVReader reader = createCSVReaderWithRightSeparator(file);
		
		/* Skip header if includeHander is false */
		if (skipHeader) {
			reader.readNext();
		}
		
		return reader;
	}
	
	/**
	 * Verify the given CSV file separator and create the reader with 
	 * the right separator. Currently, it only supports comma and tab 
	 * separators
	 * @param file - the target CSV file
	 * @return The reader with the right separator
	 * @throws IOException - Thrown while failed to open and read from file
	 */
	public static CSVReader createCSVReaderWithRightSeparator(File file) 
			throws IOException {
		
		CSVReader reader; 
		
		/* verify if it is comma separator */
		if (isTabSeparatedCSV(file)) {
			reader = createTSVReader(file);
		} else {
			reader = createCSVReader(file);
		}
		
		return reader;
	}

	public static boolean isTabSeparatedCSV(File file) throws IOException {
		CSVReader nsfCsvReader = CSVReaderUtilities.createCSVReader(file);
		
		/*
		 * Test if "," as a separator failed to create appropriate csv handler. If so create a
		 * new csv handler by using "\t" as the field separator.
		 * TODO: If this approach seems inefficient refactor to use better approach or library
		 * since the current library is not flexible enough. 
		 * */
		if (nsfCsvReader.readNext().length < NSF_CSV_FieldNames.CSV.DEFAULT_TOTAL_NSF_FIELDS) {
			return true;
		} else {
			return false;
		}
	}
	
	public static CSVReader createCSVReader(File file) throws FileNotFoundException {
		return new CSVReader(new UnicodeReader(new FileInputStream(file)));
	}
	
	public static CSVReader createTSVReader(File file) throws FileNotFoundException {
		return new CSVReader(new UnicodeReader(new FileInputStream(file)), TAB_SEPARATOR);
	}

	public static int rowCount(File file, boolean skipHeader) throws IOException {
		return createCSVReaderWithRightSeparator(file, skipHeader).readAll().size();
	}
}