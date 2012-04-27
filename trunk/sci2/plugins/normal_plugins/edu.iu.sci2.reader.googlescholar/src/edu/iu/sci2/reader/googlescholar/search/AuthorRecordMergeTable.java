package edu.iu.sci2.reader.googlescholar.search;

import java.util.HashMap;
import java.util.Map;

import prefuse.data.Table;

/**
 * Merge Table. Holds the merge table created for each of the google scholar
 * algorithms that would help users when multiple authors are returned
 * 
 * @author P632
 */

public class AuthorRecordMergeTable {

	/** The Constant MergeTable Author column. */
	public static final String CITATION_USER_ID_COLUMN = "Citation User ID";
	
	/** The Constant MergeTable University column. */
	public static final String QUERIED_NAME_COLUMN = "Queried Name";
	
	/** The Constant MergeTable Author column. */
	public static final String AUTHOR_COLUMN = "Author";

	/** The Constant MergeTable University column. */
	public static final String UNIVERSITY_COLUMN = "University";

	/** The Constant MergeTable Email column. */
	public static final String EMAIL_COLUMN = "Verified email";

	/** The Constant MergeTable Cited_by column. */
	public static final String CITED_BY_COLUMN = "Cited by";

	/** The Constant MergeTable Unique Index column. */
	public static final String UNIQUE_INDEX_COLUMN = "Unique Index";

	/** The Constant MergeTable CombineValue column. */
	public static final String COMBINE_VALUE_COLUMN = "Combine Values";

	private Table table;
	private int uniqueIndex;
	private Map<String, Integer> identifierToIndexMap;

	public AuthorRecordMergeTable() {
		table = createTable();
		uniqueIndex = 0;
		identifierToIndexMap = new HashMap<String, Integer>();
	}
	
	private Table createTable() {
		Table table = new Table();
		table.addColumn(CITATION_USER_ID_COLUMN, String.class);
		table.addColumn(QUERIED_NAME_COLUMN, String.class);
		table.addColumn(AUTHOR_COLUMN, String.class);
		table.addColumn(UNIVERSITY_COLUMN, String.class);
		table.addColumn(EMAIL_COLUMN, String.class);
		table.addColumn(CITED_BY_COLUMN, String.class);
		table.addColumn(UNIQUE_INDEX_COLUMN, String.class);
		table.addColumn(COMBINE_VALUE_COLUMN, String.class);
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
	public void addAuthorRecord(String uniqueIndentifier, AuthorRecord record) {
		
		int rowIndex = table.addRow();
		int uniqueIdentifierIndex = this.getUniqueIdentifierIndex(rowIndex, uniqueIndentifier);
		
		this.setUniqueIndex(rowIndex, uniqueIdentifierIndex);
		this.setAuthor(rowIndex, record.getName());
		this.setUniversity(rowIndex, record.getUniversity());
		this.setEmail(rowIndex, record.getEmail());
		this.setCitedBy(rowIndex, record.getUserId());
		this.setQueriedAuthor(rowIndex, record.getQueriedAuthor());
		this.setGoogleCitationUserId(rowIndex, record.getUserId());
	}
	
	private int getUniqueIdentifierIndex(int rowIndex, String uniqueIndentifier) {
		if (!identifierToIndexMap.containsKey(uniqueIndentifier)) {
			identifierToIndexMap.put(uniqueIndentifier, ++uniqueIndex);
			this.setCombinedValue(rowIndex);
		}
		
		return identifierToIndexMap.get(uniqueIndentifier);
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
		table.setString(rowIndex, CITATION_USER_ID_COLUMN, userId);
	}
	
	/**
	 * Sets the queried author.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param authorName
	 *            the queried author
	 */
	private void setQueriedAuthor(int rowIndex, String queriedAuthor) {
		table.setString(rowIndex, QUERIED_NAME_COLUMN, queriedAuthor);
	}
	
	/**
	 * Sets the author.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param authorName
	 *            the author name
	 */
	private void setAuthor(int rowIndex, String authorName) {
		table.setString(rowIndex, AUTHOR_COLUMN, authorName);
	}

	/**
	 * Sets the university.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param universityName
	 *            the university name
	 */
	private void setUniversity(int rowIndex, String universityName) {
		table.setString(rowIndex, UNIVERSITY_COLUMN, universityName);
	}

	/**
	 * Sets the email.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param email
	 *            the email
	 */
	private void setEmail(int rowIndex, String email) {
		table.setString(rowIndex, EMAIL_COLUMN, email);
	}

	/**
	 * Sets the cited by.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param citationNumber
	 *            the citation number
	 */
	private void setCitedBy(int rowIndex, String citationNumber) {
		table.setString(rowIndex, CITED_BY_COLUMN, citationNumber);
	}

	/**
	 * Sets the unique index.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param isUnique
	 *            the is unique
	 */
	private void setUniqueIndex(int rowIndex, int uniqueIdentifierIndex) {
		
		table.setString(rowIndex, UNIQUE_INDEX_COLUMN, String.valueOf(uniqueIdentifierIndex));
	}

	/**
	 * Sets the combined value.
	 * 
	 * @param rowIndex
	 *            the new combined value
	 */
	private void setCombinedValue(int rowIndex) {
		table.setString(rowIndex, COMBINE_VALUE_COLUMN, "*");
	}
}
