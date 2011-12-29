package edu.iu.nwb.analysis.isolates;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.utils.GraphUtils;

public class Delete implements Algorithm {
	Data[] data;
	Dictionary parameters;
	CIShellContext context;
	LogService logger;

	public Delete(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;
		this.logger = (LogService) context.getService(LogService.class.getName());

	}

	public Data[] execute() {
		Graph inputGraphOriginal = (Graph) this.data[0].getData();

		logNumberOfInputNodes(inputGraphOriginal.numVertices()); 

		Graph inputGraphCopy = (Graph) inputGraphOriginal.copy(); // (chaining these calls should mean getEqualEdge works)

		Set toRemove = new HashSet();

		// for each vertex in the inputGraph..
		Iterator vertexIt = inputGraphCopy.getVertices().iterator();
		while (vertexIt.hasNext()) {
			Vertex vertex = (Vertex) vertexIt.next();
			// if the vertex has degree zero...
			if (vertex.degree() == 0) {
				// mark it for removal
				toRemove.add(vertex);

			}
		}

		// remove all vertices marked for removal
		GraphUtils.removeVertices(inputGraphCopy, toRemove);
		Graph outputGraph = inputGraphCopy;

		logNumberOfOutputNodes(outputGraph.numVertices(), toRemove.size());
		
		//format and return output
		
		Data output = new BasicData(inputGraphCopy, Graph.class.getName());
		Dictionary metadata = output.getMetadata();
		metadata.put(DataProperty.LABEL, "After removing isolates");
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);

		return new Data[] { output };
	}
	
	private void logNumberOfInputNodes(int numInputNodes) {
		logger.log(LogService.LOG_INFO, "" + numInputNodes + " input nodes");
	}
	
	private void logNumberOfOutputNodes(int numOutputNodes, int numRemoved) {
		
		String wasOrWere;
		if (numRemoved == 1) {
			wasOrWere = "was";
		} else {
			wasOrWere = "were";
		}

		logger.log(LogService.LOG_INFO, "" + numOutputNodes + " output nodes, " + numRemoved + " "
				+ wasOrWere + " deleted");
	}
}