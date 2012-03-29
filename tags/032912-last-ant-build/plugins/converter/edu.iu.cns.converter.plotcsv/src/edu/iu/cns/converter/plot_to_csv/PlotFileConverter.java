package edu.iu.cns.converter.plot_to_csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.cishell.utilities.FileUtilities;

import au.com.bytecode.opencsv.CSVWriter;
import edu.iu.cns.converter.plot_to_csv.exceptiontypes.PlotFileReadingException;

public class PlotFileConverter {
	public static final String UNROUNDED_DECIMAL_PATTERN =
		"#.############################";
	public static final String PIPE_DELIMITER_PATTERN = "\\s*\\|\\s*";
	public static final String COMMENT_MARKER_PATTERN = "\\s*#+\\s*";
	public static final String CSV_FILE_EXTENSION = "csv";
	public static final String ONE_OR_MORE_WHITESPACES_PATTERN = "\\s+";
	public static final String STARTS_WITH_POUND_SIGN_PATTERN = "#.*";
	public static final String DEFAULT_COLUMN_NAME_PREFIX = "column";
	public static final String BASE_TEMPORARY_FILE_NAME = "plot_to_csv-";
	public static final String NOT_A_NUMBER_PREFIX = "NOT A NUMBER";

	
	public static File convertPlotFileToCSVFile(File plotFile)
			throws IOException, PlotFileReadingException {
		try {
			String[] plotFileHeader = readHeader(plotFile);
			File csvFile = convertPlotFile(plotFile, plotFileHeader);
			
			return csvFile;
		} catch (FileNotFoundException plotFileNotFoundException) {
			String exceptionMessage = "The plot file \"" +
									  plotFile.getAbsolutePath() +
									  "\" could not be found.";
			
			throw new PlotFileReadingException(exceptionMessage,
											   plotFileNotFoundException);
		}
	}
	
	private static String[] readHeader(File plotFile)
			throws FileNotFoundException,
				   IOException,
				   PlotFileReadingException {
		BufferedReader plotFileReader =
			new BufferedReader(new FileReader(plotFile));
		
		String[] header = null;		
		String line = null;
		try {
			line = plotFileReader.readLine();
			boolean lookingForHeader = true;
			
			while (lookingForHeader) {
				if (line == null) {
					lookingForHeader = false;
				} else if (lineIsComment(line)) {
					if (!commentLineIsEmpty(line)) {
						header = splitLineAsHeader(line);
					}
					
					line = plotFileReader.readLine();
				} else if (lineIsBlank(line)) {
					line = plotFileReader.readLine();
				} else {
					lookingForHeader = false;
				}
			}
		} catch (IOException lineReadingException) {
			throw new PlotFileReadingException(lineReadingException);
		} finally {
			plotFileReader.close();
		}
		
		if (header != null) {
			// We found a possible header.
			if (line != null) {
				/*
				 * line will be valid if there actually is data in the
				 *  file.  Verify that our found "header" at least matches
				 *  the actual data in number of columns.
				 */
				String[] defaultHeader =
					constructDefaultHeaderFromDataLine(line);
				
				if (header.length == defaultHeader.length) {
					return header;
				} else {
					return defaultHeader;
				}
			} else {
				/*
				 * We reached the end of the file without finding any
				 *  actual data.  The result CSV file will contain the
				 *  "header" we found and no data rows.
				 */
				return header;
			}
		} else {
			/*
			 * There were no comment lines, so we will try to generate a
			 *  generic header from the actual data, if there is any.
			 */ 
			if (line != null) {
				/*
				 * An actual line of data was found, so generate a generic
				 *  header from it.
				 */
				return constructDefaultHeaderFromDataLine(line);
			} else {
				/*
				 * This file is effectively empty, which calls for
				 *  us failing.
				 */
				String exceptionMessage =
					"The plot file \"" +
					plotFile.getAbsolutePath() +
					"\" is empty.  No CSV file can be created from it.";

				throw new PlotFileReadingException(exceptionMessage);
			}
		}
	}
	
	private static File convertPlotFile(File plotFile, String[] plotFileHeader)
			throws FileNotFoundException,
				   IOException,
				   PlotFileReadingException {
		BufferedReader plotFileReader =
			new BufferedReader(new FileReader(plotFile));
		File csvFile =
			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
				BASE_TEMPORARY_FILE_NAME, CSV_FILE_EXTENSION);
		CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFile));
		
		csvWriter.writeNext(plotFileHeader);
		
		try {
			String line = plotFileReader.readLine();
			
			while (line != null) {
				if (!lineIsComment(line) && !lineIsBlank(line)) {
					String[] tokens = splitLineAsData(line);
					csvWriter.writeNext(tokens);
				}
				
				line = plotFileReader.readLine();
			}
		} catch (IOException lineReadingException) {
			throw new PlotFileReadingException(lineReadingException);
		} finally {
			csvWriter.close();
		}
		
		return csvFile;
	}
	
	private static boolean lineIsComment(String line) {
		Scanner lineScanner = new Scanner(line);

		// The first token begins with a #
		return (lineScanner.hasNext(STARTS_WITH_POUND_SIGN_PATTERN));
	}
	
	private static boolean lineIsBlank(String line) {
		return (!(new Scanner(line)).hasNext()); 
	}
	
	// Expects line to be a comment line.
	private static boolean commentLineIsEmpty(String line) {
		// assert (lineIsComment(line));
	
		String[] splitLine = splitLineAsHeader(line);
		
		return (splitLine.length == 0);
	}
	
	// Expects line to not be a comment line and not be blank.
	private static String[] constructDefaultHeaderFromDataLine(String line) {
		// assert (!lineIsComment(line));
		// assert (!lineIsBlank(line));
		
		String[] tokensInLine = splitLineAsData(line);
		String[] defaultHeader = new String[tokensInLine.length];
		
		for (int ii = 0; ii < defaultHeader.length; ii++) {
			defaultHeader[ii] = DEFAULT_COLUMN_NAME_PREFIX + ii;
		}
		
		return defaultHeader;
	}

	// Expects originalLine to be a comment line.
	private static String[] splitLineAsHeader(String originalLine) {
		// assert(lineIsComment(originalLine));
		
		Scanner tokenizer = new Scanner(originalLine);
		tokenizer.skip(COMMENT_MARKER_PATTERN);

		if (tokenizer.hasNext()) {
			String commentContents = tokenizer.nextLine();

			if (commentContents.indexOf("|") != -1) {
				return commentContents.split(PIPE_DELIMITER_PATTERN);
			} else {
				return commentContents.split(ONE_OR_MORE_WHITESPACES_PATTERN);
			}
		} else {
			return new String[] {};
		}
	}
	
	private static String[] splitLineAsData(String line) {
		Scanner tokenizer = new Scanner(line);
		List tokens = new ArrayList();
		
		while (tokenizer.hasNext()) {
			tokens.add(convertToDecimalNotation(tokenizer.next()));
		}
		
		return (String[])tokens.toArray(new String[0]);
	}
	
	/* 
	 * If numberAsString holds a number in scientific notation,
	 * convert it to decimal notation.
	 */
	private static String convertToDecimalNotation(String numberAsString) {
		// Check for a scientific notation delimiter.
		if (numberAsString.indexOf("E") != -1 || numberAsString.indexOf("e") != -1) {
			Format format =
				new DecimalFormat(UNROUNDED_DECIMAL_PATTERN);
			
			try {
				return format.format(new Double(numberAsString));
			} catch (NumberFormatException numberFormatException) {
				return NOT_A_NUMBER_PREFIX + " (" + numberAsString + ")";
			}
		} else {
			return numberAsString;
		}
	}
}