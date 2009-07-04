package edu.iu.scipolicy.analysis.blondelcommunitydetection;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;

import edu.iu.nwb.util.nwbfile.NWBFileParser;

public class BlondelCommunityDetectionAlgorithm implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    
    public BlondelCommunityDetectionAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() throws AlgorithmExecutionException {
        return null;
    }
    
    public static void main(String[] args) {
    	try {
    		File nwbFile = new File("C:\\Documents and Settings\\pataphil\\Desktop\\testNetwork.nwb");
    		File testBinFile = new File("C:\\Documents and Settings\\pataphil\\Desktop\\test.bin");
    		FileOutputStream testBINFileOutputStream =
    			new FileOutputStream(testBinFile);
    		NWBToBINPreProcessor preProcessor =
    			new NWBToBINPreProcessor("", false);
    			// new NWBToBINPreProcessor(testBINFileOutputStream, "", false);
    		
    		NWBFileParser fileParser = new NWBFileParser(nwbFile);
    		fileParser.parse(preProcessor);
    		
    		nwbFile = new File("C:\\Documents and Settings\\pataphil\\Desktop\\testNetwork.nwb");
    		fileParser = new NWBFileParser(nwbFile);
    		NWBToBINConverter converter = new NWBToBINConverter(testBinFile);
    		fileParser.parse(converter);
    	}
    	catch (Exception exception) {
    		exception.printStackTrace();
    	}
    }
}