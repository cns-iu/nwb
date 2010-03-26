package edu.iu.scipolicy.visualization.geomaps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.cishell.framework.data.Data;
import org.cishell.utilities.TableUtilities;
import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import edu.iu.scipolicy.visualization.geomaps.scaling.ScalerFactory;
import edu.iu.scipolicy.visualization.geomaps.utility.Constants;

public class GeoMapsCirclesFactory extends GeoMapsAlgorithmFactory {
	public static final List<String> LATITUDE_KEYS_TO_GUESS =
		Collections.unmodifiableList(Arrays.asList(new String[]{
			"latitude", "Latitude", "lat", "Lat", "lat.", "Lat."
		}));
	public static final List<String> LONGITUDE_KEYS_TO_GUESS =
		Collections.unmodifiableList(Arrays.asList(new String[]{
			"longitude", "Longitude", "long", "Long", "long.", "Long.", "lng", "Lng", "lng.", "Lng."
		}));

	@Override
	protected AnnotationMode getAnnotationMode() {
		return new CircleAnnotationMode();
	}

	@Override
	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition oldParameters) {
		Data inData = data[0];
		Table table = (Table) inData.getData();
		
		DropdownMutator mutator = new DropdownMutator();
		
		mutator.add(GeoMapsAlgorithm.SHAPEFILE_ID,
					new ArrayList<String>(Constants.SHAPEFILES.keySet()));

		if (GeoMapsAlgorithm.SHOULD_LET_USER_CHOOSE_PROJECTION) {
			mutator.add(GeoMapsAlgorithm.PROJECTION_ID,
				new ArrayList<String>(Constants.PROJECTIONS.keySet()));
		} else {
			mutator.ignore(GeoMapsAlgorithm.PROJECTION_ID);
		}

		String[] numberColumnsForLatitude =
			TableUtilities.getValidNumberColumnNamesInTable(table);		
		swapFirstMatchToFront(numberColumnsForLatitude, LATITUDE_KEYS_TO_GUESS);		
		mutator.add(CircleAnnotationMode.LATITUDE_ID, numberColumnsForLatitude);
		
		String[] numericColumnsForLongitude =
			TableUtilities.getValidNumberColumnNamesInTable(table);	
		swapFirstMatchToFront(numericColumnsForLongitude, LONGITUDE_KEYS_TO_GUESS);		
		mutator.add(CircleAnnotationMode.LONGITUDE_ID, numericColumnsForLongitude);
		
		mutator.add(CircleAnnotationMode.AREA_ID,
					TableUtilities.getValidNumberColumnNamesInTable(table));
		mutator.add(CircleAnnotationMode.AREA_SCALING_ID,
					new ArrayList<String>(ScalerFactory.SCALER_TYPES.keySet()));
		// Expose circle area range?
		
		List<String> numericColumnsForOuterColorQuantity =
			toList(TableUtilities.getValidNumberColumnNamesInTable(table));
		numericColumnsForOuterColorQuantity.add(
				CircleAnnotationMode.USE_NO_OUTER_COLOR_TOKEN);		
		mutator.add(CircleAnnotationMode.OUTER_COLOR_QUANTITY_ID,
					numericColumnsForOuterColorQuantity,
					CircleAnnotationMode.USE_NO_OUTER_COLOR_TOKEN);
		mutator.add(CircleAnnotationMode.OUTER_COLOR_SCALING_ID,
					new ArrayList<String>(ScalerFactory.SCALER_TYPES.keySet()));
		mutator.add(CircleAnnotationMode.OUTER_COLOR_RANGE_ID,
					new ArrayList<String>(Constants.COLOR_RANGES.keySet()));
		
		List<String> numericColumnsForInnerColorQuantity =
			toList(TableUtilities.getValidNumberColumnNamesInTable(table));
		numericColumnsForInnerColorQuantity.add(
				CircleAnnotationMode.USE_NO_INNER_COLOR_TOKEN);	
		mutator.add(CircleAnnotationMode.INNER_COLOR_QUANTITY_ID,
					numericColumnsForInnerColorQuantity,
					CircleAnnotationMode.USE_NO_INNER_COLOR_TOKEN);
		mutator.add(CircleAnnotationMode.INNER_COLOR_SCALING_ID,
					new ArrayList<String>(ScalerFactory.SCALER_TYPES.keySet()));
		mutator.add(CircleAnnotationMode.INNER_COLOR_RANGE_ID,
					new ArrayList<String>(Constants.COLOR_RANGES.keySet()));
		
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
	
	private static <T> List<T> toList(T[] array) {
		List<T> list = new ArrayList<T>(array.length);
		
		for (T element : array) {
			list.add(element);
		}
		
		return list;
	}

	@Override
	protected String getOutputAlgorithmName() {
		return "GeoMapsCircles";
	}
}
