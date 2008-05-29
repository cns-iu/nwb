package edu.iu.nwb.analysis.extractattractors.algorithms;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;


public class ExtractAttractorsAlgorithmFactory implements AlgorithmFactory {


	/*
	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition parameters) {
		Table t = (Table) data[0].getData();

		ObjectClassDefinition oldDefinition = parameters;

		BasicObjectClassDefinition definition;
		try {
			definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), oldDefinition.getIcon(16));
		} catch (IOException e) {
			definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), null);
		}

		String[] columnNames = createKeyArray(t.getSchema());
		

		AttributeDefinition[] definitions = oldDefinition.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for(int ii = 0; ii < definitions.length; ii++) {
			String id = definitions[ii].getID();
			if(id.equals(ParameterDefinitions.FUNCTIONID)) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(ParameterDefinitions.FUNCTIONID, "Function Label", "The column label for the function column", AttributeDefinition.STRING, columnNames, columnNames));
			} else if(id.equals(ParameterDefinitions.NODELABEL)) {
				definition.addAttributeDefinition(ObjectClassDefinition.OPTIONAL,
						new BasicAttributeDefinition(ParameterDefinitions.NODELABEL, "Node Label", "Optional label for the nodes.", AttributeDefinition.STRING, columnNames, columnNames));
			}  else if(id.equals(ParameterDefinitions.UPDATESCHEDULE) || id.equals(ParameterDefinitions.UPDATESCHEME) || id.equals(ParameterDefinitions.INITIALCONDITION)){
				definition.addAttributeDefinition(ObjectClassDefinition.OPTIONAL, definitions[ii]);
			}
			else{
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, definitions[ii]);
			}
		}

		
		return definition;	
	}*/



    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new ExtractAttractorsAlgorithm(data, parameters, context);
    }

    

}