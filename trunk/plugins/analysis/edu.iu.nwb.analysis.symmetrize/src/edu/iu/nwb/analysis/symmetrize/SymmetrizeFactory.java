package edu.iu.nwb.analysis.symmetrize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicMetaTypeProvider;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeInformation;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;


public class SymmetrizeFactory implements AlgorithmFactory {

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new Symmetrize(data, parameters, context);
    }
    
    public MetaTypeProvider createParameters(Data[] data) {
    	Graph graph = (Graph) data[0].getData();

		BasicObjectClassDefinition definition = new BasicObjectClassDefinition("stuff", "Symmetrize Using", "Pick the aggregation functions to symmetrize using.", null);
		
		String[] numericChoices = new String[] {"drop", "max", "min", "average", "sum"};
		String[] stringChoices = new String[] {"drop", "first alphabetically", "last alphabetically"};
		
		boolean needed = false;
		
		for(int attribute = 0; attribute < graph.getEdgeTable().getColumnCount(); attribute++) {
			String columnName = graph.getEdgeTable().getColumnName(attribute);
			if(columnName.equals(graph.getEdgeSourceField()) || columnName.equals(graph.getEdgeTargetField())) {
				continue;
			}
			needed = true;
			if(graph.getEdgeTable().canGet(columnName, Number.class)) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
					new BasicAttributeDefinition(Symmetrize.PREFIX + columnName, columnName, "How to aggregate this attribute", AttributeDefinition.STRING, numericChoices, numericChoices));
			} else {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
					new BasicAttributeDefinition(Symmetrize.PREFIX + columnName, columnName, "How to aggregate this attribute", AttributeDefinition.STRING, stringChoices, stringChoices));
			}
		}
		if(needed) {
			return new BasicMetaTypeProvider(definition);
		} else {
			return null;
		}
    }
}