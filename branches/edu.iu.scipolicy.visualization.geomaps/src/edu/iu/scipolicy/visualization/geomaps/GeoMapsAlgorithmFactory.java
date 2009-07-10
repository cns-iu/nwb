package edu.iu.scipolicy.visualization.geomaps;

import java.util.Dictionary;
import java.util.Set;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

public abstract class GeoMapsAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
    @SuppressWarnings("unchecked") // TODO
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new GeoMapsAlgorithm(data, parameters, context, getAnnotationMode());
    }
    
    protected abstract AnnotationMode getAnnotationMode();
    
    public abstract ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition oldParameters);
    
    protected static AttributeDefinition formStringDropdownAttributeDefinition(AttributeDefinition oldAttributeDefinition, Set<String> optionsSet) {
    	String[] optionsArray = (String[]) optionsSet.toArray(new String[optionsSet.size()]);
	
		AttributeDefinition scaleAttributeDefinition =
			new BasicAttributeDefinition(oldAttributeDefinition.getID(),
										 oldAttributeDefinition.getName(),
										 oldAttributeDefinition.getDescription(),
										 AttributeDefinition.STRING,
										 optionsArray,
										 optionsArray);
	
		return scaleAttributeDefinition;
	}
}