package edu.iu.epic.modeling.compartment.converters.xml.in;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import javax.xml.bind.JAXBException;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.BasicDataPlus;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;
import org.xml.sax.SAXException;

import edu.iu.cns.utilities.testing.LogOnlyCIShellContext;
import edu.iu.epic.modeling.compartment.converters.xml.Constants;
import edu.iu.epic.modeling.compartment.converters.xml.ModelUnmarshaller;
import edu.iu.epic.modeling.compartment.model.Model;
import edu.iu.epic.modeling.compartment.model.exception.ModelModificationException;

public class ModelReaderAlgorithm implements Algorithm {
	public static final String TEST_FILE_PATH =
		"/edu/iu/epic/modeling/compartment/converters/xml/testing/"
		+ "good/"
		+ "test.model.xml";

	private Data inputData;
	private File inputModelFile;
	private LogService logger;

	
    public ModelReaderAlgorithm(Data inputData, File inputModelFile, LogService logger) {
    	this.inputData = inputData;
    	this.inputModelFile = inputModelFile;
    	this.logger = logger;
    }

    
	public Data[] execute() {
		try {			
			Model outputModel = ModelUnmarshaller.unmarshalModelFrom(inputModelFile, false);
			 
			BasicDataPlus outputData = new BasicDataPlus(outputModel, inputData);
			outputData.setType(DataProperty.MODEL_TYPE);
			return new Data[]{outputData};

		} catch (FileNotFoundException e) {
			logger.log(LogService.LOG_ERROR, "Error locating XML file: " + e.getMessage(), e);
			return null;
		} catch (JAXBException e) {
			String causeMessage = e.getMessage();			
			if (causeMessage == null) {
				causeMessage = e.getLinkedException().getMessage();
			}

			logger.log(LogService.LOG_ERROR, "Error parsing XML file: " + causeMessage, e);
			return null;
		} catch (ModelModificationException e) {
			logger.log(LogService.LOG_ERROR,
					"Error creating model from XML file: " + e.getMessage(), e);
			return null;
		} catch (SAXException e) {
			logger.log(LogService.LOG_ERROR,
					"Error parsing XML file: " + e.getMessage(), e);
			return null;
		} catch (IOException e) {
			logger.log(LogService.LOG_ERROR,
					"Unexpected error: XML schema not found: " + e.getMessage(), e);
			return null;
		}
	}

	
	public static void main(String[] args) {
		try {
			File testFile =
				FileUtilities.loadFileFromClassPath(ModelReaderAlgorithm.class, TEST_FILE_PATH);

			Data data = new BasicData(testFile, Constants.MODEL_MIME_TYPE);

			AlgorithmFactory algorithmFactory =	new ModelReaderAlgorithmFactory();
			Algorithm algorithm =
				algorithmFactory.createAlgorithm(
						new Data[] { data },
						new Hashtable<String, Object>(),
						new LogOnlyCIShellContext());

			System.out.println("Executing.. ");
			Data[] outputData = algorithm.execute();
			System.out.println(".. Done.");

			System.out.println("Read model file:");
			System.out.println(outputData[0].getData());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		System.exit(0);
	}
}
