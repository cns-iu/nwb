package edu.iu.nwb.converter.prefuseisi.reader;

import java.io.File;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.nwb.shared.isiutil.ISITableReaderHelper;
import edu.iu.nwb.shared.isiutil.exception.ReadISIFileException;

public class PrefuseIsiReader implements Algorithm {	
	public static final boolean SHOULD_NORMALIZE_AUTHOR_NAMES = true;
	public static final boolean SHOULD_CLEAN_AUTHOR_NAME_CAPITALIZATIONS = true;
	public static final boolean SHOULD_FILL_FILE_METADATA = false;
	public static final boolean SHOULD_CLEAN_CITED_REFERENCES = true;
	
	private File inISIFile;
    private LogService logger;

    public PrefuseIsiReader(Data[] data, CIShellContext context) {
    	this.inISIFile = (File) data[0].getData();
        
     	this.logger = (LogService)context.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		return createOutData(ISITableReaderHelper.readISIFile(
    			this.inISIFile,
    			this.logger,
    			SHOULD_NORMALIZE_AUTHOR_NAMES,
    			SHOULD_CLEAN_AUTHOR_NAME_CAPITALIZATIONS,
    			SHOULD_FILL_FILE_METADATA,
    			SHOULD_CLEAN_CITED_REFERENCES));
    	} catch (ReadISIFileException e) {
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