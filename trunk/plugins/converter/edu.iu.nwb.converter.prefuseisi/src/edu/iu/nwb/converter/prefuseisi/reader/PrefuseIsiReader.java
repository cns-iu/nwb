package edu.iu.nwb.converter.prefuseisi.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;

public class PrefuseIsiReader implements Algorithm {	
	public static final boolean NORMALIZE_AUTHOR_NAMES = true;
	
	private File inISIFile;
    private LogService log;
    private ISICitationExtractionPreparer citationExtractionPreparer;
	
    
    public PrefuseIsiReader(
    		Data[] data, Dictionary parameters, CIShellContext context) {
    	this.inISIFile = (File) data[0].getData();
        
     	this.log = (LogService)context.getService(LogService.class.getName());
     	this.citationExtractionPreparer =
     		new ISICitationExtractionPreparer(log);		
    }

    
    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		ISITableReader tableReader =
    			new ISITableReader(this.log, NORMALIZE_AUTHOR_NAMES);
			
    		Table tableWithDups =
    			tableReader.readTable(new FileInputStream(inISIFile));
		  	
            Table table = tableWithDups;			
			Table preparedTable =
				citationExtractionPreparer.prepareForCitationExtraction(table);
			
			return createOutData(preparedTable);    		
    	} catch (SecurityException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);    		
    	} catch (FileNotFoundException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);    		
    	} catch (UnsupportedEncodingException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);			
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
    }


	private Data[] createOutData(Table preparedTable) {
		Data[] tableToReturnData = 
			new Data[] { new BasicData(preparedTable, Table.class.getName()) };
		tableToReturnData[0].getMetadata().put(
				DataProperty.LABEL, "ISI Data: " + inISIFile);
		tableToReturnData[0].getMetadata().put(
				DataProperty.TYPE, DataProperty.TABLE_TYPE);
		
		return tableToReturnData;
	}
}