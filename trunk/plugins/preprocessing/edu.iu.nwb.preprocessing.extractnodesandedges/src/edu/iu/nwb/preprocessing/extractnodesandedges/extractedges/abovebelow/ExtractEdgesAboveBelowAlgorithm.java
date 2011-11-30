package edu.iu.nwb.preprocessing.extractnodesandedges.extractedges.abovebelow;

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

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.nwb.util.nwbfile.model.AttributePredicate;
import edu.iu.nwb.util.nwbfile.model.AttributePredicates;
import edu.iu.nwb.util.nwbfile.pipe.ParserPipe;
import edu.iu.nwb.util.nwbfile.pipe.ParserStage;

public class ExtractEdgesAboveBelowAlgorithm implements Algorithm {
    private Data[] data;
    private double limit;
    private boolean fromBottomInstead;
    private String numericAttribute;

    private boolean noParams = false;
    
	public ExtractEdgesAboveBelowAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
        this.data = data;
        
        //if parameter values are not defined...
        if (parameters.get("numericAttribute") == null) {
        	//skip initialization and prepare to not execute
        	LogService logger = (LogService) context.getService(LogService.class.toString());
        	logger.log(LogService.LOG_WARNING, this.getClass().toString() + " called with empty parameter list");
        	noParams = true;
        	return; 
        }
        
        this.limit = ((Number) parameters.get("fromThisNum")).doubleValue();
        this.fromBottomInstead = ((Boolean) parameters.get("belowInstead")).booleanValue();
        this.numericAttribute = (String) parameters.get("numericAttribute");
    }
	
	
	public Data[] execute() throws AlgorithmExecutionException {
		if (noParams) { return null; }
        try {
        	File inFile = (File) this.data[0].getData();
        	File outFile = NWBFileUtilities.createTemporaryNWBFile();
        	NWBFileParser reader = new NWBFileParser(inFile);
        	AttributePredicate filter;
        	if (this.fromBottomInstead) {
        		filter = AttributePredicates.keepBelow(this.numericAttribute, this.limit);
        	} else {
        		filter = AttributePredicates.keepAbove(this.numericAttribute, this.limit);
        	}
        	ParserStage handler = ParserPipe.create()
        			.requireEdgeAttribute(numericAttribute)
        			.filterEdges(filter)
        			.outputToFile(outFile);
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
    			"Edges %s %f by %s", 
    				this.fromBottomInstead ? "below" : "above",
    				this.limit,
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
