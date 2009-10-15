package edu.iu.cns.converter.grace_to_plot;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

public class GraceToPlotFileConverterAlgorithm implements Algorithm {
	public static final String PLOT_MIME_TYPE = "file:text/plot";
	public static final String OUT_LABEL = "Converted to plot file";
	
    private Data inputData;
    private File graceFile;
    
    public GraceToPlotFileConverterAlgorithm(
    		Data[] data,
    		Dictionary parameters,
    		CIShellContext ciShellContext) {
        this.inputData = data[0];
        this.graceFile = (File)this.inputData.getData();
    }

    public Data[] execute() {
        Data outputData = new BasicData(this.graceFile, PLOT_MIME_TYPE);
        Dictionary outputMetadata = outputData.getMetadata();
        outputMetadata.put(DataProperty.LABEL, OUT_LABEL);
        outputMetadata.put(DataProperty.PARENT, this.inputData);
        outputMetadata.put(DataProperty.TYPE, DataProperty.PLOT_TYPE);
        
        return new Data[] { outputData };
    }
}