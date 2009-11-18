package edu.iu.epic.modeling.compartment.converters.xml.out;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.utilities.BasicDataPlus;
import org.osgi.service.log.LogService;
import org.xml.sax.SAXException;

import edu.iu.epic.modeling.compartment.converters.xml.Constants;
import edu.iu.epic.modeling.compartment.converters.xml.ModelMarshaller;
import edu.iu.epic.modeling.compartment.converters.xml.ModelMarshaller.UnknownTransitionTypeException;
import edu.iu.epic.modeling.compartment.model.Model;

public class ModelWriterAlgorithm implements Algorithm {
	private Data inputData;
	private Model inputModel;	
	private LogService logger;

	
	public ModelWriterAlgorithm(Data inputData, Model inputModel, LogService logger) {
		this.inputData = inputData;
		this.inputModel = inputModel;
		this.logger = logger;
	}

	
	public Data[] execute() {
		try {
			File outputModelFile = ModelMarshaller.marshalModelToFile(inputModel, false);
			
			return new Data[]{
					new BasicDataPlus(outputModelFile, Constants.MODEL_MIME_TYPE, inputData) };
		} catch (JAXBException e) {
			String causeMessage = e.getMessage();			
			if (causeMessage == null) {
				causeMessage = e.getLinkedException().getMessage();
			}

			logger.log(LogService.LOG_ERROR, "Error parsing XML file: " + causeMessage, e);
			return null;
		} catch (IOException e) {
			logger.log(LogService.LOG_ERROR,
					"Error accessing XML file for model conversion: " + e.getMessage(), e);
			return null;
		} catch (SAXException e) {
			logger.log(LogService.LOG_ERROR,
					"Error parsing XML file: " + e.getMessage(), e);
			return null;
		} catch (UnknownTransitionTypeException e) {
			logger.log(LogService.LOG_ERROR, e.getMessage(), e);
			return null;
		}
	}
}
