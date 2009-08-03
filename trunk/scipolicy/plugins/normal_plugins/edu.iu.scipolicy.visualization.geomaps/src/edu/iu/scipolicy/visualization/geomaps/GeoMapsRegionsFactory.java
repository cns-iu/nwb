package edu.iu.scipolicy.visualization.geomaps;

import org.cishell.framework.data.Data;
import org.cishell.utilities.TableUtilities;
import org.cishell.utilities.mutateParameter.DropdownMutator;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import edu.iu.scipolicy.visualization.geomaps.scaling.ScalerFactory;

public class GeoMapsRegionsFactory extends GeoMapsAlgorithmFactory {

	@Override
	protected AnnotationMode getAnnotationMode() {
		return new RegionAnnotationMode();
	}
	
	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition oldParameters) {
		Data inData = data[0];
		Table table = (Table)inData.getData();
		
		DropdownMutator mutator = new DropdownMutator();
		
		mutator.add(GeoMapsAlgorithm.SHAPEFILE_ID,
					GeoMapsAlgorithm.SHAPEFILES.keySet());
		
		mutator.add(GeoMapsAlgorithm.PROJECTION_ID,
					GeoMapsAlgorithm.PROJECTIONS.keySet());
		
		mutator.add(RegionAnnotationMode.FEATURE_NAME_ID,
					TableUtilities.getValidStringColumnNamesInTable(table));
		
		mutator.add(RegionAnnotationMode.FEATURE_COLOR_QUANTITY_ID,
					TableUtilities.getValidNumberColumnNamesInTable(table));
		
		mutator.add(RegionAnnotationMode.FEATURE_COLOR_SCALING_ID,
					ScalerFactory.SCALER_TYPES.keySet());

		mutator.add(RegionAnnotationMode.FEATURE_COLOR_RANGE_ID,
					RegionAnnotationMode.COLOR_RANGES.keySet());
				
		return mutator.mutate(oldParameters);
	}
}
