package edu.iu.nwb.preprocessing.extractfromtable.extractnodes.abovebelow;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.preprocessing.extractfromtable.AddNumericAttributeParamMutator;
import edu.uci.ics.jung.graph.Graph;


public class ExtractNodesAboveBelowAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new ExtractNodesAboveBelowAlgorithm(data, parameters, context);
    }

	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {
		Graph graph = (Graph) data[0].getData();
		try {
			System.out.println("MUTATE");
		ObjectClassDefinition newParameters =
			AddNumericAttributeParamMutator.mutateForTopNodesOrEdges(graph, parameters);
		System.out.println("SUCCESS");
		return newParameters;
		} catch (AlgorithmExecutionException e) {
			System.out.println(e.toString());
			return parameters;
		}
	}
}