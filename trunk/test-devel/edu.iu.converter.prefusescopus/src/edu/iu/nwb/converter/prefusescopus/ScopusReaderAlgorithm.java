package edu.iu.nwb.converter.prefusescopus;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.conversion.DataConversionService;
import org.osgi.service.log.LogService;

import prefuse.data.DataTypeException;
import prefuse.data.Table;
import prefuse.data.column.Column;

public class ScopusReaderAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService log;
    
    private static final String AUTHOR_COLUMN_NAME = "Authors";
    private static final String AUTHOR_COLUMN_NAME_SEPARATOR = ",";
    
    public ScopusReaderAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.log = (LogService) context.getService(LogService.class.getName());
    }

    public Data[] execute() {
    	Data inputData = convertInputData(data[0]);
    	Table scopusTable = (Table) inputData.getData();
    	//normalize author names
    	scopusTable = normalizeAuthorNames(scopusTable);
        Data[] outputData = formatAsData(scopusTable);
        return outputData;
    }
    
    
    private Data convertInputData(Data inputData) {
    	 DataConversionService converter = (DataConversionService)
         context.getService(DataConversionService.class.getName());
		//this is a bit like a cast. We know the nsf format is also a csv, so we change the format to csv so
		//the Conversion service knows it is a csv when it tries to convert it to a prefuse.data.Table
		 
		//printTable((Table) inputData.getData());
		Data formatChangedData = new BasicData(inputData.getMetaData(), (File) inputData.getData(), "file:text/csv");
		Data convertedData = converter.convert(formatChangedData, "prefuse.data.Table");
		return convertedData;
    }
    
    private Table normalizeAuthorNames(Table scopusTable) {
    	System.out.println("Normalizing Author Names...");
    	Column authorColumn = scopusTable.getColumn(AUTHOR_COLUMN_NAME);
    	if (authorColumn == null) {
    		printNoAuthorColumnWarning();
    		return scopusTable;
    	}
    	try {
    	for (int rowIndex = scopusTable.getMinimumRow(); rowIndex < scopusTable.getMaximumRow(); rowIndex++) {
    		String authors = authorColumn.getString(rowIndex);
    		if (authors != null && ! authors.equals("")) {
    			System.out.println("  normalizing:" + authors);
    			String normalizedAuthors = normalizeAuthorNames(authors);
    			authorColumn.setString(normalizedAuthors, rowIndex);
    			System.out.println("  result         :" + normalizedAuthors);
    		}
    	}
    	} catch (DataTypeException e1) {
    		printColumnNotOfTypeStringWarning();
    		return scopusTable;
    	}
    	return scopusTable;
    }
    
    private String normalizeAuthorNames(String authorNames) {
    	//trim leading and trailing whitespace from each author name.
    	StringBuilder normalizedAuthorNames = new StringBuilder();
    	String[] eachAuthorName = authorNames.split(AUTHOR_COLUMN_NAME_SEPARATOR);
    	for (int i = 0; i < eachAuthorName.length; i++) {
    		String authorName = eachAuthorName[i];
    		String normalizedAuthorName = authorName.trim();
    		normalizedAuthorNames.append(normalizedAuthorName);
    		if (i < eachAuthorName.length) {
    			//append separator to the end all but the last author name
    			normalizedAuthorNames.append(AUTHOR_COLUMN_NAME_SEPARATOR);
    		}
    	}
    	return normalizedAuthorNames.toString();
    }
    
    private Data[] formatAsData(Table scopusTable) {
    	try{
			Data[] dm = new Data[] {new BasicData(scopusTable, "prefuse.data.Table")};
			dm[0].getMetaData().put(DataProperty.LABEL, "Normalized Scopus table");
			dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
			return dm;
		}catch (SecurityException exception){
			log.log(LogService.LOG_ERROR, "SecurityException", exception);
			exception.printStackTrace();
			return null;
		}
    }
    
    private void printNoAuthorColumnWarning() {
    	this.log.log(LogService.LOG_WARNING, "Unable to find column with the name '" +
    			AUTHOR_COLUMN_NAME + "' in scopus file. " +
    					"We will continue on without attempting to normalize this column");
    }
    
    private void printColumnNotOfTypeStringWarning() {
    	this.log.log(LogService.LOG_WARNING, "The column '" + AUTHOR_COLUMN_NAME + 
    			"' in the scopus file cannot be normalized, because it cannot be interpreted as text. Skipping normalization of authors");
    }
}