package edu.iu.sci2.visualization.geomaps;

import java.util.Dictionary;
import java.util.EnumSet;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationCanceledException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.utilities.ColumnNotFoundException;
import org.cishell.utilities.TableUtilities;
import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

import com.google.common.base.Functions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import edu.iu.sci2.visualization.geomaps.data.scaling.Scaling;
import edu.iu.sci2.visualization.geomaps.geo.projection.KnownProjectedCRSDescriptor;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile;
import edu.iu.sci2.visualization.geomaps.viz.Constants;
import edu.iu.sci2.visualization.geomaps.viz.FeatureDimension;
import edu.iu.sci2.visualization.geomaps.viz.RegionAnnotationMode;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension;

public class GeoMapsRegionsFactory implements AlgorithmFactory, ParameterMutator {
	public static final String SUBTITLE = "Regions";
	
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters,	CIShellContext ciShellContext) {
		String featureNameColumnName = (String) parameters.get(RegionAnnotationMode.FEATURE_NAME_ID);
		
		return new GeoMapsAlgorithm<String, FeatureDimension>(
				data,
				parameters,
				new RegionAnnotationMode(featureNameColumnName),
				"Geo Maps (Regions)",
				(LogService) ciShellContext.getService(LogService.class.getName()));
	}
	
	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition oldOCD) {
		Table table = (Table) data[0].getData();
		
		List<String> numericColumnNames = ImmutableList.of();
		try {
			numericColumnNames = Lists.newArrayList(TableUtilities.getValidNumberColumnNamesInTable(table));
		} catch (ColumnNotFoundException e) {
			// TODO Actually, now that we can disable region coloring, is this really a fail condition?
			throw new AlgorithmCreationCanceledException("TODO No numeric columns.", e);
		}
		
		List<String> stringColumnNames = ImmutableList.of();
		try {
			stringColumnNames = Lists.newArrayList(TableUtilities.getValidStringColumnNamesInTable(table));
		} catch (ColumnNotFoundException e) {
			throw new AlgorithmCreationCanceledException("TODO No string columns.", e);
		}
		
		
		DropdownMutator mutator = new DropdownMutator();
		
		mutator.add(GeoMapsAlgorithm.SHAPEFILE_ID, Shapefile.PRETTY_NAME_TO_SHAPEFILE.keySet());

		if (GeoMapsAlgorithm.LET_USER_CHOOSE_PROJECTION) {
			mutator.add(GeoMapsAlgorithm.PROJECTION_ID,	KnownProjectedCRSDescriptor.FOR_NAME.keySet());
		} else {
			mutator.ignore(GeoMapsAlgorithm.PROJECTION_ID);
		}
		
		for (VizDimension dimension : EnumSet.allOf(FeatureDimension.class)) {
			dimension.addOptionsToAlgorithmParameters(mutator, numericColumnNames);
		}

		mutator.add(RegionAnnotationMode.FEATURE_NAME_ID, stringColumnNames);
		
		mutator.add(RegionAnnotationMode.COLOR_COLUMN_NAME_ID, numericColumnNames);
		
		mutator.add(RegionAnnotationMode.COLOR_SCALING_ID,
				Collections2.transform(EnumSet.allOf(Scaling.class), Functions.toStringFunction()));

		mutator.add(RegionAnnotationMode.COLOR_RANGE_ID, Constants.COLOR_RANGES.keySet());
		
		return mutator.mutate(oldOCD);
	}
}

