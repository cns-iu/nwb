package edu.iu.nwb.preprocessing.extractnodesandedges.extractnodes.abovebelow;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.preprocessing.extractnodesandedges.AddNumericAttributeParamMutator;
import edu.iu.nwb.preprocessing.extractnodesandedges.NoNumericAttributesException;
import edu.uci.ics.jung.graph.Graph;


public class ExtractNodesAboveBelowAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
        return new ExtractNodesAboveBelowAlgorithm(data, parameters, ciShellContext);
    }

	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		System.err.println("mutateParameters");
		Graph graph = (Graph) data[0].getData();

		try {
			ObjectClassDefinition newParameters =
				AddNumericAttributeParamMutator.mutateForNodes(graph, oldParameters);

			return newParameters;
		} catch (NoNumericAttributesException e) {
			System.err.println("Exception:");
			e.printStackTrace();
			throw new AlgorithmCreationFailedException(
				"Nodes must have some numeric attribute as a basis for filtering/extraction", e);
		}
	}
}