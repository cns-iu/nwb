package edu.iu.nwb.converter.prefusebibtex;

import java.util.Iterator;
import java.util.List;

import org.osgi.service.log.LogService;

import bibtex.dom.BibtexAbstractValue;
import bibtex.dom.BibtexConcatenatedValue;
import bibtex.dom.BibtexMultipleValues;
import bibtex.dom.BibtexString;
/*
 * BibtexAbstractValue :: BibtexString | BibtexConcatenatedValue | BibtexMultipleValues
 * BibtexString :: String
 * BibtexConcatenatedValue :: BibtexAbstractValue + BibtexAbstractValue
 * BibtexMultipleValues :: [BibtexAbstractValue + Multi_Valued_Sep_Char]* //(no sep char on last instance)
 * Multi_Valued_Sep_Char = ","
 */

public class BibtexValueFormatter {
	private static final String MULTI_VALUED_SEP_CHAR = ",";
	
	private LogService log;
	public BibtexValueFormatter(LogService log) {
		this.log = log;
	}
	
	public String formatFieldValue(BibtexAbstractValue value) {
    	if (value instanceof BibtexString) {
    		return formatFieldValue((BibtexString) value);
    	} else if (value instanceof BibtexConcatenatedValue) {
			return formatFieldValue((BibtexConcatenatedValue) value);
		} else if (value instanceof BibtexMultipleValues) {
			return formatFieldValue((BibtexMultipleValues) value) ;
		} else {
			this.log.log(LogService.LOG_WARNING, "Unexpected bibtex field value " + 
					value.toString() + " of type " + value.getClass().getName() + ". Parsing contents in a generic fashion.");
			return value.toString();		
		}
	}
	
	private String formatFieldValue(BibtexConcatenatedValue value) {
		BibtexAbstractValue left = value.getLeft();
		BibtexAbstractValue right = value.getRight();
		return formatFieldValue(left) + formatFieldValue(right);
	}
	
	private String formatFieldValue(BibtexMultipleValues value) {
		StringBuilder parsedValue = new StringBuilder();
		List values = value.getValues();
		for (Iterator valuesIt = values.iterator(); valuesIt.hasNext();) {
			parsedValue.append(formatFieldValue((BibtexAbstractValue) valuesIt.next()));
			if (valuesIt.hasNext()) {
				parsedValue.append(MULTI_VALUED_SEP_CHAR);
			}
		}
		return parsedValue.toString();
	}
	
	private String formatFieldValue(BibtexString value) {
		String content = value.getContent();
		return content;
	}
}
