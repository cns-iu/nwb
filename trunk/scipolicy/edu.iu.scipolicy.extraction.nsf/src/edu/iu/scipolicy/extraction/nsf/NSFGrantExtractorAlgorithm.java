package edu.iu.scipolicy.extraction.nsf;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.data.Data;
import org.cishell.templates.database.SQLExecutionAlgorithm;
import org.cishell.templates.database.SQLFormationException;

public class NSFGrantExtractorAlgorithm extends SQLExecutionAlgorithm {
	private static String[] awardExtractionFieldsInAwardTable = {
		NSFConstants.AWARD_TITLE_FIELD,
		NSFConstants.AWARD_START_DATE,
		NSFConstants.AWARD_EXPIRATION_DATE_FIELD
	};
	
    public NSFGrantExtractorAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        super(data, parameters, context);
    }
    
    public String formSQL() throws SQLFormationException {
    	String sql = "SELECT " +
    		implodeStringArray(awardExtractionFieldsInAwardTable, ",") + " FROM " +
    		NSFConstants.AWARD_TABLE;
    	
    	return sql;
    }
    
    public static String implodeStringArray(String[] stringArray,
    										String separator) {
    	final int stringArrayLength = stringArray.length;
    	StringBuilder workingResultString = new StringBuilder();
    	
    	for (int ii = 0; ii < stringArrayLength; ii++) {
    		workingResultString.append(stringArray[ii]);
    		workingResultString.append(separator);
    	}
    	
    	return workingResultString.toString();
    }
}