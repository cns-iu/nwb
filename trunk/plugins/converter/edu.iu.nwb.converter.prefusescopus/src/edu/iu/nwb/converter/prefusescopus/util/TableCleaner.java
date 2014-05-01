package edu.iu.nwb.converter.prefusescopus.util;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.osgi.service.log.LogService;
import org.cishell.utilities.TableUtilities;

import prefuse.data.DataTypeException;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.column.Column;
import prefuse.util.collections.IntIterator;

public class TableCleaner {

	private static final String AUTHOR_COLUMN_NAME = "Authors";
	private static final String ORIG_AUTHOR_COLUMN_NAME_SEPARATOR = ", ";
	private static final String NEW_AUTHOR_COLUMN_NAME_SEPARATOR = "|";
	private static final String REFERENCE_COLUMN_NAME = "References";
	private static final String ORIG_REFERENCE_COLUMN_NAME_SEPARATOR = "; ";
	private static final String NEW_REFERENCE_COLUMN_NAME_SEPARATOR = "|";
	private static final String SELF_REFERENCE_COLUMN_NAME = "Self Reference";
	private static final String AUTHORS_COLUMN_NAME = "Authors";
	private static final String TITLE_COLUMN_NAME = "Title";
	private static final String YEAR_COLUMN_NAME = "Year";
	private static final String SOURCE_TITLE_COLUMN_NAME = "Source title";
	private static final String VOLUME_COLUMN_NAME = "Volume";
	private LogService log;

	public TableCleaner(LogService log) {
		this.log = log;
	}

	private Table addSelfReferences(Table scopusTable) throws AlgorithmExecutionException {
		//create the self-reference column
		scopusTable.addColumn(SELF_REFERENCE_COLUMN_NAME, String.class);
		//for each record in the table...
		for (IntIterator tableIt = scopusTable.rows(); tableIt.hasNext();) {
			int rowIndex = tableIt.nextInt();
			Tuple row = scopusTable.getTuple(rowIndex);
			//calculate the self-reference based on the contents of other fields
			String selfReference = createSelfReference(row);
			//add the self-reference to the current record
			scopusTable.setString(rowIndex, SELF_REFERENCE_COLUMN_NAME, selfReference);
		}
		
		return scopusTable;
	}

	private static final String ISSUE_COLUMN_NAME = "Issue";
	private static final String PAGE_START_COLUMN_NAME = "Page start";
	private static final String PAGE_END_COLUMN_NAME = "Page end";

	public Table cleanTable(Table scopusTableIn) throws AlgorithmExecutionException {
		scopusTableIn = fixBotchedUnicodeImport(scopusTableIn);
		Table scopusTable = normalizeAuthorNames(scopusTableIn);
		scopusTable = normalizeReferences(scopusTable);
		return addSelfReferences(scopusTable);
	}
	
	/**
	 * Because of an issue where Sci2 can't handle Unicode (UTF-8) encoding on CSVs, the 
	 * Byte Order Mark will get appended to the front of the first column header as garbled
	 * text. This method replaces that garbled nonsense header with the correct one. 
	 * TODO: Implement a more permanent fix for UTF-8 encoded CSV import errors
	 */
	private Table fixBotchedUnicodeImport(Table in) {
		Table returnTable = new Table();
		
		// grabs info about inputData
		Schema oldSchema = in.getSchema();		
		final int numTableColumns = oldSchema.getColumnCount();
		final int numTableRows = in.getRowCount();
		
		// add columns to return table, correcting them as iteration proceeds
		for (int i = 0; i < numTableColumns; i++) {
			String colHead = oldSchema.getColumnName(i);
			if (i == 0) {
				// assumes the first column will always be the authors column
				colHead = AUTHOR_COLUMN_NAME;
			}
			returnTable.addColumn(colHead, oldSchema.getColumnType(i));
		}
		
		// add existing rows to return table
		returnTable.addRows(numTableRows);
		for (int i = 0; i < numTableRows; i++) {
			TableUtilities.copyTableRow(i, i, returnTable, in);
		}
			
		return returnTable;
	}

	private Table normalizeAuthorNames(Table scopusTable) {
		Column authorColumn = scopusTable.getColumn(AUTHOR_COLUMN_NAME);
		if (authorColumn == null) {
			printNoAuthorColumnWarning();
			return scopusTable;
		}
		try {
	    	for (IntIterator tableIt = scopusTable.rows(); tableIt.hasNext();) {
				int rowIndex = tableIt.nextInt();
	    		String authors = authorColumn.getString(rowIndex);
	    		if (authors != null && ! authors.equals("")) {
	    			String normalizedAuthors = normalizeAuthorNames(authors);
	    			authorColumn.setString(normalizedAuthors, rowIndex);
	    		}
	    	}
		} catch (DataTypeException e) {
			printColumnNotOfTypeStringWarning(e);
			return scopusTable;
		}
		
		return scopusTable;
	}

	private String normalizeAuthorNames(String authorNames) {
		//trim leading and trailing whitespace from each author name.
		String[] eachAuthorName = authorNames.split(ORIG_AUTHOR_COLUMN_NAME_SEPARATOR);
		String normalizedAuthorNames = StringUtil.join(eachAuthorName, NEW_AUTHOR_COLUMN_NAME_SEPARATOR);
		return normalizedAuthorNames;
	}

	private Table normalizeReferences(Table scopusTable) {
		Column referenceColumn = scopusTable.getColumn(REFERENCE_COLUMN_NAME);
		if (referenceColumn == null) {
			printNoReferenceColumnWarning();
			return scopusTable;
		}
		try {
			for (IntIterator tableIt = scopusTable.rows(); tableIt.hasNext();) {
				int rowIndex = tableIt.nextInt();
	    		String references = referenceColumn.getString(rowIndex);
	    		if (references != null && ! references.equals("")) {
	    			String normalizedReferences = normalizeReferences(references);
	    			referenceColumn.setString(normalizedReferences, rowIndex);
	    		}
	    	}
		} catch (DataTypeException e) {
			printColumnNotOfTypeStringWarning(e);
			return scopusTable;
		}
		
		return scopusTable;
	}

	private String normalizeReferences(String references) {
		String[] eachReference = references.split(ORIG_REFERENCE_COLUMN_NAME_SEPARATOR);
		String normalizedReferences = StringUtil.join(eachReference, NEW_REFERENCE_COLUMN_NAME_SEPARATOR);
		return normalizedReferences;
	}

	private String createSelfReference(Tuple isiRow) throws AlgorithmExecutionException {
		   StringBuffer selfReference = new StringBuffer();
		   try {
			   String authors = extractAuthors(isiRow);
			   if (authors == null) {printNoAuthorColumnWarning(); return "";}
			   selfReference.append(authors);
			   selfReference.append(", ");
			   String title = extractTitle(isiRow);
			   if (title == null) {printNoTitleColumnWarning(); return "";}
			   selfReference.append(title);
			   selfReference.append(" ");
			   
			   String year = extractYear(isiRow);
			   if (year != null) {
				   selfReference.append(" (");
				   selfReference.append(year);
				   selfReference.append(")");
			   }
			   String sourceTitle = extractSourceTitle(isiRow);
			   if (sourceTitle != null) {
				   selfReference.append(" ");
				   selfReference.append(sourceTitle);
			   }
			   String volume = extractVolume(isiRow);
			   if (volume != null) {
				   selfReference.append(", ");
				   selfReference.append(volume);
			   }
			   String issue = extractIssue(isiRow);
			   if (issue != null) {
				   selfReference.append(" (");
				   selfReference.append(issue);
				   selfReference.append(")");
			   }
			   String pageStart = extractPageStart(isiRow);
			   if (pageStart != null) {
				   selfReference.append(", pp. ");
				   selfReference.append(pageStart);
			   } 
			   String pageEnd = extractPageEnd(isiRow);
			   if (pageEnd != null) {
				   selfReference.append("-");
				   selfReference.append(pageEnd);
			   }
			} catch (ArrayIndexOutOfBoundsException e) {
				/* Column requested does not exist (for entire table or just this
				 * field?)  Fail silently. This will happen normally.
				 * The remainder of the self reference will be returned.
				 */
			} catch (DataTypeException e) {
				/* Column type cannot be interpreted as a string (?)
				 * This should only happen if the column is of
				 * some bizarre unexpected type.
				 */
				throw new AlgorithmExecutionException(
						"Some elements in the tuple '" + isiRow
						+ "' cannot be converted to a String (apparently)", e);
			}
			
			return selfReference.toString();
	   }

	private String extractPageEnd(Tuple isiRow) {
	    	return trimBrackets(isiRow.getString(PAGE_END_COLUMN_NAME));
	}

	private String extractPageStart(Tuple isiRow) {
			return trimBrackets(isiRow.getString(PAGE_START_COLUMN_NAME));
	}

	private String extractIssue(Tuple isiRow) {
			return trimBrackets(isiRow.getString(ISSUE_COLUMN_NAME));
	}

	private String extractVolume(Tuple isiRow) {
			return isiRow.getString(VOLUME_COLUMN_NAME);
	}

	private String extractSourceTitle(Tuple isiRow) {
			return isiRow.getString(SOURCE_TITLE_COLUMN_NAME);
	}

	private String extractYear(Tuple isiRow) {
		return isiRow.getString(YEAR_COLUMN_NAME);
	}

	private String extractTitle(Tuple isiRow) {
			return isiRow.getString(TITLE_COLUMN_NAME);
	}

	private String extractAuthors(Tuple isiRow) {
			String authorsWithNewSeparator = isiRow.getString(AUTHORS_COLUMN_NAME);
			String[] eachAuthor = authorsWithNewSeparator.split("\\" + NEW_AUTHOR_COLUMN_NAME_SEPARATOR);
			//we need to use the original separator, because the reference column
			//we are constructing needs to look like the raw references that other
			//papers will use.
			String authorsWithOriginalSeparator = 
				StringUtil.join(eachAuthor, ORIG_AUTHOR_COLUMN_NAME_SEPARATOR);
			return authorsWithOriginalSeparator;
	}

	private String trimBrackets(String s) {
		if (s == null) return null;
		
		String tempS = s;
		if (tempS.startsWith("[")) {
			tempS = tempS.substring(1);
		}
		if (tempS.endsWith("]")) {
			tempS = tempS.substring(0, tempS.length() - 1);
		}
		
		String result = tempS;
		return result;
	}

	private void printNoAuthorColumnWarning() {
		this.log.log(LogService.LOG_WARNING, "Unable to find column with the name '" +
				AUTHOR_COLUMN_NAME + "' in scopus file. " +
						"We will continue on without attempting to normalize this column");
	}

	private void printNoReferenceColumnWarning() {
		this.log.log(LogService.LOG_WARNING, "Unable to find column with the name '" +
				REFERENCE_COLUMN_NAME + "' in scopus file. " +
						"We will continue on without attempting to normalize this column");
	}

	private void printNoTitleColumnWarning() {
		this.log.log(LogService.LOG_WARNING, "Unable to find column with the name '" +
				TITLE_COLUMN_NAME + "' in scopus file. " +
						"We will continue on without attempting to normalize this column");
	}

	private void printColumnNotOfTypeStringWarning(DataTypeException e) {
		this.log.log(LogService.LOG_WARNING,
				"The column '" + AUTHOR_COLUMN_NAME
				+ "' in the scopus file cannot be normalized, because it cannot "
				+ "be interpreted as text. Skipping normalization of authors", e);
	}

}
