package edu.iu.nwb.converter.prefusegraphml.writer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;

import prefuse.data.Graph;
import prefuse.data.io.DataIOException;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseGraphMLWriter implements Algorithm {
	public static final String GRAPHML_FILE_EXTENSION = "file:text/graphml+xml";
	
	private Graph inGraph;
    
	
    public PrefuseGraphMLWriter(
    		Data[] data, Dictionary parameters, CIShellContext context) {
		this.inGraph = (Graph) data[0].getData();
    }

    
    public Data[] execute() throws AlgorithmExecutionException {
    	try {
			File outGraphMLFile =
				FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
						"NWB-Session-", "graphml.xml");
			
			OutputStream outStream =
				new BufferedOutputStream(
					new FileOutputStream(outGraphMLFile));
			
			(new GraphMLWriter()).writeGraph(inGraph, outStream);
			
			return new Data[]{ new BasicData(
					outGraphMLFile, GRAPHML_FILE_EXTENSION) };			
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (DataIOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
    }
}