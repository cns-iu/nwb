package edu.iu.nwb.converter.prefuseisi.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.nwb.shared.isiutil.ISICitationExtractionPreparer;
import edu.iu.nwb.shared.isiutil.ISITableReader;
import edu.iu.nwb.shared.isiutil.exception.CitationExtractionPreparationException;
import edu.iu.nwb.shared.isiutil.exception.ReadTableException;

public class PrefuseIsiReader implements Algorithm {	
	public static final boolean NORMALIZE_AUTHOR_NAMES = true;
	
	private File inISIFile;
    private LogService logger;

    public PrefuseIsiReader(Data[] data, CIShellContext context) {
    	this.inISIFile = (File) data[0].getData();
        
     	this.logger = (LogService)context.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		ISITableReader tableReader = new ISITableReader(this.logger, NORMALIZE_AUTHOR_NAMES);
    		Table tableWithDups =
    			tableReader.readTable(this.inISIFile);
			Table preparedTable =
				new ISICitationExtractionPreparer(this.logger).prepareForCitationExtraction(
					tableWithDups, true);
			
			return createOutData(preparedTable);    		
    	} catch (SecurityException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);    		
    	} catch (FileNotFoundException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);    		
    	} catch (UnsupportedEncodingException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);			
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (ReadTableException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (CitationExtractionPreparationException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
    }

	private Data[] createOutData(Table preparedTable) {
		Data[] tableToReturnData = 
			new Data[] { new BasicData(preparedTable, Table.class.getName()) };
		tableToReturnData[0].getMetadata().put(DataProperty.LABEL, "ISI Data: " + inISIFile);
		tableToReturnData[0].getMetadata().put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		
		return tableToReturnData;
	}
}