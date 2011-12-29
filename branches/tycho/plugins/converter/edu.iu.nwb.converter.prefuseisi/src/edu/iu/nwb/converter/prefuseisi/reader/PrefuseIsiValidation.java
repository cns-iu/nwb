package edu.iu.nwb.converter.prefuseisi.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Dictionary;
import java.util.LinkedList;
import java.util.List;
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
 * All the ISI files start with a "FN" tag that identifies the file type.
 * 
 * The exact string emitted by the ISI/WebOfScience web page may change without
 * warning, and this may signify differences in the file format. After one such
 * change, too much specificity in this validator caused the tool to stop
 * loading ISI files entirely, which was a problem. So our new strategy is to be
 * loose in the validation, and then make a best effort in the loading stage.
 * 
 * See {@link edu.iu.nwb.shared.isiutil.ISITableReader}
 * 
 * @author Weixia(Bonnie) Huang
 * @author thgsmith
 */
public class PrefuseIsiValidation implements AlgorithmFactory {
	/**
	 * ISI files begin with "FN {identifier string}".
	 */
	public static final Pattern ISI_FIRST_TAG = Pattern.compile(
			"fn .*",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL);
	public static final int NUMBER_LINES_CONSIDERED = 5;

	public Algorithm createAlgorithm(Data[] data,
			Dictionary<String, Object> parameters, CIShellContext context) {
		return new ISIValidationAlgorithm(data);
	}

	public class ISIValidationAlgorithm implements Algorithm {
		public static final String ISI_MIME_TYPE = "file:text/isi";
		
		private String inISIFileName;

		
		public ISIValidationAlgorithm(Data[] data) {
			this.inISIFileName = (String) data[0].getData();
		}

		
		public Data[] execute() throws AlgorithmExecutionException {
			File inputFile = new File(inISIFileName);
			/*
			 * Depending upon whether ISI signature is found or not further
			 * actions are taken.
			 */
			if (fileHasISISignature(inputFile, NUMBER_LINES_CONSIDERED)) {
				return createOutData(inputFile);
			} else {
				throw new AlgorithmExecutionException(
						"Invalid ISI format file selected. "
						+" Unable to continue loading "
						+ inISIFileName + " file.");
			}

		}


		private boolean fileHasISISignature(File inputFile,
				int numLines) throws AlgorithmExecutionException {
			List<String> initialLines = readInitialLines(inputFile, numLines);
			
			for (String line : initialLines) {
				if (lineMatchesSignature(line)) {
					return true;
				}
			}
			return false;
		}


		/**
		 * Reads the first NUMBER_LINES_CONSIDERED non-blank lines in the
		 * inFile.
		 * 
		 * @return the first N lines, in a String array.
		 * @throws AlgorithmExecutionException
		 */
		private List<String> readInitialLines(File inFile, int numLines)
				throws AlgorithmExecutionException {
			BufferedReader inReader = null;
			List<String> firstLines = new LinkedList<String>();
			
			int currentNumberOfLinesCounter = 0;
			String lineContent;

			try {
				inReader = new BufferedReader(new UnicodeReader(
						new FileInputStream(inFile)));
				while ((lineContent = inReader.readLine()) != null
						&& (currentNumberOfLinesCounter < numLines)) {
					lineContent = lineContent.trim();
					if (!lineContent.isEmpty()) {
						firstLines.add(lineContent);
						currentNumberOfLinesCounter++;
					}
				}
			} catch (IOException e) {
				throw new AlgorithmExecutionException(e.getMessage(), e);
			} finally {
				try {
					inReader.close();
				} catch (IOException e) {
					throw new AlgorithmExecutionException(e.getMessage(), e);
				}
			}
			return firstLines;
		}

		private Data[] createOutData(File inputData) {
			Data[] validationData = new Data[] { new BasicData(inputData,
			ISI_MIME_TYPE) };
			validationData[0].getMetadata().put(DataProperty.LABEL,
					"Prefuse ISI file: " + inISIFileName);
			validationData[0].getMetadata().put(DataProperty.TYPE,
					DataProperty.TABLE_TYPE);
			return validationData;
		}

	}

	static boolean lineMatchesSignature(String line) {
		return ISI_FIRST_TAG.matcher(line).matches();
	}
}