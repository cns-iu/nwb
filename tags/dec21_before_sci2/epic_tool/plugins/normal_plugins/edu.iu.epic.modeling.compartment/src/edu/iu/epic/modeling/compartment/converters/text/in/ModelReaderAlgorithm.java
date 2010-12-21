package edu.iu.epic.modeling.compartment.converters.text.in;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.BasicDataPlus;
import org.osgi.service.log.LogService;

import edu.iu.cns.utilities.testing.LogOnlyCIShellContext;
import edu.iu.epic.modeling.compartment.converters.text.Constants;
import edu.iu.epic.modeling.compartment.converters.text.SystemErrCapturer;
import edu.iu.epic.modeling.compartment.converters.text.generated.ModelFileLexer;
import edu.iu.epic.modeling.compartment.converters.text.generated.ModelFileParser;
import edu.iu.epic.modeling.compartment.converters.text.generated.ModelFileParser.UncheckedParsingException;
import edu.iu.epic.modeling.compartment.model.Model;

public class ModelReaderAlgorithm implements Algorithm {
	public static final String TEST_DATUM_PATH =
		"/edu/iu/epic/modeling/compartment/converters/text/testing/"
		+ "good/"
		+ "test.mdl";

	private Data inputData;
	private File inputModelFile;
	private LogService logger;

    public ModelReaderAlgorithm(Data[] data, LogService logger) {
    	this.inputData = data[0];
    	this.logger = logger;

		this.inputModelFile = (File) data[0].getData();

    }

	public Data[] execute() throws AlgorithmExecutionException {
		SystemErrCapturer systemErrCapturer = new SystemErrCapturer();

		try {
			ModelFileLexer lex =
				new ModelFileLexer(new ANTLRFileStream(inputModelFile.getCanonicalPath()));
			CommonTokenStream tokens = new CommonTokenStream(lex);
		   	ModelFileParser parser = new ModelFileParser(tokens);

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
    			throw new AlgorithmExecutionException(message);
    		}


		    Model outputModel = parser.getModel();
		    
		    BasicDataPlus outputData = new BasicDataPlus(outputModel, inputData);
		    outputData.setType(DataProperty.MODEL_TYPE);
		    return new Data[]{outputData};
		} catch (IOException e) {
			logger.log(LogService.LOG_ERROR, "Error reading model file: " + e.getMessage(), e);
			return null;
		} catch (UncheckedParsingException e) {
			logger.log(
					LogService.LOG_ERROR,
					"Error parsing invalid model file: " + e.getMessage(),
					e);
			return null;
		} catch (RecognitionException e) {
			logger.log(
					LogService.LOG_ERROR,
					"Error parsing invalid model file: " + e.getMessage(),
					e);
			return null;
		}
	}

	public static void main(String[] args) {
//		File outFile = null;

		try {
			Dictionary<String, Object> parameters =	new Hashtable<String, Object>();

			URL testFileURL = ModelReaderAlgorithm.class.getResource(TEST_DATUM_PATH);
			File testFile = new File(testFileURL.toURI());
			AlgorithmFactory algorithmFactory =	new ModelReaderAlgorithmFactory();

//			File newlineTestFile = File.createTempFile("newlineTest", "mdl");
//			Writer writer = new FileWriter(newlineTestFile);
//			writer.write("7a = 5");
//			writer.close();
//			testFile = newlineTestFile;

			Data data = new BasicData(testFile, Constants.MODEL_MIME_TYPE);

			Algorithm algorithm =
				algorithmFactory.createAlgorithm(
						new Data[]{ data }, parameters, new LogOnlyCIShellContext());

			System.out.println("Executing.. ");
			Data[] outputData = algorithm.execute();
			System.out.println(".. Done.");

			System.out.println("Read model file:");
			System.out.println(outputData[0].getData());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		// A battery of smaller tests to augment the files in testing/bad.
//		System.out.println("Parameter expression tests:");
//		String[] parameterExpressionTests =
//			new String[]{ "", "0", "7", ".3", "2.7", "12.7", "a*b", "a%b", "+", "r!beta", "7a" };
//		for (int ii = 0; ii < parameterExpressionTests.length; ii++) {
//			String parameterExpressionTest = parameterExpressionTests[ii];
//			System.out.println(
//					"'" + parameterExpressionTest + "'" + " is valid?\t\t"
//					+ Model.isValidParameterExpression(parameterExpressionTest));
//		}
//
//		System.out.println();
//
//		System.out.println("Compartment ID tests:");
//		String[] compartmentIDTests =
//			new String[]{ "S", "L", "Ia", "A7", "_S", "7A", "a+b", "a%b" };
//		for (int ii = 0; ii < compartmentIDTests.length; ii++) {
//			String compartmentIDTest = compartmentIDTests[ii];
//			System.out.println("'" + compartmentIDTest + "'" + " is valid?\t\t"
//					+ Model.isValidCompartmentName(compartmentIDTest));
//		}
//
//		ModelFileParser parser = ModelFileParser.createParserOn("a+b*foo-c");
//		System.out.println("Referenced parameters:");
//		try {
//			Set<String> referencedParameters = new HashSet<String>();
//			parser.parameterValue(referencedParameters);
//
//			for (Iterator<String> referencedParametersIt = referencedParameters.iterator();
//					referencedParametersIt.hasNext();) {
//				String referencedParameter = referencedParametersIt.next();
//				System.out.println(referencedParameter);
//			}
//		} catch (RecognitionException e) {
//			e.printStackTrace();
//		}
//
//		String[] modelFileTests = new String[]{ "asdajklwehfv\nakl;sertu;al\n" };
//		for (int ii = 0; ii < modelFileTests.length; ii++) {
//			String modelFileTest = modelFileTests[ii];
//			ModelFileParser modelFileParser = ModelFileParser.createParserOn(modelFileTest);
//			try {
//				System.out.println("'" + modelFileTest + "'"
//						+ " is valid?\t\t" + modelFileParser.modelFile());
//			} catch (RecognitionException e) {
//				e.printStackTrace();
//			}
//		}

		System.exit(0);
	}
}
