package edu.iu.nwb.converter.cerncoltmatrix_nwbfile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cern.colt.matrix.DoubleMatrix2D;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class CernColtMatrixToNWBFileWriter {
	public static File writeCernColtMatrixToNWBFile(DoubleMatrix2D matrix)
			throws IOException{
	
		File nwbFile = NWBFileUtilities.createTemporaryNWBFile();
		
		NWBFileWriter nwbFileWriter = new NWBFileWriter(nwbFile);
		double[][] doubleMatrix = matrix.toArray();
		
		// Output node schema.
		
		nwbFileWriter.setNodeSchema(NWBFileWriter.getDefaultNodeSchema());
		
		// Output nodes and determine whether or not the matrix is directed.
		
		Map emptyAttributes = new HashMap();
		boolean hasDeterminedIfDirected = false;
		boolean isDirected = false;
		
		for (int ii = 0; ii < doubleMatrix.length; ii++) {
			int nodeID = ii + 1;
			nwbFileWriter.addNode(nodeID, "node" + nodeID, emptyAttributes);
			
			if (!hasDeterminedIfDirected) {
				for (int jj = 0; jj < doubleMatrix[ii].length; jj++) {
					if (doubleMatrix[ii][jj] != doubleMatrix[jj][ii]) {
						hasDeterminedIfDirected = true;
						isDirected = true;
						
						break;
					}
				}
			}
		}
		
		// Set the edge schema and output the edges.
		
		LinkedHashMap edgeSchema = NWBFileWriter.getDefaultEdgeSchema();
		
		if (isDirected) {
			nwbFileWriter.setDirectedEdgeSchema(edgeSchema);
			
			// This loop will iterate over every cell in the matrix.
			for (int ii = 0; ii < doubleMatrix.length; ii++) {
				for (int jj = 0; jj < doubleMatrix[ii].length; jj++) {
					double weight = doubleMatrix[ii][jj];
					
					if (weight != 0.0) {
						// Add a new edge.
						int sourceNodeID = ii + 1;
						int targetNodeID = jj + 1;
						HashMap attributes = new HashMap();
						attributes.put("weight", new Double(weight));
						
						nwbFileWriter.addDirectedEdge(
							sourceNodeID, targetNodeID, attributes);
					}
				}
			}
		}
		else {
			nwbFileWriter.setUndirectedEdgeSchema(edgeSchema);
			
			// This loop will only iterate over the upper half of the matrix.
			for (int ii = 0; ii < doubleMatrix.length; ii++) {
				for (int jj = ii; jj < doubleMatrix[ii].length; jj++) {
					double weight = doubleMatrix[ii][jj];
					
					if (weight != 0.0) {
						// Add a new edge.
						
						int sourceNodeID = ii + 1;
						int targetNodeID = jj + 1;
						HashMap attributes = new HashMap();
						attributes.put("weight", new Double(weight));
						
						nwbFileWriter.addUndirectedEdge(
							sourceNodeID, targetNodeID, attributes);
					}
				}
			}
		}
		
		nwbFileWriter.finishedParsing();
		
		return nwbFile;
	}
}