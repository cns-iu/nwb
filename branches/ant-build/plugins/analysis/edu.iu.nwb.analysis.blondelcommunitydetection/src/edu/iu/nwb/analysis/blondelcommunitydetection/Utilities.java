package edu.iu.nwb.analysis.blondelcommunitydetection;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

public class Utilities {
	@SuppressWarnings("unchecked")
		public static Data[] wrapFileAsOutputData(
				File outputFile, String mimeType, Data inputData) {
    	Data outputFileData = new BasicData(outputFile, mimeType);
    	Dictionary outputFileMetaData = outputFileData.getMetadata();
    	outputFileMetaData.put(DataProperty.LABEL, "With community attributes");
    	outputFileMetaData.put(DataProperty.PARENT, inputData);
    	outputFileMetaData.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
    	
    	return new Data[] { outputFileData };
    }
}