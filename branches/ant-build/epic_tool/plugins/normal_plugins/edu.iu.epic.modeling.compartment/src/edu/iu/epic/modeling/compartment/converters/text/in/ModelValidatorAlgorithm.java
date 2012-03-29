package edu.iu.epic.modeling.compartment.converters.text.in;

import java.io.File;
import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.RecognitionException;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.BasicDataPlus;
import org.osgi.service.log.LogService;

import edu.iu.epic.modeling.compartment.converters.text.Constants;
import edu.iu.epic.modeling.compartment.converters.text.SystemErrCapturer;
import edu.iu.epic.modeling.compartment.converters.text.generated.ModelFileParser;
import edu.iu.epic.modeling.compartment.converters.text.generated.ModelFileParser.UncheckedParsingException;

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
    		ModelFileParser parser =
    			ModelFileParser.createParserOn(new ANTLRFileStream(inputModelFilePath));

    		try {
	    		systemErrCapturer.startCapturing();
	    		parser.modelFile();
    		} finally {
    			systemErrCapturer.stopCapturing();
    		}

    		if (!systemErrCapturer.isEmpty()) {
    			String message = "Unable to validate compartmental model file "
    				+ "due to these syntax errors:\n"
    				+ systemErrCapturer.getCapturedMessages();
    			logger.log(LogService.LOG_ERROR, message);
    			return null;
    		}

    		// Any exceptions thrown will signal an invalid file.
    		parser.getModel();

    		BasicDataPlus outData =
    			new BasicDataPlus(new File(inputModelFilePath), Constants.MODEL_MIME_TYPE);
    		outData.setType(DataProperty.MODEL_TYPE);
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