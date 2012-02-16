package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class StringAggregator implements SingleFunctionAggregator<String> {
	
	private String textDelimiter;

	public StringAggregator(String textDelimiter) {
		this.textDelimiter = textDelimiter;
	}

	public String aggregateValue(List<String> objectsToAggregate) {

		StringBuffer currentAggregatedText = new StringBuffer();
		
		for (String currentCellText : objectsToAggregate) {
			currentAggregatedText.append(currentCellText);
			currentAggregatedText.append(textDelimiter);
		} 
		
		return currentAggregatedText.toString();
	}
}
