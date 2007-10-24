package edu.iu.nwb.converter.prefuseisi.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import prefuse.data.Schema;
import prefuse.data.Table;
import edu.iu.nwb.shared.isiutil.ContentType;
import edu.iu.nwb.shared.isiutil.ISITag;

/**
 * 
 * @author mwlinnem
 * based off of code from rduhon
 */
public class NewISITableReader {
	
	private static final String NORMALIZED_SEPARATOR = "|";
	private static final int TAG_LENGTH = 2;

	private static Schema schema = new Schema();
	static {
		
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
				schema.addColumn(tag.name, tagTableDataType);
				}
			}
		}
	}
	
	public Table readTable(FileInputStream stream) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		
		TableData tableData = new TableData(schema);
		
		String currentLine = moveToNextNonEmptyLine(reader);
		
		while (currentLine != null) {
			String currentTagName = extractTagName(currentLine);
			ISITag currentTag = ISITag.getTag(currentTagName);
			
			if (currentTag == null) {
				//either we had an error in the program or there is something wrong with the file.
				currentLine = moveToNextNonEmptyLine(reader);
			}else if (currentTag.equals(ISITag.END_OF_FILE)) {
				//we're done with the whole file
				break;
			} else if (currentTag.equals(ISITag.END_OF_RECORD)) {
				//we're done with this record
				tableData.moveOnToNextRow();
				currentLine = moveToNextNonEmptyLine(reader);
			} else if (currentTag.equals(ISITag.FILE_TYPE)) {
				//tag ignored
				currentLine = moveToNextNonEmptyLine(reader);
			} else if (currentTag.equals(ISITag.VERSION_NUMBER)) {
				//tag ignored
				currentLine = moveToNextNonEmptyLine(reader);
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
				//side-effects the table data
				System.err.println("Unrecognized tag '" + currentTag + "'");
				System.err.println("Treating tag as if it held single-value text data");
				currentLine = addStringTagData(currentTag, currentLine, reader, tableData);
			}
			
		}
		
		Table constructedTable = tableData.getTable();
		return constructedTable;
	}
		
	private String addIntTagData(ISITag currentTag, String currentLine,
			BufferedReader reader, TableData tableData) throws IOException {
		currentLine = removeTag(currentLine);
		
		String[] parts = currentLine.split(" ");
		String integerPart = parts[1];
		int intValue = Integer.parseInt(integerPart);
		
		tableData.setInt(currentTag.name, intValue);
		
		String nextLine = moveToNextNonEmptyLine(reader);
		return nextLine;
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
			nextLine = moveToNextNonEmptyLine(reader);
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
		return processMultilineTagData(currentTag, currentLine, reader, table, "|", null);
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
		
		tableData.setString(currentTag.name, allTagDataString);
		
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
