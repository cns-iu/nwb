package edu.iu.nwb.preprocessing.nwbfile_cerncoltmatrix;

import java.io.File;

import cern.colt.matrix.DoubleMatrix2D;
import edu.iu.nwb.util.nwbfile.GetMetadataAndCounts;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.NWBMetadataParsingException;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class Converter {
	public static DoubleMatrix2D convertNWBFileToMatrix(
			File nwbFile, boolean isWeighted, String weightAttribute) 
			throws NWBMetadataParsingException, ParsingException {
		GetMetadataAndCounts metadata =
			NWBFileUtilities.parseMetadata(nwbFile);
		int nodeCount = metadata.getNodeCount();
		// TODO: This could probably be optimized.
		NWBFileToCernColtMatrixHandler converter =
			new NWBFileToCernColtMatrixHandler(
				nodeCount, isWeighted, weightAttribute);
		
		NWBFileUtilities.parseNWBFileWithHandler(nwbFile, converter);
		
		if (converter.hadIssue()) {
			throw new ParsingException(converter.getIssue());
		}
		
		DoubleMatrix2D matrix = converter.getMatrix();
		
		return matrix;
	}
}