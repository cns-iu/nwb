package edu.iu.nwb.converter.prefusensf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.conversion.ConversionException;
import org.cishell.service.conversion.DataConversionService;
import org.cishell.utilities.UnicodeReader;
import org.osgi.service.log.LogService;

import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.column.Column;

/**
 * @author mvukas
 *
 */
public class NSFReaderAlgorithm implements Algorithm {
	public static final String CSV_MIME_TYPE = "file:text/csv";
	public static final int READ_AHEAD_LIMIT = 2;
	public static final String OLD_VERSION_FIRST_HEADER = "Award Number";
	
	private boolean isOldNsfFormat = false;
	
	private String inputNameSeparator = ",";
	private String outputNameSeparator = ",";

	private String primaryPiColumnName = "PrincipalInvestigator";
	private String coPiColumnName = "Co-PIName(s)";
	private String allPiColumnName = "AllInvestigators";
	private String awardNumberColumnName = "AwardNumber";
	private String awardedToDateColumnName = "AwardedAmountToDate";
	private String arraAmountColumnName = "ARRAAmount";
	
	private Data[] data;
	private LogService log;	
	private DataConversionService conversionService;
	

	public NSFReaderAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		this.data = data;
		this.log = (LogService) context.getService(LogService.class.getName());
		this.conversionService = (DataConversionService)
			context.getService(DataConversionService.class.getName());
	}

	
	public Data[] execute() throws AlgorithmExecutionException {
		Data inputTableData = convertInputData(data[0]);
		Table inputTable = (Table) inputTableData.getData();
		
		Table copiedTable = copyAndNormalizeTable(inputTable);
		copiedTable = normalizeCoPIs(copiedTable);
		copiedTable = addPIColumn(copiedTable);
		if (isOldNsfFormat)
			copiedTable = normalizePrimaryPIs(copiedTable);
		
		return createOutData(data[0], copiedTable);
	}
	
	/**
	 * Iterates through an imported NSF table and handles some quirks caused by Prefuse's CSV importing
	 * mechanism. Normalizations include: changing dollar amounts from Strings to floats and also
	 * changing the corresponding column type so that certain aggregate functions will work
	 * 
	 * TODO: Since this function iterates through the entire table while making a new table object, it 
	 * would be most efficient to integrate some of the other table parsing private functions (like
	 * "normalizeCoPIs") to this one
	 * 
	 * @return a new table containing the proper schema and data changes
	 */
	private Table copyAndNormalizeTable(Table inputTable) {
		Table returnTable = new Table();
		
		// grabs Schema and dimensions for input table
		final Schema oldSchema = inputTable.getSchema();		
		final int numTableColumns = oldSchema.getColumnCount();
		final int numTableRows = inputTable.getRowCount();
		
		// add correct number of rows to return table
		returnTable.addRows(numTableRows);
		
		// add columns to return table, correcting them as iteration proceeds
		for (int col = 0; col < numTableColumns; col++) {
			String colHead = oldSchema.getColumnName(col);
			
			// go through and set the column type as needed before adding it
			Class currentColumnType = oldSchema.getColumnType(col);
			if (!isOldNsfFormat && (colHead.equals(awardedToDateColumnName) || colHead.equals(arraAmountColumnName))) {
				currentColumnType = Float.class;
			}
			returnTable.addColumn(colHead, currentColumnType);
			
			// iterate through the rows of the column, adding data and possibly normalizing it too
			for (int row = 0; row < numTableRows; row++) {
				Object currentCellValue = inputTable.get(row, col);
				if (!isOldNsfFormat && (colHead.equals(awardedToDateColumnName) || colHead.equals(arraAmountColumnName))) {
					if (currentCellValue instanceof String) {
						currentCellValue = normalizeDollarAmountString((String) currentCellValue);
					}
				}
				returnTable.set(row, col, currentCellValue);
			}
		}	
		return returnTable;
	}
	
	/**
	 * Turns a dollar value, in string form, into a float
	 * Ex: "$521,241.00" -> 521241.0
	 */
	private float normalizeDollarAmountString(String origValue) {
		if (origValue.length() < 5) {
			// should always have a length of at least 5, ex: $0.00
			return 0;
		}
		else {
			// remove everything except letters, numbers, and decimal point
			String normalizedContents = origValue.replaceAll("[^a-zA-Z0-9.]", "");
			try {
				// will throw exception if string contains something weird like words
				return Float.parseFloat(normalizedContents);
			} catch (NumberFormatException e) {
				log.log(2, "Invalid data found in column containing dollar amounts: \"" + origValue +
						"\". Assuming value of $0 for this row.");
				return 0;
			}
		}
	}

	
	/**
	 * TODO: integrate this into single function to improve efficiency
	 * @see NSFReaderAlgorithm#copyAndNormalizeTable(Table)
	 */
	private Table normalizeCoPIs(Table nsfTable) {
		Column coPIColumn = nsfTable.getColumn(coPiColumnName);
		for (int rowIndex = 0; rowIndex < nsfTable.getRowCount(); rowIndex++) {
			String contents = (String) coPIColumn.getString(rowIndex);
			if (contents != null && (!contents.equals(""))) {
				String[] coPINames = contents
						.split("\\" + inputNameSeparator);
				String[] normalizedCOPINames = new String[coPINames.length];
				for (int coPIIndex = 0; coPIIndex < coPINames.length; coPIIndex++) {
					String coPIName = coPINames[coPIIndex];
					String normalizedName = normalizeCOPIName(coPIName);
					normalizedCOPINames[coPIIndex] = normalizedName;
				}
				String normalizedContents = join(normalizedCOPINames,
						outputNameSeparator);
				coPIColumn.setString(normalizedContents, rowIndex);
			}
		}
		return nsfTable;
	}
	
	/**
	 * TODO: integrate this into single function to improve efficiency
	 * @see NSFReaderAlgorithm#copyAndNormalizeTable(Table)
	 */
	private Table normalizePrimaryPIs(Table nsfTable) {
		Column coPIColumn = nsfTable.getColumn(primaryPiColumnName);
		for (int rowIndex = 0; rowIndex < nsfTable.getRowCount(); rowIndex++) {
			String primaryPIName = (String) coPIColumn.getString(rowIndex);
			if (primaryPIName != null && (!primaryPIName.equals(""))) {
				String normalizedPIName = normalizePrimaryPIName(primaryPIName);
				coPIColumn.setString(normalizedPIName, rowIndex);
			}
		}
		return nsfTable;
	}

	/**
	 * TODO: integrate this into single function to improve efficiency
	 * @see NSFReaderAlgorithm#copyAndNormalizeTable(Table)
	 */
	private Table addPIColumn(Table normalizedNSFTable) {
		// add extra column made up of primary pi name + outputNameSeparator +
		// all the co-pi names
		normalizedNSFTable.addColumn(allPiColumnName, String.class);
		Column allPIColumn = normalizedNSFTable.getColumn(allPiColumnName);
		Column primaryPIColumn = normalizedNSFTable
				.getColumn(primaryPiColumnName);
		Column coPIColumn = normalizedNSFTable.getColumn(coPiColumnName);
		for (int rowIndex = 0; rowIndex < normalizedNSFTable.getRowCount(); rowIndex++) {
			String primaryPI = primaryPIColumn.getString(rowIndex);
			String coPIs = coPIColumn.getString(rowIndex);
			String allPIs = null;
			if (primaryPI != null && coPIs != null) {
				allPIs = primaryPI + outputNameSeparator + coPIs;
			} else if (primaryPI == null && coPIs == null) {
				allPIs = "";
			} else if (primaryPI == null) {
				allPIs = coPIs;
			} else if (coPIs == null) {
				allPIs = primaryPI;
			}
			allPIColumn.setString(allPIs, rowIndex);
		}
		// normalizedNSFTable.addColumn(allPiColumnName,
		// "CONCAT_WS('" + outputNameSeparator + "',[" +
		// primaryPiColumnName + "],[" + coPiColumnName + "])");
		return normalizedNSFTable;
	}

	private Data[] createOutData(Data originalData, Table normalizedNSFTable)
			throws AlgorithmExecutionException {
		try {
			Data[] dm = new Data[] { new BasicData(normalizedNSFTable,
					"prefuse.data.Table") };
			dm[0].getMetadata().put(DataProperty.LABEL, "Normalized NSF table");
			dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
			dm[0].getMetadata().put(DataProperty.PARENT, originalData);
			return dm;
		} catch (SecurityException e) {
			throw new AlgorithmExecutionException(e);
		}
	}

	private String normalizeCOPIName(String coPIName) {
		// remove all excessive whitespace between characters.
		String oneOrMoreWhitespaces = "[\\s]+";
		String normalizedCoPIName = coPIName.replaceAll(oneOrMoreWhitespaces,
				" ").trim();
		return normalizedCoPIName;
	}

	private String normalizePrimaryPIName(String primaryPIName) {
		// take everything after the last comma, remove it from the back, and
		// stick it on front
		int lastCommaIndex = primaryPIName.lastIndexOf(",");
		if (lastCommaIndex == -1) {
			printUnexpectedPrimaryPINameWarning(primaryPIName);
			return primaryPIName;
		}

		String beforeComma = primaryPIName.substring(0, lastCommaIndex).trim();
		String afterComma = primaryPIName.substring(lastCommaIndex + 1).trim();

		String normalizedPrimaryPIName = afterComma + " " + beforeComma;
		return normalizedPrimaryPIName;
	}

	private void printUnexpectedPrimaryPINameWarning(String primaryPIName) {
		log.log(LogService.LOG_WARNING,
				"Expected to find a comma separating last name"
				+ " from first name in the primary investigator name '"
				+ primaryPIName
				+ "'. \r\n "
				+ " We will not normalize this name, and will instead leave it as it is.");
	}

	private String join(String[] tokens, String separator) {
		StringBuffer joinedTokens = new StringBuffer();
		for (int i = 0; i < tokens.length; i++) {
			joinedTokens.append(tokens[i]);
			// add separator to end of all but last token
			if (i < tokens.length - 1) {
				joinedTokens.append(separator);
			}
		}
		return joinedTokens.toString();
	}

	private Data convertInputData(Data inputData)
			throws AlgorithmExecutionException {
		// PrintTable((Table) inputData.getData());
		Dictionary metadata = inputData.getMetadata();
		Data formatChangedData = new BasicData(metadata,
				cleanNSFCSVFormat((File) inputData.getData()),
				CSV_MIME_TYPE);

		try {
			Data convertedData = conversionService.convert(formatChangedData,
					"prefuse.data.Table");

			if (!(convertedData.getData() instanceof Table)) {
				throw new ConversionException(
					"Output of conversion was not a prefuse.data.Table");
			}

			return convertedData;
		} catch (ConversionException e) {
			throw new AlgorithmExecutionException(
					"Could not convert format to prefuse.data.Table", e);
		}
	}
	
	private File cleanNSFCSVFormat(File escapedQuoteFile) throws AlgorithmExecutionException {
		BufferedReader in = null;
		PrintWriter out = null;

		try {
			InputStream stream = new FileInputStream(escapedQuoteFile);
			/*
			 * UnicodeReader contains a hack for eating funny encoding
			 * characters that are sometimes stuck onto the beginning of files
			 * (BOMs). Necessary due to bug in standard reader.
			 */
			in = new BufferedReader(new UnicodeReader(stream));

			File outFile = File.createTempFile("cleanedNSFCSV", "csv");
			out = new PrintWriter(outFile, "UTF-8");

			String headerLine = in.readLine();
			if (headerLine != null) {
				// check NSF version, make changes if using the old file format
				if (headerLine.indexOf(OLD_VERSION_FIRST_HEADER) != -1) {
					isOldNsfFormat = true;
					log.log(2, "Old version NSF file format detected. Use | delimiter and \"-pre2014\" version " +
							"aggregation function files when executing algorithms on this file.");
					inputNameSeparator = "|";
					outputNameSeparator = "|";
					primaryPiColumnName = "Principal Investigator";
					coPiColumnName = "Co-PI Name(s)";
					allPiColumnName = "All Investigators";
					awardNumberColumnName = "Award Number";
					awardedToDateColumnName = "Awarded Amount To Date";
					arraAmountColumnName = "ARRA Amount";
				}
				else {
					log.log(2, "New version NSF file format detected. Use , delimiter and current version " +
							"aggregation function files when executing algorithms on this file.");
				}
				
				if (hasTwoAwardNumberColumns(headerLine)) {
					headerLine = renameDuplicateAwardNumberColumn(headerLine);
				}
				out.write(headerLine + "\r\n");
			}

			int c;
			while ((c = in.read()) != -1) {
				// if we see \". write "" instead
				if (c == '\\') {
					in.mark(READ_AHEAD_LIMIT);
					int nextC = in.read();
					if (nextC == '\"') {
						out.write('\"');
					}
					in.reset();
				}
				// if we see \r then something other than \n, don't write the \r
				else if (c == '\r') {
					in.mark(READ_AHEAD_LIMIT);
					int nextC = in.read();
					if (nextC != '\n') {
						// don't write the \r
					} else {
						// write the \r
						out.write('\r');
					}
					in.reset();
				} else {
					out.write((char) c);
				}
			}

			return outFile;
		} catch (FileNotFoundException e) {
			log.log(LogService.LOG_WARNING,
					"NSFReader could not find a file at "
					+ escapedQuoteFile.getAbsolutePath(), e);
			return escapedQuoteFile;
		} catch (IOException e) {
			log.log(LogService.LOG_WARNING,
					"Unable to remove slash escaped quotes from nsf csv file "
					+ "due to IO Exception", e);
			return escapedQuoteFile;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				throw new AlgorithmExecutionException(
						"Could not close file: " + e.getMessage(), e);
			}
		}
	}

	private boolean hasTwoAwardNumberColumns(String headerLine) {
		return headerLine.indexOf(awardNumberColumnName) != headerLine
				.lastIndexOf(awardNumberColumnName);
	}

	private String renameDuplicateAwardNumberColumn(String line) {
		return line.replaceAll(awardNumberColumnName,
				awardNumberColumnName + " Duplicate").replaceFirst(
				awardNumberColumnName + " Duplicate",
				awardNumberColumnName);
	}
}