package edu.iu.nwb.preprocessing.text.normalization;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.column.Column;

public class StandardNormalyzer implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
	private LogService log;
	private Analyzer analyzer;

    
    public StandardNormalyzer(Data[] data, Dictionary parameters, 
    							CIShellContext context, String[] stopWords) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        log = (LogService) context.getService(LogService.class.getName());
        
    	
        if (stopWords != null) {
        	this.analyzer = new SnowballAnalyzer("English", stopWords);
        }
        else {
        	log.log(LogService.LOG_WARNING, "No stop words.");
        	this.analyzer = new SnowballAnalyzer("English");
        }       	
        
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	
    	Table input = (Table) data[0].getData();
        
		Table output = input.getSchema().instantiate();
        output.addRows(input.getRowCount());
        
        String separator = (String) parameters.get("separator");
        
        Set columns = columnsToNormalize(parameters);
        
        copyAndNormalize(input, output, columns, separator);
        
        
        return prepareOutputData(output, columns);
    }

	private void copyAndNormalize(Table input, Table output, Set columns, String separator) throws AlgorithmExecutionException {
		for(int ii = 0; ii < input.getColumnCount(); ii++) {
        	Column fromColumn = input.getColumn(ii);
        	Column toColumn = output.getColumn(ii);
        	
        	if(columns.contains(input.getColumnName(ii))) {
        		if(!fromColumn.getColumnType().equals(String.class)) {
            		log.log(LogService.LOG_WARNING, "Selected columns must be Strings");
            		continue;
            	}
        		normalizeColumn(fromColumn, toColumn, separator);
        	} else {
        		copyColumn(fromColumn, toColumn);
        	}
        	
        }
	}

	private void normalizeColumn(Column fromColumn, Column toColumn, String separator) throws AlgorithmExecutionException {
		for(int row = 0; row < fromColumn.getRowCount(); row++) {
			String value = fromColumn.getString(row);
			if(value != null) {
				toColumn.setString(normalize(value, separator), row);
			}
		}
	}

	private void copyColumn(Column fromColumn, Column toColumn) {
		for(int row = 0; row < fromColumn.getRowCount(); row++) {
			toColumn.set(fromColumn.get(row), row);
		}
	}

	private String normalize(String string, String separator) throws AlgorithmExecutionException {
		
		TokenStream tokens = analyzer.tokenStream("unused", new StringReader(string));
		List normalized = new ArrayList();
		
		Token token;
		try {
			while((token = tokens.next()) != null) {
				normalized.add(token.termText());
			}
		} catch (IOException e) {
			throw new AlgorithmExecutionException("Unable to normalize text.", e);
			
		}
		
		return join(normalized, separator);
	}

	private Data[] prepareOutputData(Table output, Set columns) {
		Data outputData = new BasicData(output, Table.class.getName());
		Dictionary metadata = outputData.getMetadata();
		metadata.put(DataProperty.LABEL, "with normalized " + join(columns, ", "));
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
		
		return new Data[]{ outputData };
	}

	private Set columnsToNormalize(Dictionary parameters) {
		Set columns = new TreeSet();
        Enumeration options = parameters.keys();
        while(options.hasMoreElements()) {
        	String key = (String) options.nextElement();
        	Boolean truth = new Boolean(true);
        	if(key.startsWith(StandardNormalyzerFactory.PREFIX) && truth.equals(parameters.get(key))) {
        		String column = key.substring(StandardNormalyzerFactory.PREFIX.length());
        		columns.add(column);
        	}
        }
		return columns;
	}
    
    private static String join(Collection s, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        Iterator iter = s.iterator();
        if (iter.hasNext()) {
            buffer.append(iter.next());
            while (iter.hasNext()) {
                buffer.append(delimiter);
                buffer.append(iter.next());
            }
        }
        return buffer.toString();
    }
}

