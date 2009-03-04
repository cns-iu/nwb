package edu.iu.nwb.converter.prefusebibtex;


import java.io.File;
import java.io.FileReader;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import bibtex.dom.BibtexFile;
import bibtex.expansions.ExpansionException;
import bibtex.expansions.MacroReferenceExpander;
import bibtex.parser.BibtexParser;


public class BibtexValidation implements AlgorithmFactory {
	
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		return new BibtexValidationAlgorithm(data, parameters, context);
	}

	public class BibtexValidationAlgorithm implements Algorithm {
		Data[] data;
		Dictionary parameters;
		CIShellContext context;

		public BibtexValidationAlgorithm(Data[] inputData, Dictionary parameters, CIShellContext ciShellContext) {
			this.data = inputData;
			this.parameters = parameters;
			this.context = ciShellContext;
			
			
		}

		public Data[] execute() throws AlgorithmExecutionException {

			String fileHandler = (String) data[0].getData();
			File inputData = new File(fileHandler);
			try{
				/*
				 * validator function is used to validate the data & only if the input file has valid bibtex data 
				 * then its loaded into the workbench for further manipulations by the user. 
				 * */
				validateSelectedFileforBibtexFormat(fileHandler);
				Data[] validationData = new Data[] {new BasicData(inputData, "file:text/bibtex")};
				validationData[0].getMetadata().put(DataProperty.LABEL, "BibTeX File: " + fileHandler);
				validationData[0].getMetadata().put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
				return validationData;
			}catch (SecurityException exception){
				throw new AlgorithmExecutionException(exception);
			}

		}

		private void validateSelectedFileforBibtexFormat(String fileHandler)
				throws AlgorithmExecutionException {
			BibtexFile bibtexFile = new BibtexFile();
	    	BibtexParser parser = new BibtexParser(true);
	    	try {
	    	parser.parse(bibtexFile, new FileReader(fileHandler));
	    	} catch (Exception e) {
	    		throw new AlgorithmExecutionException("Fatal exception occurred while parsing bibtex file.", e);
	    	} finally {
//	    		logger.log(LogService.LOG_ERROR, "Error occured while loading " + fileHandler);
	    	}
	    	try {
	    	MacroReferenceExpander macroExpander = 
	    		new MacroReferenceExpander(true, true, false, false);
	    	macroExpander.expand(bibtexFile);
	    	} catch (ExpansionException e) {
	    		throw new AlgorithmExecutionException("Error occurred while parsing bibtex file.", e);
	    	}
		}
	}
}
