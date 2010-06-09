package edu.iu.epic.modeling.compartment.converters.xml.out;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.utilities.BasicDataPlus;
import org.osgi.service.log.LogService;
import org.xml.sax.SAXException;

import edu.iu.epic.modeling.compartment.converters.xml.Constants;
import edu.iu.epic.modeling.compartment.converters.xml.ModelUnmarshaller;
import edu.iu.epic.modeling.compartment.model.exception.ModelModificationException;

public class ModelHandlerAlgorithm implements Algorithm {
	private Data inputData;
	private File modelFile;
	private LogService logger;


	public ModelHandlerAlgorithm(Data inputData, File modelFile, LogService logger) {
		this.inputData = inputData;
    	this.modelFile = modelFile;
    	this.logger = logger;
    }


    public Data[] execute() {
    	try {    		
    		ModelUnmarshaller.unmarshalModelFrom(modelFile, true);
    		
			BasicDataPlus outData =
				new BasicDataPlus(modelFile, Constants.MODEL_FILE_EXTENSION, inputData);
			outData.setLabel("Model: " + modelFile.getName());
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
			logger.log(LogService.LOG_ERROR, "Error handling XML file: " + e.getMessage(), e);
			return null;
		}
    }
}