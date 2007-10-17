package edu.iu.nwb.converter.prefuseisi.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import prefuse.data.Schema;
import prefuse.data.Table;

/**
 * 
 * @author mwlinnem
 * based off of code from rduhon
 */
public class NewISITableReader {
	
	private static final String NORMALIZED_SEPARATOR = "|";
	private static final int TAG_LENGTH = 2;
		
	private static List multivalueColumnTags = 
		Arrays.asList(new String[]{
				"AF",
				"AU",
				"C1",
				"CR",
				"DE",
				"ID",
				"SC",
				});
	
	private static List stringColumnTags = 
		Arrays.asList(new String[]{
				"AB",
				"AR",
				"BP",
				"DI",
				"DT",
				"EP",
				"GA",
				"IS",
				"J9",
				"JI",
				"LA",
				"PD",
				"PI",
				"PN",
				"PT",
				"PU",
				"RP",
				"SE",
				"SI",
				"SN",
				"SO",
				"SU",
				"TI",
				"VL",
				"WP",
				"UT" //looks like ISI:000173979600014. Treating it as a string for now.
				});
	
	private static List intColumnTags = 
		Arrays.asList(new String[]{
				"NR",
				"PG",
				"PY",
				"TC"
				});
	
	private static String fileTypeTag = "FN";
	private static String versionTag = "VR";
	private static String endRecordTag = "ER";
	private static String endFileTag = "EF";
	
	private static List allColumnTags = new ArrayList();
	static {
		allColumnTags.addAll(multivalueColumnTags);
		allColumnTags.addAll(stringColumnTags);
		allColumnTags.addAll(intColumnTags);
		allColumnTags.add(fileTypeTag);
		allColumnTags.add(versionTag);
		allColumnTags.add(endRecordTag);
		allColumnTags.add(endFileTag);
	}
	
	private static Schema schema = new Schema();
	static {
		Iterator multivalueColumnTagIter = multivalueColumnTags.iterator();
		while (multivalueColumnTagIter.hasNext()) {
			String multivalueColumnTag = (String) multivalueColumnTagIter.next();
			
			/*
			 * We represent multiple values in a single column using a string,
			 * where each value is separated by the special normalized separator.
			 */
			schema.addColumn(multivalueColumnTag, String.class);
		}
		
		Iterator stringColumnTagIter = stringColumnTags.iterator();
		while (stringColumnTagIter.hasNext()) {
			String stringColumnTag = (String) stringColumnTagIter.next();
			
			schema.addColumn(stringColumnTag, String.class);
		}
		
		Iterator intColumnTagIter = intColumnTags.iterator();
		while (intColumnTagIter.hasNext()) {
			String intColumnTag = (String) intColumnTagIter.next();
			
			schema.addColumn(intColumnTag, int.class);
		}
	}
	
	private static Map separators = new HashMap();
	
	static {
		separators.put("AF", "\n");
		separators.put("AU", "\n");
		separators.put("CR", "\n");
		separators.put("C1", "\n");
		separators.put("ID", ";");
		separators.put("DE", ";"); //guessing, since the format does not specify, but the other keyword field is ; separated
		separators.put("SC", ";");
	}
	
	public Table readTable(FileInputStream stream) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		
		TableData tableData = new TableData(schema);
		
		String currentLine = moveToNextNonEmptyLine(reader);
		
		while (currentLine != null) {
			String currentTag = extractTag(currentLine);
			
			if (currentTag == null) {
				//either we had an error in the program or there is something wrong with the file.
				currentLine = moveToNextNonEmptyLine(reader);
			}else if (currentTag.equals(endFileTag)) {
				//we're done with the whole file
				break;
			} else if (currentTag.equals(endRecordTag)) {
				//we're done with this record
				tableData.moveOnToNextRow();
				currentLine = moveToNextNonEmptyLine(reader);
			} else if (currentTag.equals(fileTypeTag)) {
				//tag ignored
				currentLine = moveToNextNonEmptyLine(reader);
			} else if (currentTag.equals(versionTag)) {
				//tag ignored
				currentLine = moveToNextNonEmptyLine(reader);
			} else if (intColumnTags.contains(currentTag)) {
				//side-effects the table data
				currentLine = addIntTagData(currentTag, currentLine, reader, tableData);
			} else if (stringColumnTags.contains(currentTag)) {
				//side-effects the table data
				currentLine = addStringTagData(currentTag, currentLine, reader, tableData);
			} else if (multivalueColumnTags.contains(currentTag)) {
				//side-effects the table data
				currentLine = addMultivalueTagData(currentTag, currentLine, reader, tableData);
			} else {
				System.err.println("Unrecognized tag '" + currentTag + "'");
				System.err.println("Attempting to skip it over...");
				currentLine = moveToNextNonEmptyLine(reader);
			}
			
		}
		
		Table constructedTable = tableData.getTable();
		return constructedTable;
	}
		
	private String addIntTagData(String currentTag, String currentLine,
			BufferedReader reader, TableData tableData) throws IOException {
		currentLine = removeTag(currentLine);
		
		String[] parts = currentLine.split(" ");
		String integerPart = parts[1];
		int intValue = Integer.parseInt(integerPart);
		
		tableData.setInt(currentTag, intValue);
		
		String nextLine = moveToNextNonEmptyLine(reader);
		return nextLine;
	}
	
	private String addStringTagData(String currentTag, String currentLine,
			BufferedReader reader, TableData tableData) throws IOException {
		String nextLine;
		
		nextLine = processMultilineTagDataNormally(currentTag, 
				currentLine, reader, tableData);
		
		return nextLine;
	}
	
	private String addMultivalueTagData(String currentTag, String currentLine,
			BufferedReader reader, TableData tableData) throws IOException {
		
		String separator = (String) separators.get(currentTag);
		
		String nextLine;
		
		if (separator.equals("\n")) {
			nextLine = processMultilineTagDataWithNewlineSeparators(
					currentTag, currentLine, reader, tableData);
		} else {
			nextLine = processMultilineTagDataWithNonNewlineSeparators(
					currentTag, currentLine, reader, tableData, separator);
		}
		
		return nextLine;
	}
	
	private String processMultilineTagDataNormally(String currentTag, 
			String currentLine,
			BufferedReader reader,
			TableData table) throws IOException {
		return processMultilineTagData(currentTag, currentLine, reader, table, " ", null);
	}
	
	private String processMultilineTagDataWithNewlineSeparators(String currentTag,
			String currentLine,
			BufferedReader reader,
			TableData table) throws IOException {
		return processMultilineTagData(currentTag, currentLine, reader, table, "|", null);
	}
	
	private String processMultilineTagDataWithNonNewlineSeparators(String currentTag,
			String currentLine,
			BufferedReader reader,
			TableData table,
			String separator) throws IOException {
		return processMultilineTagData(currentTag, currentLine, reader, table, " ", separator);
	}
	
	private String processMultilineTagData(String currentTag,
			String currentLine,
			BufferedReader reader,
			TableData tableData,
			String appendString,
			String separatorString) throws IOException {
		StringBuilder stringSoFar = new StringBuilder();

		currentLine = removeTag(currentLine);
		
		do {
			stringSoFar.append(appendString);
			stringSoFar.append(currentLine);
		} while ((currentLine = moveToNextNonEmptyLine(reader)).startsWith("  "));
		
		/*
		 * take off the first append character 
		 * (so we don't have a comma before the first element, for instance)
		 */
		stringSoFar.delete(0, appendString.length());
		
		String allTagDataString = stringSoFar.toString();
		
		if (separatorString != null) {
			allTagDataString.replace(separatorString, NORMALIZED_SEPARATOR);
		}
		
		tableData.setString(currentTag, allTagDataString);
		
		String nextLineAfterThisTag = currentLine;
		return nextLineAfterThisTag;
	}
	
	private String extractTag(String line) {
		
		String tag;
		if (line != null && line.length() >= TAG_LENGTH) {
			tag = line.substring(0, TAG_LENGTH);
		} else {
			System.err.println("Invalid line in isi file. Could not extract tag from line \r\n" +
					line);
			System.err.println("Skipping line...");
			tag = null;
		}
		
		return tag;
	}
	
	private String moveToNextNonEmptyLine(BufferedReader reader) throws IOException {
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (! (line.equals("") ||
					line.equals("\n") ||
					line.equals("\r") ||
					line.equals("\r\n"))) {
				break;
			}
		}
		
		return line;
	}
	
	private String removeTag(String line) {
		return line.substring(TAG_LENGTH);
	}
	
	private class TableData {
		private Table table;
		
		private int currentRow;
		private boolean currentRowIsFinished;
		
		public TableData(Schema schema) {
			table = schema.instantiate();
			currentRowIsFinished = true; //will cause first row to be created
		}
		
		public void moveOnToNextRow() {
			currentRowIsFinished = true;
		}
		
		public void setInt(String columnTag, int value) {
			ensureRowNotFinishedYet();
			
			table.setInt(currentRow, columnTag, value);
		}
		
		public void setString(String columnTag, String value) {
			ensureRowNotFinishedYet();
			
			table.setString(currentRow, columnTag, value);
		}
		
		public Table getTable() {
			return table;
		}
		
		private void ensureRowNotFinishedYet() {
			if (currentRowIsFinished) {
				currentRow = table.addRow();
				currentRowIsFinished = false;
			}
		}
	}
}
