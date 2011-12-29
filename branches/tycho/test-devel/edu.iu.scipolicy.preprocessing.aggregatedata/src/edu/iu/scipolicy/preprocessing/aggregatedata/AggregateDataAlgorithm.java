package edu.iu.scipolicy.preprocessing.aggregatedata;

import java.text.DecimalFormat;
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

public class AggregateDataAlgorithm implements Algorithm {
	
	private static final String TRUNCATE_COLUMN = "truncate";
	private Data[] data;
    private Dictionary parameters;
	private LogService logger;
	
    public static final String AGGREGATE_ON_COLUMN = "aggregateoncolumn";
    private static final String DELIMITER = "delimiter";
    
    
    public AggregateDataAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
    	
        this.data = data;
        this.parameters = parameters;
		this.logger = (LogService) context.getService(LogService.class.getName());
	}

    public Data[] execute() throws AlgorithmExecutionException {

		String addressColumnName = (String) parameters.get(AGGREGATE_ON_COLUMN);
		String delimiterColumnName = (String) parameters.get(DELIMITER);
		
		Table originalInputTable = (Table) this.data[0].getData();
		
		/*
		 * After getting the Prefuse Table data pass it for parsing ZIP codes.
		 * */
		AggregateDataComputation zipcodeExtractionComputation = 
			new AggregateDataComputation(delimiterColumnName, addressColumnName, originalInputTable, logger);
		
		logger.log(LogService.LOG_INFO, zipcodeExtractionComputation.getTotalZipcodesExtracted()
										+ " ZIP codes extracted from "
										+ zipcodeExtractionComputation.getTotalAddressesConsidered()
										+ " addresses.");
				
		
		/*
		 * After getting the output in table format make it available to the user.
		 * */
		Data output = new BasicData(zipcodeExtractionComputation.getOutputTable(), Table.class.getName());
		Dictionary metadata = output.getMetadata();
		metadata.put(DataProperty.LABEL, "ZIP codes for addresses in \"" 
										 + addressColumnName 
										 + "\" is added. "
//										 + calculateParsingPercentage(zipcodeExtractionComputation.getTotalZipcodesExtracted(), 
//												 					  zipcodeExtractionComputation.getTotalAddressesConsidered())
										 + "% ZIP codes extracted.");
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		
		return new Data[]{ output };
    }
    
    private double calculateParsingPercentage(int extractedZipcodesCount, int totalAddresses) {
       	DecimalFormat roundedRatioFormat = new DecimalFormat("#.###");
		return Double.valueOf(roundedRatioFormat.format(((double) extractedZipcodesCount * 100.0) / totalAddresses));
    }
}