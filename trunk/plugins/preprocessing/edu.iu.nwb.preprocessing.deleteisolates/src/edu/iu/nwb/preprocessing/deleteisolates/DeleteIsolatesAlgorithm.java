package edu.iu.nwb.preprocessing.deleteisolates;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Set;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;

import edu.iu.nwb.preprocessing.deleteisolates.exceptions.NonIsolateNodeFindingException;
import edu.iu.nwb.preprocessing.deleteisolates.exceptions.IsolateNodeStrippingException;
import edu.iu.nwb.preprocessing.deleteisolates.nwbfileparserhandlers.NodeIDFilteringNWBWriter;
import edu.iu.nwb.preprocessing.deleteisolates.nwbfileparserhandlers.NonIsolateFinder;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class DeleteIsolatesAlgorithm implements Algorithm {
	public static final String OUTPUT_DATA_LABEL = "With isolates removed";

	private Data inData;
	private LogService logService;
	
	
	public static class Factory implements AlgorithmFactory {
	    public Algorithm createAlgorithm(
	    		Data[] data, Dictionary parameters, CIShellContext context) {
	        return new DeleteIsolatesAlgorithm(data, parameters, context);
	    }
	}	
    public DeleteIsolatesAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext context) {
    	this.inData = data[0];
        this.logService =
        	(LogService) context.getService(LogService.class.getName());
    }

    
    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		File inputNWBFile = (File) inData.getData();
    		
    		Set nonIsolateNodeIDs = findNonIsolateNodes(inputNWBFile);
    		File isolateFreeNWBFile =
    			copyNWBFileWithoutIsolates(inputNWBFile, nonIsolateNodeIDs);
    		
    		return createOutputData(isolateFreeNWBFile);
    	} catch (NonIsolateNodeFindingException nonIsolateNodeFindingException) {
    		throw new AlgorithmExecutionException(nonIsolateNodeFindingException);
    	} catch (IsolateNodeStrippingException isolateNodeStrippingException) {
    		throw new AlgorithmExecutionException(isolateNodeStrippingException);
    	}
    }
    
    private Set findNonIsolateNodes(File inputNWBFile)
    		throws NonIsolateNodeFindingException {
    	try {
    		NonIsolateFinder nonIsolateFinder = new NonIsolateFinder();
    		NWBFileParser nonIsolateFinderFileParser =
    			new NWBFileParser(inputNWBFile);
    		nonIsolateFinderFileParser.parse(nonIsolateFinder);
    		
    		return nonIsolateFinder.getNonIsolateNodeIDs();
    	} catch (IOException ioException) {
    		throw new NonIsolateNodeFindingException(ioException);
    	} catch (ParsingException parsingException) {
    		throw new NonIsolateNodeFindingException(parsingException);
    	}
    }
    
    private File copyNWBFileWithoutIsolates(File inputNWBFile,
    										Set nonIsolateNodeIDs)
    		throws IsolateNodeStrippingException {
    	try {
    		File outputNWBFile = FileUtilities.
				createTemporaryFileInDefaultTemporaryDirectory("DeleteIsolates-", "nwb");
    		NodeIDFilteringNWBWriter nodeIDFilteringNWBWriter =
    			new NodeIDFilteringNWBWriter(nonIsolateNodeIDs, outputNWBFile);
    		nodeIDFilteringNWBWriter.setNodeCount(nonIsolateNodeIDs.size());
    		NWBFileParser isolateStripperFileParser =
    			new NWBFileParser(inputNWBFile);
    		isolateStripperFileParser.parse(nodeIDFilteringNWBWriter);
    		
    		logNumberOfDeletedIsolates(nodeIDFilteringNWBWriter.getNumberFiltered());
    		
    		return outputNWBFile;
    	} catch (IOException ioException) {
    		throw new IsolateNodeStrippingException(ioException);
    	} catch (ParsingException parsingException) {
    		throw new IsolateNodeStrippingException(parsingException);
    	}
    }

	private void logNumberOfDeletedIsolates(int numberOfDeletedIsolates) {
		String pluralizedNodeString;    		
		if (numberOfDeletedIsolates == 1) {
			pluralizedNodeString = " isolate node.";
		} else {
			pluralizedNodeString = " isolate nodes.";
		}
		
		logService.log(LogService.LOG_INFO,
							"Removed " +
								numberOfDeletedIsolates +
								pluralizedNodeString);
	}
    
    private Data[] createOutputData(File outputNWBFile) {
    	Data outputFileData =
    		new BasicData(outputNWBFile, NWBFileProperty.NWB_MIME_TYPE);
    	Dictionary outputFileMetadata = outputFileData.getMetadata();
    	outputFileMetadata.put(DataProperty.LABEL, OUTPUT_DATA_LABEL);
    	outputFileMetadata.put(DataProperty.PARENT, inData);
    	outputFileMetadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
    	
    	return new Data[]{ outputFileData };
    }
}