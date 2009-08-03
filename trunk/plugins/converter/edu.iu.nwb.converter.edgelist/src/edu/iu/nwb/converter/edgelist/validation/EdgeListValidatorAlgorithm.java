package edu.iu.nwb.converter.edgelist.validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.iu.nwb.converter.edgelist.common.EdgeListParser;
import edu.iu.nwb.converter.edgelist.common.InvalidEdgeListFormatException;
import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class EdgeListValidatorAlgorithm implements Algorithm {    
	public static final String EDGE_LIST_MIME_TYPE = "file:text/edge";
	private String edgeListFilePath;
	
        
    public EdgeListValidatorAlgorithm(Data[] data,
    								  Dictionary parameters,
    								  CIShellContext context) {
    	this.edgeListFilePath = (String) data[0].getData();
    }

    
    public Data[] execute() throws AlgorithmExecutionException {    	
    	File edgeListFile = new File(edgeListFilePath);
		try {
			validateEdgeList(edgeListFile);
		} catch (AlgorithmExecutionException e) {
			String message =
				"Error: Unable to validate edgelist file: " + e.getMessage();
			throw new AlgorithmExecutionException(message, e);
		}
		
		// If no exceptions were thrown, then edgeListFile is valid

		return createOutData(edgeListFilePath, edgeListFile);	
	}
    
    private void validateEdgeList(File edgeListFile)
    		throws AlgorithmExecutionException {
    	EdgeListParser parser = new EdgeListParser(edgeListFile, true);
    	
		try {
			/* NWBFileParserAdapter is essentially a null object implementation
			 * of NWBFileWriter.
			 */
			parser.parseInto(new NWBFileParserAdapter());
		} catch (FileNotFoundException e){
			throw new AlgorithmExecutionException(e.getMessage(), e);						
		} catch (IOException e){
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (InvalidEdgeListFormatException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);					
		}
    }


	private Data[] createOutData(String edgeListFilePath, File edgeListFile) {
		Data[] outData =
			new Data[] {new BasicData(edgeListFile, EDGE_LIST_MIME_TYPE)};
		outData[0].getMetadata().put(DataProperty.LABEL,
									 "EdgeList file: " + edgeListFilePath);
		outData[0].getMetadata().put(DataProperty.TYPE,
									 DataProperty.NETWORK_TYPE);
		return outData;
	}
}