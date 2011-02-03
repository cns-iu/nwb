package edu.iu.nwb.preprocessing.tablefilter;

import java.util.Dictionary;
import java.util.regex.Pattern;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.expression.ColumnExpression;

public class Filter implements Algorithm {
    private Data inputData;
    private Table inputTable;
    private Pattern pattern;
    private String column;
    private int cutoff;
    
    public Filter(
    		Data data, Table inputTable, Pattern pattern, String column, int cutoff) {
        this.inputData = data;
        this.inputTable = inputTable;
        this.pattern = pattern;
        this.column = column;
        this.cutoff = cutoff;
    }

    public Data[] execute() {
    	Table outputTable = this.inputTable.select(
    		new ColumnExpression(Filter.this.column) {
				public boolean getBoolean(Tuple tuple) {
					int elementCount =
						Filter.this.pattern.split(this.get(tuple).toString()).length;

					return elementCount < Filter.this.cutoff;
				}
			},
			null);
    	
    	Data outputData = new BasicData(outputTable, Table.class.getName());
    	
    	Dictionary<String, Object> metadata = outputData.getMetadata();
    	String label =
    		String.format("Rows with less than %d in column %s", this.cutoff,  this.column);
		metadata.put(DataProperty.LABEL, label);
		metadata.put(DataProperty.PARENT, this.inputData);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
    	
        return new Data[] { outputData };
    }
}