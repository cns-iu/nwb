package edu.iu.epic.modeling.compartment.converters.xml.in;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.BasicDataPlus;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import edu.iu.cns.utilities.testing.LogOnlyCIShellContext;
import edu.iu.epic.modeling.compartment.converters.xml.Constants;
import edu.iu.epic.modeling.compartment.model.Model;

public class ModelReaderAlgorithm implements Algorithm {
	public static final String TEST_FILE_PATH =
		"/edu/iu/epic/modeling/compartment/converters/xml/testing/"
		+ "good/"
		+ "test.model.xml";

	private Data inputData;
	private File inputModelFile;
	private LogService logger;

    public ModelReaderAlgorithm(Data[] data, LogService logger) {
    	this.inputData = data[0];
    	this.logger = logger;

		this.inputModelFile = (File) data[0].getData();
    }

	public Data[] execute() {
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();			
			ModelXMLHandler modelXMLHandler = new ModelXMLHandler();
			xmlReader.setContentHandler(modelXMLHandler);
			xmlReader.setErrorHandler(modelXMLHandler);
			
			FileReader fileReader = new FileReader(inputModelFile);
			
			xmlReader.parse(new InputSource(fileReader));
			
			Model outputModel = modelXMLHandler.getModel();
			
			return new Data[] { new BasicDataPlus(outputModel, inputData) };
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

	public static void main(String[] args) {
		try {
			File testFile =
				FileUtilities.loadFileFromClassPath(ModelReaderAlgorithm.class, TEST_FILE_PATH);

//			File testFile = File.createTempFile("newlineTest", "mdl");
//			Writer writer = new FileWriter(testFile);
//			writer.write("a=5");
//			writer.close();

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
