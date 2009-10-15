package edu.iu.cns.converter.plot_to_csv;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

public class PlotToCSVConverterAlgorithm implements Algorithm {
	public static final String OUT_LABEL = "as table";
	public static final String CSV_MIME_TYPE = "file:text/csv";
	
	private Data inData;
	private File inFile;
	
    public PlotToCSVConverterAlgorithm(Data[] data,
    				  				   Dictionary parameters,
    				  				   CIShellContext ciShellContext) {
        this.inData = data[0];
        this.inFile = (File)this.inData.getData();
    }
    
    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		File csvFile =
    			PlotFileConverter.convertPlotFileToCSVFile(this.inFile);
    		
    		return wrapTableForOutput(csvFile);
    	} catch (Exception plotFileToCSVFileConversionException) {
    		String exceptionMessage =
    			"Could not convert .plot file \"" +
    			this.inFile.getAbsolutePath() +
    			"\" to .csv file.";
    		
    		throw new AlgorithmExecutionException(
    			exceptionMessage, plotFileToCSVFileConversionException);
    	}
    }
    
    private Data[] wrapTableForOutput(File outTable) {
    	Data outData = new BasicData(outTable, CSV_MIME_TYPE);    	
    	
    	Dictionary outMetadata = outData.getMetadata();
    	outMetadata.put(DataProperty.LABEL, OUT_LABEL);
    	outMetadata.put(DataProperty.PARENT, this.inData);
    	outMetadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
    	
    	return new Data[] { outData };
    }
    
    public static void main(String[] arguments) {
    	File file = new File("C:\\Documents and Settings\\pataphil\\Desktop\\test_plot\\test.grace");
    	Data data = new BasicData(file, "file:text/plot");
    	Data[] d = new Data[] { data };
    	try {
    		new PlotToCSVConverterAlgorithm(d, null, null).execute();
    	} catch (Exception e) { e.printStackTrace(); }
    }
}