package edu.iu.nwb.preprocessing.extractfromtable.extractnodes.top;

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


public class ExtractTopNodesAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
    	System.out.println("Creating top nodes algorithm");
        return new ExtractTopNodesAlgorithm(data, parameters, context);
    }

	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {
		Graph graph = (Graph) data[0].getData();
		Table nodeTable = graph.getNodeTable();
		ObjectClassDefinition newParameters =
			AddNumericAttributeParamMutator.mutateForTopNodesOrEdges(nodeTable, parameters);
		return newParameters;
	}

}