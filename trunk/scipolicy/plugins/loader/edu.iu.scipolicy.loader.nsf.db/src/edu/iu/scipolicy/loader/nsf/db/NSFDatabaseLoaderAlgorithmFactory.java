package edu.iu.scipolicy.loader.nsf.db;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class NSFDatabaseLoaderAlgorithmFactory implements AlgorithmFactory {
	
	@SuppressWarnings("unchecked")
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {

		return new NSFDatabaseLoaderAlgorithm(data, 
											  parameters, 
											  context);
	}

}