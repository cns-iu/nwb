package edu.iu.nwb.preprocessing.tablefilter;

import java.util.Dictionary;
import java.util.regex.Pattern;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.expression.ColumnExpression;

public class Filter implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public Filter(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	Table original = (Table) this.data[0].getData();
    	
    	String column = (String) this.parameters.get("column");
    	final Pattern pattern = Pattern.compile("\\Q" + (String) this.parameters.get("separator") + "\\E");
    	final int cutoff = ((Integer) this.parameters.get("cutoff")).intValue();
    	
    	Table table = original.select(new ColumnExpression(column){
    			public boolean getBoolean(Tuple tuple) {
    				return pattern.split(this.get(tuple).toString()).length < cutoff;
    			}
			}, null);
    	
    	Data output = new BasicData(table, Table.class.getName());
    	
    	Dictionary metadata = output.getMetadata();
		metadata.put(DataProperty.LABEL, "Rows with less than " + cutoff + " in column " + column);
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
    	
        return new Data[] {output};
    }
}