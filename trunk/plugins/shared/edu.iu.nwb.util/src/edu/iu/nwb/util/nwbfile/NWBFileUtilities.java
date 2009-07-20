package edu.iu.nwb.util.nwbfile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.cishell.utilities.FileUtilities;


public class NWBFileUtilities {
	public static final String[] DEFAULT_NUMBER_KEY_TYPES = new String[] {
		NWBFileProperty.TYPE_INT,
		NWBFileProperty.TYPE_FLOAT
	};
	
	public static final String[] DEFAULT_NUMBER_KEYS_TO_SKIP = new String[] {
		NWBFileProperty.ATTRIBUTE_SOURCE,
		NWBFileProperty.ATTRIBUTE_TARGET
	};
	
	public static Map NECESSARY_EDGE_ATTRIBUTES;
	static {
		/* It's very important that the implementation is LinkedHashMap,
		 * as this preserves key order according to insertion order.
		 * An unordered map could violate the schema specification that
		 * ATTRIBUTE_SOURCE must come before ATTRIBUTE_TARGET.
		 * By the same reasoning, you must not re-order the insertions below.
		 */
		Map m = new LinkedHashMap();
		m.put(NWBFileProperty.ATTRIBUTE_SOURCE, NWBFileProperty.TYPE_INT);
		m.put(NWBFileProperty.ATTRIBUTE_TARGET, NWBFileProperty.TYPE_INT);
		NECESSARY_EDGE_ATTRIBUTES = Collections.unmodifiableMap(m);
	}
	
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
    	catch (IOException ioException) {
    		throw new ParsingException(ioException);
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
	
	public static LinkedHashMap getEdgeSchemaFromMetadata(
			GetNWBFileMetadata nwbFileMetaDataGetter) {
		LinkedHashMap directedEdgeSchema =
    		nwbFileMetaDataGetter.getDirectedEdgeSchema();
    	LinkedHashMap undirectedEdgeSchema =
    		nwbFileMetaDataGetter.getUndirectedEdgeSchema();
    	
		if (directedEdgeSchema != null) {
    		return directedEdgeSchema;
    	}
    	else {
    		return undirectedEdgeSchema;
    	}
	}
	
	public static LinkedHashMap getEdgeMetadata(File nwbFile)
			throws NWBMetadataParsingException {
		GetNWBFileMetadata nwbFileMetaDataGetter = parseMetadata(nwbFile);
		
		return getEdgeSchemaFromMetadata(nwbFileMetaDataGetter);
	}
	
	public static File createTemporaryNWBFile() throws IOException {
		File temporaryNWBFile =
			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory
				("NWB-", "nwb");
		
		return temporaryNWBFile;
	}
}