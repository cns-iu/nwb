package edu.iu.scipolicy.visualization.geomaps;

import java.util.ArrayList;

import org.cishell.framework.data.Data;
import org.cishell.utilities.TableUtilities;
import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import edu.iu.scipolicy.visualization.geomaps.scaling.ScalerFactory;
import edu.iu.scipolicy.visualization.geomaps.utility.Constants;

public class GeoMapsRegionsFactory extends GeoMapsAlgorithmFactory {

	@Override
	protected AnnotationMode getAnnotationMode() {
		return new RegionAnnotationMode();
	}
	
	@Override
	public ObjectClassDefinition mutateParameters(
			Data[] data,
			ObjectClassDefinition oldParameters) {
		Data inData = data[0];
		Table table = (Table)inData.getData();
		
		DropdownMutator mutator = new DropdownMutator();
		
		mutator.add(GeoMapsAlgorithm.SHAPEFILE_ID,
					new ArrayList<String>(Constants.SHAPEFILES.keySet()));

		if (GeoMapsAlgorithm.SHOULD_LET_USER_CHOOSE_PROJECTION) {
			mutator.add(GeoMapsAlgorithm.PROJECTION_ID,
					new ArrayList<String>(Constants.PROJECTIONS.keySet()));
		} else {
			mutator.ignore(GeoMapsAlgorithm.PROJECTION_ID);
		}

		mutator.add(RegionAnnotationMode.FEATURE_NAME_ID,
					TableUtilities.getValidStringColumnNamesInTable(table));
		
		mutator.add(RegionAnnotationMode.FEATURE_COLOR_QUANTITY_ID,
					TableUtilities.getValidNumberColumnNamesInTable(table));
		
		mutator.add(RegionAnnotationMode.FEATURE_COLOR_SCALING_ID,
					new ArrayList<String>(ScalerFactory.SCALER_TYPES.keySet()));

		mutator.add(RegionAnnotationMode.FEATURE_COLOR_RANGE_ID,
					new ArrayList<String>(Constants.COLOR_RANGES.keySet()));
		
		return mutator.mutate(oldParameters);
	}

	@Override
	protected String getOutputAlgorithmName() {
		return "GeoMapsRegions";
	}
}
