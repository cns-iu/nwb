package edu.iu.nwb.converter.edgelist.conversion;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import edu.iu.nwb.converter.edgelist.common.EdgeListParser;
import edu.iu.nwb.converter.edgelist.common.InvalidEdgeListFormatException;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;

/*
 * Converts from EdgeList format to NWB file format
 */
public class EdgeListToNWBAlgorithm implements Algorithm {
	private File inputEdgeFile;
	

	public EdgeListToNWBAlgorithm(Data[] data,
								  Dictionary parameters,
								  CIShellContext context) {
		this.inputEdgeFile = (File) data[0].getData();
	}

	
	public Data[] execute() throws AlgorithmExecutionException {		
		File outputNWBFile = convertEdgeListToNWBFile(inputEdgeFile);
				
		Data[] outData =
			new Data[] { new BasicData(outputNWBFile,
									   NWBFileProperty.NWB_MIME_TYPE) };
		
		return outData;
	}
	
	private File convertEdgeListToNWBFile(File inputEdgeFile)
			throws AlgorithmExecutionException {
		try {
			/* parser converts the contents of inputEdgeFile and
			 * writes them to outputNWBFile through writer 
			 */
			EdgeListParser parser = new EdgeListParser(inputEdgeFile);			
			File outputNWBFile = NWBFileUtilities.createTemporaryNWBFile();
			NWBFileWriter writer = new NWBFileWriter(outputNWBFile);
			parser.parseInto(writer);
			
			return outputNWBFile;
		} catch (IOException e) {
			throw new AlgorithmExecutionException("Error creating the output file: " + e.getMessage(), e);
		} catch (InvalidEdgeListFormatException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}
}