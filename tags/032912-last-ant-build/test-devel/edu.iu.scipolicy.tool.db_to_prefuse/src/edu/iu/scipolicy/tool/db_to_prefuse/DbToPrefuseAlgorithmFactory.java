package edu.iu.scipolicy.tool.db_to_prefuse;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class DbToPrefuseAlgorithmFactory implements AlgorithmFactory {
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		return new DbToPrefuseAlgorithm(data, parameters, context);
	}
}