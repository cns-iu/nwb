package edu.iu.scipolicy.loader.isi.db;

import java.io.File;
import java.util.Dictionary;

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
import edu.iu.scipolicy.loader.isi.db.model.ISIModel;
import edu.iu.scipolicy.loader.isi.db.utilities.ISIDatabaseCreator;
import edu.iu.scipolicy.loader.isi.db.utilities.ISITableModelExtractor;

public class ISIDatabaseLoaderAlgorithm implements Algorithm {
	public static final boolean SHOULD_NORMALIZE_AUTHOR_NAMES = true;
	public static final boolean SHOULD_CLEAN_AUTHOR_NAME_CAPITALIZATIONS = true;
	public static final boolean SHOULD_FILL_FILE_METADATA = false;
	public static final boolean SHOULD_CLEAN_CITED_REFERENCES = true;

    private Data inData;
    private LogService logger;
    
    public ISIDatabaseLoaderAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext ciShellContext) {
        this.inData = data[0];

        this.logger = (LogService)ciShellContext.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	System.err.println("Executing");
    	// Convert input ISI data to an ISI table.

    	Table isiTable = convertISIToCSV(this.inData, this.logger);
    	//return wrapAsOutputData(isiTable, this.inData);

    	// Convert the ISI CSV to an ISI database.

    	Data[] databaseData = convertTableToDatabase(isiTable, this.inData);

    	// Annotate ISI database as output data with metadata and return it.

        return annotateOutputData(databaseData);
    }
    
    private Table convertISIToCSV(Data isiData, LogService logger)
    		throws AlgorithmExecutionException {
    	// Read the input ISI data.

    	File inISIFile = (File)isiData.getData();

    	try {
    		return ISITableReaderHelper.readISIFile(
    			inISIFile,
    			logger,
    			SHOULD_NORMALIZE_AUTHOR_NAMES,
    			SHOULD_CLEAN_AUTHOR_NAME_CAPITALIZATIONS,
    			SHOULD_FILL_FILE_METADATA,
    			SHOULD_CLEAN_CITED_REFERENCES);
    	} catch (ReadISIFileException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}
    }
    
    private Data[] convertTableToDatabase(Table table, Data inData) {
    	// Create an in-memory ISI model based off of the table.
    		
    	ISIModel model = ISITableModelExtractor.extractModel(table);

    	// Use the ISI model to create an ISI database.
    	
    	ISIDatabase database = ISIDatabaseCreator.createFromModel(model);

    	// Wrap the resulting ISI database as output and return it. 

    	return wrapAsOutputData(database, inData);
    }

    private Data[] annotateOutputData(Data[] outputData) {
    	return null;
    }
    
    private Data[] wrapAsOutputData(Object outputObject, Data parentData) {
    	Data outData = new BasicData(outputObject, outputObject.getClass().getName());
    	Dictionary metadata = outData.getMetadata();
    	metadata.put(DataProperty.PARENT, parentData);
    	metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);

    	return new Data[] { outData };
    }
}