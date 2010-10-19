package edu.iu.epic.modelbuilder.gui;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;

public class ModelBuilderImportModelAlgorithmFactory extends ModelBuilderGUIAlgorithmFactory {
	@Override
	public Algorithm createAlgorithm(Data[] data, Dictionary<String, Object> parameters,
			CIShellContext context) {
		return new ModelBuilderGUIAlgorithm(data, context);
	}
}
