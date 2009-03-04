package edu.iu.nwb.converter.prefuseisi.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Dictionary;
import java.util.regex.Pattern;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

/**
 * @author Weixia(Bonnie) Huang
 */
public class PrefuseIsiValidation implements AlgorithmFactory {

	/*
	 * All the ISI files have FN * ISI * pattern as it's starting line. This is used to identify 
	 * whether or not it is an ISI file.
	 * */

	private final String ISI_IDENTIFIER_SIGNATURE = "fn{1}.*isi{1}.*";
	private final int NUMBER_LINES_CONSIDERED = 5;

	public Algorithm createAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		return new ISIValidationAlgorithm(data, parameters, context);
	}

	public class ISIValidationAlgorithm implements Algorithm {
		Data[] data;
		Dictionary parameters;
		CIShellContext context;
		private LogService logger;

		public ISIValidationAlgorithm(Data[] inputData,
				Dictionary parameters, CIShellContext ciShellContext) {
			this.data = inputData;
			this.parameters = parameters;
			this.context = ciShellContext;

			logger = (LogService) ciShellContext.getService(LogService.class.getName());

		}

		public Data[] execute() throws AlgorithmExecutionException {
			String fileHandler = (String) data[0].getData();
			File inputData = new File(fileHandler);
			boolean isiIdentifierFound = false;

			/*
			 * ISIIdentifierFound variable tracks whether or not ISI signature is found
			 * by using the isiSignatureSearcher method.
			 * */
			isiIdentifierFound = isiSignatureSearcher(fileHandler);

			/*
			 * Depending upon whether ISI signature is found or not further actions are taken.
			 * */
			if (isiIdentifierFound) {
				try {
					Data[] validationData = new Data[] { new BasicData(inputData,
					"file:text/isi") };
					validationData[0].getMetadata().put(DataProperty.LABEL,
							"Prefuse ISI file: " + fileHandler);
					validationData[0].getMetadata().put(DataProperty.TYPE,
							DataProperty.TABLE_TYPE);
					return validationData;
				} catch (SecurityException exception) {
					throw new AlgorithmExecutionException(exception);
				}
			} else {
				logger.log(LogService.LOG_ERROR,"Invalid ISI format file selected.\nUnable to continue loading "
						+ fileHandler + " file.");
				throw new AlgorithmExecutionException(
						"Invalid ISI format file selected. Unable to continue loading "
						+ fileHandler + " file.");
			}

		}

		/**
		 * This method is used to search for ISI signature inside the input file. It disregards all the 
		 * empty lines from top. Right now it searches for the ISI signature i.e. "FN * ISI *" only 
		 * in the top 5 lines but this parameter is configurable.
		 * @param fileHandler
		 * @param ISIIdentifierFound
		 * @return
		 * @throws AlgorithmExecutionException
		 */
		//TODO: refactor isiIdentifier here too
		private boolean isiSignatureSearcher(String fileHandler) throws AlgorithmExecutionException {
			boolean isiIdentifierFound = false;
			try {
				BufferedReader isiInputFileReader = new BufferedReader(
						new FileReader(fileHandler));
				String lineContent;
				int currentNumberOfLinesCounter = 0;
				

				while (
						(lineContent = isiInputFileReader.readLine()) != null
						&& !isiIdentifierFound 
						&& (currentNumberOfLinesCounter < NUMBER_LINES_CONSIDERED) 

				) {
						
					lineContent = lineContent.trim();
					if (lineContent.length() > 0) {
						/*
						 * RegExp for searching for the ISI Signature in a case insensitive manner. 
						 * */
						isiIdentifierFound = Pattern
						.compile(
								ISI_IDENTIFIER_SIGNATURE,
								Pattern.CASE_INSENSITIVE
								| Pattern.UNICODE_CASE)
								.matcher(lineContent).matches();

						currentNumberOfLinesCounter++;
					}

				}
				isiInputFileReader.close();
			} catch (IOException testFileException) {
				throw new AlgorithmExecutionException(testFileException);
			}
			return isiIdentifierFound;
		}
	}
}