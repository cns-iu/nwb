package edu.iu.nwb.converter.prefuseisi.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
import org.cishell.utilities.UnicodeReader;

/**
 * @author Weixia(Bonnie) Huang
 */
public class PrefuseIsiValidation implements AlgorithmFactory {
	/*
	 * All the ISI files have FN * ISI * pattern as it's starting line. This is used to identify 
	 * whether or not it is an ISI file.
	 */
	public static final String ISI_IDENTIFIER_SIGNATURE = "fn{1}.*isi{1}.*";
	public static final int NUMBER_LINES_CONSIDERED = 5;

	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
		return new ISIValidationAlgorithm(data);
	}

	public class ISIValidationAlgorithm implements Algorithm {
		public static final String ISI_MIME_TYPE = "file:text/isi";
		
		private String inISIFile;

		
		public ISIValidationAlgorithm(Data[] data) {
			this.inISIFile = (String) data[0].getData();
		}

		
		public Data[] execute() throws AlgorithmExecutionException {
			File inputData = new File(inISIFile);
			boolean isiIdentifierFound = false;

			/*
			 * ISIIdentifierFound variable tracks whether or not ISI signature
			 * is found by using the isiSignatureSearcher method.
			 * */
			isiIdentifierFound = isiSignatureSearcher(inISIFile);

			/*
			 * Depending upon whether ISI signature is found or not further
			 * actions are taken.
			 * */
			if (isiIdentifierFound) {
				try {
					return createOutData(inputData);
				} catch (SecurityException e) {
					throw new AlgorithmExecutionException(e.getMessage(), e);
				}
			} else {
				throw new AlgorithmExecutionException(
						"Invalid ISI format file selected. "
						+" Unable to continue loading "
						+ inISIFile + " file.");
			}

		}

		private Data[] createOutData(File inputData) {
			Data[] validationData = new Data[] { new BasicData(inputData,
			ISI_MIME_TYPE) };
			validationData[0].getMetadata().put(DataProperty.LABEL,
					"Prefuse ISI file: " + inISIFile);
			validationData[0].getMetadata().put(DataProperty.TYPE,
					DataProperty.TABLE_TYPE);
			return validationData;
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
		private boolean isiSignatureSearcher(String fileHandler)
				throws AlgorithmExecutionException {
			boolean isiIdentifierFound = false;
			try {
				BufferedReader isiInputFileReader =
					new BufferedReader(new UnicodeReader(new FileInputStream(fileHandler)));
				String lineContent;
				int currentNumberOfLinesCounter = 0;				

				while ((lineContent = isiInputFileReader.readLine()) != null
						&& !isiIdentifierFound 
						&& (currentNumberOfLinesCounter < NUMBER_LINES_CONSIDERED)) {						
					lineContent = lineContent.trim();
					if (lineContent.length() > 0) {
						/* Regex for searching for the ISI Signature in
						 * a case insensitive manner. 
						 */
						isiIdentifierFound =
							Pattern.compile(
								ISI_IDENTIFIER_SIGNATURE,
								Pattern.CASE_INSENSITIVE
								| Pattern.UNICODE_CASE)
									.matcher(lineContent).matches();

						currentNumberOfLinesCounter++;
					}
				}
				
				isiInputFileReader.close();
			} catch (IOException e) {
				throw new AlgorithmExecutionException(e.getMessage(), e);
			}
			
			return isiIdentifierFound;
		}
	}
}