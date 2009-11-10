package edu.iu.epic.modeling.compartment.converter.in;

import java.io.File;
import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.RecognitionException;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.utilities.BasicDataPlus;
import org.osgi.service.log.LogService;

import edu.iu.epic.modeling.compartment.converter.Constants;
import edu.iu.epic.modeling.compartment.converter.SystemErrCapturer;
import edu.iu.epic.modeling.compartment.grammar.parsing.StrictModelFileParser;
import edu.iu.epic.modeling.compartment.grammar.parsing.ModelFileParser.UncheckedParsingException;

public class ModelValidatorAlgorithm implements Algorithm {    
	private String inputModelFilePath;
	private LogService logger;
    
	
	public ModelValidatorAlgorithm(Data[] data, LogService logger) {
    	this.inputModelFilePath = (String) data[0].getData();
    	this.logger = logger;
    }


    public Data[] execute() {
    	SystemErrCapturer systemErrCapturer = new SystemErrCapturer();
    	
    	try {
    		StrictModelFileParser parser =
    			StrictModelFileParser.createParserOn(new ANTLRFileStream(inputModelFilePath));

    		try {
	    		systemErrCapturer.startCapturing();
	    		parser.modelFile();
    		} finally {
    			systemErrCapturer.stopCapturing();
    		}
    		
    		if (!systemErrCapturer.isEmpty()) {
    			String message = "Unable to validate compartment model file "
    				+ "due to these syntax errors:\n"
    				+ systemErrCapturer.getCapturedMessages();
    			logger.log(LogService.LOG_ERROR, message);
    			return null;
    		}
    		
    		// Any exceptions thrown will signal an invalid file.
    		parser.getModel();
    		
//    		if (parser.encounteredRecognitionException()) {
//    			// TODO Fetch and pass on the cause
//    			throw new AlgorithmExecutionException(
//    					"Failure strictly validating invalid model file");
//    		}
    		
    		BasicDataPlus outData =
    			new BasicDataPlus(new File(inputModelFilePath), Constants.MODEL_MIME_TYPE);
    		outData.setLabel("Model: " + inputModelFilePath);
    		return new Data[]{ outData };
		} catch (RecognitionException e) {
			logger.log(
					LogService.LOG_ERROR,
					"Failure validating invalid model file: " + e.getMessage(),
					e);
			return null;
		} catch (IOException e) {
			logger.log(LogService.LOG_ERROR, "Error reading model file: " + e.getMessage(), e);
			return null;
		} catch (UncheckedParsingException e) {
			logger.log(
					LogService.LOG_ERROR,
					"Error parsing model file during validation: " + e.getMessage(),
					e);
			return null;
		}
    }
}