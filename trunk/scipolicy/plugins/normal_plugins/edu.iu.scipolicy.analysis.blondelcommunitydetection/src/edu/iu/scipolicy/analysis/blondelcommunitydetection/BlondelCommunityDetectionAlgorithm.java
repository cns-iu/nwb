package edu.iu.scipolicy.analysis.blondelcommunitydetection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.scipolicy.utilities.FileUtilities;

public class BlondelCommunityDetectionAlgorithm implements Algorithm {
	private AlgorithmFactory blondelCommunityDetectionAlgorithmFactory;
	private Data[] data;
	private Dictionary parameters;
	private CIShellContext context;
    
    public BlondelCommunityDetectionAlgorithm(
    		AlgorithmFactory blondelCommunityDetectionAlgorithmFactory,
    		Data[] data,
    		Dictionary parameters,
    		CIShellContext context) {
    	this.blondelCommunityDetectionAlgorithmFactory =
    		blondelCommunityDetectionAlgorithmFactory;
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	Data inputData = this.data[0];
    	File inputNWBFile = (File)inputData.getData();
    	Dictionary inputMetaData = inputData.getMetadata();
    	System.err.println("inputMetaData: " + inputData.getFormat());
    	
    	Node.reset();
    	
    	File binFile = this.processConversion(inputNWBFile);
    	File communityTreeFile =
    		this.runCommunityDetection(binFile, inputData);
    	File outputNWBFile =
    		this.mergeCommunitiesFileWithNWBFile(communityTreeFile,
    											 inputNWBFile);
    	
        Data[] outData = wrapFileAsOutputData(outputNWBFile,
        									  "file:text/nwb",
        									  inputData);
        
        return outData;
    }
    
    public static void main(String[] args) {
    	try {
    		Node.reset();
    		File nwbFile = new File("C:\\Documents and Settings\\pataphil\\Desktop\\testNetwork2.nwb");
    		File testBinFile = new File("C:\\Documents and Settings\\pataphil\\Desktop\\test2.bin");
    		NWBToBINPreProcessor preProcessor =
    			new NWBToBINPreProcessor();
    		
    		NWBFileParser fileParser = new NWBFileParser(nwbFile);
    		fileParser.parse(preProcessor);
    		
    		nwbFile = new File("C:\\Documents and Settings\\pataphil\\Desktop\\testNetwork2.nwb");
    		fileParser = new NWBFileParser(nwbFile);
    		NWBToBINConverter converter =
    			new NWBToBINConverter(Node.getNodes(),
    								  testBinFile,
    								  "",
    								  false);
    		fileParser.parse(converter);
    	}
    	catch (Exception exception) {
    		exception.printStackTrace();
    	}
    }
    
    private Data[] wrapFileAsOutputData(File outputFile,
    									String mimeType,
    									Data inputData) {
    	Data outputFileData = new BasicData(outputFile, mimeType);
    	Dictionary outputFileMetaData = outputFileData.getMetadata();
    	outputFileMetaData.put(DataProperty.LABEL,
    						   "With community attributes");
    	outputFileMetaData.put(DataProperty.PARENT, inputData);
    	outputFileMetaData.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
    	
    	return new Data[] { outputFileData };
    }
    
    private File processConversion(File nwbFile)
    		throws AlgorithmExecutionException {
    	this.preProcessNWBFile(nwbFile);
    	
    	return convertNWBFileToBINFile(nwbFile);
    }
    
    private File runCommunityDetection(File binFile, Data inputData)
    		throws AlgorithmExecutionException {
    	Data[] communityDetectionData =
    		this.wrapFileAsOutputData(binFile, "file:text/bin", inputData);
    	Algorithm communityDetectionAlgorithm =
    		this.blondelCommunityDetectionAlgorithmFactory.createAlgorithm(
    			communityDetectionData, this.parameters, this.context);
    	Data[] executionResultData = communityDetectionAlgorithm.execute();
    	// TODO: Verify the output here?
    	File treeFile = (File)executionResultData[0].getData();
    	
    	return treeFile;
    }
    
    private File mergeCommunitiesFileWithNWBFile(File communitiesFile,
    											 File nwbFile)
    		throws AlgorithmExecutionException {
    	try {
    		File outputNWBFile =
    			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory("blondel-nwb-", "nwb");
    		NWBAndTreeFilesMerger merger = new NWBAndTreeFilesMerger(
    			communitiesFile, outputNWBFile, Node.getNodes());
    		
    		NWBFileParser fileParser = new NWBFileParser(nwbFile);
    		fileParser.parse(merger);
    		
    		return outputNWBFile;
    		
    		// TODO: hadIssue
    	}
    	catch (FileNotFoundException fileNotFoundException) {
    		throw new AlgorithmExecutionException(fileNotFoundException);
    	}
    	catch (IOException ioException) {
    		throw new AlgorithmExecutionException(ioException);
    	}
    	catch (ParsingException parsingException) {
    		throw new AlgorithmExecutionException(parsingException);
    	}
    	catch (TreeFileParsingException treeFileParsingException) {
    		throw new AlgorithmExecutionException(treeFileParsingException);
    	}
    }
    
    private void preProcessNWBFile(File nwbFile)
    		throws AlgorithmExecutionException {
    	NWBToBINPreProcessor preProcessor = new NWBToBINPreProcessor();
    	NWBFileParser preProcessorFileParser;
    	
    	try {
    		preProcessorFileParser = new NWBFileParser(nwbFile);
    		preProcessorFileParser.parse(preProcessor);
    	}
    	catch (IOException ioException) {
    		throw new AlgorithmExecutionException(
    			"Failed to read NWB file that is being preprocessed for " +
    				"conversion to BIN file.",
    			ioException);
    	}
    	catch (ParsingException parsingException) {
    		throw new AlgorithmExecutionException(
    			"Failed to parse NWB file that is " +
    				"being preprocessed for conversion to BIN file.",
    			parsingException);
    	}
    }
    
    private File convertNWBFileToBINFile(File nwbFile)
    		throws AlgorithmExecutionException {
    	File outputBINFile;
    	
    	try {
    		outputBINFile = FileUtilities.
    			createTemporaryFileInDefaultTemporaryDirectory("TEMP-BLONDEL",
    														   "bin");
    	}
    	catch (IOException ioException) {
    		throw new AlgorithmExecutionException(
    			"Failed to create temporary BIN file.", ioException);
    	}
    	
    	// TODO: Get the second (weightAttribute) and third (isWeighted) values
    	// from input parameters.
    	NWBToBINConverter converter = new NWBToBINConverter(Node.getNodes(),
    														outputBINFile,
    														"",
    														false);
    	NWBFileParser converterFileParser;
    	
    	try {
    		converterFileParser = new NWBFileParser(nwbFile);
    		converterFileParser.parse(converter);
    	}
    	catch (IOException ioException) {
    		throw new AlgorithmExecutionException(
    			"Failed to read NWB file that is being converted to BIN file.",
    			ioException);
    	}
    	catch (ParsingException parsingException) {
    		throw new AlgorithmExecutionException(
    			"Failed to parse NWB file that is " +
    				"being converted to BIN file.",
    			parsingException);
    	}
    	
    	return outputBINFile;
    }
}