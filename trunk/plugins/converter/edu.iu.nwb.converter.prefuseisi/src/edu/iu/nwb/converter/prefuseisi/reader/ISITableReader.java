package edu.iu.nwb.converter.prefuseisi.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.osgi.service.log.LogService;

import prefuse.data.Schema;
import prefuse.data.Table;
import edu.iu.nwb.shared.isiutil.ContentType;
import edu.iu.nwb.shared.isiutil.ISITag;

/**
 * 
 * @author mwlinnem
 * based off of code from rduhon
 */
public class ISITableReader {
	
	private static final String NORMALIZED_SEPARATOR = "|";
	private static final int TAG_LENGTH = 2;
	
	private LogService log;
	private boolean normalizeAuthorNames;
	
	public ISITableReader(LogService log, boolean normalizeAuthorNames) {
		this.log = log;
		
		setNormalizeAuthorNames(normalizeAuthorNames);
	}
	
	public void setNormalizeAuthorNames(boolean normalizeAuthorNames) {
		this.normalizeAuthorNames = normalizeAuthorNames;
	}
	
	public Table readTable(FileInputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		
		TableData tableData = generateEmptyISITable();
		
		String currentLine = moveToNextLineWithTag(reader);
		
		while (currentLine != null) {
			String currentTagName = extractTagName(currentLine);
			ISITag currentTag = getOrCreateNewTag(currentTagName, tableData);
			
			 if (currentTag.equals(ISITag.END_OF_FILE)) {
				//we're done with the whole file
				break;
			} else if (currentTag.equals(ISITag.END_OF_RECORD)) {
				//we're done with this record
				tableData.moveOnToNextRow();
				currentLine = moveToNextLineWithTag(reader);
			} else if (currentTag.equals(ISITag.FILE_TYPE)) {
				//tag ignored
				currentLine = moveToNextLineWithTag(reader);
			} else if (currentTag.equals(ISITag.VERSION_NUMBER)) {
				//tag ignored
				currentLine = moveToNextLineWithTag(reader);
			} else if (currentTag.type.equals(ContentType.INTEGER)) {
				//side-effects the table data
				currentLine = addIntTagData(currentTag, currentLine, reader, tableData);
			} else if (currentTag.type.equals(ContentType.TEXT)) {
				//side-effects the table data
				currentLine = addStringTagData(currentTag, currentLine, reader, tableData);
			} else if (currentTag.type.equals(ContentType.MULTI_VALUE_TEXT)) {
				//side-effects the table data
				currentLine = addMultivalueTagData(currentTag, currentLine, reader, tableData);
			} else {
				//either we had an error in the program or there is something wrong with the file.
				currentLine = moveToNextLineWithTag(reader);
			}
			
		}
		
		Table constructedTable = tableData.getTable();
		return constructedTable;
	}
		
	private String addIntTagData(ISITag currentTag, String currentLine,
			BufferedReader reader, TableData tableData) throws IOException {
		currentLine = removeTag(currentLine);
		currentLine = currentLine.trim();
		
		try {
			int intValue = Integer.parseInt(currentLine);
		
			tableData.setInt(currentTag.name, intValue);
		
			String nextLine = moveToNextLineWithTag(reader);
			return nextLine;
		} catch (NumberFormatException e) {
			System.err.println("Tag '" + currentTag + "' " + "with data '" 
					+ currentLine + "' could not be parsed as an integer.");
			System.err.println("Treating the data as text instead");
			return addMultivalueTagData(currentTag, currentLine, reader, tableData);
		}
	}
	
	private String addStringTagData(ISITag currentTag, String currentLine,
			BufferedReader reader, TableData tableData) throws IOException {
		String nextLine;
		
		nextLine = processMultilineTagDataNormally(currentTag, 
				currentLine, reader, tableData);
		
		return nextLine;
	}
	
	private String addMultivalueTagData(ISITag currentTag, String currentLine,
			BufferedReader reader, TableData tableData) throws IOException {
		
		String separator = currentTag.separator;
		
		String nextLine;
		
		if (separator == null) {
			System.err.println("Programmer error: multi-value text tag not provided with separator");
			nextLine = moveToNextLineWithTag(reader);
		} else if (separator.equals("\n")) {
			nextLine = processMultilineTagDataWithNewlineSeparators(
					currentTag, currentLine, reader, tableData);
		} else {
			nextLine = processMultilineTagDataWithNonNewlineSeparators(
					currentTag, currentLine, reader, tableData, separator);
		}
		
		return nextLine;
	}
	
	private String processMultilineTagDataNormally(ISITag currentTag, 
			String currentLine,
			BufferedReader reader,
			TableData table) throws IOException {
		return processMultilineTagData(currentTag, currentLine, reader, table, " ", null);
	}
	
	private String processMultilineTagDataWithNewlineSeparators(ISITag currentTag,
			String currentLine,
			BufferedReader reader,
			TableData table) throws IOException {
		return processMultilineTagData(currentTag, currentLine, reader, table, NORMALIZED_SEPARATOR, null);
	}
	
	private String processMultilineTagDataWithNonNewlineSeparators(ISITag currentTag,
			String currentLine,
			BufferedReader reader,
			TableData table,
			String separator) throws IOException {
		return processMultilineTagData(currentTag, currentLine, reader, table, " ", separator);
	}
	
	private String processMultilineTagData(ISITag currentTag,
			String currentLine,
			BufferedReader reader,
			TableData tableData,
			String appendString,
			String separatorString) throws IOException {
		StringBuilder stringSoFar = new StringBuilder();

		currentLine = removeTag(currentLine);
		
		do {
			currentLine = currentLine.trim();
			currentLine = tagSpecificProcessing(currentTag, currentLine);
			
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
			allTagDataString = allTagDataString.replace(separatorString, NORMALIZED_SEPARATOR);
		}
		
		try {
		tableData.setString(currentTag.name, allTagDataString);
		} catch (Exception e) {
			log.log(LogService.LOG_INFO, "currentTag name: " + currentTag.name);
			log.log(LogService.LOG_INFO, "currentTag type: " + currentTag.type);
			log.log(LogService.LOG_INFO, "allTagDataString: " + allTagDataString);
			log.log(LogService.LOG_ERROR, "Error occurred while setting table data", e);
			e.printStackTrace();
		}
		
		String nextLineAfterThisTag = currentLine;
		return nextLineAfterThisTag;
	}
	
	private String extractTagName(String line) {
		
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
	
	private String moveToNextLineWithTag(BufferedReader reader) throws IOException {
		String nextNonEmptyLine;
		while ((nextNonEmptyLine = moveToNextNonEmptyLine(reader)) != null) {
			if (startsWithTag(nextNonEmptyLine)) {
				return nextNonEmptyLine;
			}
		}
		
		return null;
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
		return line.substring(Math.min(TAG_LENGTH, line.length()));
	}
	
	private ISITag getOrCreateNewTag(String tagName, TableData tableData) {
		
		Object getTagResult = ISITag.getTag(tagName);
		
		ISITag tag;
		if (getTagResult != null) {
			tag = (ISITag) getTagResult;
		} else {
			//since we have no stored information on this tag...
			//we attempt to parse the tag data in the most general way possible.
				
			System.err.println("Unrecognized tag '" + tagName + "'");
			System.err.println("Treating tag as if it held single-value text data");
				
			ContentType currentTagContentType = ContentType.TEXT;
			ISITag.addTag(tagName, currentTagContentType);
			tableData.addColumn(tagName, currentTagContentType.getTableDataType());
			
			tag = ISITag.getTag(tagName);
		}
		
		return tag;
	}
	
	private boolean startsWithTag(String potentialTag) {
		if (potentialTag.length() >= 2 &&
				(! Character.isWhitespace(potentialTag.charAt(0))) &&
				(! Character.isWhitespace(potentialTag.charAt(1)))) {
			return true;
		} else {
			return false;
		}
				
	}
	
	private String tagSpecificProcessing(ISITag tag, String line) {
		String processedLine;
		
		if (tag.equals(ISITag.AUTHORS)) {
			if (normalizeAuthorNames) {
				processedLine = processAuthorLine(line);
			} else {
				processedLine = line;
			}
		} else if (tag.equals(ISITag.CITED_REFERENCES)) {
			if (normalizeAuthorNames) {
			//same basic idea as authors tag, except each line of 
			//cited references contains more than just the author name.
			//everything before the first comma is the (primary?) authors name
			//(I have never seen more than one author listed per citation
			//so hopefully that never happens, since this will 
			//only do its magic on the first name.
			
			String[] fields = line.split(",");
			
			if (fields.length == 0) {
				System.err.println("Error in tagSpecificProcessing");
				return line;
			}
			
			String authorField = fields[0];
			String processedAuthorField = processAuthorLine(authorField);
			fields[0] = processedAuthorField;
			
			processedLine = joinOver(fields, ",");
			} else {
				processedLine = line;
			}
		} else {
			processedLine = line;
		}
		
		return processedLine;
	}
	
	private String processAuthorLine(String line) {
		//capitalize every word in the author name, except the last
		//(which is most likely an abbreviation of the authors first and/or middle name)
		
		String[] words = line.split(" ");
		for (int ii = 0; ii < words.length -1; ii++) {
			words[ii] = capitalizeOnlyFirstLetter(words[ii]);
		}
		
		return joinOver(words, " ");
	}
	
	private String capitalizeOnlyFirstLetter(String s) {
		if (s.length() > 0) {
			return Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase();
		} else {
			return s;
		}
	}
	
	private String joinOver(String[] parts, String joiner) {
		StringBuilder joinBuilder = new StringBuilder();
		for (int ii = 0; ii < parts.length; ii++) {
			joinBuilder.append(parts[ii]);
			
			if (ii < parts.length - 1) {
				joinBuilder.append(joiner);
			}
		}
		
		return joinBuilder.toString();
	}
	
	private TableData generateEmptyISITable() {
		Schema isiTableSchema = new Schema();
		
		//for each content type for isi tags..
		ContentType[] isiTagContentTypes = ContentType.getAllContentTypes();
		for (int ii = 0; ii < isiTagContentTypes.length; ii++) {
			ContentType isiTagContentType = isiTagContentTypes[ii];
			
			//for each tag corresponding to that content type...
			ISITag[] tagsOfThisContentType = ISITag.getTagsWithContentType(isiTagContentType);
			for (int jj = 0; jj < tagsOfThisContentType.length; jj++) {
				ISITag tag = tagsOfThisContentType[jj];
				
				Class tagTableDataType = tag.type.getTableDataType();
				
				//add that tag to the table schema, with the table storage data type associated with the tags content type
				//(e.g. Text -> String, Multi-value Text -> String)
				if (tagTableDataType != null) {
				isiTableSchema.addColumn(tag.name, tagTableDataType);
				}
			}
		}
		
		TableData emptyISITable = new TableData(isiTableSchema);
		return emptyISITable;
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
		
		public void addColumn(String columnName, Class columnType) {
			table.addColumn(columnName, columnType);
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
