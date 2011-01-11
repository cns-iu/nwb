package edu.iu.sci2.filtering.topnpercent;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.sci2.filtering.topncommon.TopNUtilities;

public class TopNPercentFactory implements AlgorithmFactory, ParameterMutator {
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		Data inputData = data[0];
		Integer topNPercentInteger = (Integer) parameters.get(TopNUtilities.TOP_N_PERCENT_ID);
		float topNPercent = ((float) topNPercentInteger.intValue() / 100.0f);
		String columnToSortBy = ((String) parameters.get(TopNUtilities.COLUMN_TO_SORT_BY_ID));
		boolean isDescending =
			((Boolean) parameters.get(TopNUtilities.IS_DESCENDING_ID)).booleanValue();

		return new TopNPercentAlgorithm(inputData, topNPercent, columnToSortBy, isDescending);
	}

	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {
		return TopNUtilities.mutateParameters(data, parameters);
	}
}