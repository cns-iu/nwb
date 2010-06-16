package edu.iu.nwb.converter.prefusebibtex;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.UnicodeReader;

import bibtex.dom.BibtexFile;
import bibtex.expansions.ExpansionException;
import bibtex.expansions.MacroReferenceExpander;
import bibtex.parser.BibtexParser;
import bibtex.parser.ParseException;


public class BibtexValidation implements AlgorithmFactory {	
	public Algorithm createAlgorithm(
			Data[] data, Dictionary parameters, CIShellContext context) {
		return new BibtexValidationAlgorithm(data, parameters, context);
	}

	public class BibtexValidationAlgorithm implements Algorithm {
		public static final String BIBTEX_MIME_TYPE = "file:text/bibtex";

		private String inBibtexFileName;

		
		public BibtexValidationAlgorithm(
				Data[] data, Dictionary parameters, CIShellContext context) {
			this.inBibtexFileName = (String) data[0].getData();
		}

		
		public Data[] execute() throws AlgorithmExecutionException {
			File inputData = new File(inBibtexFileName);
			
			try {
				validateSelectedFileforBibtexFormat(inBibtexFileName);
				
				return createOutData(inputData);
			} catch (SecurityException e) {
				throw new AlgorithmExecutionException(e.getMessage(), e);
			}

		}

		private Data[] createOutData(File inputData) {
			Data[] validationData = new Data[]{
					new BasicData(inputData, BIBTEX_MIME_TYPE)};
			validationData[0].getMetadata().put(
					DataProperty.LABEL, "BibTeX File: " + inBibtexFileName);
			validationData[0].getMetadata().put(
					DataProperty.TYPE, DataProperty.TABLE_TYPE);
			return validationData;
		}

		private void validateSelectedFileforBibtexFormat(String fileHandler)
				throws AlgorithmExecutionException {
			BibtexFile bibtexFile = new BibtexFile();
	    	BibtexParser parser = new BibtexParser(true);
	    	
	    	try {
				parser.parse(bibtexFile, new UnicodeReader(new FileInputStream(fileHandler)));
			} catch (ParseException e) {
				String message =
					"Error parsing BibTeX file: " + e.getMessage();
				throw new AlgorithmExecutionException(message, e);
			} catch (FileNotFoundException e) {
				String message = "Couldn't find BibTeX file: " + e.getMessage();
				throw new AlgorithmExecutionException(message, e);
			} catch (IOException e) {
				String message = "File access error: " + e.getMessage();
				throw new AlgorithmExecutionException(message, e);
			}
			
	    	try {
		    	MacroReferenceExpander macroExpander = 
		    		new MacroReferenceExpander(true, true, false, false);
		    	
		    	macroExpander.expand(bibtexFile);
	    	} catch (ExpansionException e) {
	    		throw new AlgorithmExecutionException(
	    				"Error occurred while parsing BibTeX file.", e);
	    	}
		}
	}
}
