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
import edu.uci.ics.jung.utils.UserData;

public class Delete implements Algorithm {
	Data[] data;
	Dictionary parameters;
	CIShellContext context;

	public Delete(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;

	}

	public Data[] execute() {

		LogService logger = (LogService)context.getService(LogService.class.getName());


		Graph inputGraph = (Graph) this.data[0].getData();
		logger.log(LogService.LOG_INFO, "" + inputGraph.numVertices() + " input nodes");

		int core = 1;



		Graph outputGraph = (Graph) inputGraph.copy(); //chaining these calls should mean getEqualEdge works






		Iterator checking = outputGraph.getVertices().iterator();

		Set toRemove = new HashSet();

		while(checking.hasNext()) {
			Vertex vertex = (Vertex) checking.next();

			if(vertex.degree() == 0) {

				toRemove.add(vertex);

			}
		}


		GraphUtils.removeVertices(outputGraph, toRemove);





		Data output = new BasicData(outputGraph, Graph.class.getName());

		Dictionary metadata = output.getMetadata();
		metadata.put(DataProperty.LABEL, "After removing isolates");
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);

		return new Data[]{output};




	}
}