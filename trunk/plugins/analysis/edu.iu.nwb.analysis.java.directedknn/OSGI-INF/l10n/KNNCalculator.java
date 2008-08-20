package edu.iu.nwb.analysis.java.directedknn.components;

import java.io.File;

import org.cishell.framework.algorithm.ProgressMonitor;

public class KNNCalculator {

	private ProgressMonitor progMonitor;
	private int progress = 0;
	
	public KNNCalculator(ProgressMonitor pm){
		this.progMonitor = pm;
	}
	
	public File[] calculateKNN(final Graph originalGraph){
		
	}
	
}
