package edu.iu.scipolicy.loader.isi.db.converter;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class ISIDatabaseToAnyDatabaseConverterAlgorithmFactory implements AlgorithmFactory {
	public Algorithm createAlgorithm(
			Data[] data, Dictionary parameters, CIShellContext ciShellContext) {
		return new ISIDatabaseToAnyDatabaseConverterAlgorithm(data, parameters, ciShellContext);
	}
}