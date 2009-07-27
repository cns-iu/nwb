package edu.iu.nwb.analysis.blondelcommunitydetection.algorithmstages;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

import edu.iu.nwb.analysis.blondelcommunitydetection.Utilities;
import edu.iu.nwb.analysis.blondelcommunitydetection.algorithmstages.exceptiontypes.CommunityDetectionRunnerException;

// This is the second stage of the Blondel Community Detection algorithm.
// The input to this stage is the output of the first stage, which is a BIN
// file.
// The input BIN file is passed into the
// edu.iu.nwb.shared.blondelexecutable static executable algorithm,
// which in turn executes the C++-compiled "community" executable on the input
// BIN file.  The "community" executable produces a tree file, which is the
// output of this stage.
public class CommunityDetectionRunner {
	private AlgorithmFactory blondelCommunityDetectionAlgorithmFactory;
	private Dictionary parameters;
	private CIShellContext ciShellContext;
	
	public CommunityDetectionRunner(
			AlgorithmFactory blondelCommunityDetectionAlgorithmFactory,
			Dictionary parameters,
			CIShellContext ciShellContext) {
		this.blondelCommunityDetectionAlgorithmFactory =
			blondelCommunityDetectionAlgorithmFactory;
		this.parameters = parameters;
		this.ciShellContext = ciShellContext;
	}
	
	//TODO: Don't bother with inputData
	public File runCommunityDetection(File inputBINFile, Data inputData)
			throws CommunityDetectionRunnerException {
		Data[] communityDetectionData =
    		Utilities.wrapFileAsOutputData(
    			inputBINFile, "file:text/bin", inputData);
		
    	Algorithm communityDetectionAlgorithm =
    		this.blondelCommunityDetectionAlgorithmFactory.createAlgorithm(
    			communityDetectionData, this.parameters, this.ciShellContext);
    	
    	try {
    		Data[] executionResultData = communityDetectionAlgorithm.execute();
    		// TODO: Verify the output here?
    		File treeFile = (File)executionResultData[0].getData();
    	
    		return treeFile;
    	} catch (AlgorithmExecutionException algorithmExecutionException) {
    		throw new CommunityDetectionRunnerException(
    			algorithmExecutionException);
    	}
	}
}