package edu.iu.nwb.converter.prefusescopus;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.iu.nwb.converter.prefusecsv.validator.PrefuseCsvValidationAlgorithm;

public class ScopusValidatorAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public ScopusValidatorAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() throws AlgorithmExecutionException {
		String scopusFileName = (String) data[0].getData();
		File inData = new File(scopusFileName);

		try{
			
			/*
			 * Use the CSV Validator Algorithm for both NSF & SCOPUS Files. Since both are CSV based File Formats.
			 * */
			PrefuseCsvValidationAlgorithm csvValidator = new PrefuseCsvValidationAlgorithm();

			/*
			 * validateSelectedFileforCSVFormat throws an exception if the file
			 * path provided to it does not point to valid csv file. Else it
			 * will not throw any error.
			 */			
			csvValidator.validateSelectedFileforCSVFormat(scopusFileName);
			
			Data[] validationData = new Data[] {new BasicData(inData, "file:text/scopus")};
			validationData[0].getMetadata().put(DataProperty.LABEL, "Scopus csv file: " + scopusFileName);
			validationData[0].getMetadata().put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
			return validationData;
		}catch (SecurityException exception){
			throw new AlgorithmExecutionException(exception);
		}
    }
}