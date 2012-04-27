package edu.iu.sci2.reader.googlescholar.citationtable;

import prefuse.data.Table;

/**
 * Merge Table. Holds the merge table created for each of the google scholar
 * algorithms that would help users when multiple authors are returned
 * 
 * Author: P632
 * 
 * Code Review: P632
 */

public class CitationRecordTable {

	/** The table. */
	private Table table;

	/** The Constant TITLE_COLUMN. */
	private static final String TITLE_COLUMN_NAME = "Title";

	/** The Constant AUTHOR_COLUMN. */
	private static final String AUTHOR_COLUMN_NAME = "Author";

	/** The Constant CITED_BY_COLUMN. */
	private static final String CITED_BY_COLUMN_NAME = "Cited by";

	/** The Constant YEAR_COLUMN. */
	private static final String YEAR_COLUMN_NAME = "Year";

	
	/**
	 * Instantiates a new merge table.
	 */
	public CitationRecordTable() {
		createTable();
	}
	
	private void createTable() {
		table = new Table();
		table.addColumn(TITLE_COLUMN_NAME, String.class);
		table.addColumn(AUTHOR_COLUMN_NAME, String.class);
		table.addColumn(CITED_BY_COLUMN_NAME, Integer.class);
		table.addColumn(YEAR_COLUMN_NAME, Integer.class);
	}
	
	/**
	 * Gets the table.
	 * 
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}
	
	/**
	 * 
	 * @param uniqueIndentifier
	 * 			Identifier to verify if this is result from the same search query 
	 * @param record
	 * 			A record to be added
	 */
	public void addCitationRecord(CitationRecord record) {
		
		int rowIndex = table.addRow();
		this.setTitle(rowIndex, record.getTitle());
		this.setAuthors(rowIndex, record.getAuthors());
		this.setCitedBy(rowIndex, record.getCitedBy());
		this.setYear(rowIndex, record.getYear());
	}

	/**
	 * Sets the Title.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param authorName
	 *            the citation title
	 */
	private void setTitle(int rowIndex, String userId) {
		table.set(rowIndex, TITLE_COLUMN_NAME, userId);
	}
	
	/**
	 * Sets the authors.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param string
	 *            the author list
	 */
	private void setAuthors(int rowIndex, String value) {
		table.set(rowIndex, AUTHOR_COLUMN_NAME, value);
	}

	/**
	 * Sets the cited by.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param value
	 *            the cited by
	 */
	private void setCitedBy(int rowIndex, Integer value) {
		table.set(rowIndex, CITED_BY_COLUMN_NAME, value);
	}

	/**
	 * Sets the year.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param value
	 *            the citation year
	 */
	private void setYear(int rowIndex, Integer value) {
		table.set(rowIndex, YEAR_COLUMN_NAME, value);
	}
}