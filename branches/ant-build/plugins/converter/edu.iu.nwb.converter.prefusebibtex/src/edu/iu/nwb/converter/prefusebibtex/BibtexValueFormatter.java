package edu.iu.nwb.converter.prefusebibtex;

import java.util.Iterator;
import java.util.List;

import org.osgi.service.log.LogService;

import bibtex.dom.BibtexAbstractValue;
import bibtex.dom.BibtexConcatenatedValue;
import bibtex.dom.BibtexMacroReference;
import bibtex.dom.BibtexMultipleValues;
import bibtex.dom.BibtexString;

public class BibtexValueFormatter {
	private static final String MULTI_VALUED_SEP_CHAR = ",";	
	private static final boolean CLEANING_ENABLED = true;

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
		} else if (value instanceof BibtexMacroReference){
			return formatFieldValue((BibtexMacroReference) value);
		} else {
			this.log.log(LogService.LOG_WARNING,
					"Unexpected bibtex field value "
					+ value.toString() + " of type " + value.getClass().getName()
					+ ". Parsing contents in a generic fashion.");
			return value.toString();		
		}
	}
	
	private String formatFieldValue(BibtexConcatenatedValue value) {
		BibtexAbstractValue left = value.getLeft();
		BibtexAbstractValue right = value.getRight();
		
		return formatFieldValue(left) + formatFieldValue(right);
	}
	
	private String formatFieldValue(BibtexMultipleValues value) {
		StringBuffer parsedValue = new StringBuffer();
		List values = value.getValues();
		for (Iterator valuesIt = values.iterator(); valuesIt.hasNext();) {
			parsedValue.append(
					formatFieldValue((BibtexAbstractValue) valuesIt.next()));
			
			if (valuesIt.hasNext()) {
				parsedValue.append(MULTI_VALUED_SEP_CHAR);
			}
		}
		
		return parsedValue.toString();
	}
	
	private String formatFieldValue(BibtexString value) {
		String content = value.getContent();
		
		if (CLEANING_ENABLED) {
			content = cleanLatexString(content);
		}
		
		return content;
	}
	
	private String formatFieldValue(BibtexMacroReference value) {
		return value.getKey();
	}
	
	
	public String cleanLatexString(String s) {
		String cleanedString;
		if ((s.startsWith("{") && s.endsWith("}"))
				|| (s.startsWith("\"") && s.endsWith("\"")) ) {
			// Remove wrapping characters from either end.
			cleanedString = s.substring(1, s.length() - 1);
		} else {
			cleanedString = s;
		}
		
		return cleanedString;
	}
}
