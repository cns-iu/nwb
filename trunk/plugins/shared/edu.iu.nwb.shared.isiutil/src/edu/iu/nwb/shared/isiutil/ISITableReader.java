package edu.iu.nwb.shared.isiutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.cishell.utilities.UnicodeReader;
import org.osgi.service.log.LogService;

import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;
import edu.iu.nwb.shared.isiutil.exception.ReadTableException;

/**
 * 
 * @author mwlinnem
 * based off of code from rduhon
 */
public class ISITableReader {	
	public static final String NORMALIZED_SEPARATOR = "|";
	public static final int MIN_TAG_LENGTH = 2;
	public static final String FILE_PATH_COLUMN_NAME = "File Name";
	
	/**
	 * The Web of Knowledge web site changed its export format in September of
	 * 2011, without changing the VN "version number" of the file. They changed
	 * the first line FN "file type" from "ISI Export Format" to
	 * "Thomson Reuters Web Of Knowledge".  This was accompanied by a change
	 * in the semantics of the author address field, so we need to know what
	 * version we are dealing with.
	 */
	public static enum FileVersion {
		OLD,      // Until September of 2011.
		NEW_2011, // September of 2011, until...
		UNKNOWN   // Whenever they decide they hate us, again. 
	}
	private String versionNumber = "";
	private FileVersion isiVersion = FileVersion.UNKNOWN; // may handle this differently later...

	
	private LogService log;
	private boolean normalizeAuthorNames;
	private String fileType = "";
	
	public ISITableReader(LogService log, boolean normalizeAuthorNames) {
		this.log = log;
		
		this.normalizeAuthorNames = normalizeAuthorNames;
	}
	
	public String getFileType() {
		return this.fileType;
	}

	public String getVersionNumber() {
		return this.versionNumber;
	}

	public Table readTable(String originalFileName, File file) throws IOException, ReadTableException {
		return readTable(originalFileName, file, false);
	}

	public Table readTable(String originalFileName, File file, boolean shouldFillFileMetadata)
			throws IOException, ReadTableException {
		return readTable(originalFileName, file, shouldFillFileMetadata, true);
	}
	
	public Table readTable(
			String originalFileName,
			File file,
			boolean shouldFillFileMetadata,
			boolean shouldClean)
			throws IOException, ReadTableException {
		BufferedReader reader =
			new BufferedReader(new UnicodeReader(new FileInputStream(file)));
					
		TableData tableData = generateEmptyISITable();
		String currentLine = moveToNextLineWithTag(reader);
		
		while (currentLine != null) {
			String currentTagName = extractTagName(currentLine);
			ISITag currentTag = getOrCreateNewTag(currentTagName, tableData);
			
			 if (currentTag.equals(ISITag.END_OF_FILE)) {
				// Tag ignored.
				currentLine = moveToNextLineWithTag(reader);
			} else if (currentTag.equals(ISITag.END_OF_RECORD)) {
				// We're done with this record.
				tableData.moveOnToNextRow();
				currentLine = moveToNextLineWithTag(reader);
			} else if (currentTag.equals(ISITag.FILE_TYPE)) {
				this.setFileType(extractTagValue(currentLine));
				
				currentLine = moveToNextLineWithTag(reader);
			} else if (currentTag.equals(ISITag.VERSION_NUMBER)) {
				this.versionNumber = extractTagValue(currentLine);
				currentLine = moveToNextLineWithTag(reader);
			} else if (currentTag.type.equals(ContentType.INTEGER)) {
				// Side-effects the table data.
				currentLine =
					addIntTagData(currentTag, currentLine, reader, tableData, shouldClean);
			} else if (currentTag.type.equals(ContentType.TEXT)) {
				// Side-effects the table data.
				currentLine = addStringTagData(
					currentTag, currentLine, reader, tableData, shouldClean);
			} else if (currentTag.type.equals(ContentType.MULTI_VALUE_TEXT)) {
				// Side-effects the table data.
				currentLine =
					addMultivalueTagData(currentTag, currentLine, reader, tableData, shouldClean);
			} else {
				// Either we had an error in the program or there is something wrong with the file.
				String logMessage =
					"No case in ISITableReader to handle the tag " +
					currentTag.columnName +
					".  Moving on to next tag.";
				this.log.log(LogService.LOG_WARNING, logMessage);
				currentLine = moveToNextLineWithTag(reader);
			}
		}

		Table constructedTable = tableData.getTable();
		
		if (shouldFillFileMetadata) {
			// Side-effects the table.
			fillFileMetadata(constructedTable, originalFileName);
		}

		return constructedTable;
	}

	
	private void setFileType(String fileType) {
		this.fileType = fileType;
		
		if (this.fileType.toLowerCase().contains("isi")) {
			this.isiVersion = FileVersion.OLD;
			this.log.log(LogService.LOG_INFO,
					"Found old-style ISI/Web Of Knowledge file.");
		} else if (this.fileType.toLowerCase().contains("web of knowledge")) {
			this.isiVersion = FileVersion.NEW_2011;
			this.log.log(LogService.LOG_INFO,
					"Found new-style ISI/Web Of Knowledge file.");
		} else {
			this.isiVersion = FileVersion.UNKNOWN;
			this.log.log(LogService.LOG_WARNING,
					"New ISI/Web of Knowledge file type?  " + fileType);
		}
	}

	private String addIntTagData(
			ISITag currentTag,
			String currentLine,
			BufferedReader reader,
			TableData tableData,
			boolean shouldClean) throws IOException, ReadTableException {
		String tagValue = extractTagValue(currentLine);
		
		try {
			int intValue = Integer.parseInt(tagValue);
			tableData.setInt(currentTag.columnName, intValue);
			String nextLine = moveToNextLineWithTag(reader);

			return nextLine;
		} catch (NumberFormatException e) {
			String logMessage =
				"WARNING: Tag '" + currentTag + "' " + "with data '" +
				tagValue + "' could not be parsed as an integer.  " +
				"Treating the data as text instead";
			this.log.log(LogService.LOG_WARNING, logMessage, e);

			return addMultivalueTagData(currentTag, tagValue, reader, tableData, shouldClean);
		}
	}
	
	private String addStringTagData(
			ISITag currentTag,
			String currentLine,
			BufferedReader reader,
			TableData tableData,
			boolean shouldClean) throws IOException, ReadTableException {
		return processMultilineTagDataNormally(
			currentTag, currentLine, reader, tableData, shouldClean);
	}
	
	private String addMultivalueTagData(
			ISITag currentTag,
			String currentLine,
			BufferedReader reader,
			TableData tableData,
			boolean shouldClean) throws IOException, ReadTableException {
		String separator = currentTag.separator;
		String nextLine;
		
		if (separator == null) {
			this.log.log(LogService.LOG_WARNING,
				"Programmer error: multi-value text tag not provided with separator");
			nextLine = moveToNextLineWithTag(reader);
		} else if (separator.equals("\n")) {
			nextLine = processMultilineTagDataWithNewlineSeparators(
				currentTag, currentLine, reader, tableData, shouldClean);
		} else {
			nextLine = processMultilineTagDataWithNonNewlineSeparators(
				currentTag, currentLine, reader, tableData, separator, shouldClean);
		}
		
		return nextLine;
	}
	
	private String processMultilineTagDataNormally(
			ISITag currentTag,
			String currentLine,
			BufferedReader reader,
			TableData table,
			boolean shouldClean) throws IOException, ReadTableException {
		return processMultilineTagData(
			currentTag, currentLine, reader, table, " ", null, shouldClean);
	}
	
	private String processMultilineTagDataWithNewlineSeparators(
			ISITag currentTag,
			String currentLine,
			BufferedReader reader,
			TableData table,
			boolean shouldClean) throws IOException, ReadTableException {
		return processMultilineTagData(
			currentTag, currentLine, reader, table, NORMALIZED_SEPARATOR, null, shouldClean);
	}
	
	private String processMultilineTagDataWithNonNewlineSeparators(
			ISITag currentTag,
			String currentLine,
			BufferedReader reader,
			TableData table,
			String separator,
			boolean shouldClean) throws IOException, ReadTableException {
		return processMultilineTagData(
			currentTag, currentLine, reader, table, " ", separator, shouldClean);
	}
	
	//DOIs all start with the DOI directory code, 10, followed by a dot.
	private static final String LOOKS_LIKE_A_DOI = "10\\..*";

	/**
	 * Side-effects {@code currentLine} variable!
	 */
	private String processMultilineTagData(
			ISITag currentTag,
			String currentLine,
			BufferedReader reader,
			TableData tableData,
			String appendString,
			String separatorString,
			boolean shouldClean) throws IOException, ReadTableException {
		currentLine = removeTag(currentLine);
		StringBuffer stringSoFar = new StringBuffer();
		
		do {
			currentLine = currentLine.trim();

			if (currentTag.equals(ISITag.CITED_REFERENCES) && 
					currentLine.matches(LOOKS_LIKE_A_DOI)) {
				/*
				 * ISI sucks, and puts DOIs on the next line in a list of cited references.
				 * This is kind of a hot mess.
				 */
				
				// Add the DOI onto the main part of the reference with a space between.
				stringSoFar.append(" ");
				
				/*
				 * We don't do the tag specific handling, because it expects every entry in cited
				 *  references to be a citation (this is a hack).
				 * We also skip appending the appendString so these will be treated as one entry.
				 */
			} else {
				if (shouldClean) {
					currentLine = tagSpecificProcessing(currentTag, currentLine);
				}

				stringSoFar.append(appendString);
			}

			stringSoFar.append(currentLine);
			
		} while ((currentLine = moveToNextNonEmptyLine(reader)).startsWith("  "));
		
		/*
		 * Take off the first append character (so we don't have a comma before the first element,
		 *  for instance).
		 */
		stringSoFar.delete(0, appendString.length());
		
		String allTagDataString = stringSoFar.toString();
		
		if (separatorString != null) {
			allTagDataString = allTagDataString.replaceAll(separatorString, NORMALIZED_SEPARATOR);
		}
		
		try {
			tableData.setString(currentTag.columnName, allTagDataString);
		} catch (Exception e) {
			this.log.log(LogService.LOG_INFO,
					"currentTag name: " + currentTag.columnName + "\n"
					+ "currentTag type: " + currentTag.type + "\n"
					+ "allTagDataString: " + allTagDataString, e);
			throw new ReadTableException(
					"Error occurred while setting table data: "
					+ e.getMessage(), e);
		}
		
		String nextLineAfterThisTag = currentLine;
		return nextLineAfterThisTag;
	}
	
	private String[] splitTagLine(String line) {
		String[] lineParts = new String[] {"", ""}; // tag, rest
		if (line != null && line.length() >= MIN_TAG_LENGTH) {
			lineParts = line.split(" ", 2);
		} else {
			this.log.log(LogService.LOG_WARNING,
					"Invalid line in isi file. Could not extract tag from line \r\n"
					+ line + "\nSkipping line...");
		}
		
		return lineParts;
	}
	
	private String extractTagName(String line) {
		return splitTagLine(line)[0];
	}

	private String extractTagValue(String lineWithTag) {
		String lineWithoutTag = removeTag(lineWithTag);

		return lineWithoutTag.trim();
	}

	private static String moveToNextLineWithTag(BufferedReader reader) throws IOException {
		String nextNonEmptyLine;
		while ((nextNonEmptyLine = moveToNextNonEmptyLine(reader)) != null) {
			if (startsWithTag(nextNonEmptyLine)) {
				return nextNonEmptyLine;
			}
		}
		
		return null;
	}
	
	private static String moveToNextNonEmptyLine(BufferedReader reader) throws IOException {
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
		return splitTagLine(line)[1];
	}
	
	private ISITag getOrCreateNewTag(String tagName, TableData tableData) {
		
		ISITag getTagResult = ISITag.getTag(tagName);

		if (getTagResult != null) {
			return getTagResult;
		}
		this.log.log(LogService.LOG_WARNING,
				"Unrecognized tag '" + tagName + "'.  Treating tag as if "
				+ "it held single-value text data");
			
		ContentType currentTagContentType = ContentType.TEXT;
		ISITag.addArbitraryTag(tagName, tagName, currentTagContentType);
		tableData.addColumn(ISITag.getColumnName(tagName), currentTagContentType.getTableDataType());
		
		return ISITag.getTag(tagName);
	}
	
	private static boolean startsWithTag(String potentialTag) {
		if (potentialTag.length() >= 2 &&
				(! Character.isWhitespace(potentialTag.charAt(0))) &&
				(! Character.isWhitespace(potentialTag.charAt(1)))) {
			return true;
		}
		return false;
				
	}
	
	private String tagSpecificProcessing(ISITag tag, String line) {
		String processedLine;
		
		if (tag.equals(ISITag.AUTHORS)) {
			if (this.normalizeAuthorNames) {
				processedLine = processAuthorLine(line);
				this.log.log(LogService.LOG_INFO, "Author names from the '" + ISITag.AUTHORS + "' field have been normalized by capatilizing the first letter of all but the last word in the author's name.");
			} else {
				processedLine = line;
			}
		} else if (tag.equals(ISITag.CITED_REFERENCES)) {
			if (this.normalizeAuthorNames) {
				/*
				 * Same basic idea as authors tag, except each line of  cited references contains
				 *  more than just the author name.
				 * Everything before the first comma is the (primary?) author's name.
				 * (I have never seen more than one author listed per citation so hopefully that
				 *  never happens, since this will only do its magic on the first name.
				 */
				String[] fields = line.split(",");

				if (fields.length == 0) {
					String logMessage = "Skipping this line because no fields were found: " + line;
					this.log.log(LogService.LOG_WARNING, logMessage);

					return line;
				}

				String authorField = fields[0];
				String processedAuthorField = processAuthorLine(authorField);
				fields[0] = processedAuthorField;

				processedLine = joinOver(fields, ",");
				this.log.log(LogService.LOG_INFO, "Author names from the '" + ISITag.CITED_REFERENCES + "' field have been normalized by capatilizing the first letter of all but the last word in the author's name.");
			} else {
				processedLine = line;
			}
		} else {
			processedLine = line;
		}
		
		return processedLine;
	}
	
	private static String processAuthorLine(String line) {
		/*
		 * Capitalize every word in the author name, except the last (which is most likely an
		 *  abbreviation of the authors first and/or middle name).
		 */

		String[] words = line.split(" ");

		for (int ii = 0; ii < words.length - 1; ii++) {
			words[ii] = capitalizeOnlyFirstLetter(words[ii]);
		}

		return joinOver(words, " ");
	}
	
	private static String capitalizeOnlyFirstLetter(String s) {
		if (s.length() > 0) {
			return Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase();
		}
		return s;
	}
	
	private static String joinOver(String[] parts, String joiner) {
		StringBuffer joinBuilder = new StringBuffer();
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
		
		// For each ISI tag, alphabetically:
		ISITag[] isiTagsAlphabetically = ISITag.getTagsAlphabetically();

		for (int ii = 0; ii < isiTagsAlphabetically.length; ii++) {
			ISITag tag = isiTagsAlphabetically[ii];
			
			/*
			 * Add that tag to the table schema, with the table storage data type associated with
			 *  the tags content type (e.g. Text -> String, Multi-value Text -> String).
			 */
			Class<?> tagTableDataType = tag.type.getTableDataType();
			
			if (tagTableDataType != null) {
				isiTableSchema.addColumn(tag.columnName, tagTableDataType);
			}
		}

		TableData emptyISITable = new TableData(isiTableSchema);

		return emptyISITable;
	}
	
	private void fillFileMetadata(Table table, String absoluteFilePath) {
		table.addColumn(FILE_PATH_COLUMN_NAME, String.class, absoluteFilePath);
		int fileTypeColumnIndex = table.getColumnNumber(ISITag.FILE_TYPE.getColumnName());
		int versionNumberColumnIndex =
			table.getColumnNumber(ISITag.VERSION_NUMBER.getColumnName());

		for (IntIterator rows = table.rows(); rows.hasNext(); ) {
			Tuple row = table.getTuple(rows.nextInt());
			row.setString(fileTypeColumnIndex, this.fileType);
			row.setString(versionNumberColumnIndex, this.versionNumber);
		}
	}
	
	private class TableData {
		private Table table;
		
		private int currentRow;
		private boolean currentRowIsFinished;
		
		public TableData(Schema schema) {
			this.table = schema.instantiate();
			this.currentRowIsFinished = true; //will cause first row to be created
		}
		
		public void moveOnToNextRow() {
			this.currentRowIsFinished = true;
		}
		
		public void setInt(String columnTag, int value) {
			ensureRowNotFinishedYet();
			
			this.table.setInt(this.currentRow, columnTag, value);
		}
		
		public void setString(String columnTag, String value) {
			ensureRowNotFinishedYet();
			
			this.table.setString(this.currentRow, columnTag, value);
		}
		
		public void addColumn(String columnName, Class<?> columnType) {
			this.table.addColumn(columnName, columnType);
		}
		
		public Table getTable() {
			return this.table;
		}
		
		private void ensureRowNotFinishedYet() {
			if (this.currentRowIsFinished) {
				this.currentRow = this.table.addRow();
				this.currentRowIsFinished = false;
			}
		}
	}
}
