/*package edu.iu.nwb.analysis.java.directedknn.components;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;

public class DirectedKNNCalculator {
	
	private ProgressMonitor progMonitor;
	private int progress = 0;
	
	public DirectedKNNCalculator(ProgressMonitor pm){
		this.progMonitor = pm;
	}
	
	public File[] calculateKNN(final Graph originalGraph)throws AlgorithmExecutionException{
		File[] knnFileArray = new File[5];
		int nodeCount = originalGraph.getNodeCount();
		double[] knn_in_in = new double[nodeCount];
		double[] knn_in_out = new double[nodeCount];
		double[] knn_out_in = new double[nodeCount];
		double[] knn_out_out = new double[nodeCount];
		
		
		
		
		return knnFileArray;
	}
	
	public static File createTempFile() throws AlgorithmExecutionException{
		try{
			File tempFile = File.createTempFile(new Long(new Date().getTime()).toString(), ".txt");
			return tempFile;
		}catch(IOException ioe){
			throw new AlgorithmExecutionException("Error creating the files for degree-degree correlations.");
		}
	}

}
*/