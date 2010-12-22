package edu.iu.sci2.visualization.geomaps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.cishell.framework.data.Data;
import org.cishell.utilities.TableUtilities;
import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import edu.iu.sci2.visualization.geomaps.scaling.ScalerFactory;
import edu.iu.sci2.visualization.geomaps.utility.Constants;

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
		Arrays.sort(numberColumnsForLatitude, new Latitudishness().reverse());		
		mutator.add(CircleAnnotationMode.LATITUDE_ID, numberColumnsForLatitude);
		
		String[] numericColumnsForLongitude =
			TableUtilities.getValidNumberColumnNamesInTable(table);
		Arrays.sort(numericColumnsForLongitude, new Longitudishness().reverse());	
		mutator.add(CircleAnnotationMode.LONGITUDE_ID, numericColumnsForLongitude);
		
		mutator.add(CircleAnnotationMode.AREA_ID,
					TableUtilities.getValidNumberColumnNamesInTable(table));
		mutator.add(CircleAnnotationMode.AREA_SCALING_ID,
					new ArrayList<String>(ScalerFactory.SCALER_TYPES.keySet()));
		// Expose circle area range?
		
		List<String> numericColumnsForOuterColorQuantity =
			Lists.newArrayList(TableUtilities.getValidNumberColumnNamesInTable(table));
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
			Lists.newArrayList(TableUtilities.getValidNumberColumnNamesInTable(table));
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

	@Override
	protected String getOutputAlgorithmName() {
		return "GeoMapsCircles";
	}
	
	
	protected static class Latitudishness extends Ordering<String> {
		public int compare(String left, String right) {
			return score(left).compareTo(score(right));
		}

		private Integer score(String s) {
			String normal = s.toLowerCase();

			if (normal.contains("latitude")) 	{ return 3;	}
			else if (normal.contains("lat."))	{ return 2; }
			else if (normal.contains("lat"))	{ return 1; }
			else								{ return 0;	}
		}		
	}
	
	protected static class Longitudishness extends Ordering<String> {
		public int compare(String left, String right) {
			return score(left).compareTo(score(right));
		}

		private Integer score(String s) {
			String normal = s.toLowerCase();

			if (normal.contains("longitude"))	{ return 3; }
			else if (normal.contains("lng"))	{ return 2; }
			else if (normal.contains("long"))	{ return 1;	}
			else								{ return 0;	}
		}		
	}
}
