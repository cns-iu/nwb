package edu.iu.nwb.converter.cerncoltmatrix_nwbfile;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import cern.colt.matrix.DoubleMatrix2D;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;

public class CernColtMatrixToNWBFileAlgorithm implements Algorithm {
	private Data inputData;
	private DoubleMatrix2D matrix;
    
    public CernColtMatrixToNWBFileAlgorithm(Data[] data,
    										Dictionary parameters,
    										CIShellContext context) {
    	this.inputData = data[0];
    	this.matrix = (DoubleMatrix2D)this.inputData.getData();
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		File nwbFile = CernColtMatrixToNWBFileWriter.writeCernColtMatrixToNWBFile(
    			this.matrix);
    	
    		Data[] outData = this.wrapOutData(nwbFile);
    
    		return outData;
    		
    	} catch (IOException e) {
    		throw new AlgorithmExecutionException(e);
    	}
    }
    
    private Data[] wrapOutData(File nwbFile) {
    	Data outData = new BasicData(nwbFile, NWBFileProperty.NWB_MIME_TYPE);
    	Dictionary metaData = outData.getMetadata();
    	metaData.put(DataProperty.LABEL, "As Network");
    	metaData.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
    	metaData.put(DataProperty.PARENT, this.inputData);
    	
    	return new Data[] { outData };
    }
}