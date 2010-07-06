package edu.iu.nwb.util.nwbfile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import org.cishell.utilities.FileUtilities;


public class NWBFileUtilities {
	public static final Collection<String> DEFAULT_NUMBER_KEY_TYPES =
		Collections.unmodifiableList(Arrays.asList(
			NWBFileProperty.TYPE_INT, NWBFileProperty.TYPE_FLOAT));
	public static final Collection<String> DEFAULT_NUMBER_KEYS_TO_SKIP =
		Collections.unmodifiableList(Arrays.asList(
			NWBFileProperty.ATTRIBUTE_SOURCE,
			NWBFileProperty.ATTRIBUTE_TARGET));
	
	/*
	 * This creates an NWBFileParser with nwbFile and passes in handler when
	 * parsing.  handler is thus side-effected.
	 */
	public static void parseNWBFileWithHandler(File nwbFile, NWBFileParserHandler handler)
			throws ParsingException {
		NWBFileParser nwbParser;
    	
    	try {
    		nwbParser = new NWBFileParser(nwbFile);
    		nwbParser.parse(handler);
    	}
    	catch (IOException e) {
    		throw new ParsingException(e);
    	}
	}
	
	/*
	 * This parses out the NWB metadata from the passed-in NWB file and
	 * returns it.
	 */
	// TODO: Create NWBMetadata class?
	public static GetMetadataAndCounts parseMetadata(File nwbFile)
			throws NWBMetadataParsingException {
		GetMetadataAndCounts nwbFileMetaDataGetter = new GetMetadataAndCounts();
    	
    	try {
    		parseNWBFileWithHandler(nwbFile, nwbFileMetaDataGetter);
    	}
    	catch (ParsingException parsingException) {
    		throw new NWBMetadataParsingException(parsingException);
    	}
    	
    	return nwbFileMetaDataGetter;
	}
	
	public static LinkedHashMap<String, String> getEdgeSchemaFromMetadata(
			GetNWBFileMetadata nwbFileMetaDataGetter) {
		LinkedHashMap<String, String> directedEdgeSchema =
    		nwbFileMetaDataGetter.getDirectedEdgeSchema();
    	LinkedHashMap<String, String> undirectedEdgeSchema =
    		nwbFileMetaDataGetter.getUndirectedEdgeSchema();
    	
		if (directedEdgeSchema != null) {
    		return directedEdgeSchema;
    	}
    	else {
    		return undirectedEdgeSchema;
    	}
	}
	
	public static LinkedHashMap<String, String> getEdgeMetadata(File nwbFile)
			throws NWBMetadataParsingException {
		GetNWBFileMetadata nwbFileMetaDataGetter = parseMetadata(nwbFile);

		return getEdgeSchemaFromMetadata(nwbFileMetaDataGetter);
	}
	
	public static LinkedHashMap<String, String> getNodeSchema(File nwbFile)
			throws NWBMetadataParsingException {
		GetNWBFileMetadata nwbFileMetaDataGetter = parseMetadata(nwbFile);
		
		return nwbFileMetaDataGetter.getNodeSchema();
	}
	
	public static File createTemporaryNWBFile() throws IOException {
		File temporaryNWBFile =
			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory
				("NWB-", "nwb");
		
		return temporaryNWBFile;
	}
}