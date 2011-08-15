package edu.iu.nwb.preprocessing.text.normalization;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.column.Column;

public class StandardNormalizer implements Algorithm {
    private Data inputData;
    private Table inputTable;
    private String separator;
    private Set<String> columnsToNormalize;
	private LogService logger;
	private Analyzer analyzer;
	private boolean usingDefaultStopWords;

    public StandardNormalizer(
    		Data inputData,
    		Table inputTable,
    		LogService logger,
    		String separator,
    		Set<String> columnsToNormalize,
    		String[] stopWords,
    		boolean usingDefaultStopWords) {
        this.inputData = inputData;
        this.inputTable = inputTable;
        this.logger = logger;
        this.separator = separator;
        this.columnsToNormalize = columnsToNormalize;
        this.usingDefaultStopWords = usingDefaultStopWords;

        if (stopWords != null) {
        	this.analyzer = new SnowballAnalyzer("English", stopWords);
        } else {
        	logger.log(LogService.LOG_WARNING, "No stop words.");
        	this.analyzer = new SnowballAnalyzer("English");
        }       	
        
    }

    public Data[] execute() throws AlgorithmExecutionException {
		Table output = this.inputTable.getSchema().instantiate();
        output.addRows(this.inputTable.getRowCount());
        
        copyAndNormalize(this.inputTable, output, this.columnsToNormalize, separator);

        if (this.usingDefaultStopWords) {
        	String logMessage =
        		"The stop word file you specified could not be read. " +
        		"The built-in default was used.";
        	this.logger.log(LogService.LOG_WARNING, logMessage);
        }

        return prepareOutputData(output, this.columnsToNormalize);
    }

	private void copyAndNormalize(Table input, Table output, Set<String> columns, String separator)
			throws AlgorithmExecutionException {
		for (int ii = 0; ii < input.getColumnCount(); ii++) {
        	Column fromColumn = input.getColumn(ii);
        	Column toColumn = output.getColumn(ii);
        	
        	if (columns.contains(input.getColumnName(ii))) {
        		if (!fromColumn.getColumnType().equals(String.class)) {
            		this.logger.log(LogService.LOG_WARNING, "Selected columns must be Strings");

            		continue;
            	}

        		normalizeColumn(fromColumn, toColumn, separator);
        	} else {
        		copyColumn(fromColumn, toColumn);
        	}
        	
        }
	}

	private void normalizeColumn(Column fromColumn, Column toColumn, String separator)
			throws AlgorithmExecutionException {
		for (int ii = 0; ii < fromColumn.getRowCount(); ii++) {
			String value = fromColumn.getString(ii);

			if (value != null) {
				toColumn.setString(normalize(value, separator), ii);
			}
		}
	}

	private void copyColumn(Column fromColumn, Column toColumn) {
		for (int ii = 0; ii < fromColumn.getRowCount(); ii++) {
			toColumn.set(fromColumn.get(ii), ii);
		}
	}

	private String normalize(String unnormalized, String separator)
			throws AlgorithmExecutionException {
		TokenStream tokens = analyzer.tokenStream("unused", new StringReader(unnormalized));
		List<String> normalized = new ArrayList<String>();

		try {
			Token token;

			while ((token = tokens.next()) != null) {
				normalized.add(token.termText());
			}

			return StringUtilities.implodeItems(normalized, separator);
		} catch (IOException e) {
			throw new AlgorithmExecutionException("Unable to normalize text.", e);
		}
	}

	private Data[] prepareOutputData(Table output, Set<String> columns) {
		Data outputData = new BasicData(output, Table.class.getName());
		Dictionary<String, Object> metadata = outputData.getMetadata();
		String label =
			String.format("with normalized %s", StringUtilities.implodeItems(columns, ", "));
		metadata.put(DataProperty.LABEL, label);
		metadata.put(DataProperty.PARENT, this.inputData);
		metadata.put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
		
		return new Data[]{ outputData };
	}
}

