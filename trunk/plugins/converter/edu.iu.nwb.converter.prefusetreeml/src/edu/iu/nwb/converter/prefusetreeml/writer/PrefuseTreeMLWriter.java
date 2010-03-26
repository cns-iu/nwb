package edu.iu.nwb.converter.prefusetreeml.writer;

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
import edu.iu.nwb.converter.prefusetreeml.writer.prefusecode.TreeMLWriter;
/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseTreeMLWriter implements Algorithm {
    public static final String TREEML_MIME_TYPE = "file:text/treeml+xml";
    
	private Graph inGraph;
    
	
    public PrefuseTreeMLWriter(
    		Data[] data, Dictionary parameters, CIShellContext context) {
        this.inGraph = (Graph) data[0].getData();
    }

    
    public Data[] execute() throws AlgorithmExecutionException {
    	try {
			File outTreeMLFile =
				FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
						"TreeML-", "treeml.xml");
			
			OutputStream outStream =
				new BufferedOutputStream(new FileOutputStream(outTreeMLFile));
			(new TreeMLWriter()).writeGraph(inGraph, outStream) ;
			
			return new Data[]{ new BasicData(outTreeMLFile, TREEML_MIME_TYPE) };
		} catch (DataIOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
    }
}