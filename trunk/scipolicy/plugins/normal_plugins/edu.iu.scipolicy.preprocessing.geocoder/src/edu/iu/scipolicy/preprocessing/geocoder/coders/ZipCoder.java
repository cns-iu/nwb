package edu.iu.scipolicy.preprocessing.geocoder.coders;

//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.cishell.framework.algorithm.AlgorithmExecutionException;
//
//public class ZipCoder {

//	private static final int CACHE_ROW_ABBREVIATION_INDEX = 1;
//	private static final int CACHE_ROW_FULLFORM_INDEX = 0;
//	private static final int CACHE_ROW_LONGITUDE_INDEX = 3;
//	private static final int CACHE_ROW_LATITUDE_INDEX = 2;
//	
//	
//	
//	private static URL zipCodeFile = null;
//	
//	
//	
//	private static Map<String, List<Double>> zipCodeToLocation = null;
//	
//	
//	
//	public static void setZipCodeFile(URL zipCodeFile) {
//		ZipCoder.zipCodeFile = zipCodeFile;
//	}
//	
//
//
//	
//
//	
//	
//
//	/**
//	 * @return the zipCodeToLocation
//	 */
//	public static Map<String, List<Double>> getZipCodeToLocation() {
//		if (zipCodeToLocation == null)  {
//			initializeZipCodeLocationMappings(zipCodeFile);
//		}
//
//		return zipCodeToLocation;
//	}
//
//	
//
//	
//
//	
//	private CSVReader createNsfCsvReader(File nsfCsv) throws IOException {
//		//TODO: Currently we only support "csv" nsf files, not "excel" nsf files.
//		//TODO: Add flexibility to support tabbed separated and double-quote escape chars.
//		final char fieldSeparator = ',';
//		final char fieldQuoteCharacter = '"';
//		final int lineToStartReadingFrom = 0;
//		final char quoteEscapeCharacter = '\\';
//		
//		CSVReader nsfCsvReader = new CSVReader(new FileReader(nsfCsv),
//				fieldSeparator, fieldQuoteCharacter, lineToStartReadingFrom, quoteEscapeCharacter);
//		
//		return nsfCsvReader;
//	}
//	
//	private Map<String, Integer> createMapFromNsfColumnNameToColumnIndex(CSVReader nsfCsvReader) 
//		throws AlgorithmExecutionException, IOException {
//		String[] columnNames = nsfCsvReader.readNext();
//		if (columnNames == null || columnNames.length == 0) {
//			throw new AlgorithmExecutionException("Cannot read in an empty nsf file");
//		}
//
//		Map<String, Integer> columnNameToColumnIndex = new HashMap<String, Integer>();
//		for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
//			columnNameToColumnIndex.put(columnNames[columnIndex].trim(), columnIndex);
//		}
//		
//		return columnNameToColumnIndex;
//	}
//}
