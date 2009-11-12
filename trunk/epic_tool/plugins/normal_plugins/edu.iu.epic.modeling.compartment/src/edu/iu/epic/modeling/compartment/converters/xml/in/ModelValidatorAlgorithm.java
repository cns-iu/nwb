package edu.iu.epic.modeling.compartment.converters.xml.in;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.utilities.BasicDataPlus;
import org.osgi.service.log.LogService;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import edu.iu.epic.modeling.compartment.converters.xml.Constants;

public class ModelValidatorAlgorithm implements Algorithm {
	private String inputModelFilePath;
	private LogService logger;


	public ModelValidatorAlgorithm(Data[] data, LogService logger) {
    	this.inputModelFilePath = (String) data[0].getData();
    	this.logger = logger;
    }


    public Data[] execute() {
    	try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();			
			ModelXMLHandler modelXMLHandler = new ModelXMLHandler();
			xmlReader.setContentHandler(modelXMLHandler);
			xmlReader.setErrorHandler(modelXMLHandler);
			
			FileReader fileReader = new FileReader(inputModelFilePath);
			
			xmlReader.parse(new InputSource(fileReader));
			
			BasicDataPlus outData =
				new BasicDataPlus(new File(inputModelFilePath), Constants.MODEL_MIME_TYPE);
			outData.setLabel("Model: " + inputModelFilePath);
			return new Data[]{ outData };
		} catch (FileNotFoundException e) {
			logger.log(LogService.LOG_ERROR, "Error locating XML file: " + e.getMessage(), e);
			return null;
		} catch (IOException e) {
			logger.log(LogService.LOG_ERROR, "Error accessing XML file: " + e.getMessage(), e);
			return null;
		} catch (SAXException e) {
			logger.log(LogService.LOG_ERROR, "Error parsing XML file: " + e.getMessage(), e);
			return null;
		}		
    }
}