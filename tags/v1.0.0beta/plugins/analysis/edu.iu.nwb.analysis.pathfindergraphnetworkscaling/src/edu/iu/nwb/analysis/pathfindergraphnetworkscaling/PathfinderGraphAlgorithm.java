package edu.iu.nwb.analysis.pathfindergraphnetworkscaling;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import cern.colt.matrix.DoubleMatrix2D;
import edu.iu.nwb.analysis.pathfindergraphnetworkscaling.old.PathFinderAlgorithm;
import edu.uci.ics.jung.graph.Graph;

/**
 * @author Russell Duhon
 *
 */
public class PathfinderGraphAlgorithm implements Algorithm {
	private Data[] data;
	private Dictionary parameters;
	private CIShellContext context;


	/**
	 * Construct with the appropriate parameters
	 * @param data This contains the input graph
	 * @param parameters This contains r and q
	 * @param context And this allows access to some additional capabilities
	 */
	public PathfinderGraphAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;
	}

	public Data[] execute() {

		//LogService log = (LogService) context.getService(LogService.class.getName());

		Graph inputGraph = (Graph) data[0].getData();

		GraphMetadataMemory memory = new GraphMetadataMemory(inputGraph);

		Data inputData = new BasicData(memory.getMatrix(), DoubleMatrix2D.class.getName());

		PathFinderAlgorithm pathfinder = new PathFinderAlgorithm(new Data[]{ inputData },
				parameters, context);

		Data[] pathfinderData = pathfinder.execute();
		Data outputMatrix = pathfinderData[0];

		Data outputData = null;


		Graph outputGraph = memory.reconstructMetadata((DoubleMatrix2D) outputMatrix.getData());
		outputData = new BasicData(outputGraph, Graph.class.getName());

		Dictionary map = outputData.getMetadata();
		map.put(DataProperty.MODIFIED,
				new Boolean(true));
		map.put(DataProperty.PARENT, data[0]);
		map.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		map.put(DataProperty.LABEL, "Pathfinder Network Scaling");




		return new Data[]{ outputData };
	}
}