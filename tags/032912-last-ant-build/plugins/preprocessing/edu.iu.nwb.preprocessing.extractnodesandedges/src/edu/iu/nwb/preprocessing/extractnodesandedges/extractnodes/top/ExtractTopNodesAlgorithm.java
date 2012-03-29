package edu.iu.nwb.preprocessing.extractnodesandedges.extractnodes.top;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import com.google.common.collect.Ordering;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;
import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.nwb.util.nwbfile.model.Node;
import edu.iu.nwb.util.nwbfile.pipe.ParserPipe;
import edu.iu.nwb.util.nwbfile.pipe.ParserStage;

public class ExtractTopNodesAlgorithm implements Algorithm {
    private Data[] data;
    private int numTopNodes;
    private boolean fromBottomInstead;
    private String numericAttribute;

    private boolean noParams = false;
    
	public ExtractTopNodesAlgorithm(Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
        this.data = data;
        
        //if parameter values are not defined...
        if (parameters.get("numericAttribute") == null) {
        	//skip initialization and prepare to not execute
        	LogService logger = (LogService) context.getService(LogService.class.toString());
        	logger.log(LogService.LOG_WARNING, this.getClass().toString() + " called with empty parameter list");
        	noParams = true;
        	return; 
        }
        
        this.numTopNodes = ((Integer) parameters.get("numTopNodes")).intValue();
        this.fromBottomInstead = ((Boolean) parameters.get("fromBottomInstead")).booleanValue();
        this.numericAttribute = (String) parameters.get("numericAttribute");
    }
	
	
	public Data[] execute() throws AlgorithmExecutionException {
		if (noParams) { return null; }
        try {
        	File inFile = (File) this.data[0].getData();
        	File outFile = NWBFileUtilities.createTemporaryNWBFile();
        	NWBFileParser reader = new NWBFileParser(inFile);
        	Ordering<Node> ordering = ParserPipe.getNaturalOrdering(numericAttribute);
        	if (! this.fromBottomInstead) {
        		ordering = ordering.reverse();
        	}
        	ParserStage handler = ParserPipe.create()
        			.requireNodeAttribute(numericAttribute)
        			.keepMinimumNodes(this.numTopNodes, ordering)
        			.outputTo(new NWBFileWriter(outFile));
        	reader.parse(handler);
        	return createOutputData(outFile);
        } catch (IOException e) {
        	throw new AlgorithmExecutionException(e);
        } catch (ParsingException e) {
        	throw new AlgorithmExecutionException(e);
        }
	}
	
    private Data[] createOutputData(File outputNWBFile) {
    	String label = String.format(
    			"%s %d nodes by %s", 
    				this.fromBottomInstead ? "Bottom" : "Top",
    				this.numTopNodes,
    				this.numericAttribute);
    	Data outputFileData =
    		new BasicData(outputNWBFile, NWBFileProperty.NWB_MIME_TYPE);
    	Dictionary<String,Object> outputFileMetadata = outputFileData.getMetadata();
    	outputFileMetadata.put(DataProperty.LABEL, label);
    	outputFileMetadata.put(DataProperty.PARENT, this.data[0]);
    	outputFileMetadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
    	
    	return new Data[]{ outputFileData };
    }

}
