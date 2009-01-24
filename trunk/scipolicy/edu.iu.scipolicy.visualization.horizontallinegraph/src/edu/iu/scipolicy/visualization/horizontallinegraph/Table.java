package edu.iu.scipolicy.visualization.horizontallinegraph;

public interface Table {
	public String[] getColumnNames();
	public int getNumRowsLeft();
	public TableRow getNextRow() throws NoMoreRowsException;
}