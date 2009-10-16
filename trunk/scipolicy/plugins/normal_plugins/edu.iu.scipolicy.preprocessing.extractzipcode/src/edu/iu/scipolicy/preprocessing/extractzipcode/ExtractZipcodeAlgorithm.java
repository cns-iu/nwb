package edu.iu.scipolicy.preprocessing.extractzipcode;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;

/**
 * This algorithm parses the address information provided and extracts ZIP codes from it.
 * Currently it accepts ZIP codes which are in United States of America format i.e. either
 * xxxxx (short form) or xxxxx-xxxx (long form).
 * The output of this algorithm will be the original table with 1 columns added for 
 * Zipcode.
 * @author cdtank
 *
 */

public class ExtractZipcodeAlgorithm implements Algorithm {
	
	private static final String TRUNCATE_COLUMN = "truncate";
	private Data[] data;
    private Dictionary parameters;
	private LogService logger;
	
    public static final String PLACE_NAME_COLUMN = "addresscolumn";
    
    
    public ExtractZipcodeAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
    	
        this.data = data;
        this.parameters = parameters;
		this.logger = (LogService) context.getService(LogService.class.getName());
	}

    public Data[] execute() throws AlgorithmExecutionException {

		boolean truncate = Boolean.parseBoolean(parameters.get(TRUNCATE_COLUMN).toString());
		String addressColumnName = (String) parameters.get(PLACE_NAME_COLUMN);
		
		Table originalInputTable = (Table) this.data[0].getData();
		
		/*
		 * After getting the Prefuse Table data pass it for parsing ZIP codes.
		 * */
		ExtractZipcodeComputation geoCoderComputation = 
			new ExtractZipcodeComputation(truncate, addressColumnName, originalInputTable, logger);
		
		/*
		 * After getting the output in table format make it available to the user.
		 * */
		Data output = new BasicData(geoCoderComputation.getOutputTable(), Table.class.getName());
		Dictionary metadata = output.getMetadata();
		metadata.put(DataProperty.LABEL, "ZIP codes for addresses in \"" 
										 + addressColumnName 
										 + "\" is added.");
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		
		return new Data[]{ output };
    }
}