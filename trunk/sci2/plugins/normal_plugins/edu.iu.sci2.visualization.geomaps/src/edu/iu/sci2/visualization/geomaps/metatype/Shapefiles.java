package edu.iu.sci2.visualization.geomaps.metatype;

import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;
import org.opengis.referencing.operation.TransformException;

import com.google.common.collect.ImmutableMap;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.ShapefileToPostScriptWriter;
import edu.iu.sci2.visualization.geomaps.utility.Constants;

public class Shapefiles {
	public static final String DEFAULT = "default";
	public static final ImmutableMap<String,String> DEFAULT_PROJECTIONS = ImmutableMap.of(
			Constants.COUNTRIES_SHAPEFILE_KEY, Constants.ECKERT_IV_DISPLAY_NAME,
			Constants.US_STATES_SHAPEFILE_KEY, Constants.ALBERS_EQUAL_AREA_DISPLAY_NAME,
			DEFAULT, Constants.MERCATOR_DISPLAY_NAME);
			
	
	public static ShapefileToPostScriptWriter getPostScriptWriter(Dictionary<String, Object> parameters)
			throws AlgorithmExecutionException, TransformException {
		String shapefileKey = (String) parameters.get(GeoMapsAlgorithm.SHAPEFILE_ID);
		String shapefilePath = Constants.SHAPEFILES.get(shapefileKey);
		URL shapefileURL = GeoMapsAlgorithm.class.getResource(shapefilePath);
		
		String featureNameKey = Constants.FEATURE_NAME_KEY.get(shapefileKey);

		String projectionName;
		if (GeoMapsAlgorithm.SHOULD_LET_USER_CHOOSE_PROJECTION) {
			projectionName = (String) parameters.get(GeoMapsAlgorithm.PROJECTION_ID);
		} else {
			if (DEFAULT_PROJECTIONS.containsKey(shapefileKey)) {
				projectionName = DEFAULT_PROJECTIONS.get(shapefileKey);
			} else {
				projectionName = DEFAULT_PROJECTIONS.get(DEFAULT);
			}
		}

		
		ShapefileToPostScriptWriter postScriptWriter;
		postScriptWriter =
			new ShapefileToPostScriptWriter(
					shapefileURL, projectionName, featureNameKey);
		return postScriptWriter;
	}
	
	public static void addShapefileAndProjectionParameters(DropdownMutator mutator) {
		mutator.add(GeoMapsAlgorithm.SHAPEFILE_ID,
				new ArrayList<String>(Constants.SHAPEFILES.keySet()));
		
		if (GeoMapsAlgorithm.SHOULD_LET_USER_CHOOSE_PROJECTION) {
			mutator.add(GeoMapsAlgorithm.PROJECTION_ID,
				new ArrayList<String>(Constants.PROJECTIONS.keySet()));
		} else {
			mutator.ignore(GeoMapsAlgorithm.PROJECTION_ID);
		}
	}
}
