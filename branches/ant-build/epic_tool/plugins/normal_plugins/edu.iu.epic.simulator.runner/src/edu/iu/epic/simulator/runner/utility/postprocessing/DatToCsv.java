package edu.iu.epic.simulator.runner.utility.postprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import org.cishell.utilities.FileUtilities;

import au.com.bytecode.opencsv.CSVWriter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class DatToCsv {
	public static final double HACK_THRESHOLD = 4e9;
	public static final int HACK_SUBSTITUTE = 0;
	
	public static final String TEMP_CSV_FILE_PREFIX = "epic-populations";
	public static final String CSV_FILE_EXTENSION = "csv";
	public static final String DAT_FILE_COMMENT_MARKER = "#";
	public static final String DAT_FILE_FIRST_COLUMN_NAME = "time";	
	public static final String DAT_FILE_COLUMN_NAMES_LINE_PREFIX =
		String.format("%s %s", DAT_FILE_COMMENT_MARKER, DAT_FILE_FIRST_COLUMN_NAME);
	
	private File datFile;
	private List<String> compartments;
	
	public DatToCsv(File datFile) {
		this.datFile = datFile;
	}
	
	public File convert() throws IOException {
		File csvFile =
			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
					TEMP_CSV_FILE_PREFIX, CSV_FILE_EXTENSION);
		final CSVWriter csvWriter =
			new CSVWriter(
					Files.newWriter(csvFile, Charset.forName("UTF-8")),
					CSVWriter.DEFAULT_SEPARATOR,
					CSVWriter.NO_QUOTE_CHARACTER);
		
		BufferedReader datReader = Files.newReader(datFile, Charset.forName("UTF-8"));
		
		convert(datReader, csvWriter);
		
		datReader.close();
		csvWriter.close();
		
		return csvFile;
	}
	
	public List<String> getCompartments() {
		return compartments;
	}

	private void convert(BufferedReader datReader, CSVWriter csvWriter) throws IOException {
		String datLine = null;
		while ((datLine = datReader.readLine()) != null) {
			processLine(datLine, csvWriter);
		}
	}

	private void processLine(String datLine, CSVWriter csvWriter) {
		if (datLine.startsWith(DAT_FILE_COLUMN_NAMES_LINE_PREFIX)) {
			// Skip the comment marker and take only the column names
			String lineStartingWithColumns =
				datLine.substring(datLine.indexOf(DAT_FILE_FIRST_COLUMN_NAME));
			
			String lineStartingWithCompartments =
				lineStartingWithColumns.substring(DAT_FILE_FIRST_COLUMN_NAME.length()).trim();
			compartments = Lists.newArrayList(lineStartingWithCompartments.split(" "));
			
			csvWriter.writeNext(lineStartingWithColumns.split(" "));
		} else if (datLine.startsWith(DAT_FILE_COMMENT_MARKER)) {
			// Skip other comments
		} else {
			List<String> processedPopulations = processPopulationLine(datLine);
			
			/* If any entry (including time) is non-zero, write the line.
			 * Otherwise skip it.
			 * This is meant to suppress the "0,0,0,..." line that sometimes comes out first
			 * in the "exact" simulator at the moment.
			 */
			if (anyNonZero(processedPopulations)) {
				csvWriter.writeNext(
						processedPopulations.toArray(new String[processedPopulations.size()]));
			}			
		}
	}

	private static List<String> processPopulationLine(String datLine) {
		List<String> processedPopulations = Lists.newArrayList();
		
		Iterator<String> iterator = ImmutableList.copyOf(datLine.split(" ")).iterator();
		
		int time = Integer.valueOf(iterator.next());		
		processedPopulations.add(String.valueOf(time));
		
		while (iterator.hasNext()) {
			String populationString = iterator.next();
			
			Number population = processPopulation(populationString);
			processedPopulations.add(String.valueOf(population));
		}
		
		return processedPopulations;
	}

	private static Number processPopulation(String populationString) {
		// Interpret as an integer when possible
		Number population;
		try {
			population = Integer.valueOf(populationString);
		} catch (NumberFormatException e) {
			population = Float.valueOf(populationString);
		}
		
		/* TODO Hack. The core simulator code seems to overflow at times, especially on the first
		 * timestep. Until the core code is fixed, this will attempt to correct for that.
		 * See if Bruno knows how to fix the overflow or whether he could give us an expert opinion
		 * on what to do here.
		 */
		if (population.floatValue() > HACK_THRESHOLD) {
			return HACK_SUBSTITUTE;
		} else {
			return population;
		}
	}
	
	private static boolean anyNonZero(List<String> processedPopulations) {
		for (String population : processedPopulations) {
			if (Integer.valueOf(population) != 0) {
				return true;
			}
		}
		
		return false;
	}
}
