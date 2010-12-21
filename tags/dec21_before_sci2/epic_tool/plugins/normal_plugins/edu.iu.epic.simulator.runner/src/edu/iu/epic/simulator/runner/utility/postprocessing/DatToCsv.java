package edu.iu.epic.simulator.runner.utility.postprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.cishell.utilities.FileUtilities;

public class DatToCsv {
	public static final String CSV_FILE_EXTENSION = "csv";
	public static final String DAT_FILE_COMMENT_MARKER = "#";
	public static final String DAT_FILE_FIRST_COLUMN_NAME = "time";	
	public static final String DAT_FILE_COLUMN_NAMES_LINE_PREFIX =
		DAT_FILE_COMMENT_MARKER + " " + DAT_FILE_FIRST_COLUMN_NAME;
	
	private File datFile;

	
	public DatToCsv(File datFile) {
		this.datFile = datFile;
	}

	
	public File convert() throws IOException {
		// TODO Fix ordering of columns?
		File csvFile =
			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
					"simulator_output", CSV_FILE_EXTENSION);
		BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile));
		
		BufferedReader reader = new BufferedReader(new FileReader(this.datFile));
		
		String line = reader.readLine();
		do {
			writer.write(convertDatFileLineToCSV(line));
	
			line = reader.readLine();
		} while (line != null);
	
		reader.close();
		writer.close();			
		
		return csvFile;
	}

	private String convertDatFileLineToCSV(String line) {
		if (line.startsWith(DAT_FILE_COLUMN_NAMES_LINE_PREFIX)) {
			// Skip the comment marker and take only the column names
			String lineStartingWithColumns =
				line.substring(line.indexOf(DAT_FILE_FIRST_COLUMN_NAME));
			
			return (commaSeparate(lineStartingWithColumns) + "\n");
		} else if (line.startsWith(DAT_FILE_COMMENT_MARKER)) {
			return "";
		} else {
			return commaSeparate(line) + "\n";
		}
	}

	private String commaSeparate(String spaceSeparated) {
		String[] tokens = spaceSeparated.split(" +");
		
		StringBuilder builder = new StringBuilder();
		
		for (int ii = 0; ii < tokens.length; ii++) {
			builder.append(tokens[ii]);
			
			boolean isFinalToken = (ii == tokens.length - 1);
			if (!isFinalToken) {
				builder.append(",");
			}
		}
		
		return builder.toString();
	}
}
