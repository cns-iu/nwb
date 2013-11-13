package edu.iu.nwb.converter.prefusescopus;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.conversion.ConversionException;
import org.cishell.service.conversion.DataConversionService;
import org.cishell.utilities.TableUtilities;
import org.cishell.utilities.FileUtilities;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.log.LogService;

import prefuse.data.DataTypeException;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.column.Column;
import prefuse.util.collections.IntIterator;
import edu.iu.nwb.converter.prefusescopus.util.StringUtil;
import edu.iu.nwb.converter.prefusescopus.util.TableCleaner;

public class ScopusReaderAlgorithm implements Algorithm {
    public static final String CSV_MIME_TYPE = "file:text/csv";
	Data[] data;
    CIShellContext context;
    LogService log;
    
    public ScopusReaderAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.context = context;
        this.log = (LogService) context.getService(LogService.class.getName());
    }

	public Data[] execute() throws AlgorithmExecutionException {
    	Data inputData = convertInputData(data[0]);
    	Table scopusTable = (Table) inputData.getData();
    	TableCleaner cleaner = new TableCleaner(this.log);
    	scopusTable = cleaner.cleanTable(scopusTable);
    	
    	URL configPath = FileUtilities.lookupResourceUrl(ScopusReaderAlgorithm.class, "scopus.hmap");
    	
    	// call table standardizing function to replace specified Scopus headers
    	Table finalTable = null;
		try {
			finalTable = TableUtilities.standardizeTable(configPath, scopusTable);
		} catch (IOException e) {
			String errorMsg = "An error has occurred while attempting to read the Scopus format file. " +
							"Please contact cns-sci2-help-l@iulist.indiana.edu for assistance.";
			throw new AlgorithmExecutionException(errorMsg);
		}
    	
        Data[] outputData = formatAsData(finalTable);
        return outputData;
    }
    
    private Data convertInputData(Data inputData) throws AlgorithmExecutionException {
    	DataConversionService converter = (DataConversionService)
        	context.getService(DataConversionService.class.getName());
    	
    	/* This is a bit like a cast. We know the nsf format is also a csv, so
		 * we change the format to csv so the Conversion service knows it is a
		 * csv when it tries to convert it to a prefuse.data.Table
		 */
		Data formatChangedData = new BasicData(inputData.getMetadata(),
											  (File) inputData.getData(),
											  CSV_MIME_TYPE);
		try {
			Data convertedData =
				converter.convert(formatChangedData, Table.class.getName());
			return convertedData;
		} catch (ConversionException e){
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
    }

	private Data[] formatAsData(Table scopusTable)
			throws AlgorithmExecutionException {
		try {
			Data[] dm = new Data[] {new BasicData(scopusTable, Table.class.getName())};
			dm[0].getMetadata().put(DataProperty.LABEL, "Normalized Scopus table");
			dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
			return dm;
		} catch (SecurityException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}
}