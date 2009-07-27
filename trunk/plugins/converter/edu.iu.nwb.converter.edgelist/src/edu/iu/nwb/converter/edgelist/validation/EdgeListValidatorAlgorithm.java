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
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.edgelist.common.EdgeListParser;
import edu.iu.nwb.converter.edgelist.common.InvalidEdgeListFormatException;
import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class EdgeListValidatorAlgorithm implements Algorithm {    
	public static final String REVIEW_SPEC_MESSAGE =
		"The file selected as .edge is not a valid EdgeList file." + "\n"
		+ "Please review the EdgeList file format specification at" + "\n"
		+ "https://nwb.slis.indiana.edu/community/?n=LoadData.Edgelist" + "\n";
	public static final String EDGE_LIST_MIME_TYPE = "file:text/edge";
	private String edgeListFilePath;
    LogService logger;
	
        
    public EdgeListValidatorAlgorithm(Data[] data,
    								  Dictionary parameters,
    								  CIShellContext context) {
    	this.edgeListFilePath = (String) data[0].getData();
        this.logger =
        	(LogService) context.getService(LogService.class.getName());
    }

    
    public Data[] execute() throws AlgorithmExecutionException {    	
    	File edgeListFile = new File(edgeListFilePath);
		try {
			validateEdgeList(edgeListFile);
		} catch (AlgorithmExecutionException e) {
			throw new AlgorithmExecutionException(
					"Unable to validate edgelist file: " + e.getMessage(),
					e);
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
			throw new AlgorithmExecutionException(e);						
		} catch (IOException ioe){
			throw new AlgorithmExecutionException(ioe);
		} catch (InvalidEdgeListFormatException e) {
			logger.log(LogService.LOG_ERROR, REVIEW_SPEC_MESSAGE);
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