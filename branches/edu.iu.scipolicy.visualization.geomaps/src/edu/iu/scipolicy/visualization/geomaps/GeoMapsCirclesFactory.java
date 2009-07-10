package edu.iu.scipolicy.visualization.geomaps;

import java.io.IOException;

import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import edu.iu.scipolicy.utilities.MutateParameterUtilities;
import edu.iu.scipolicy.visualization.geomaps.scaling.ScalerFactory;

public class GeoMapsCirclesFactory extends GeoMapsAlgorithmFactory {

	@Override
	protected AnnotationMode getAnnotationMode() {
		return new CircleAnnotationMode();
	}

	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition oldParameters) {
		Data inData = data[0];
		Table table = (Table)inData.getData();
		BasicObjectClassDefinition newParameters;
		
		try {
			newParameters =
				new BasicObjectClassDefinition(oldParameters.getID(),
											   oldParameters.getName(),
											   oldParameters.getDescription(),
											   oldParameters.getIcon(16));
		}
		catch (IOException e) {
			newParameters = new BasicObjectClassDefinition
				(oldParameters.getID(),
				 oldParameters.getName(),
				 oldParameters.getDescription(), null);
		}
		
		AttributeDefinition[] oldAttributeDefinitions =	oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL);
		
		for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
			AttributeDefinition newAttributeDefinition = oldAttributeDefinition;
			
			if (oldAttributeDefinitionID.equals(CircleAnnotationMode.CIRCLE_AREA_SCALING_ID)
					|| oldAttributeDefinitionID.equals(CircleAnnotationMode.CIRCLE_COLOR_SCALING_ID)) {
				newAttributeDefinition = formStringDropdownAttributeDefinition(oldAttributeDefinition, ScalerFactory.SCALER_TYPES.keySet());
			}
			else if (oldAttributeDefinitionID.equals(CircleAnnotationMode.LATITUDE_ID)
					|| oldAttributeDefinitionID.equals(CircleAnnotationMode.LONGITUDE_ID)
					|| oldAttributeDefinitionID.equals(CircleAnnotationMode.CIRCLE_AREA_ID)
					|| oldAttributeDefinitionID.equals(CircleAnnotationMode.CIRCLE_COLOR_QUANTITY_ID)) {
				newAttributeDefinition = MutateParameterUtilities.formNumberAttributeDefinition(oldAttributeDefinition, table);
			}
			else if (oldAttributeDefinitionID.equals(CircleAnnotationMode.CIRCLE_COLOR_RANGE_ID)) {
				newAttributeDefinition = formStringDropdownAttributeDefinition(oldAttributeDefinition, CircleAnnotationMode.COLOR_RANGES.keySet());
			}
			
			newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED, newAttributeDefinition);
		}
		
		return newParameters;
	}
}
