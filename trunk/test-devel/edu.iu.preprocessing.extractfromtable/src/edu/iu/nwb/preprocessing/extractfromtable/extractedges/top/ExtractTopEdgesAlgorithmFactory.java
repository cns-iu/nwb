package edu.iu.nwb.preprocessing.extractfromtable.extractedges.top;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Graph;
import prefuse.data.Table;
import edu.iu.nwb.preprocessing.extractfromtable.AddNumericAttributeParamMutator;


public class ExtractTopEdgesAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new ExtractTopEdgesAlgorithm(data, parameters, context);
    }

	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {
		Graph graph = (Graph) data[0].getData();
		Table edgeTable = graph.getEdgeTable();
		ObjectClassDefinition newParameters =
			AddNumericAttributeParamMutator.mutateForTopNodesOrEdges(edgeTable, parameters);
		return newParameters;
	}
    

}