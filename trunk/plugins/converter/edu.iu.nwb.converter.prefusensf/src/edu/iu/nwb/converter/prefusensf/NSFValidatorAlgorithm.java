package edu.iu.nwb.converter.prefusensf;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

public class NSFValidatorAlgorithm implements Algorithm {
	private String inNSFFileName;

	public NSFValidatorAlgorithm(
			Data[] data, Dictionary parameters, CIShellContext context) {
		this.inNSFFileName = (String) data[0].getData();
	}

	public Data[] execute() throws AlgorithmExecutionException {
		File inNSFFile = new File(inNSFFileName);

		try {
			/*
			 * TODO: Write a proper/working NSF File validator. Initially used
			 * CSV Validator for this but it doesnt work as expected. Use the
			 * CSV Validator Algorithm for both NSF & SCOPUS Files. Since both
			 * are CSV based File Formats.
			 * 
			 * 
			 * PrefuseCsvValidator csvValidator = new
			 * PrefuseCsvValidator();
			 * 
			 * 
			 * validateSelectedFileforCSVFormat throws an exception if the file
			 * path provided to it does not point to valid csv file. Else it
			 * will not throw any error.
			 * 
			 * csvValidator.validateSelectedFileforCSVFormat(nsfFileName);
			 */

			return createOutData(inNSFFile);
		} catch (SecurityException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}

	private Data[] createOutData(File nsfFile) {
		Data[] validatedData =
			new Data[] { new BasicData(nsfFile, "file:text/nsf") };
		validatedData[0].getMetadata().put(
				DataProperty.LABEL,	"NSF csv file: " + inNSFFileName);
		validatedData[0].getMetadata().put(
				DataProperty.TYPE, DataProperty.TABLE_TYPE);
		return validatedData;
	}
}