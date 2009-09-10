package edu.iu.nwb.analysis.deleteisolates;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;

import edu.iu.nwb.analysis.deleteisolates.exceptiontypes.FindNonIsolateNodesException;
import edu.iu.nwb.analysis.deleteisolates.exceptiontypes.StripIsolateNodesException;
import edu.iu.nwb.analysis.deleteisolates.nwbfileparserhandlers.IsolateStripper;
import edu.iu.nwb.analysis.deleteisolates.nwbfileparserhandlers.NonIsolateFinder;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class DeleteIsolatesAlgorithm implements Algorithm {
	public static final String OUTPUT_DATA_LABEL="With isolates removed";
	public static final String FIND_NON_ISOLATE_NODES_EXCEPTION_MESSAGE =
		"Failed to read NWB file that non-isolate nodes are being found in.";
	public static final String STRIP_ISOLATE_NODES_EXCEPTION_MESSAGE =
		"Failed to read NWB file that is being stripped of isolates.";
	
	private Data inputData;
	private LogService logService;
    
    public DeleteIsolatesAlgorithm(Data[] data,
    				  Dictionary parameters,
    				  CIShellContext ciShellContext) {
        this.inputData = data[0];
        this.logService =
        	(LogService)ciShellContext.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	File inputNWBFile = (File)this.inputData.getData();
    	
    	/*
    	 * TODO Maybe switch from a unified two-step process to two
    	 *  distinct passes?
    	 * 1: Remove isolates (parser finds isolated node ids, handler to write
    	 *  out that file).
    	 * 2: Normalize node IDs (this could even be an NWB utility).
    	 */
    	
    	try {
    		/* These variable and method names do not fully describe their
    		 * functionality.  They both remove isolates (the stated purpose)
    		 * and normalize node IDs (a hidden purpose).
    		 */
    		Map nonIsolateNodes = findNonIsolateNodes(inputNWBFile);
    		File isolateFreeNWBFile =
    			copyNWBFileWithoutIsolates(inputNWBFile, nonIsolateNodes);
    		
    		return createOutputData(isolateFreeNWBFile);
    	} catch (FindNonIsolateNodesException findNonIsolateNodesException) {
    		throw new AlgorithmExecutionException(
    				findNonIsolateNodesException.getMessage(),
    				findNonIsolateNodesException);
    	} catch (StripIsolateNodesException stripIsolateNodesException) {
    		throw new AlgorithmExecutionException(
    				stripIsolateNodesException.getMessage(),
    				stripIsolateNodesException);
    	}
    }
    
    private Map findNonIsolateNodes(File inputNWBFile)
    		throws FindNonIsolateNodesException {
    	try {
    		NonIsolateFinder nonIsolateFinder = new NonIsolateFinder();
    		NWBFileParser nonIsolateFinderFileParser =
    			new NWBFileParser(inputNWBFile);
    		nonIsolateFinderFileParser.parse(nonIsolateFinder);
    		
    		return nonIsolateFinder.getNodeIDsFound();
    	} catch (IOException ioException) {
    		throw new FindNonIsolateNodesException(
    			FIND_NON_ISOLATE_NODES_EXCEPTION_MESSAGE,
    			ioException);
    	} catch (ParsingException parsingException) {
    		throw new FindNonIsolateNodesException(
    			FIND_NON_ISOLATE_NODES_EXCEPTION_MESSAGE,
    			parsingException);
    	}
    }
    
    private File copyNWBFileWithoutIsolates(File inputNWBFile,
    										Map nonIsolateNodes)
    		throws StripIsolateNodesException {
    	try {
    		File outputNWBFile = FileUtilities.
				createTemporaryFileInDefaultTemporaryDirectory("NWB-", "nwb");
    		IsolateStripper isolateStripper =
    			new IsolateStripper(nonIsolateNodes, outputNWBFile);
    		NWBFileParser isolateStripperFileParser =
    			new NWBFileParser(inputNWBFile);
    		isolateStripperFileParser.parse(isolateStripper);
    		
    		int isolateNodeCount = isolateStripper.getIsolateNodeCount();
    		String pluralizedNodeString;
    		
    		if (isolateNodeCount != 1) {
    			pluralizedNodeString = " isolate nodes.";
    		} else {
    			pluralizedNodeString = " isolate node.";
    		}
    		
    		this.logService.log(LogService.LOG_INFO,
    							"Removed " +
    								isolateStripper.getIsolateNodeCount() +
    								pluralizedNodeString);
    		
    		return outputNWBFile;
    	} catch (IOException ioException) {
    		throw new StripIsolateNodesException(
    			STRIP_ISOLATE_NODES_EXCEPTION_MESSAGE,
    			ioException);
    	} catch (ParsingException parsingException) {
    		throw new StripIsolateNodesException(
    			STRIP_ISOLATE_NODES_EXCEPTION_MESSAGE,
    			parsingException);
    	}
    }
    
    private Data[] createOutputData(File outputNWBFile) {
    	Data outputFileData =
    		new BasicData(outputNWBFile, this.inputData.getFormat());
    	Dictionary outputFileMetadata = outputFileData.getMetadata();
    	outputFileMetadata.put(DataProperty.LABEL, OUTPUT_DATA_LABEL);
    	outputFileMetadata.put(DataProperty.PARENT, this.inputData);
    	outputFileMetadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
    	
    	return new Data[] { outputFileData };
    }
}