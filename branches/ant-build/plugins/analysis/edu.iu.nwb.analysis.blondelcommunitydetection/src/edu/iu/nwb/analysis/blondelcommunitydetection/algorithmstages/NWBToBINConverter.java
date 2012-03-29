package edu.iu.nwb.analysis.blondelcommunitydetection.algorithmstages;

import java.io.File;
import java.io.IOException;

import org.cishell.utilities.FileUtilities;

import edu.iu.nwb.analysis.blondelcommunitydetection.NetworkInfo;
import edu.iu.nwb.analysis.blondelcommunitydetection.algorithmstages.exceptiontypes.NWBToBINConversionException;
import edu.iu.nwb.analysis.blondelcommunitydetection.nwbfileparserhandlers.nwb_to_bin.Converter;
import edu.iu.nwb.analysis.blondelcommunitydetection.nwbfileparserhandlers.nwb_to_bin.Preprocessor;
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
	public static File convertNWBFileToBINFile(
			File inputNWBFile, NetworkInfo networkInfo, String weightAttribute, boolean isWeighted)
			throws NWBToBINConversionException {
		
		// (networkInfo gets side-effected in both of these steps.)

		preprocessNWBFile(inputNWBFile, networkInfo, weightAttribute, isWeighted);
    	
    	return doConversion(inputNWBFile, networkInfo, weightAttribute, isWeighted);
	}
	
	private static void preprocessNWBFile(
			File nwbFile, NetworkInfo networkInfo, String weightAttribute, boolean isWeighted)
    		throws NWBToBINConversionException {
		// TODO Preprocessor etc.
    	Preprocessor preprocessor = new Preprocessor(networkInfo, weightAttribute, isWeighted);
    	NWBFileParser preprocessorFileParser;
    	
    	try {
    		preprocessorFileParser = new NWBFileParser(nwbFile);
    		preprocessorFileParser.parse(preprocessor);

    		NWBToBINConversionException exceptionThrown = preprocessor.getExceptionThrown();

    		if (exceptionThrown != null) {
    			throw exceptionThrown;
    		}
    	} catch (IOException e) {
    		String exceptionMessage =
    			"Failed to read NWB file that is being preprocessed for conversion to BIN file: " +
    			"\"" + e.getMessage() + "\"";
    		throw new NWBToBINConversionException(exceptionMessage, e);
    	} catch (ParsingException e) {
    		String exceptionMessage =
    			"Failed to parse NWB file that is being preprocessed for " +
    			"conversion to BIN file: " +
    			"\"" + e.getMessage() + "\"";
    		throw new NWBToBINConversionException(exceptionMessage, e);
    	} catch (NWBToBINConversionException e) {
    		String exceptionMessage =
    			"Failed to parse NWB file that is being preprocessed for " +
    			"conversion to BIN file: " +
    			"\"" + e.getMessage() + "\"";
    		throw new NWBToBINConversionException(exceptionMessage, e);
    	}
    }
	
	// This modifies networkInfo.
	private static File doConversion(
			File nwbFile, NetworkInfo networkInfo, String weightAttribute, boolean isWeighted)
    		throws NWBToBINConversionException {
    	File outputBINFile;
    	
    	try {
    		outputBINFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
    			"TEMP-BLONDEL", "bin");
    	} catch (IOException e) {
    		throw new NWBToBINConversionException("Failed to create temporary BIN file.", e);
    	}
    	
    	Converter converter = new Converter(
    		networkInfo, outputBINFile, weightAttribute, isWeighted);
    	NWBFileParser converterFileParser;
    	
    	try {
    		converterFileParser = new NWBFileParser(nwbFile);
    		converterFileParser.parse(converter);
    	} catch (IOException e) {
    		throw new NWBToBINConversionException(
    			"Failed to read NWB file that is being converted to BIN file.", e);
    	} catch (ParsingException e) {
    		throw new NWBToBINConversionException(
    			"Failed to parse NWB file that is being converted to BIN file.", e);
    	}
    	
    	return outputBINFile;
    }
}