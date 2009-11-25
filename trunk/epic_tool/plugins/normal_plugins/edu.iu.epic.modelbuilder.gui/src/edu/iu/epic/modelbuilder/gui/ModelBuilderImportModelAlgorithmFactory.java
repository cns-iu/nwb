package edu.iu.epic.modelbuilder.gui;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;

public class ModelBuilderImportModelAlgorithmFactory extends ModelBuilderGUIAlgorithmFactory {

	@Override
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		System.out.println("import model builder algorithm factory was selected");
		return new ModelBuilderGUIAlgorithm(data, context);
	}

}
