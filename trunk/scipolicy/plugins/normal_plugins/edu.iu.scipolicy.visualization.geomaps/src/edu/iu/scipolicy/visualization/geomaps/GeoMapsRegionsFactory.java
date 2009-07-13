package edu.iu.scipolicy.visualization.geomaps;

import java.io.IOException;

import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import edu.iu.scipolicy.utilities.MutateParameterUtilities;
import edu.iu.scipolicy.visualization.geomaps.scaling.ScalerFactory;

public class GeoMapsRegionsFactory extends GeoMapsAlgorithmFactory {

	@Override
	protected AnnotationMode getAnnotationMode() {
		return new RegionAnnotationMode();
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
			
			if (oldAttributeDefinitionID.equals(RegionAnnotationMode.FEATURE_NAME_ID)) {
				newAttributeDefinition = MutateParameterUtilities.formLabelAttributeDefinition(oldAttributeDefinition, table);
			}
			else if (oldAttributeDefinitionID.equals(RegionAnnotationMode.FEATURE_COLOR_QUANTITY_ID)) {
				newAttributeDefinition = MutateParameterUtilities.formNumberAttributeDefinition(oldAttributeDefinition, table);
			}
			else if (oldAttributeDefinitionID.equals(RegionAnnotationMode.FEATURE_COLOR_SCALING_ID)) {
				newAttributeDefinition = formStringDropdownAttributeDefinition(oldAttributeDefinition, ScalerFactory.SCALER_TYPES.keySet());
			}
			else if (oldAttributeDefinitionID.equals(RegionAnnotationMode.FEATURE_COLOR_RANGE_ID)) {
				newAttributeDefinition = formStringDropdownAttributeDefinition(oldAttributeDefinition, RegionAnnotationMode.COLOR_RANGES.keySet());
			}
			else if (oldAttributeDefinitionID.equals(RegionAnnotationMode.SHAPEFILE_ID)) {
				newAttributeDefinition = formStringDropdownAttributeDefinition(oldAttributeDefinition, GeoMapsAlgorithmFactory.SHAPEFILES.keySet());
			}
			
			
			newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED, newAttributeDefinition);
		}
		
		return newParameters;
	}
}
