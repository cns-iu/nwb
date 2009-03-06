package edu.iu.nwb.converter.prefusecsv.validator;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.io.CSVTableReader;
import prefuse.data.io.DataIOException;

public class PrefuseCsvValidationAlgorithm implements Algorithm {
	Data[] data;
	Dictionary parameters;
	CIShellContext context;

	public PrefuseCsvValidationAlgorithm(Data[] inputData,
			Dictionary parameters, CIShellContext ciShellContext) {
		this.data = inputData;
		this.parameters = parameters;
		this.context = ciShellContext;
		
	}

	public PrefuseCsvValidationAlgorithm() {
		// TODO Auto-generated constructor stub
	}

	public Data[] execute() throws AlgorithmExecutionException {
		LogService logger = (LogService) context.getService(LogService.class
				.getName());

		String fileHandler = (String) data[0].getData();
		File inputData = new File(fileHandler);

		try {
			/*
			 * validator function is used to validate the data & only if the
			 * input file has valid CSV data then its loaded into the workbench
			 * for further manipulations by the user.
			 */
			validateSelectedFileforCSVFormat(fileHandler);
			Data[] validationData = new Data[] { new BasicData(inputData,
					"file:text/csv") };
			validationData[0].getMetadata().put(DataProperty.LABEL,
					"Prefuse CSV file: " + fileHandler);
			validationData[0].getMetadata().put(DataProperty.TYPE,
					DataProperty.TABLE_TYPE);
			return validationData;
		} catch (SecurityException exception) {
			throw new AlgorithmExecutionException(
					"Improper CSV file selected. Got the following security exception ",
					exception);
		}

	}

	/*
	 * Validation Function
	 */
	//TODO: Document contract (what it means if exception is thrown, what it means if it is not)
	public void validateSelectedFileforCSVFormat(String csvFileName)
			throws AlgorithmExecutionException {
		CSVTableReader csvValidator = new CSVTableReader();

		Table nodes = null;
		Table edges = null;
		
		try {
			
			nodes = csvValidator.readTable(csvFileName);
			edges = csvValidator.readTable(csvFileName);
		} catch (DataIOException e) {
			System.err.println("ERROR VALIDATING CSV CONTEXT");
			throw new AlgorithmExecutionException("Error reading tables: "
					+ e.getMessage(), e);
		}
	}
}
