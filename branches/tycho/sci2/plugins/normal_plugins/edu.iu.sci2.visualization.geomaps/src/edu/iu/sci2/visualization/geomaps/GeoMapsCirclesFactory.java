package edu.iu.sci2.visualization.geomaps;

import java.util.Collections;
import java.util.Dictionary;
import java.util.EnumSet;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.utilities.ColumnNotFoundException;
import org.cishell.utilities.TableUtilities;
import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

import com.google.common.collect.ImmutableList;
import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.sci2.visualization.geomaps.metatype.Parameters;
import edu.iu.sci2.visualization.geomaps.viz.CircleDimension;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension;
import edu.iu.sci2.visualization.geomaps.viz.model.CircleAnnotationMode;
import edu.iu.sci2.visualization.geomaps.viz.ps.HowToRead;

public abstract class GeoMapsCirclesFactory implements AlgorithmFactory, ParameterMutator {
	public static final String SUBTITLE = "Proportional Symbol Map";
	public static final StringTemplate TEMPLATE_FOR_HOW_TO_READ = 
			HowToRead.TEMPLATE_GROUP.getInstanceOf("proportionalSymbols");

	@Override
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters,	CIShellContext ciShellContext) {
		String latitudeColumnName =
				(String) parameters.get(GeoMapsNetworkFactory.Parameter.LATITUDE.id());
		String longitudeColumnName =
				(String) parameters.get(GeoMapsNetworkFactory.Parameter.LONGITUDE.id());
		
		return new GeoMapsAlgorithm<Coordinate, CircleDimension>(
				data,
				parameters,
				getPageLayout(),
				new CircleAnnotationMode(longitudeColumnName, latitudeColumnName),
				SUBTITLE,
				TEMPLATE_FOR_HOW_TO_READ,
				(LogService) ciShellContext.getService(LogService.class.getName()));
	}
	
	abstract PageLayout getPageLayout();
	
	public static class Print extends GeoMapsCirclesFactory {
		@Override
		PageLayout getPageLayout() {
			return PageLayout.PRINT;
		}		
	}
	
	public static class Web extends GeoMapsCirclesFactory {
		@Override
		PageLayout getPageLayout() {
			return PageLayout.WEB;
		}
	}

	@Override
	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition oldParameters) {
		Table table = (Table) data[0].getData();
		
		List<String> numericColumnNames = Collections.emptyList();
		try {
			numericColumnNames =
					ImmutableList.copyOf(TableUtilities.getValidNumberColumnNamesInTable(table));
		} catch (ColumnNotFoundException e) {
			// Numeric columns are necessary.  Latitude and longitude are not optional.
			String message =
				"Table does not seem to have any purely numeric columns.  "
				+ "If your table does not have columns for the latitudes and longitudes of records,"
				+ " you may wish to use one of the geocoders under Analysis > Geospatial.";
			throw new AlgorithmCreationFailedException(message, e);
		}
		
		DropdownMutator mutator = new DropdownMutator();
		
		Parameters.addShapefileAndProjectionParameters(mutator);
	
		Parameters.addLatitudeParameter(mutator, numericColumnNames, GeoMapsNetworkFactory.Parameter.LATITUDE.id());
		Parameters.addLongitudeParameter(mutator, numericColumnNames, GeoMapsNetworkFactory.Parameter.LONGITUDE.id());
		
		for (VizDimension dimension : EnumSet.allOf(CircleDimension.class)) {
			dimension.addOptionsToAlgorithmParameters(mutator, numericColumnNames);
		}

		return mutator.mutate(oldParameters);
	}
}