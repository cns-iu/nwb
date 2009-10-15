package edu.iu.cns.converter.plot_to_grace;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

public class PlotToGraceFileConverterAlgorithm implements Algorithm {
	public static final String GRACE_MIME_TYPE = "file:text/grace";
	public static final String OUT_LABEL = "Converted to grace file";
	
    private Data inputData;
    private File plotFile;
    
    public PlotToGraceFileConverterAlgorithm(
    		Data[] data,
    		Dictionary parameters,
    		CIShellContext ciShellContext) {
        this.inputData = data[0];
        this.plotFile = (File)this.inputData.getData();
    }

    public Data[] execute() {
        Data outputData = new BasicData(this.plotFile, GRACE_MIME_TYPE);
        Dictionary outputMetadata = outputData.getMetadata();
        outputMetadata.put(DataProperty.LABEL, OUT_LABEL);
        outputMetadata.put(DataProperty.PARENT, this.inputData);
        outputMetadata.put(DataProperty.TYPE, DataProperty.PLOT_TYPE);
        
        return new Data[] { outputData };
    }
}