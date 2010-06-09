package edu.iu.epic.modeling.compartment.converters.xml.in;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.BasicDataPlus;
import org.osgi.service.log.LogService;
import org.xml.sax.SAXException;

import edu.iu.epic.modeling.compartment.converters.xml.Constants;
import edu.iu.epic.modeling.compartment.converters.xml.ModelUnmarshaller;
import edu.iu.epic.modeling.compartment.model.exception.ModelModificationException;

public class ModelValidatorAlgorithm implements Algorithm {
	private Data inputData;
	private String inputModelFilePath;
	private LogService logger;
	

	public ModelValidatorAlgorithm(Data inputData, String inputModelFilePath, LogService logger) {
		this.inputData = inputData;
    	this.inputModelFilePath = inputModelFilePath;
    	this.logger = logger;
    }


    public Data[] execute() {
    	try {    		
    		ModelUnmarshaller.unmarshalModelFrom(new File(inputModelFilePath), true);
			
			BasicDataPlus outData =
				new BasicDataPlus(
						new File(inputModelFilePath), Constants.MODEL_MIME_TYPE, inputData);
			outData.setType(DataProperty.MODEL_TYPE);
			outData.setLabel("Model: " + inputModelFilePath);
			return new Data[]{ outData };
		} catch (FileNotFoundException e) {
			logger.log(LogService.LOG_ERROR, "Error locating XML file: " + e.getMessage(), e);
			return null;
		} catch (ModelModificationException e) {
			logger.log(
					LogService.LOG_ERROR,
					"Error creating model from file: " + e.getMessage(), e);
			return null;
		} catch (JAXBException e) {
			String causeMessage = e.getMessage();			
			if (causeMessage == null) {
				causeMessage = e.getLinkedException().getMessage();
			}

			logger.log(LogService.LOG_ERROR, "Error parsing XML file: " + causeMessage, e);
			return null;
		} catch (SAXException e) {
			logger.log(LogService.LOG_ERROR, "Error parsing XML file: " + e.getMessage(), e);
			return null;
		} catch (IOException e) {
			logger.log(LogService.LOG_ERROR,
					"Unexpected error: XML schema not found: " + e.getMessage(), e);
			return null;
		}
    }
}