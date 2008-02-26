package edu.iu.nwb.converter.prefuserefer.util;

import prefuse.data.Schema;
import prefuse.data.Table;

public class TableData {
	private Table table;
	
	private int currentRow;
	private boolean currentRowIsFinished;
	private String DEFAULT_MULTI_VAL_SEPARATOR = ";";
	
	public TableData(Schema schema) {
		table = schema.instantiate();
		currentRowIsFinished = true; //will cause first row to be created
	}
	
	public void moveOnToNextRow() {
		currentRowIsFinished = true;
	}
	
	public void setString(String columnTag, String value) {
		setString(columnTag, value, DEFAULT_MULTI_VAL_SEPARATOR);
	}
	
	//will create the column if it does not already exist
	public void setString(String columnTag, String value, String multiValSeparator) {
		ensureRowNotFinishedYet();
		
		try {
			String currentContents = table.getString(currentRow, columnTag);
			if (currentContents == null || currentContents == "") {
				//this is first value for field
				table.setString(currentRow, columnTag, value);
			} else {
				//already has contents. Add value on to current contents
				table.setString(currentRow , columnTag, currentContents + multiValSeparator + value);
			}
		} catch (Exception e1) {
			//maybe column does not yet exist. Add it and try again.
			addColumn(columnTag, String.class);
			try {
			table.setString(currentRow, columnTag, value);
			} catch (Exception e2) {
				//something else must be wrong.
				throw new Error(e2);
			}
		}
	}
	
	public void addColumn(String columnName, Class columnType) {
		table.addColumn(columnName, columnType);
	}
	
	public Table getPrefuseTable() {
		return table;
	}
	
	private void ensureRowNotFinishedYet() {
		if (currentRowIsFinished) {
			currentRow = table.addRow();
			currentRowIsFinished = false;
		}
	}
}
