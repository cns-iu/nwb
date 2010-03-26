package edu.iu.nwb.converter.jungpajeknet.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.decorators.ConstantEdgeValue;
import edu.uci.ics.jung.graph.decorators.NumberEdgeValue;
import edu.uci.ics.jung.graph.decorators.UserDatumNumberEdgeValue;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.io.PajekNetWriter;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class JungPajekNetWriter implements Algorithm, VertexStringer {
	public static final String DEFAULT_VERTEX_LABEL = "";
	public static final double DEFAULT_WEIGHT = 1.0;
	public static final String PAJEK_NET_FILE_EXTENSION = "net";
	public static final String PAJEK_NET_MIME_TYPE = "file:application/pajek";
	public static final String WEIGHT_ATTRIBUTE_KEY = "weight";
	
	private Graph inGraph;
    
	
    public JungPajekNetWriter(Data[] data,
    						  Dictionary parameters,
    						  CIShellContext context) {
        this.inGraph = (Graph) data[0].getData();
    }

    
    public Data[] execute() throws AlgorithmExecutionException {
		try {
			File outPajekNETFile =
	    		FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
	    				"Pajek-", PAJEK_NET_FILE_EXTENSION);
			
			Writer writer =
				new BufferedWriter(
						new OutputStreamWriter(
								new FileOutputStream(outPajekNETFile)));
			
			/* UserDatumNumberEdgeValue will return null for getNumber() if
			 * it can't find the weight attribute, so this is necessary to
			 * suppress NullPointerExceptions internal to Jung (1.7.4) itself.
			 */
			NumberEdgeValue edgeValueReader;
			if (inGraph.containsUserDatumKey(WEIGHT_ATTRIBUTE_KEY)) {
				edgeValueReader =
					new UserDatumNumberEdgeValue(WEIGHT_ATTRIBUTE_KEY);
			} else {
				edgeValueReader = new ConstantEdgeValue(DEFAULT_WEIGHT);
			}
			
			(new PajekNetWriter()).save(
					inGraph, 
					writer,
					this,
					edgeValueReader);			
			
			return new Data[]{ new BasicData(
					outPajekNETFile, PAJEK_NET_MIME_TYPE) };
		} catch (IOException e) {
			String message = "File access error: " + e.getMessage();
			throw new AlgorithmExecutionException(message, e);
		}
    }

    public String getLabel(ArchetypeVertex v) {
        Object label = v.getUserDatum("label");
        
        if (label != null) {
        	return String.valueOf(label);
        } else {
        	return DEFAULT_VERTEX_LABEL;
        }
    }
}