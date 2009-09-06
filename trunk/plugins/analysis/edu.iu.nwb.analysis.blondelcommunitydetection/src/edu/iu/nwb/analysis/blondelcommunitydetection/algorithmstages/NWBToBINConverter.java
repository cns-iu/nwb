package edu.iu.nwb.analysis.blondelcommunitydetection.algorithmstages;

import java.io.File;
import java.io.IOException;

import org.cishell.utilities.FileUtilities;

import edu.iu.nwb.analysis.blondelcommunitydetection.NetworkInfo;
import edu.iu.nwb.analysis.blondelcommunitydetection.algorithmstages.exceptiontypes.NWBToBINConversionException;
import edu.iu.nwb.analysis.blondelcommunitydetection.nwbfileparserhandlers.nwb_to_bin.Converter;
import edu.iu.nwb.analysis.blondelcommunitydetection.nwbfileparserhandlers.nwb_to_bin.PreProcessor;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.ParsingException;

/*
 * This is the first stage of the Blondel Community Detection algorithm.
 * The input to this stage is the NWB file that the user chose to run this
 * algorithm on.
 * The input NWB file is first preprocessed, which entails:
   * Marking the nodes that are found on edges and 
   * keeping track of the number of edges for each node, then finally
   * annotating each of these nodes with some data for writing the header of
   *  the BIN file that is created in the conversion step.
 * The input NWB must then be converted, which entails:
   * Writing the header generated in the preprocessing step and using it to
   *  know how to write the neighbors/edges for each node as the edges
   *  are processed.
 */
public class NWBToBINConverter {
	public static File convertNWBFileToBINFile(File inputNWBFile,
											   NetworkInfo networkInfo,
											   String weightAttribute,
											   boolean isWeighted)
			throws NWBToBINConversionException {
		
		// (networkInfo gets side-effected in both of these steps.)

		preProcessNWBFile(inputNWBFile, networkInfo);
    	
    	return doConversion(
    		inputNWBFile, networkInfo, weightAttribute, isWeighted);
	}
	
	private static void preProcessNWBFile(File nwbFile,
										  NetworkInfo networkInfo)
    		throws NWBToBINConversionException {
    	PreProcessor preProcessor = new PreProcessor(networkInfo);
    	NWBFileParser preProcessorFileParser;
    	
    	try {
    		preProcessorFileParser = new NWBFileParser(nwbFile);
    		preProcessorFileParser.parse(preProcessor);
    	} catch (IOException ioException) {
    		throw new NWBToBINConversionException(
    			"Failed to read NWB file that is being preprocessed for " +
    				"conversion to BIN file.",
    			ioException);
    	} catch (ParsingException parsingException) {
    		throw new NWBToBINConversionException(
    			"Failed to parse NWB file that is " +
    				"being preprocessed for conversion to BIN file.",
    			parsingException);
    	}
    }
	
	// This modifies networkInfo.
	private static File doConversion(File nwbFile,
									 NetworkInfo networkInfo,
									 String weightAttribute,
									 boolean isWeighted)
    		throws NWBToBINConversionException {
    	File outputBINFile;
    	
    	try {
    		outputBINFile = FileUtilities.
    			createTemporaryFileInDefaultTemporaryDirectory("TEMP-BLONDEL",
    														   "bin");
    	} catch (IOException ioException) {
    		throw new NWBToBINConversionException(
    			"Failed to create temporary BIN file.", ioException);
    	}
    	
    	Converter converter =
    		new Converter(networkInfo,
    							  outputBINFile,
    							  weightAttribute,
    							  isWeighted);
    	NWBFileParser converterFileParser;
    	
    	try {
    		converterFileParser = new NWBFileParser(nwbFile);
    		converterFileParser.parse(converter);
    	} catch (IOException ioException) {
    		throw new NWBToBINConversionException(
    			"Failed to read NWB file that is being converted to BIN file.",
    			ioException);
    	} catch (ParsingException parsingException) {
    		throw new NWBToBINConversionException(
    			"Failed to parse NWB file that is " +
    				"being converted to BIN file.",
    			parsingException);
    	}
    	
    	return outputBINFile;
    }
}