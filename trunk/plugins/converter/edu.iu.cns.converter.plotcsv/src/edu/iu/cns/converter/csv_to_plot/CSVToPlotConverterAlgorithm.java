package edu.iu.cns.converter.csv_to_plot;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

public class CSVToPlotConverterAlgorithm implements Algorithm {
	public static final String OUT_LABEL = "as table";
	public static final String PLOT_MIME_TYPE = "file:text/plot";
	
	private Data inData;
	private File inFile;
	private LogService logger;
	
    public CSVToPlotConverterAlgorithm(Data[] data,
    				  				   Dictionary parameters,
    				  				   CIShellContext ciShellContext) {
        this.inData = data[0];
        this.inFile = (File)this.inData.getData();
        this.logger =
        	(LogService)ciShellContext.getService(LogService.class.getName());
    }
    
    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		File plotFile =
    			CSVFileConverter.convertCSVFileToPlotFile(this.inFile,
    													  this.logger);
    		
    		return wrapTableForOutput(plotFile);
    	} catch (Exception csvFileToPlotFileConversionException) {
    		String exceptionMessage =
    			"Could not convert .csv file \"" +
    			this.inFile.getAbsolutePath() +
    			"\" to .plot file.";
    		
    		throw new AlgorithmExecutionException(
    			exceptionMessage, csvFileToPlotFileConversionException);
    	}
    }
    
    private Data[] wrapTableForOutput(File outTable) {
    	Data outData = new BasicData(outTable, PLOT_MIME_TYPE);    	
    	
    	Dictionary outMetadata = outData.getMetadata();
    	outMetadata.put(DataProperty.LABEL, OUT_LABEL);
    	outMetadata.put(DataProperty.PARENT, this.inData);
    	outMetadata.put(DataProperty.TYPE, DataProperty.PLOT_TYPE);
    	
    	return new Data[] { outData };
    }
}