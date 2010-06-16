package edu.iu.nwb.converter.prefusexgmml.writer;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;

import edu.berkeley.guir.prefuse.graph.Graph;
/**
 * @author Weixia(Bonnie) Huang
 */
public class PrefuseXGMMLWriter implements Algorithm {
	public static final String XGMML_MIME_TYPE = "file:text/xgmml+xml";
	public static final String XGMML_FILE_EXTENSION = "xgmml.xml";
	
	private Graph inGraph;

	
	public PrefuseXGMMLWriter(
			Data[] data, Dictionary parameters,	CIShellContext context) {
		this.inGraph = (Graph) (data[0].getData());
	}

	
	public Data[] execute() throws AlgorithmExecutionException {
		try {
			File outXGMMLFile =
				FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
						"XGMML-", XGMML_FILE_EXTENSION);
			
			(new XGMMLGraphWriter()).writeGraph(inGraph, outXGMMLFile);

			 return new Data[]{ new BasicData(outXGMMLFile, XGMML_MIME_TYPE) };
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}
}