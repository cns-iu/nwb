package edu.iu.scipolicy.visualization.geomaps;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;
import edu.iu.scipolicy.utilities.MutateParameterUtilities;
import edu.iu.scipolicy.utilities.TableUtilities;
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
		BasicObjectClassDefinition newParameters;

		try {
			newParameters = new BasicObjectClassDefinition(oldParameters
					.getID(), oldParameters.getName(), oldParameters
					.getDescription(), oldParameters.getIcon(16));
		} catch (IOException e) {
			newParameters = new BasicObjectClassDefinition(oldParameters
					.getID(), oldParameters.getName(), oldParameters
					.getDescription(), null);
		}

		AttributeDefinition[] oldAttributeDefinitions = oldParameters
				.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
			AttributeDefinition newAttributeDefinition = oldAttributeDefinition;

			if (oldAttributeDefinitionID
					.equals(CircleAnnotationMode.CIRCLE_AREA_SCALING_ID)
					|| oldAttributeDefinitionID
							.equals(CircleAnnotationMode.CIRCLE_COLOR_SCALING_ID)) {
				newAttributeDefinition = formStringDropdownAttributeDefinition(
						oldAttributeDefinition, ScalerFactory.SCALER_TYPES
								.keySet());
			} else if (oldAttributeDefinitionID.equals(CircleAnnotationMode.LATITUDE_ID)) {
				String[] numberColumns = TableUtilities.getValidNumberColumnNamesInTable(table);
				swapFirstMatchToFront(numberColumns, LATITUDE_KEYS);
				newAttributeDefinition = formStringDropdownAttributeDefinition(oldAttributeDefinition, Arrays.asList(numberColumns));
			} else if (oldAttributeDefinitionID.equals(CircleAnnotationMode.LONGITUDE_ID)) {
				String[] numberColumns = TableUtilities.getValidNumberColumnNamesInTable(table);
				swapFirstMatchToFront(numberColumns, LONGITUDE_KEYS);
				newAttributeDefinition = formStringDropdownAttributeDefinition(oldAttributeDefinition, Arrays.asList(numberColumns));
			}
			else if ( oldAttributeDefinitionID.equals(CircleAnnotationMode.CIRCLE_AREA_ID)
					|| oldAttributeDefinitionID.equals(CircleAnnotationMode.CIRCLE_COLOR_QUANTITY_ID)) {
				newAttributeDefinition = MutateParameterUtilities
						.formNumberAttributeDefinition(oldAttributeDefinition,
								table);
			} else if (oldAttributeDefinitionID
					.equals(CircleAnnotationMode.CIRCLE_COLOR_RANGE_ID)) {
				newAttributeDefinition = formStringDropdownAttributeDefinition(
						oldAttributeDefinition,
						CircleAnnotationMode.COLOR_RANGES.keySet());
			} else if (oldAttributeDefinitionID
					.equals(GeoMapsAlgorithm.SHAPEFILE_ID)) {
				newAttributeDefinition = formStringDropdownAttributeDefinition(
						oldAttributeDefinition,
						GeoMapsAlgorithm.SHAPEFILES.keySet());
			} else if (oldAttributeDefinitionID
					.equals(GeoMapsAlgorithm.PROJECTION_ID)) {
				newAttributeDefinition = formStringDropdownAttributeDefinition(
						oldAttributeDefinition,
						GeoMapsAlgorithm.PROJECTIONS.keySet());
			}

			newParameters.addAttributeDefinition(
					ObjectClassDefinition.REQUIRED, newAttributeDefinition);
		}

		return newParameters;
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
