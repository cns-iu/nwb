package edu.iu.nwb.preprocessing.extractnodesandedges.extractedges.abovebelow;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.preprocessing.extractnodesandedges.AddNumericAttributeParamMutator;
import edu.uci.ics.jung.graph.Graph;


public class ExtractEdgesAboveBelowAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
        return new ExtractEdgesAboveBelowAlgorithm(data, parameters, context);
    }

	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {
		Graph graph = (Graph) data[0].getData();

		try {
			ObjectClassDefinition newParameters =
				AddNumericAttributeParamMutator.mutateForEdges(graph, parameters);

			return newParameters;
		} catch (Exception e) {
			String exceptionMessage =
				"Edges must have some numeric attribute (other than source and target) as a " +
				"basis for filtering/extraction";
			throw new AlgorithmCreationFailedException(exceptionMessage);
//			return new BasicObjectClassDefinition(
//				"No Usable Numeric Attributes",
//				"No Usable Numeric Attributes", 
//				"Edges must have some numeric attribute (other than source and target) as a " +
//					"basis for filtering/extraction",
//				null);
		}
	}
}