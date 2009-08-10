package edu.iu.scipolicy.visualization.geomaps;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.cishell.framework.data.Data;
import org.cishell.utilities.TableUtilities;
import org.cishell.utilities.mutateParameter.DropdownMutator;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import edu.iu.scipolicy.visualization.geomaps.scaling.ScalerFactory;

public class GeoMapsCirclesFactory extends GeoMapsAlgorithmFactory {
	public static final List<String> LATITUDE_KEYS = Collections.unmodifiableList(Arrays.asList(new String[]{
		"latitude", "Latitude", "lat", "Lat", "lat.", "Lat."
	}));
	public static final List<String> LONGITUDE_KEYS = Collections.unmodifiableList(Arrays.asList(new String[]{
		"longitude", "Longitude", "long", "Long", "long.", "Long.", "lng", "Lng", "lng.", "Lng."
	}));

	@Override
	protected AnnotationMode getAnnotationMode() {
		return new CircleAnnotationMode();
	}

	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition oldParameters) {
		Data inData = data[0];
		Table table = (Table) inData.getData();
		
		DropdownMutator mutator = new DropdownMutator();
		
		mutator.add(GeoMapsAlgorithm.SHAPEFILE_ID,
					GeoMapsAlgorithm.SHAPEFILES.keySet());
		
		mutator.add(GeoMapsAlgorithm.PROJECTION_ID,
					GeoMapsAlgorithm.PROJECTIONS.keySet());
		
		String[] numberColumnsForLat =
			TableUtilities.getValidNumberColumnNamesInTable(table);		
		swapFirstMatchToFront(numberColumnsForLat, LATITUDE_KEYS);		
		mutator.add(CircleAnnotationMode.LATITUDE_ID, numberColumnsForLat);
		
		String[] numberColumnsForLong =
			TableUtilities.getValidNumberColumnNamesInTable(table);	
		swapFirstMatchToFront(numberColumnsForLong, LONGITUDE_KEYS);		
		mutator.add(CircleAnnotationMode.LONGITUDE_ID, numberColumnsForLong);
		
		mutator.add(CircleAnnotationMode.CIRCLE_AREA_ID,
					TableUtilities.getValidNumberColumnNamesInTable(table));
		mutator.add(CircleAnnotationMode.CIRCLE_AREA_SCALING_ID,
					ScalerFactory.SCALER_TYPES.keySet());
		// Expose circle area range?
		
		mutator.add(CircleAnnotationMode.CIRCLE_COLOR_QUANTITY_ID,
					TableUtilities.getValidNumberColumnNamesInTable(table));
		mutator.add(CircleAnnotationMode.CIRCLE_COLOR_SCALING_ID,
					ScalerFactory.SCALER_TYPES.keySet());
		mutator.add(CircleAnnotationMode.CIRCLE_COLOR_RANGE_ID,
					CircleAnnotationMode.COLOR_RANGES.keySet());
		
		return mutator.mutate(oldParameters);
	}
	
	private static void swapFirstMatchToFront(String[] array, List<String> targets) {
		for ( String target : targets ) {
			int index = indexOf(target, array);
			
			if ( index != -1 ) {
				swap(array, 0, index);
				return;
			}
		}
	}
	
	private static int indexOf(Object o, Object[] array) {
		for ( int ii = 0; ii < array.length; ii++ ) {
			if ( array[ii].equals(o) ) {
				return ii;
			}
		}
		
		return -1;
	}
	
	private static void swap(Object[] array, int i, int j) {
		Object temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
}
