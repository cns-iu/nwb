package edu.iu.nwb.converter.prefusescopus;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.iu.nwb.converter.prefusecsv.validator.PrefuseCsvValidator;

public class ScopusValidatorAlgorithm implements Algorithm {
	public static final String SCOPUS_MIME_TYPE = "file:text/scopus";
	
	private String inScopusFileName;
    
	
    public ScopusValidatorAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext context) {
		this.inScopusFileName = (String) data[0].getData();
    }

    
    public Data[] execute() throws AlgorithmExecutionException {
		File inScopusFile = new File(inScopusFileName);

		try {			
			/*
			 * Use the CSV Validator Algorithm for both NSF & SCOPUS Files.
			 * Since both are CSV based File Formats.
			 * */
			PrefuseCsvValidator csvValidator =
				new PrefuseCsvValidator();

			/*
			 * validateSelectedFileforCSVFormat throws an exception if the file
			 * path provided to it does not point to valid csv file. Else it
			 * will not throw any error.
			 */			
			csvValidator.validateSelectedFileforCSVFormat(inScopusFileName);
			
			return createOutData(inScopusFile);
		} catch (SecurityException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
    }

	private Data[] createOutData(File inData) {
		Data[] validationData = new Data[]{ new BasicData(
				inData, SCOPUS_MIME_TYPE) };
		validationData[0].getMetadata().put(
				DataProperty.LABEL, "Scopus csv file: " + inScopusFileName);
		validationData[0].getMetadata().put(
				DataProperty.TYPE, DataProperty.MATRIX_TYPE);
		return validationData;
	}
}