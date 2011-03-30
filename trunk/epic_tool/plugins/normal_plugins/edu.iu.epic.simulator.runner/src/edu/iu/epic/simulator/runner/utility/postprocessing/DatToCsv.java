package edu.iu.epic.simulator.runner.utility.postprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.cishell.utilities.FileUtilities;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class DatToCsv {
	public static final double HACK_THRESHOLD = 4e9;
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
	
	/** TODO Hack.  The shell code itself seems to overflow at times, especially on the first
	 * timestep.  Until the shell code is fixed, this will attempt to correct for that. */
	private static String fixOverflow(String csvLine) {
		final String SEP = ",";

		return Joiner.on(SEP).join(Collections2.transform(
				Lists.newArrayList(csvLine.split(SEP)),
				new Function<String, Float>() {
					public Float apply(String token) {
						Float value = Float.valueOf(token);
						
						if (value > HACK_THRESHOLD) {
							System.err.println(
									"Fixed a seemingly overflowing compartment population " +
									"in postprocessing.");
							return 0f;
						} else {
							return value;
						}
					}					
				}
			));
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
			return fixOverflow(commaSeparate(line)) + "\n";
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
