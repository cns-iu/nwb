package edu.iu.nwb.converter.prefuserefer.util;

import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

import prefuse.data.Schema;
import prefuse.data.Table;

public class TableData {
	private Table table;
	
	private int currentRow;
	private boolean currentRowIsFinished;
	private Map colNameToReadableColName;
	
	private String DEFAULT_MULTI_VAL_SEPARATOR = "|";
	
	public TableData(Schema schema, Map colNameToReadableColName) {
		table = schema.instantiate();
		this.colNameToReadableColName = colNameToReadableColName;
		currentRowIsFinished = true; //will cause first row to be created
	}
	
	public void moveOnToNextRow() {
		currentRowIsFinished = true;
	}
	
	public void setString(String columnTag, String value)
			throws AlgorithmExecutionException {
		setString(columnTag, value, DEFAULT_MULTI_VAL_SEPARATOR);
	}
	
	//will create the column if it does not already exist
	public void setString(
			String columnTag, String value, String multiValSeparator)
				throws AlgorithmExecutionException {
		ensureRowNotFinishedYet();
		
		String readableColumnTag = getReadableColName(columnTag);
		try {
			String currentContents = table.getString(currentRow, readableColumnTag);
			if (currentContents == null || currentContents == "") {
				//this is first value for field
				table.setString(currentRow, readableColumnTag, value);
			} else {
				//already has contents. Add value on to current contents
				table.setString(currentRow , readableColumnTag, currentContents + multiValSeparator + value);
			}
		} catch (Exception e1) {
			//maybe column does not yet exist. Add it and try again.
			addColumn(readableColumnTag, String.class);
			try {
				table.setString(currentRow, readableColumnTag, value);
			} catch (Exception e) {
				//something else must be wrong.
				throw new AlgorithmExecutionException(
					"Error occurred while adding the value " + value
					+ " to the column " + readableColumnTag + " on row "
					+ currentRow, e);
			}
		}
	}
	
	public void addColumn(String columnName, Class columnType) {
		String readableColumnName = getReadableColName(columnName);
		table.addColumn(readableColumnName, columnType);
	}
	
	public Table getPrefuseTable() {
		return table;
	}
	
	private String getReadableColName(String colName) {
		String readableColName = (String) colNameToReadableColName.get(colName);
		if (readableColName != null) {
			return readableColName;
		} else {
			return colName;
		}
	}
	
	private void ensureRowNotFinishedYet() {
		if (currentRowIsFinished) {
			currentRow = table.addRow();
			currentRowIsFinished = false;
		}
	}
}
