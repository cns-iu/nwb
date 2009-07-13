package edu.iu.scipolicy.visualization.geomaps;

import java.awt.Color;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.scipolicy.visualization.geomaps.utility.Range;

public abstract class GeoMapsAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	public static final Map<String, String> SHAPEFILES;
	static {
		Map<String, String> t = new HashMap<String, String>();
		t.put("States", "/edu/iu/scipolicy/visualization/geomaps/shapefiles/tl_2008_us_state.shp");
		t.put("Countries", "/edu/iu/scipolicy/visualization/geomaps/shapefiles/countries.shp");
		SHAPEFILES = Collections.unmodifiableMap(t);
	}
	
	
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