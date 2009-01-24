package edu.iu.scipolicy.visualization.horizontallinegraph;

import java.util.ArrayList;

class TableImpl implements Table {
	private String[] columnNames;
	private ArrayList tableRows;
	private int currentRow;
	
	TableImpl(String[] columnNames) {
		this.columnNames = columnNames;
		this.tableRows = new ArrayList();
		this.currentRow = 0;
	}
	
	public void addRow(TableRow tableRow) {
		this.tableRows.add(tableRow);
	}
	
	public String[] getColumnNames() {
		return this.columnNames;
	}
	
	public int getNumRowsLeft() {
		return (this.tableRows.size() - this.currentRow);
	}
	
	public TableRow getNextRow() throws NoMoreRowsException {
		if (getNumRowsLeft() == 0)
			throw new NoMoreRowsException("No more rows left in table.");
		
		TableRow nextRow = (TableRow)this.tableRows.get(this.currentRow);
		this.currentRow++;
		
		return nextRow;
	}
}