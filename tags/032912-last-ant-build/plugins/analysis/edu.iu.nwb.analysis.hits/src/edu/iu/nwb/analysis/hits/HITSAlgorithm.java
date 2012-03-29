package edu.iu.nwb.analysis.hits;

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

import edu.iu.nwb.converter.nwb.common.ValidateNWBFile;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.ParsingException;

/**
 * Kleinberg's hypertext-induced topic selection 
 * <a href="http://citeseer.ist.psu.edu/kleinberg99authoritative.html">(HITS)</a> algorithm 
 * is a very popular and effective algorithm to rank documents based on the link information 
 * among a set of documents. The algorithm presumes that a good hub is a document that points 
 * to many others, and a good authority is a document that many documents point to. 
 * 
 * Hubs and authorities exhibit a mutually reinforcing relationship: a better hub points to 
 * many good authorities, and a better authority is pointed to by many good hubs.
 * 
 * @author Chintan Tank
 */

public class HITSAlgorithm implements Algorithm{
	
	private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
	private LogService logger;
	private static int DEFAULT_NUMBER_OF_ITERATIONS = 20;
	private int numberOfIterations = DEFAULT_NUMBER_OF_ITERATIONS;
	
    /**
     * Construct with the appropriate parameters
     * @param data This contains the input graph
     * @param parameters This contains number of iterations to be performed.
     * @param context And this allows access to some additional capabilities
     */
    public HITSAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
		this.logger = (LogService) context.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
		
		File inputData = (File)data[0].getData();
		
    	int numberOfNodes = 0;
    	
    	try {
			numberOfNodes = validateInputFile(inputData);
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException("Inappropriate NWB File provided.", e);
		}
    	
    	if(numberOfNodes > 0) {
			try {
				
				numberOfIterations = (Integer) parameters.get("iterations");
				String edgeWeightColumnName = (String) parameters.get("weightcolumn");
				
				if(numberOfIterations <= 0) {
					logger.log(LogService.LOG_WARNING, "Number of Iterations should be more than 0. Default " +
							"value of \"Number of Iterations\" (20) used.");
					numberOfIterations = DEFAULT_NUMBER_OF_ITERATIONS;
				}

				
				/*
				 * Used to process the provided file for HITS.
				 * */
				NWBFileParser parser = new NWBFileParser(inputData);
				HITSComputation hitsComputation = new HITSComputation(numberOfNodes, numberOfIterations, edgeWeightColumnName);
				parser.parse(hitsComputation);
				
				/*
				 * Used to generate the output file containing the modified graph information. 2 new 
				 * attributes - authority & hub score are added for each node.
				*/
				File outputNWBFile = File.createTempFile("hits-", ".nwb");
				NWBFileParser outputParser = new NWBFileParser(inputData);
				outputParser.parse(new HITSAlgorithmOutputGenerator(hitsComputation, outputNWBFile));
				
				Data outNWBData = new BasicData(outputNWBFile,"file:text/nwb");
				outNWBData.getMetadata().put(DataProperty.LABEL, "HITS on network with " + numberOfNodes 
						+ " nodes. Authority & Hubs updated " + numberOfIterations + " times.");
				outNWBData.getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
				outNWBData.getMetadata().put(DataProperty.PARENT, data[0]);
				return new Data[]{outNWBData};
			
			} catch (FileNotFoundException e) {
				throw new AlgorithmExecutionException("NWB File'" + inputData.getAbsolutePath() + "' not found.");
			} catch (IOException e) {
				throw new AlgorithmExecutionException(e);
			} catch (ParsingException e) {
				throw new AlgorithmExecutionException(e);
			}
    	} 
    	else {
    		return data;
    	}


	}

	private static int validateInputFile(File inData) throws ParsingException, AlgorithmExecutionException {
		ValidateNWBFile validateParser = new ValidateNWBFile();

		try {
			validateParser.validateNWBFormat(inData);
			return validateParser.getTotalNumOfNodes();
		} catch (Exception e) {
			throw new AlgorithmExecutionException("Inappropriate NWB File provided.");
		}
		
	}

}
