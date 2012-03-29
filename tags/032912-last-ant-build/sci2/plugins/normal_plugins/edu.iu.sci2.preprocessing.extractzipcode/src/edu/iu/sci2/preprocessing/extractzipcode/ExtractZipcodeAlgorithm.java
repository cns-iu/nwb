package edu.iu.sci2.preprocessing.extractzipcode;

import java.text.DecimalFormat;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
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
    private Dictionary<String, Object> parameters;
	private LogService logger;
	
    public static final String PLACE_NAME_COLUMN = "addresscolumn";
    
    
    public ExtractZipcodeAlgorithm(Data[] data, Dictionary<String, Object> parameters,
			CIShellContext context) {
    	
        this.data = data;
        this.parameters = parameters;
		this.logger = (LogService) context.getService(LogService.class.getName());
	}

    @SuppressWarnings("unchecked") // Raw Dictionary of metadata
	public Data[] execute() {

		boolean truncate = Boolean.parseBoolean(parameters.get(TRUNCATE_COLUMN).toString());
		String addressColumnName = (String) parameters.get(PLACE_NAME_COLUMN);
		
		Table originalInputTable = (Table) this.data[0].getData();
		
		/*
		 * After getting the Prefuse Table data pass it for parsing ZIP codes.
		 * */
		ExtractZipcodeComputation zipcodeExtractionComputation = 
			new ExtractZipcodeComputation(truncate, addressColumnName, originalInputTable, logger);
		
		logger.log(LogService.LOG_INFO, zipcodeExtractionComputation.getTotalZipcodesExtracted()
										+ " ZIP codes extracted from "
										+ zipcodeExtractionComputation.getTotalAddressesConsidered()
										+ " addresses.");
				
		
		/*
		 * After getting the output in table format make it available to the user.
		 * */
		Data output = new BasicData(zipcodeExtractionComputation.getOutputTable(), Table.class.getName());
		Dictionary<String, Object> metadata = output.getMetadata();
		metadata.put(DataProperty.LABEL, "ZIP codes for addresses in \"" 
										 + addressColumnName 
										 + "\" is added. "
										 + calculateParsingPercentage(zipcodeExtractionComputation.getTotalZipcodesExtracted(), 
												 					  zipcodeExtractionComputation.getTotalAddressesConsidered())
										 + "% ZIP codes extracted.");
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		
		return new Data[]{ output };
    }
    
    private double calculateParsingPercentage(int extractedZipcodesCount, int totalAddresses) {
       	DecimalFormat roundedRatioFormat = new DecimalFormat("#.###");
		return Double.valueOf(roundedRatioFormat.format((extractedZipcodesCount * 100.0) / totalAddresses));
    }
}