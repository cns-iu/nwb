package edu.iu.nwb.analysis.blondelcommunitydetection;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

import edu.iu.nwb.analysis.blondelcommunitydetection.algorithmstages.CommunityDetectionRunner;
import edu.iu.nwb.analysis.blondelcommunitydetection.algorithmstages.NWBAndTreeFilesMerger;
import edu.iu.nwb.analysis.blondelcommunitydetection.algorithmstages.NWBToBINConverter;

public class BlondelCommunityDetectionAlgorithm implements Algorithm {
	public static final String WEIGHT_FIELD_ID = "weight";
	public static final String IS_WEIGHTED_FIELD_ID = "isweighted";
	public static final String NO_EDGE_WEIGHT_VALUE = "unweighted";
	
	private AlgorithmFactory blondelCommunityDetectionAlgorithmFactory;
	
	private Data inputData;
	private File inputNWBFile;
	
	private String weightAttribute;
	private boolean isWeighted;
	
	private Dictionary<String, Object> parameters;
	private CIShellContext context;

    public BlondelCommunityDetectionAlgorithm(
    		AlgorithmFactory blondelCommunityDetectionAlgorithmFactory,
    		Data[] data,
    		Dictionary<String, Object> parameters,
    		CIShellContext context) {
    	this.blondelCommunityDetectionAlgorithmFactory =
    		blondelCommunityDetectionAlgorithmFactory;
    	
        this.inputData = data[0];
        this.inputNWBFile = (File)inputData.getData();
        this.weightAttribute = parameters.get(WEIGHT_FIELD_ID).toString();
        
        if (this.weightAttribute.equals(NO_EDGE_WEIGHT_VALUE)) {
        	this.isWeighted = false;
        } else {
        	this.isWeighted = true;
        }
        
        this.parameters = parameters;
        this.context = context;
    }

    /*
     * The C++ implementation of Blondel's community detection algorithm expects the input to be a
     *  BIN network file, and it outputs a tree hierarchy file that contains the generated
     *  community structures.  This algorithm first converts the input NWB to a BIN file and passes
     *  the result into the compiled C++ community detection program.  This algorithm then merges
     *  the resulting tree file with the input NWB file, producing a new NWB with nodes that are
     *  annotated with the appropriate community attributes.
     */
    public Data[] execute() throws AlgorithmExecutionException {
    	NetworkInfo networkInfo = new NetworkInfo();
    	
    	// Convert the NWB file to a BIN file.
    	
    	File binFile = NWBToBINConverter.convertNWBFileToBINFile(
    		this.inputNWBFile, networkInfo, this.weightAttribute, this.isWeighted);
    	
    	/*
    	 * Run community detection on the BIN file,
    	 *  producing a TREE file with community-annotation.
    	 */
    	
    	CommunityDetectionRunner communityDetectionRunner = new CommunityDetectionRunner(
    		this.blondelCommunityDetectionAlgorithmFactory, this.parameters, this.context);
    	
    	File communityTreeFile =
    		communityDetectionRunner.runCommunityDetection(binFile, this.inputData);
    	
    	/*
    	 *  Merge the TREE file with community-annotation and the original
    	 *  NWB file,
    	 *  producing a community-annotated NWB file.
    	 */

    	File outputNWBFile = NWBAndTreeFilesMerger.mergeCommunitiesFileWithNWBFile(
    		communityTreeFile, this.inputNWBFile, networkInfo);
    	
    	// Wrap the community-annotated NWB file in Data[] for output.
    	
        Data[] outData =
        	Utilities.wrapFileAsOutputData(outputNWBFile, "file:text/nwb", this.inputData);
        
        // Return Data[].
        
        return outData;
    }
}
