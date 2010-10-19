package edu.iu.epic.modelbuilder.gui;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;

public class ModelBuilderNewModelAlgorithmFactory extends ModelBuilderGUIAlgorithmFactory {

	@Override
	public Algorithm createAlgorithm(Data[] data, Dictionary<String, Object> parameters,
			CIShellContext context) {
		System.out.println("new model builder algorithm factory was selected");
		return new ModelBuilderGUIAlgorithm(data, context);
	}

}
