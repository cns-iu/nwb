package edu.iu.nwb.converter.prefusexgmml.reader;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.io.XMLGraphReader;

public class PrefuseXGMMLReader implements Algorithm {
	public static final String ID_KEY = "id";
//	public static final String HACK_ID_KEY = "idForLabel";

	private File inGraphFile;

    public PrefuseXGMMLReader(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
        this.inGraphFile = (File) data[0].getData();
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		Graph graph = (new XMLGraphReader()).loadGraph(inGraphFile.getAbsoluteFile());

    		/* This graph is actually a *Prefuse Alpha* graph (so not the prefuse.data.Graph type).
    		 * Prefuse Alpha graphs seem to add an "id" attribute automatically, which this code
    		 * was written to reverse.
    		 * See http://cns-jira.slis.indiana.edu/browse/SCISQUARED-205 for more details on why
    		 * an "id" attribute is problematic.
    		 */
//    		for (Iterator it = graph.getNodes(); it.hasNext();) {
//    			Node node = (Node) it.next();
//    			Map attributes = node.getAttributes();
//
//    			if (attributes.containsKey(ID_KEY)) {
//    				Object idValue = attributes.get(ID_KEY);
//    				attributes.remove(ID_KEY);
//    				attributes.put(HACK_ID_KEY, idValue);
//    			}
//    		}
    		
    		return createOutData(graph);
    	} catch (IOException e) {
       		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}
    }

	private Data[] createOutData(Graph graph) {
		Data outputData = new BasicData(graph, Graph.class.getName());
		outputData.getMetadata().put(DataProperty.LABEL, "Old Prefuse Graph: " + inGraphFile);
		outputData.getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);

		return new Data[] { outputData };
	}
}