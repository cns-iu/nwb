package edu.iu.sci2.visualization.geomaps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.data.Data;
import org.cishell.utilities.ColumnNotFoundException;
import org.cishell.utilities.TableUtilities;
import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import edu.iu.sci2.visualization.geomaps.metatype.GeoCoordinateParameterFinder;
import edu.iu.sci2.visualization.geomaps.metatype.Shapefiles;
import edu.iu.sci2.visualization.geomaps.scaling.ScalerFactory;
import edu.iu.sci2.visualization.geomaps.utility.Constants;

public class GeoMapsCirclesFactory extends GeoMapsAlgorithmFactory {
	@Override
	protected AnnotationMode getAnnotationMode() {		
		return new CircleAnnotationMode();
	}

	@Override
	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition oldParameters) {
		Data inData = data[0];
		Table table = (Table) inData.getData();
		
		List<String> numericColumnNames = Collections.emptyList();
		try {
			numericColumnNames =
				ImmutableList.copyOf(
						TableUtilities.getValidNumberColumnNamesInTable(table));
		} catch (ColumnNotFoundException e) {
			String message =
				"Table does not seem to have any purely numeric columns.  "
				+ "If your table does not have columns for the latitudes and longitudes of records,"
				+ " you may wish to use one of the geocoders under Analysis > Geospatial.";
			throw new AlgorithmCreationFailedException(message, e);					
		}
		
		DropdownMutator mutator = new DropdownMutator();
		
		Shapefiles.addShapefileAndProjectionParameters(mutator);
	
		GeoCoordinateParameterFinder.addLatitudeParameter(mutator, numericColumnNames, CircleAnnotationMode.LATITUDE_ID);		
		GeoCoordinateParameterFinder.addLongitudeParameter(mutator, numericColumnNames, CircleAnnotationMode.LONGITUDE_ID);		
		addAreaParameters(mutator, numericColumnNames);		
		addOuterColorParameters(mutator, numericColumnNames);		
		addInnerColorParameters(mutator, numericColumnNames);

		return mutator.mutate(oldParameters);
	}

	private void addInnerColorParameters(DropdownMutator mutator, List<String> numericColumnNames) {
		numericColumnNames = Lists.newArrayList(numericColumnNames);

		numericColumnNames.add(
			CircleAnnotationMode.USE_NO_INNER_COLOR_TOKEN);	
		
		mutator.add(CircleAnnotationMode.INNER_COLOR_QUANTITY_ID,
				numericColumnNames,
				CircleAnnotationMode.USE_NO_INNER_COLOR_TOKEN);
		mutator.add(CircleAnnotationMode.INNER_COLOR_SCALING_ID,
				new ArrayList<String>(ScalerFactory.SCALER_TYPES.keySet()));
		mutator.add(CircleAnnotationMode.INNER_COLOR_RANGE_ID,
				new ArrayList<String>(Constants.COLOR_RANGES.keySet()));
	}

	private void addOuterColorParameters(DropdownMutator mutator, List<String> numericColumnNames) {
		numericColumnNames = Lists.newArrayList(numericColumnNames);
		
		numericColumnNames.add(
				CircleAnnotationMode.USE_NO_OUTER_COLOR_TOKEN);		
		
		mutator.add(CircleAnnotationMode.OUTER_COLOR_QUANTITY_ID,
				numericColumnNames,
				CircleAnnotationMode.USE_NO_OUTER_COLOR_TOKEN);
		mutator.add(CircleAnnotationMode.OUTER_COLOR_SCALING_ID,
				new ArrayList<String>(ScalerFactory.SCALER_TYPES.keySet()));
		mutator.add(CircleAnnotationMode.OUTER_COLOR_RANGE_ID,
				new ArrayList<String>(Constants.COLOR_RANGES.keySet()));
	}

	private void addAreaParameters(DropdownMutator mutator, List<String> numericColumnNames) {
		numericColumnNames = Lists.newArrayList(numericColumnNames);
		
		mutator.add(CircleAnnotationMode.AREA_ID, numericColumnNames);
		mutator.add(CircleAnnotationMode.AREA_SCALING_ID,
					new ArrayList<String>(ScalerFactory.SCALER_TYPES.keySet()));
	}
	
	@Override
	protected String getOutputAlgorithmName() {
		return "GeoMapsCircles";
	}
}
