package edu.iu.nwb.preprocessing.nwbfile_cerncoltmatrix;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import cern.colt.matrix.DoubleMatrix2D;
import edu.iu.nwb.util.nwbfile.NWBMetadataParsingException;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class NWBFileToCernColtMatrixAlgorithm implements Algorithm {
	public static final String WEIGHT_FIELD_ID = "weight";
	public static final String NO_EDGE_WEIGHT_VALUE = "unweighted";
    
    private Data inputData;
    private File inputFile;
    private boolean isWeighted;
    private String weightAttribute;
    
    public NWBFileToCernColtMatrixAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
        this.inputData = data[0];
        this.inputFile = (File)this.inputData.getData();
        this.weightAttribute = parameters.get(WEIGHT_FIELD_ID).toString();
        
        if (this.weightAttribute.equals(NO_EDGE_WEIGHT_VALUE)) {
        	this.isWeighted = false;
        } else {
        	this.isWeighted = true;
        }
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		DoubleMatrix2D matrix =
    			Converter.convertNWBFileToMatrix(
    				this.inputFile, this.isWeighted, this.weightAttribute);
    	
    	
    		Data[] outData = this.wrapOutData(matrix);
    	
        	return outData;
        
    	} catch (NWBMetadataParsingException nwbMetaDataParsingException) {
    		throw new AlgorithmExecutionException(
    			"Error parsing meta data for input NWB file",
    			nwbMetaDataParsingException);
    	} catch (ParsingException parsingException) {
    		throw new AlgorithmExecutionException(
    			"Error parsing input NWB file", parsingException);
    	}
    }
    
    @SuppressWarnings("unchecked")	// Raw Dictionary.
    private Data[] wrapOutData(DoubleMatrix2D matrix) {
    	Data outData = new BasicData(matrix, matrix.getClass().getName());
    	Dictionary<String, Object> metaData = outData.getMetadata();
    	metaData.put(DataProperty.LABEL, "As Matrix");
    	metaData.put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
    	metaData.put(DataProperty.PARENT, this.inputData);
    	
    	return new Data[] { outData };
    }
}