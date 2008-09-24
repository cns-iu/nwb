package edu.iu.nwb.analysis.symmetrize;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Graph;
import prefuse.data.Table;


public class SymmetrizeFactory implements AlgorithmFactory, ParameterMutator {

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new Symmetrize(data, parameters, context);
    }

	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {
		Graph graph = (Graph) data[0].getData();

		BasicObjectClassDefinition definition = new BasicObjectClassDefinition("stuff", "Symmetrize Using", "Pick the aggregation functions to symmetrize using.", null);
		
		String[] numericChoices = new String[] {"drop", "max", "min", "average", "sum"};
		String[] stringChoices = new String[] {"drop", "first alphabetically", "last alphabetically"};
		String[] bareChoices = new String[] {"max", "min"};
		
		
		boolean bare = true;
		
		Table edgeTable = graph.getEdgeTable();
		for(int attribute = 0; attribute < edgeTable.getColumnCount(); attribute++) {
			String columnName = edgeTable.getColumnName(attribute);
			if(columnName.equals(graph.getEdgeSourceField()) || columnName.equals(graph.getEdgeTargetField())) {
				continue;
			}
			bare = false;
			if(edgeAttributeIsNumeric(graph, columnName)) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
					new BasicAttributeDefinition(Symmetrize.PREFIX + columnName, columnName, "How to aggregate this attribute", AttributeDefinition.STRING, numericChoices, numericChoices));
			} else {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
					new BasicAttributeDefinition(Symmetrize.PREFIX + columnName, columnName, "How to aggregate this attribute", AttributeDefinition.STRING, stringChoices, stringChoices));
			}
		}
		
		
		
		definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
				new BasicAttributeDefinition("matrix", "Treat graph as a matrix (instead of an edgelist)", "Attributes on non-existent edges " +
						"will be treated as zero or the empty string. " +
						"Any edges set to entirely zero or the empty string will be removed.",
						AttributeDefinition.BOOLEAN));
		if(bare) {
			definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
					new BasicAttributeDefinition("bare", "Rule for present/not present (only used if treated as a matrix)",
							"Present edges are treated as having an attribute with weight 1; non-present edges are treated as " +
							"having weight zero.",
							AttributeDefinition.STRING, bareChoices, bareChoices));
		}
		
		return definition;
	}

	private boolean edgeAttributeIsNumeric(Graph graph, String columnName) {
		Table edgeTable = graph.getEdgeTable();
		return edgeTable.canGet(columnName, Number.class) || edgeTable.canGetDouble(columnName);
	}
}