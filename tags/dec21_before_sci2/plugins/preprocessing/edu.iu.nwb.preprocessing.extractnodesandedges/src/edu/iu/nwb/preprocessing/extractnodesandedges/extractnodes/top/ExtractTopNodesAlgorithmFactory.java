package edu.iu.nwb.preprocessing.extractnodesandedges.extractnodes.top;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.preprocessing.extractnodesandedges.AddNumericAttributeParamMutator;
import edu.uci.ics.jung.graph.Graph;


public class ExtractTopNodesAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new ExtractTopNodesAlgorithm(data, parameters, context);
    }

	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {
		System.out.println("Extracting top nodes");
		Graph graph = (Graph) data[0].getData();
		try {
		ObjectClassDefinition newParameters =
			AddNumericAttributeParamMutator.mutateForNodes(graph, parameters);
		System.out.println("Done extracting top nodes");
		return newParameters;
		} catch (Exception e) {
			return new BasicObjectClassDefinition("No Usable Numeric Attributes", "No Usable Numeric Attributes", 
					"Nodes must have some numeric attribute (other than source and target) as a basis for filtering/extraction", null);
		}
	}

}