package edu.iu.nwb.converter.prefusecsv.validator;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.iu.nwb.converter.prefusecsv.preprocessing.CSVFilePreprocessor;

import prefuse.data.io.CSVTableReader;
import prefuse.data.io.DataIOException;

public class PrefuseCsvValidator implements Algorithm {
	public static final String CSV_MIME_TYPE = "file:text/csv";
	
	private String inCSVFilePath;

	
	public static class Factory implements AlgorithmFactory {
		public Algorithm createAlgorithm(
				Data[] data, Dictionary parameters, CIShellContext context) {
			return new PrefuseCsvValidator(data);
		}
	}
	public PrefuseCsvValidator() {
		// For edu.iu.nwb.converter.prefusescopus.ScopusValidatorAlgorithm
	}	
	public PrefuseCsvValidator(Data[] data) {
		this.inCSVFilePath = (String) data[0].getData();		
	}


	public Data[] execute() throws AlgorithmExecutionException {

		try {
			File inputData = CSVFilePreprocessor.execute(inCSVFilePath);
			
			/* validator function is used to validate the data & only if the
			 * input file has valid CSV data then its loaded into the workbench
			 * for further manipulations by the user.
			 */
			validateSelectedFileforCSVFormat(inputData.getAbsolutePath());
			
			return createOutData(inputData);
		} catch (SecurityException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}

	}

	private Data[] createOutData(File inputData) {
		Data[] validationData = new Data[] { new BasicData(inputData,
				CSV_MIME_TYPE) };
		validationData[0].getMetadata().put(DataProperty.LABEL,
				"CSV file: " + inputData.getAbsolutePath());
		validationData[0].getMetadata().put(DataProperty.TYPE,
				DataProperty.TABLE_TYPE);
		return validationData;
	}

	/*
	 * Validation Function
	 */
	//TODO: Document contract (what it means if exception is thrown, what it means if it is not)
	public void validateSelectedFileforCSVFormat(String csvFileName)
			throws AlgorithmExecutionException {
		CSVTableReader csvValidator = new CSVTableReader();

		try {			
			csvValidator.readTable(csvFileName);
		} catch (DataIOException e) {
			throw new AlgorithmExecutionException(
				"Error reading tables: " + e.getMessage(), e);
		}
	}
}
