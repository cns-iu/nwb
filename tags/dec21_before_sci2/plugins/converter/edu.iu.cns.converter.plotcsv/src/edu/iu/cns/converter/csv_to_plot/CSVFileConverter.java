package edu.iu.cns.converter.csv_to_plot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.cishell.utilities.FileUtilities;
import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;

import au.com.bytecode.opencsv.CSVReader;
import edu.iu.cns.converter.csv_to_plot.exceptiontypes.CSVFileReadingException;

/*
 * This class sucks and should probably be rewritten to use a PlotFileWriter
 *  (or something).
 */
public class CSVFileConverter {
	public static final String DEFAULT_COLUMN_NAME_PREFIX = "column";
	public static final String BASE_TEMPORARY_FILE_NAME = "csv_to_plot-";
	
	public static File convertCSVFileToPlotFile(File csvFile,
												LogService logger)
			throws IOException, CSVFileReadingException {
		CSVReader csvFileReader = new CSVReader(new FileReader(csvFile));
		try {
			String[] firstRow = csvFileReader.readNext();
			
			if (firstRow != null) {
				// TODO: Verify that the first row is actually a header.
				String[] header = extractHeaderFromFirstRow(firstRow);
				File plotFile = FileUtilities.
					createTemporaryFileInDefaultTemporaryDirectory(
						BASE_TEMPORARY_FILE_NAME, "plot");
				BufferedWriter plotFileWriter =
					new BufferedWriter(new FileWriter(plotFile));
				
				writeHeader(plotFileWriter, header);
				
				String[] row = csvFileReader.readNext();
				
				// TODO: while loop to convert the CSV file to the plot file.
				
				plotFileWriter.close();
				
				return plotFile;
			} else {
				String emptyCSVFileExceptionMessage =
					"The CSV file \"" +
					csvFile.getAbsolutePath() +
					"\" is empty.";
				
				throw new CSVFileReadingException(
					emptyCSVFileExceptionMessage);
			}
		} catch (FileNotFoundException csvFileNotFoundException) {
			String exceptionMessage = "The CSV file \"" +
									  csvFile.getAbsolutePath() +
									  "\" could not be found.";
			
			throw new CSVFileReadingException(exceptionMessage,
											  csvFileNotFoundException);
		}
	}
	
	private static String[] extractHeaderFromFirstRow(String[] firstRow) {
		try {
			for (int ii = 0; ii < firstRow.length; ii++) {
				Double.parseDouble(firstRow[ii]);
			}
		} catch (NumberFormatException notADoubleException) {
			return firstRow;
		}

		return constructDefaultHeader(firstRow.length);
	}
	
	private static String[] constructDefaultHeader(int headerLength) {
		String[] defaultHeader = new String[headerLength];
		
		for (int ii = 0; ii < defaultHeader.length; ii++) {
			defaultHeader[ii] = DEFAULT_COLUMN_NAME_PREFIX + ii;
		}
		
		return defaultHeader;
	}
	
	private static void writeHeader(BufferedWriter writer, String[] header)
			throws IOException {
		writer.write("# " + StringUtilities.implodeStringArray(header, " | "));
		writer.newLine();
	}
	
	private static void writeRow(BufferedWriter writer, String[] row)
			throws IOException {
		writer.write(StringUtilities.implodeStringArray(row, " "));
		writer.newLine();
	}
}