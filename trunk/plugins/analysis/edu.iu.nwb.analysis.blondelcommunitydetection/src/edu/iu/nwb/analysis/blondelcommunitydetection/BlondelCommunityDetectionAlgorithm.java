package edu.iu.nwb.analysis.blondelcommunitydetection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;

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
	private LogService logger;

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
        
        // grab log for outputting error messages
     	logger = (LogService) context.getService(LogService.class.getName());
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

    	File tempNWBFile = NWBAndTreeFilesMerger.mergeCommunitiesFileWithNWBFile(
    		communityTreeFile, this.inputNWBFile, networkInfo);
    	
    	/*
    	 * For Sci2-1175 JIRA, check and modify the NWB file before outputting it
    	 */
    	File outputNWBFile = removeOuterNodeAttribute(tempNWBFile);
    	
    	// Wrap the community-annotated NWB file in Data[] for output.
    	
        Data[] outData =
        	Utilities.wrapFileAsOutputData(outputNWBFile, "file:text/nwb", this.inputData);
        
        // Return Data[].
        
        return outData;
    }
    
    /*
     * Takes an NWB file as input and checks to see if the outermost Blondel community level is a duplicate.
     * If that level is a duplicate of the preceding level, it is stripped out and a new file is outputted.
     * This is done to correct a bug in the Blondel algorithm which generates an unnecessary, identical community level attribute.
     */
	private File removeOuterNodeAttribute(File inputFile) throws AlgorithmExecutionException {		
    	// read in data from input file
    	InputStream fileIn;
		try {
			fileIn = new FileInputStream(inputFile);
		} catch (FileNotFoundException e) {
			logger.log(LogService.LOG_ERROR, "Error when opening Blondel generated NWB file.");
			throw new AlgorithmExecutionException("Error while attempting to open a new FileInputStream for the C++ generated NWB file.", e);
		}
    	BufferedReader input = new BufferedReader(new InputStreamReader(fileIn));
    	String line = null;
    	StringBuilder bigOutputString = new StringBuilder();
    	String ls = System.getProperty("line.separator");
    	// go through data line by line
    	boolean hitEdgeSection = false;
    	boolean isOuterLevelUnique = false;
    	int lineCounter = 0;
    	// this exception handling block will always close the FileInputStream
    	try {
			while ((line = input.readLine()) != null) {
				// split line up into array elements, separated by tabs
				String[] lineArray = line.split("\t");
				// if iterator is in "Nodes" section, strip last attribute
				if (lineCounter == 1 || (isNumeric(lineArray[0]) && !hitEdgeSection)) {
					StringBuilder lineOutput = new StringBuilder();
					// loop through all line elements, except last one
					for (int i = 0; i < lineArray.length - 1; i++) {
						lineOutput.append(lineArray[i]);
						lineOutput.append("\t");
					}
					// check to see if the outermost community level is really a duplicate
					isOuterLevelUnique = !lineArray[lineArray.length - 1].equals(lineArray[lineArray.length - 2]);
					
					bigOutputString.append(lineOutput.toString());
					bigOutputString.append(ls);
				}
				// else iterator is either above or below "Nodes" section, copy as is
				else {
					hitEdgeSection = lineCounter != 0;
					bigOutputString.append(line);
					bigOutputString.append(ls);
				}
				lineCounter++;
			}
		} catch (IOException e) {
			logger.log(LogService.LOG_ERROR, "Error when reading Blondel generated NWB file.");
			throw new AlgorithmExecutionException("Error when reading Blondel generated NWB file using input.readLine().", e);
		} finally {
			close(fileIn);
		}
    	
    	// write output to new temporary file
    	File outputFile;
    	BufferedWriter writer;
		try {
			outputFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory("blondel-nwb-", "nwb");
			writer = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e) {
			logger.log(LogService.LOG_ERROR, "Error when creating final Blondel file output.");
			throw new AlgorithmExecutionException("Error when creating new Blondel NWB file and opening a BufferedWriter to the file.", e);
		}
		// this exception handling block will always close the BufferedWriter stream
    	try {
			writer.write(bigOutputString.toString());
		} catch (IOException e) {
			logger.log(LogService.LOG_ERROR, "Error when writing to Blondel NWB file output.");
			throw new AlgorithmExecutionException("Error when writing to Blondel NWB file output using BufferedWriter.write().", e);
		} finally {
			close(writer);
		}
    	
    	/*
    	 * Returns original input file if the outer level is unique. Else, it returns a new File object with the
    	 * outermost community level stripped out.
    	 */
    	if (isOuterLevelUnique)
    		return inputFile;
    	else
    		return outputFile;
    }
	
    /*
     * Quietly closes a @Closeable object (like file I/O). Throws an exception if close does not work,
     * since that means the algorithm probably will not work.
     */
    private void close(Closeable c) throws AlgorithmExecutionException {
        if (c == null) return; 
        try {
            c.close();
        } catch (IOException e) {
        	logger.log(LogService.LOG_ERROR, "Error when attempting to close file stream after algorithm execution.");
            throw new AlgorithmExecutionException("Error when attempting to close an NWB file stream using close() method.", e);
        }
     }
    
    /*
     * Checks if a string is a number or not, returns a boolean
     */
    private static boolean isNumeric(String str)  {  
      try  {  
        @SuppressWarnings("unused")
		double d = Double.parseDouble(str);  
      }  catch(NumberFormatException nfe)  {  
        return false;  
      }  
      return true;  
    }
}
