package edu.iu.nwb.converter.prefusensf;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.iu.nwb.converter.prefusecsv.validator.PrefuseCsvValidationAlgorithm;
import edu.iu.nwb.converter.prefusecsv.validator.PrefuseCsvValidationAlgorithmFactory;

public class NSFValidatorAlgorithm implements Algorithm {
	Data[] data;
	Dictionary parameters;
	CIShellContext context;

	public NSFValidatorAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;
	}

	public Data[] execute() throws AlgorithmExecutionException {
		String nsfFileName = (String) data[0].getData();
		File nsfFile = new File(nsfFileName);

		try {

			/*
			 * TODO: Write a proper/working NSF File validator. Initially used
			 * CSV Validator for this but it doesnt work as expected. Use the
			 * CSV Validator Algorithm for both NSF & SCOPUS Files. Since both
			 * are CSV based File Formats.
			 * 
			 * 
			 * PrefuseCsvValidationAlgorithm csvValidator = new
			 * PrefuseCsvValidationAlgorithm();
			 * 
			 * 
			 * validateSelectedFileforCSVFormat throws an exception if the file
			 * path provided to it does not point to valid csv file. Else it
			 * will not throw any error.
			 * 
			 * csvValidator.validateSelectedFileforCSVFormat(nsfFileName);
			 */

			Data[] validatedData = new Data[] { new BasicData(nsfFile,
			"file:text/nsf") };
			validatedData[0].getMetadata().put(DataProperty.LABEL,
					"NSF csv file: " + nsfFileName);
			validatedData[0].getMetadata().put(DataProperty.TYPE,
					DataProperty.TABLE_TYPE);
			return validatedData;
		} catch (SecurityException exception) {
			throw new AlgorithmExecutionException(exception);
		}

	}
}