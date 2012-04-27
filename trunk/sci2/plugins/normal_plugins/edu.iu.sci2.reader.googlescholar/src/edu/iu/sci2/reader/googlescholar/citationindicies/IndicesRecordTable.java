package edu.iu.sci2.reader.googlescholar.citationindicies;

import prefuse.data.Table;

/**
 * Merge Table. Holds the merge table created for each of the google scholar
 * algorithms that would help users when multiple authors are returned
 * 
 * Author: P632
 * 
 * Code Review: P632
 */

public class IndicesRecordTable {

	public static final String USER_ID_COLUMN_NAME = "Citation User ID";

	/** The Constant CITATION_TABLE_CITATIONS_ALL_COLUMN. */
	public static final String CITATIONS_COLUMN_NAME = "Citations";

	/** The Constant CITATION_TABLE_CITATIONS_2007_COLUMN. */
	public static final String CITATIONS_SINCE_2007_COLUMN_NAME = "Citations since 2007";

	/** The Constant CITATION_TABLE_H_INDEX_COLUMN. */
	public static final String H_INDEX_COLUMN_NAME = "h-index";

	/** The Constant CITATION_TABLE_H_INDEX_2007_COLUMN. */
	public static final String H_INDEX_SINCE_2007_COLUMN_NAME = "h-index since 2007";

	/** The Constant CITATION_TABLE_I10_INDEX_COLUMN. */
	public static final String I10_INDEX_COLUMN_NAME = "i10-index";

	/** The Constant CITATION_TABLE_I10_INDEX_2007_COLUMN. */
	public static final String I10_INDEX_SINCE_2007_COLUMN_NAME = "i10-index since 2007";

	/** The table. */
	private Table table;

	public IndicesRecordTable() {
		this.table = createTable();
	}
	
	private Table createTable() {
		Table table = new Table();
		table.addColumn(USER_ID_COLUMN_NAME, String.class);
		table.addColumn(CITATIONS_COLUMN_NAME, String.class);
		table.addColumn(CITATIONS_SINCE_2007_COLUMN_NAME, String.class);
		table.addColumn(H_INDEX_COLUMN_NAME, String.class);
		table.addColumn(H_INDEX_SINCE_2007_COLUMN_NAME, String.class);
		table.addColumn(I10_INDEX_COLUMN_NAME, String.class);
		table.addColumn(I10_INDEX_SINCE_2007_COLUMN_NAME, String.class);
		return table;
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
	public void addIndicesRecord(IndicesRecord record) {
		
		int rowIndex = table.addRow();
		this.setGoogleCitationUserId(rowIndex, record.getUserId());
		this.setCitations(rowIndex, record.getCitations());
		this.setCitationsSince2007(rowIndex, record.getCitationsSince2007());
		this.setHIndex(rowIndex, record.getHIndex());
		this.setHIndexSince2007(rowIndex, record.getHIndexSince2007());
		this.setI10Index(rowIndex, record.getI10Index());
		this.setI10IndexSince2007(rowIndex, record.getI10IndexSince2007());
	}

	/**
	 * Sets the queried author.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param authorName
	 *            the queried author
	 */
	private void setGoogleCitationUserId(int rowIndex, String userId) {
		table.setString(rowIndex, USER_ID_COLUMN_NAME, userId);
	}
	
	/**
	 * Sets the unique index.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param string
	 *            the value of the h-index
	 */
	private void setCitations(int rowIndex, String value) {
		table.setString(rowIndex, CITATIONS_COLUMN_NAME, value);
	}

	/**
	 * Sets the unique index.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param value
	 *            the value of the h-index since 2007
	 */
	private void setCitationsSince2007(int rowIndex, String value) {
		table.setString(rowIndex, CITATIONS_SINCE_2007_COLUMN_NAME, value);
	}

	/**
	 * Sets the unique index.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param value
	 *            the value of the h-index
	 */
	private void setHIndex(int rowIndex, String value) {
		table.setString(rowIndex, H_INDEX_COLUMN_NAME, value);
	}

	/**
	 * Sets the unique index.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param value
	 *            the value of the h-index since 2007
	 */
	private void setHIndexSince2007(int rowIndex, String value) {
		table.setString(rowIndex, H_INDEX_SINCE_2007_COLUMN_NAME, value);
	}

	/**
	 * Sets the unique index.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param value
	 *            the value of the h-index
	 */
	private void setI10Index(int rowIndex, String value) {
		table.setString(rowIndex, I10_INDEX_COLUMN_NAME, value);
	}

	/**
	 * Sets the unique index.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param value
	 *            the value of the h-index since 2007
	 */
	private void setI10IndexSince2007(int rowIndex, String value) {
		table.setString(rowIndex, I10_INDEX_SINCE_2007_COLUMN_NAME, value);
	}
}
