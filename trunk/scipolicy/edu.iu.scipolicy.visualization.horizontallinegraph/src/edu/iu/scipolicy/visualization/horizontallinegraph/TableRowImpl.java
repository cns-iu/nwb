package edu.iu.scipolicy.visualization.horizontallinegraph;

public class TableRowImpl implements TableRow {
	String[] columnNames;
	Object[] cells;
	
	public TableRowImpl(String[] columnNames, Object[] cells) {
		this.columnNames = columnNames;
		this.cells = cells;
	}
	
	public Object getValue(String key) throws NoSuchKeyValueException {
		int index = -1;
		
		for (int ii = 0; ii < columnNames.length; ii++) {
			if (columnNames[ii].equals(key)) {
				index = ii;
				
				break;
			}
		}
		
		if (index == -1) {
			throw new NoSuchKeyValueException
				("No value could be found for key \'" + key + "\'.");
		}
		
		return cells[index];
	}
}