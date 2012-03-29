package edu.iu.nwb.analysis.isidupremover;

import prefuse.data.Table;


public class TablePair {

	//I wish Java could return multiple values...
	
	private Table noDupTable;
	private Table dupTable;
	
	public TablePair(Table noDupTable, Table dupTable) {
		this.noDupTable = noDupTable;
		this.dupTable = dupTable;
	}
	
	public Table getNoDupTable() {
		return this.noDupTable;
	}
	
	public Table getDupTable() {
		return this.dupTable;
	}
}
